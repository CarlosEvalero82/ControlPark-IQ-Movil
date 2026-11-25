package co.edu.sena.controlparkiq.movil.data.modelo;

import java.math.BigDecimal;
import java.util.List;

/** Información del residente que inició sesión (DTO ResumenResidenteDTO). */
public class ResumenResidente {

    private Persona persona;
    private List<Vehiculo> vehiculos;
    private List<CargoMensual> detalleMensualidad;
    private BigDecimal mensualidadTotal;
    private List<Sesion> vehiculosAdentro;
    private List<Sesion> historial;

    public Persona getPersona() {
        return persona;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public List<CargoMensual> getDetalleMensualidad() {
        return detalleMensualidad;
    }

    public BigDecimal getMensualidadTotal() {
        return mensualidadTotal;
    }

    public List<Sesion> getVehiculosAdentro() {
        return vehiculosAdentro;
    }

    public List<Sesion> getHistorial() {
        return historial;
    }

    /** Valor mensual que paga el residente por uno de sus vehículos. */
    public static class CargoMensual {

        private String placa;
        private String tipo;
        private BigDecimal valor;

        public String getPlaca() {
            return placa;
        }

        public String getTipo() {
            return tipo;
        }

        public BigDecimal getValor() {
            return valor;
        }
    }
}
