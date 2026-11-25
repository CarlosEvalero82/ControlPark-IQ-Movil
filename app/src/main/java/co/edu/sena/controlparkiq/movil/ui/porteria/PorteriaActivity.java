package co.edu.sena.controlparkiq.movil.ui.porteria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.List;

import co.edu.sena.controlparkiq.movil.R;
import co.edu.sena.controlparkiq.movil.data.modelo.Factura;
import co.edu.sena.controlparkiq.movil.data.modelo.Pago;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import co.edu.sena.controlparkiq.movil.data.repositorio.PorteriaRepositorio;
import co.edu.sena.controlparkiq.movil.ui.comun.ActividadBase;
import co.edu.sena.controlparkiq.movil.ui.comun.RespuestaPantalla;
import co.edu.sena.controlparkiq.movil.ui.comun.SesionAdapter;
import co.edu.sena.controlparkiq.movil.util.Formato;
import co.edu.sena.controlparkiq.movil.util.Validador;

/**
 * Módulo de portería: registra la entrada por placa (el sistema asigna el
 * espacio), muestra los vehículos dentro y registra la salida con el cobro
 * al visitante.
 *
 * @author Carlos Eduardo Valero
 */
public class PorteriaActivity extends ActividadBase {

    private TextInputLayout capaPlaca;
    private TextInputEditText txtPlaca;
    private MaterialButton btnRegistrarEntrada;
    private TextView txtResultadoEntrada;
    private TextView txtTituloLista;
    private TextView txtVacio;
    private SwipeRefreshLayout refrescar;
    private SesionAdapter adaptador;
    private PorteriaRepositorio repositorio;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        setContentView(R.layout.activity_porteria);
        aplicarMargenesSistema(findViewById(R.id.raiz));
        configurarEncabezado(getString(R.string.porteria_titulo));

        capaPlaca = findViewById(R.id.capaPlaca);
        txtPlaca = findViewById(R.id.txtPlaca);
        btnRegistrarEntrada = findViewById(R.id.btnRegistrarEntrada);
        txtResultadoEntrada = findViewById(R.id.txtResultadoEntrada);
        txtTituloLista = findViewById(R.id.txtTituloLista);
        txtVacio = findViewById(R.id.txtVacio);
        refrescar = findViewById(R.id.refrescar);
        repositorio = new PorteriaRepositorio(api);

