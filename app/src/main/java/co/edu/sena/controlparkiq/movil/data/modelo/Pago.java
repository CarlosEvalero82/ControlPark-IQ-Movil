package co.edu.sena.controlparkiq.movil.data.modelo;

/** Registra el pago de una factura (DTO PagoDTO del servidor). */
public class Pago {

    public static final String EFECTIVO = "EFECTIVO";
    public static final String TARJETA = "TARJETA";
    public static final String TRANSFERENCIA = "TRANSFERENCIA";

    private final String metodoPago;

    public Pago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }
}
