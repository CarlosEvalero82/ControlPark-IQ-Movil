package co.edu.sena.controlparkiq.movil.data.api;

/**
 * Configuración de la conexión con la API REST de ControlPark-IQ.
 * La API está desplegada en Render (evidencia GA8-220501096-AA1-EV02).
 *
 * @author Carlos Eduardo Valero
 */
public final class ApiConfig {

    /** URL pública de la API. Usa HTTPS, por eso Android permite la conexión. */
    public static final String URL_BASE = "https://controlpark-iq-api.onrender.com/";

    /**
     * El plan gratuito de Render apaga el servidor cuando no se usa; la primera
     * petición puede tardar cerca de un minuto mientras arranca.
     */
    public static final int SEGUNDOS_CONEXION = 60;
    public static final int SEGUNDOS_LECTURA = 90;

    private ApiConfig() {
        // Solo constantes
    }
}
