package co.edu.sena.controlparkiq.movil.data.modelo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Indicadores del tablero del administrador (DTO ResumenOperacionDTO). */
public class ResumenOperacion {

    private String generado;
    private int totalEspacios;
    private Map<String, Long> espaciosPorEstado;
    private double porcentajeOcupacion;
    private List<OcupacionZona> ocupacionPorZona;
    private int vehiculosAdentro;
    private int entradasHoy;
    private BigDecimal ingresosHoy;
    private BigDecimal ingresosMes;
    private long facturasPendientes;
    private BigDecimal valorPendiente;
    private long vehiculosResidentes;
    private BigDecimal mensualidadesProyectadas;

    public String getGenerado() {
        return generado;
    }

    public int getTotalEspacios() {
        return totalEspacios;
    }

    public Map<String, Long> getEspaciosPorEstado() {
        return espaciosPorEstado;
    }

    public double getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }

    public List<OcupacionZona> getOcupacionPorZona() {
        return ocupacionPorZona;
    }

    public int getVehiculosAdentro() {
        return vehiculosAdentro;
    }

    public int getEntradasHoy() {
        return entradasHoy;
    }

    public BigDecimal getIngresosHoy() {
        return ingresosHoy;
    }

    public BigDecimal getIngresosMes() {
        return ingresosMes;
    }

    public long getFacturasPendientes() {
        return facturasPendientes;
    }

    public BigDecimal getValorPendiente() {
        return valorPendiente;
    }

    public long getVehiculosResidentes() {
        return vehiculosResidentes;
    }

    public BigDecimal getMensualidadesProyectadas() {
        return mensualidadesProyectadas;
    }

    /** Ocupación de una zona del parqueadero. */
    public static class OcupacionZona {

        private String zona;
        private String tipo;
        private long total;
        private long ocupados;
        private long disponibles;

        public String getZona() {
            return zona;
        }

        public String getTipo() {
            return tipo;
        }

        public long getTotal() {
            return total;
        }

        public long getOcupados() {
            return ocupados;
        }

        public long getDisponibles() {
            return disponibles;
        }
    }
}
