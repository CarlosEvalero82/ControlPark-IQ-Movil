package co.edu.sena.controlparkiq.movil.data.api;

/** Entrega el token de la sesión actual, o null si no hay sesión. */
public interface ProveedorToken {

    String getToken();
}
