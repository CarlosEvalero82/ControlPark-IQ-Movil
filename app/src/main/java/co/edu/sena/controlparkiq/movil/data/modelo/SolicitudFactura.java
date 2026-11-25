package co.edu.sena.controlparkiq.movil.data.modelo;

/** Pide al servidor generar la factura de una sesión (DTO FacturaDTO). */
public class SolicitudFactura {

    private final Long sesionId;

    public SolicitudFactura(Long sesionId) {
        this.sesionId = sesionId;
    }

    public Long getSesionId() {
        return sesionId;
    }
}
