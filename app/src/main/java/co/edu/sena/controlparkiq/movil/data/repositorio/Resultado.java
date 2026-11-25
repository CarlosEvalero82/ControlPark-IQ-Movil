package co.edu.sena.controlparkiq.movil.data.repositorio;

/**
 * Respuesta asíncrona de un repositorio hacia la pantalla.
 *
 * @param <T> tipo de dato que se obtiene cuando la operación es exitosa
 * @author Carlos Eduardo Valero
 */
public interface Resultado<T> {

    String MENSAJE_SESION_VENCIDA = "Su sesión venció. Inicie sesión de nuevo.";

    void exito(T datos);

    void error(String mensaje);

    /** La API respondió 401: el token venció o no es válido. */
    default void sesionVencida() {
        error(MENSAJE_SESION_VENCIDA);
    }
}
