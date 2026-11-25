package co.edu.sena.controlparkiq.movil.data;

import co.edu.sena.controlparkiq.movil.util.AlmacenSesion;

/** Sesión en memoria para las pruebas (reemplaza a SharedPreferences). */
public class AlmacenSesionEnMemoria implements AlmacenSesion {

    private String token;
    private String usuario;
    private String rol;

    @Override
    public void guardar(String token, String usuario, String rol) {
        this.token = token;
        this.usuario = usuario;
        this.rol = rol;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getUsuario() {
        return usuario;
    }

    @Override
    public String getRol() {
        return rol;
    }

    @Override
    public boolean estaActiva() {
        return token != null;
    }

    @Override
    public void cerrar() {
        token = null;
        usuario = null;
        rol = null;
    }
}
