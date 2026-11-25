package co.edu.sena.controlparkiq.movil.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Guarda la sesión en SharedPreferences, en el espacio privado de la app
 * (solo esta aplicación puede leerlo: modelo multiusuario de Android).
 * Patrón Singleton: una sola instancia para toda la aplicación.
 *
 * @author Carlos Eduardo Valero
 */
public final class SesionManager implements AlmacenSesion {

    private static final String ARCHIVO = "controlpark_sesion";
    private static final String CLAVE_TOKEN = "token";
    private static final String CLAVE_USUARIO = "usuario";
    private static final String CLAVE_ROL = "rol";

    private static SesionManager instancia;

    private final SharedPreferences preferencias;

    private SesionManager(Context contexto) {
        preferencias = contexto.getApplicationContext()
                .getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
    }

    public static synchronized SesionManager getInstancia(Context contexto) {
        if (instancia == null) {
            instancia = new SesionManager(contexto);
        }
        return instancia;
    }

    @Override
    public void guardar(String token, String usuario, String rol) {
        preferencias.edit()
                .putString(CLAVE_TOKEN, token)
                .putString(CLAVE_USUARIO, usuario)
                .putString(CLAVE_ROL, rol)
                .apply();
    }

    @Override
    public String getToken() {
        return preferencias.getString(CLAVE_TOKEN, null);
    }

    @Override
    public String getUsuario() {
        return preferencias.getString(CLAVE_USUARIO, "");
    }

    @Override
    public String getRol() {
        return preferencias.getString(CLAVE_ROL, "");
    }

    @Override
    public boolean estaActiva() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    @Override
    public void cerrar() {
        preferencias.edit().clear().apply();
    }
}
