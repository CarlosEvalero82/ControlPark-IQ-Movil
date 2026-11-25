package co.edu.sena.controlparkiq.movil.data.repositorio;

import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.modelo.Credenciales;
import co.edu.sena.controlparkiq.movil.data.modelo.LoginRespuesta;
import co.edu.sena.controlparkiq.movil.data.modelo.Respuesta;
import co.edu.sena.controlparkiq.movil.util.AlmacenSesion;

/**
 * Módulo de autenticación: inicia y cierra la sesión del usuario.
 *
 * @author Carlos Eduardo Valero
 */
public class AuthRepositorio extends RepositorioBase {

    private final AlmacenSesion almacen;

    public AuthRepositorio(ControlParkApi api, AlmacenSesion almacen) {
        super(api);
        this.almacen = almacen;
    }

    /** Inicia sesión y, si es correcto, guarda el token y el rol en el celular. */
    public void iniciarSesion(String usuario, String contrasena, Resultado<LoginRespuesta> resultado) {
        ejecutar(api.iniciarSesion(new Credenciales(usuario.trim(), contrasena)), new Resultado<LoginRespuesta>() {
            @Override
            public void exito(LoginRespuesta datos) {
                almacen.guardar(datos.getToken(), datos.getUsuario(), datos.getRol());
                resultado.exito(datos);
            }

            @Override
            public void error(String mensaje) {
                resultado.error(mensaje);
            }

            @Override
            public void sesionVencida() {
                // En el login, 401 significa usuario o contraseña incorrectos
                resultado.error("Usuario o contraseña incorrectos");
            }
        });
    }

    /** Avisa a la API que cierre el token y borra la sesión guardada. */
    public void cerrarSesion(Runnable alTerminar) {
        ejecutar(api.cerrarSesion(), new Resultado<Respuesta>() {
            @Override
            public void exito(Respuesta datos) {
                terminar();
            }

            @Override
            public void error(String mensaje) {
                // Aunque falle la red, la sesión se borra del celular
                terminar();
            }

            private void terminar() {
                almacen.cerrar();
                alTerminar.run();
            }
        });
    }
}
