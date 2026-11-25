package co.edu.sena.controlparkiq.movil.ui.comun;

import android.app.Activity;
import android.content.Intent;

import co.edu.sena.controlparkiq.movil.ui.admin.AdminActivity;
import co.edu.sena.controlparkiq.movil.ui.login.LoginActivity;
import co.edu.sena.controlparkiq.movil.ui.porteria.PorteriaActivity;
import co.edu.sena.controlparkiq.movil.ui.residente.ResidenteActivity;

/**
 * Mapa de navegación de la app: decide a qué pantalla va cada rol.
 *
 * @author Carlos Eduardo Valero
 */
public final class Navegador {

    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String PORTERIA = "PORTERIA";
    public static final String RESIDENTE = "RESIDENTE";

    private Navegador() {
        // Clase de utilidades
    }

    /** Abre la pantalla principal del rol y borra las pantallas anteriores. */
    public static void irSegunRol(Activity origen, String rol) {
        Class<?> destino = pantallaDelRol(rol);
        abrirComoInicio(origen, destino);
    }

    public static void irAlLogin(Activity origen) {
        abrirComoInicio(origen, LoginActivity.class);
    }

    public static Class<?> pantallaDelRol(String rol) {
        if (ADMINISTRADOR.equals(rol)) {
            return AdminActivity.class;
        }
        if (PORTERIA.equals(rol)) {
            return PorteriaActivity.class;
        }
        if (RESIDENTE.equals(rol)) {
            return ResidenteActivity.class;
        }
        return LoginActivity.class;
    }

    /** Nombre del rol para mostrar en pantalla. */
    public static String nombreRol(String rol) {
        if (ADMINISTRADOR.equals(rol)) {
            return "Administrador";
        }
        if (PORTERIA.equals(rol)) {
            return "Portería";
        }
        if (RESIDENTE.equals(rol)) {
            return "Residente";
        }
        return "";
    }

    private static void abrirComoInicio(Activity origen, Class<?> destino) {
        Intent intencion = new Intent(origen, destino);
        intencion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        origen.startActivity(intencion);
        origen.finish();
    }
}
