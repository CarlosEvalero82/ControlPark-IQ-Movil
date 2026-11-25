package co.edu.sena.controlparkiq.movil.data.modelo;

import java.util.List;

/**
 * Respuesta general de la API: indica si la operación fue exitosa,
 * un mensaje y, cuando hay errores de validación, la lista de errores.
 * Es el DTO RespuestaDTO del servidor.
 */
public class Respuesta {

    private boolean exito;
    private String mensaje;
    private List<String> errores;

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<String> getErrores() {
        return errores;
    }
}