        adaptador = new SesionAdapter(this::confirmarSalida);
        RecyclerView lista = findViewById(R.id.listaSesiones);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adaptador);

        btnRegistrarEntrada.setOnClickListener(v -> registrarEntrada());
        refrescar.setOnRefreshListener(this::cargarVehiculosAdentro);
        txtTituloLista.setText(getString(R.string.porteria_dentro, 0));
        cargarVehiculosAdentro();
    }

    // ---------------------------------------------------------------- Entrada

    private void registrarEntrada() {
        String placa = Validador.normalizarPlaca(txtPlaca.getText() == null ? "" : txtPlaca.getText().toString());
        capaPlaca.setError(null);
        if (!Validador.esPlacaValida(placa)) {
            capaPlaca.setError(getString(R.string.porteria_placa_invalida));
            return;
        }

        btnRegistrarEntrada.setEnabled(false);
        repositorio.registrarEntrada(placa, new RespuestaPantalla<Sesion>(this) {
            @Override
            public void exito(Sesion nueva) {
                btnRegistrarEntrada.setEnabled(true);
                txtPlaca.setText("");
                String codigo = nueva.getEspacio() != null ? nueva.getEspacio().getCodigo() : "";
                String zona = nueva.getEspacio() != null && nueva.getEspacio().getZona() != null
                        ? nueva.getEspacio().getZona().getNombre() : "";
                mostrarResultadoEntrada(getString(R.string.porteria_entrada_ok, placa, codigo, zona), true);
                cargarVehiculosAdentro();
            }

            @Override
            public void error(String mensaje) {
                btnRegistrarEntrada.setEnabled(true);
                mostrarResultadoEntrada(mensaje, false);
            }
        });
    }

    private void mostrarResultadoEntrada(String texto, boolean exito) {
        txtResultadoEntrada.setVisibility(View.VISIBLE);
        txtResultadoEntrada.setText(texto);
        txtResultadoEntrada.setTextColor(ContextCompat.getColor(this, exito ? R.color.cp_verde : R.color.cp_rojo));
    }

    // ---------------------------------------------------------------- Lista

    private void cargarVehiculosAdentro() {
        refrescar.setRefreshing(true);
        repositorio.vehiculosAdentro(new RespuestaPantalla<List<Sesion>>(this) {
            @Override
            public void exito(List<Sesion> sesiones) {
                refrescar.setRefreshing(false);
                adaptador.actualizar(sesiones);
                int cantidad = sesiones == null ? 0 : sesiones.size();
                txtTituloLista.setText(getString(R.string.porteria_dentro, cantidad));
                txtVacio.setVisibility(cantidad == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void error(String mensaje) {
                refrescar.setRefreshing(false);
                mostrarMensaje(mensaje);
            }
        });
    }

    // ---------------------------------------------------------------- Salida y cobro

    private void confirmarSalida(Sesion sesion) {
        String placa = sesion.getVehiculo().getPlaca();
        String espacio = sesion.getEspacio() != null ? sesion.getEspacio().getCodigo() : "";
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.porteria_salida)
                .setMessage(getString(R.string.porteria_confirmar_salida, placa, espacio))
                .setPositiveButton(R.string.si, (dialogo, boton) -> registrarSalida(sesion))
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void registrarSalida(Sesion sesion) {
        repositorio.registrarSalida(sesion.getId(), new RespuestaPantalla<Sesion>(this) {
            @Override
            public void exito(Sesion cerrada) {
                cargarVehiculosAdentro();
                if (sesion.esDeVisitante()) {
                    generarFactura(sesion, cerrada);
                } else {
                    mostrarMensaje(getString(R.string.porteria_salida_ok, sesion.getVehiculo().getPlaca()));
                }
            }

            @Override
            public void error(String mensaje) {
                mostrarMensaje(mensaje);
            }
        });
    }

    private void generarFactura(Sesion original, Sesion cerrada) {
        repositorio.generarFactura(original.getId(), new RespuestaPantalla<Factura>(this) {
            @Override
            public void exito(Factura factura) {
                mostrarCobro(original, cerrada, factura);
            }

            @Override
            public void error(String mensaje) {
                mostrarMensaje(mensaje);
            }
        });
    }

    private void mostrarCobro(Sesion original, Sesion cerrada, Factura factura) {
        View contenido = LayoutInflater.from(this).inflate(R.layout.dialogo_cobro, null);
        ((TextView) contenido.findViewById(R.id.txtCobroPlaca)).setText(original.getVehiculo().getPlaca()
                + " · " + original.getVehiculo().getPropietario().getNombreCompleto());

        String salida = cerrada.getHoraSalida();
        long minutos = Formato.minutosDesde(original.getHoraEntrada(), new Date());
        ((TextView) contenido.findViewById(R.id.txtCobroTiempo)).setText(Formato.hora(original.getHoraEntrada())
                + " → " + Formato.hora(salida) + " · " + Formato.duracion(minutos));
        ((TextView) contenido.findViewById(R.id.txtCobroDetalle)).setText(getString(R.string.cobro_horas,
                factura.getHorasCobradas(), Formato.moneda(factura.getValorHora())));
        ((TextView) contenido.findViewById(R.id.txtCobroTotal)).setText(Formato.moneda(factura.getValorTotal()));
        RadioGroup grupoMetodo = contenido.findViewById(R.id.grupoMetodo);

        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.cobro_titulo, factura.getId()))
                .setView(contenido)
                .setCancelable(false)
                .setPositiveButton(R.string.cobro_pagar,
                        (dialogo, boton) -> pagarFactura(factura, metodoElegido(grupoMetodo)))
                .setNegativeButton(R.string.cobro_despues, (dialogo, boton) ->
                        mostrarMensaje(getString(R.string.cobro_pendiente, factura.getId())))
                .show();
    }

    private static String metodoElegido(RadioGroup grupo) {
        int elegido = grupo.getCheckedRadioButtonId();
        if (elegido == R.id.opcionTarjeta) {
            return Pago.TARJETA;
        }
        if (elegido == R.id.opcionTransferencia) {
            return Pago.TRANSFERENCIA;
        }
        return Pago.EFECTIVO;
    }

    private void pagarFactura(Factura factura, String metodo) {
        repositorio.pagarFactura(factura.getId(), metodo, new RespuestaPantalla<Factura>(this) {
            @Override
            public void exito(Factura pagada) {
                mostrarMensaje(getString(R.string.cobro_ok, pagada.getId(), Formato.moneda(pagada.getValorTotal())));
            }

            @Override
            public void error(String mensaje) {
                mostrarMensaje(mensaje);
            }
        });
    }
}
