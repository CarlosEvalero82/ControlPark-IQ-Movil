package co.edu.sena.controlparkiq.movil.ui.residente;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import co.edu.sena.controlparkiq.movil.R;
import co.edu.sena.controlparkiq.movil.data.modelo.Persona;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenResidente;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import co.edu.sena.controlparkiq.movil.data.modelo.Vehiculo;
import co.edu.sena.controlparkiq.movil.data.repositorio.ResidenteRepositorio;
import co.edu.sena.controlparkiq.movil.ui.comun.ActividadBase;
import co.edu.sena.controlparkiq.movil.ui.comun.Componentes;
import co.edu.sena.controlparkiq.movil.ui.comun.RespuestaPantalla;
import co.edu.sena.controlparkiq.movil.ui.comun.SesionAdapter;
import co.edu.sena.controlparkiq.movil.util.Formato;

/**
 * Módulo del residente: su mensualidad con el detalle por vehículo, sus
 * vehículos, los que están dentro del conjunto y su historial reciente.
 *
 * @author Carlos Eduardo Valero
 */
public class ResidenteActivity extends ActividadBase {

    private SwipeRefreshLayout refrescar;
    private TextView txtNombre;
    private TextView txtUnidad;
    private TextView txtMensualidad;
    private TextView txtTituloVehiculos;
    private TextView txtTituloAdentro;
    private TextView txtSinHistorial;
    private LinearLayout contenedorMensualidad;
    private LinearLayout contenedorVehiculos;
    private LinearLayout contenedorAdentro;
    private SesionAdapter adaptadorHistorial;
    private ResidenteRepositorio repositorio;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        setContentView(R.layout.activity_residente);
        aplicarMargenesSistema(findViewById(R.id.raiz));
        configurarEncabezado(getString(R.string.residente_titulo));

        refrescar = findViewById(R.id.refrescar);
        txtNombre = findViewById(R.id.txtNombre);
        txtUnidad = findViewById(R.id.txtUnidad);
        txtMensualidad = findViewById(R.id.txtMensualidad);
        txtTituloVehiculos = findViewById(R.id.txtTituloVehiculos);
        txtTituloAdentro = findViewById(R.id.txtTituloAdentro);
        txtSinHistorial = findViewById(R.id.txtSinHistorial);
        contenedorMensualidad = findViewById(R.id.contenedorMensualidad);
        contenedorVehiculos = findViewById(R.id.contenedorVehiculos);
        contenedorAdentro = findViewById(R.id.contenedorAdentro);
        repositorio = new ResidenteRepositorio(api);

        // Mismo adaptador de portería, sin botón de salida (solo consulta)
        adaptadorHistorial = new SesionAdapter(null);
        RecyclerView listaHistorial = findViewById(R.id.listaHistorial);
        listaHistorial.setLayoutManager(new LinearLayoutManager(this));
        listaHistorial.setAdapter(adaptadorHistorial);

        refrescar.setOnRefreshListener(this::cargar);
        cargar();
    }

    private void cargar() {
        refrescar.setRefreshing(true);
        repositorio.resumen(new RespuestaPantalla<ResumenResidente>(this) {
            @Override
            public void exito(ResumenResidente resumen) {
                refrescar.setRefreshing(false);
                mostrar(resumen);
            }

            @Override
            public void error(String mensaje) {
                refrescar.setRefreshing(false);
                mostrarMensaje(mensaje);
            }
        });
    }

    private void mostrar(ResumenResidente r) {
        Persona persona = r.getPersona();
        if (persona != null) {
            txtNombre.setText(persona.getNombreCompleto());
            String unidad = persona.getUnidad() != null ? persona.getUnidad().getDescripcion() + " · " : "";
            txtUnidad.setText(unidad + getString(R.string.residente_documento, persona.getDocumento()));
        }

        // Mensualidad y su detalle por vehículo
        txtMensualidad.setText(Formato.moneda(r.getMensualidadTotal()));
        contenedorMensualidad.removeAllViews();
        if (r.getDetalleMensualidad() != null) {
            for (ResumenResidente.CargoMensual cargo : r.getDetalleMensualidad()) {
                Componentes.agregarFila(contenedorMensualidad, cargo.getPlaca() + " · " + nombreTipo(cargo.getTipo()),
                        Formato.moneda(cargo.getValor()));
            }
        }

        // Vehículos registrados
        List<Vehiculo> vehiculos = r.getVehiculos();
        int cantidad = vehiculos == null ? 0 : vehiculos.size();
        txtTituloVehiculos.setText(getString(R.string.residente_mis_vehiculos, cantidad));
        contenedorVehiculos.removeAllViews();
        if (vehiculos != null) {
            for (Vehiculo vehiculo : vehiculos) {
                Componentes.agregarFila(contenedorVehiculos, vehiculo.getPlaca() + " · " + vehiculo.getDescripcion(),
                        nombreTipo(vehiculo.getTipo()));
            }
        }

        // Vehículos dentro del conjunto
        List<Sesion> adentro = r.getVehiculosAdentro();
        int dentro = adentro == null ? 0 : adentro.size();
        txtTituloAdentro.setText(getString(R.string.residente_adentro, dentro));
        contenedorAdentro.removeAllViews();
        if (dentro == 0) {
            Componentes.agregarFila(contenedorAdentro, getString(R.string.residente_ninguno_adentro), "");
        } else {
            for (Sesion sesion : adentro) {
                String espacio = sesion.getEspacio() != null ? sesion.getEspacio().getCodigo() : "";
                Componentes.agregarFila(contenedorAdentro,
                        sesion.getVehiculo().getPlaca() + " · espacio " + espacio,
                        "desde " + Formato.hora(sesion.getHoraEntrada()));
            }
        }

        // Historial
        List<Sesion> historial = r.getHistorial();
        adaptadorHistorial.actualizar(historial);
        txtSinHistorial.setVisibility(historial == null || historial.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private static String nombreTipo(String tipo) {
        return "MOTO".equals(tipo) ? "Moto" : "Carro";
    }
}
