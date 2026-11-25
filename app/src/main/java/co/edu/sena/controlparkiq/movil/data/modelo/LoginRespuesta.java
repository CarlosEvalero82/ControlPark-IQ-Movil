package co.edu.sena.controlparkiq.movil.data.modelo;

/**
 * Respuesta del inicio de sesión: además del mensaje trae el token,
 * el usuario, el rol y la fecha en que vence la sesión.
 */
public class LoginRespuesta extends Respuesta {

    private String token;
    private String usuario;
    private String rol;
    private Long personaId;
    private String expira;

    public String getToken() {
        return token;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public String getExpira() {
        return expira;
    }
}
