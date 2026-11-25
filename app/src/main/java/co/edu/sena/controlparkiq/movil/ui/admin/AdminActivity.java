package co.edu.sena.controlparkiq.movil.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Map;

import co.edu.sena.controlparkiq.movil.R;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenOperacion;
import co.edu.sena.controlparkiq.movil.data.repositorio.ReporteRepositorio;
import co.edu.sena.controlparkiq.movil.ui.comun.ActividadBase;
import co.edu.sena.controlparkiq.movil.ui.comun.Componentes;
import co.edu.sena.controlparkiq.movil.ui.comun.RespuestaPantalla;
import co.edu.sena.controlparkiq.movil.ui.porteria.PorteriaActivity;
import co.edu.sena.controlparkiq.movil.util.Formato;

/**
 * Módulo del administrador: tablero con la ocupación, los ingresos,
 * los cobros pendientes y la ocupación por zona, calculados por la API.
 *
 * @author Carlos Eduardo Valero
 */
public class AdminActivity extends ActividadBase {

    private static final String OCUPADO = "OCUPADO";
    private static final String INHABILITADO = "INHABILITADO";

    private SwipeRefreshLayout refrescar;
    private TextView txtActualizado;
    private LinearLayout contenedorIndicadores;
    private LinearLayout contenedorZonas;
    private LinearLayout contenedorEstados;
    private ReporteRepositorio repositorio;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        setContentView(R.layout.activity_admin);
        aplicarMargenesSistema(findViewById(R.id.raiz));
        configurarEncabezado(getString(R.string.admin_titulo));

        refrescar = findViewById(R.id.refrescar);
        txtActualizado = findViewById(R.id.txtActualizado);
        contenedorIndicadores = findViewById(R.id.contenedorIndicadores);
        contenedorZonas = findViewById(R.id.contenedorZonas);
        contenedorEstados = findViewById(R.id.contenedorEstados);
        repositorio = new ReporteRepositorio(api);

        refrescar.setOnRefreshListener(this::cargarTablero);
        findViewById(R.id.btnIrPorteria).setOnClickListener(v ->
                startActivity(new Intent(this, PorteriaActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Se recarga al volver de portería para ver los cambios
        cargarTablero();
    }

    private void cargarTablero() {
        refrescar.setRefreshing(true);
        repositorio.resumenOperacion(new RespuestaPantalla<ResumenOperacion>(this) {
            @Override
            public void exito(ResumenOperacion resumen) {
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

    private void mostrar(ResumenOperacion r) {
        txtActualizado.setText(getString(R.string.actualizado, Formato.fechaHora(r.getGenerado())));

        Map<String, Long> estados = r.getEspaciosPorEstado();
        long ocupados = valor(estados, OCUPADO);
        long habilitados = r.getTotalEspacios() - valor(estados, INHABILITADO);

        // Indicadores en filas de dos tarjetas
        contenedorIndicadores.removeAllViews();
        LinearLayout fila1 = Componentes.nuevaFilaIndicadores(contenedorIndicadores);
        Componentes.agregarIndicador(fila1, getString(R.string.admin_ocupacion),
                Formato.porcentaje(r.getPorcentajeOcupacion()),
                getString(R.string.admin_ocupacion_detalle, (int) ocupados, (int) habilitados));
        Componentes.agregarIndicador(fila1, getString(R.string.admin_dentro),
                String.valueOf(r.getVehiculosAdentro()),
                getString(R.string.admin_dentro_detalle, r.getEntradasHoy()));

        LinearLayout fila2 = Componentes.nuevaFilaIndicadores(contenedorIndicadores);
        Componentes.agregarIndicador(fila2, getString(R.string.admin_ingresos),
                Formato.moneda(r.getIngresosHoy()),
                getString(R.string.admin_ingresos_detalle, Formato.moneda(r.getIngresosMes())));
        Componentes.agregarIndicador(fila2, getString(R.string.admin_pendientes),
                String.valueOf(r.getFacturasPendientes()),
                getString(R.string.admin_pendientes_detalle, Formato.moneda(r.getValorPendiente())));

        LinearLayout fila3 = Componentes.nuevaFilaIndicadores(contenedorIndicadores);
        Componentes.agregarIndicador(fila3, getString(R.string.admin_mensualidades),
                Formato.moneda(r.getMensualidadesProyectadas()),
                getString(R.string.admin_mensualidades_detalle, (int) r.getVehiculosResidentes()));

        // Ocupación por zona
        contenedorZonas.removeAllViews();
        if (r.getOcupacionPorZona() != null) {
            for (ResumenOperacion.OcupacionZona zona : r.getOcupacionPorZona()) {
                int porcentaje = zona.getTotal() == 0 ? 0 : (int) (zona.getOcupados() * 100 / zona.getTotal());
                Componentes.agregarZona(contenedorZonas, zona.getZona(),
                        getString(R.string.admin_zona_detalle, (int) zona.getOcupados(),
                                (int) zona.getDisponibles(), (int) zona.getTotal()),
                        porcentaje);
            }
        }

        // Espacios por estado
        contenedorEstados.removeAllViews();
        if (estados != null) {
            for (Map.Entry<String, Long> estado : estados.entrySet()) {
                Componentes.agregarFila(contenedorEstados, nombreEstado(estado.getKey()),
                        String.valueOf(estado.getValue()));
            }
        }
        Componentes.agregarFila(contenedorEstados, "Total", String.valueOf(r.getTotalEspacios()));
    }

    private static long valor(Map<String, Long> mapa, String clave) {
        if (mapa == null || mapa.get(clave) == null) {
            return 0;
        }
        return mapa.get(clave);
    }

    private static String nombreEstado(String estado) {
        switch (estado) {
            case "DISPONIBLE":
                return "Disponible";
            case "OCUPADO":
                return "Ocupado";
            case "RESERVADO":
                return "Reservado";
            case "INHABILITADO":
                return "Inhabilitado";
            default:
                return estado;
        }
    }
}
