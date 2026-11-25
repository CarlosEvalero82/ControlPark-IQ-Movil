package co.edu.sena.controlparkiq.movil.data.modelo;

/**
 * Sesión de parqueo: la estadía de un vehículo desde que entra hasta que sale.
 * Es la entidad SesionParqueo del servidor.
 */
public class Sesion {

    public static final String ABIERTA = "ABIERTA";

    private Long id;
    private Vehiculo vehiculo;
    private Espacio espacio;
    private String horaEntrada;
    private String horaSalida;
    private String estado;

    public Long getId() {
        return id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Espacio getEspacio() {
        return espacio;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public boolean estaAbierta() {
        return ABIERTA.equals(estado);
    }

    /** Un visitante paga al salir; un residente no (paga mensualidad). */
    public boolean esDeVisitante() {
        return vehiculo != null && vehiculo.getPropietario() != null
                && vehiculo.getPropietario().esVisitante();
    }
}
