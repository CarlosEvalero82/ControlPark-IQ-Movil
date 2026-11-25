package co.edu.sena.controlparkiq.movil.util;

/**
 * Contrato para guardar la sesión del usuario (token, usuario y rol).
 * Permite cambiar la forma de almacenamiento sin tocar el resto de la app
 * y usar una versión en memoria en las pruebas unitarias.
 *
 * @author Carlos Eduardo Valero
 */
public interface AlmacenSesion {

    void guardar(String token, String usuario, String rol);

    String getToken();

    String getUsuario();

    String getRol();

    boolean estaActiva();

    void cerrar();
}
