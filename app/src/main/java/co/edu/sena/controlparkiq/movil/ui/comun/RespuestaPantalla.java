package co.edu.sena.controlparkiq.movil.ui.comun;

import co.edu.sena.controlparkiq.movil.data.repositorio.Resultado;

/**
 * Resultado usado por las pantallas: si la sesión vence, lleva al usuario
 * al inicio de sesión sin repetir ese código en cada pantalla.
 *
 * @param <T> tipo de dato que entrega la API
 * @author Carlos Eduardo Valero
 */
public abstract class RespuestaPantalla<T> implements Resultado<T> {

    private final ActividadBase actividad;

    protected RespuestaPantalla(ActividadBase actividad) {
        this.actividad = actividad;
    }

    @Override
    public void sesionVencida() {
        actividad.alVencerSesion();
    }
}
