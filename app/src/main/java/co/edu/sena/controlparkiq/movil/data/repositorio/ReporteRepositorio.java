package co.edu.sena.controlparkiq.movil.data.repositorio;

import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenOperacion;

/**
 * Módulo del administrador: indicadores de ocupación e ingresos.
 *
 * @author Carlos Eduardo Valero
 */
public class ReporteRepositorio extends RepositorioBase {

    public ReporteRepositorio(ControlParkApi api) {
        super(api);
    }

    public void resumenOperacion(Resultado<ResumenOperacion> resultado) {
        ejecutar(api.resumenOperacion(), resultado);
    }
}
