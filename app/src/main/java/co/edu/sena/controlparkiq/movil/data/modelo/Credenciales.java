package co.edu.sena.controlparkiq.movil.data.modelo;

/** Datos que se envían para iniciar sesión (DTO CredencialesDTO del servidor). */
public class Credenciales {

    private final String usuario;
    private final String contrasena;

    public Credenciales(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}
