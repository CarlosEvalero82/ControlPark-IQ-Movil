package co.edu.sena.controlparkiq.movil.data.repositorio;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import co.edu.sena.controlparkiq.movil.data.modelo.Respuesta;
import okhttp3.ResponseBody;

/**
 * Convierte los errores de la API y de la red en mensajes claros para el usuario.
 *
 * @author Carlos Eduardo Valero
 */
public final class MensajesError {

    static final String SIN_CONEXION = "No hay conexión a internet. Revise la red e intente de nuevo.";
    static final String TIEMPO_AGOTADO = "El servidor no respondió a tiempo. Si es la primera conexión, "
            + "espere un momento mientras arranca e intente de nuevo.";
    static final String ERROR_GENERAL = "No se pudo completar la operación. Intente de nuevo.";

    private static final Gson GSON = new Gson();

    private MensajesError() {
        // Clase de utilidades
    }

    /** Lee el mensaje que envía la API en el cuerpo de una respuesta de error. */
    public static String desdeRespuesta(int codigoHttp, ResponseBody cuerpo) {
        try {
            if (cuerpo != null) {
                Respuesta respuesta = GSON.fromJson(cuerpo.charStream(), Respuesta.class);
                if (respuesta != null && respuesta.getErrores() != null && !respuesta.getErrores().isEmpty()) {
                    return respuesta.getErrores().get(0);
                }
                if (respuesta != null && respuesta.getMensaje() != null) {
                    return respuesta.getMensaje();
                }
            }
        } catch (RuntimeException e) {
            // El cuerpo no era JSON: se usa el mensaje general
        }
        return ERROR_GENERAL + " (código " + codigoHttp + ")";
    }

    /** Traduce una falla de red a un mensaje para el usuario. */
    public static String desdeFalla(Throwable falla) {
        if (falla instanceof SocketTimeoutException) {
            return TIEMPO_AGOTADO;
        }
        if (falla instanceof UnknownHostException || falla instanceof ConnectException) {
            return SIN_CONEXION;
        }
        if (falla instanceof IOException) {
            return SIN_CONEXION;
        }
        return ERROR_GENERAL;
    }
}
