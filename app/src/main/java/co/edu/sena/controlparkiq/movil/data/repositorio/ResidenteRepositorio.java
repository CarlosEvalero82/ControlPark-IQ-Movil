package co.edu.sena.controlparkiq.movil.data.repositorio;

import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenResidente;

/**
 * Módulo del residente: sus vehículos, su mensualidad y su historial.
 * El servidor identifica al residente por el token, no por un parámetro.
 *
 * @author Carlos Eduardo Valero
 */
public class ResidenteRepositorio extends RepositorioBase {

    public ResidenteRepositorio(ControlParkApi api) {
        super(api);
    }

    public void resumen(Resultado<ResumenResidente> resultado) {
        ejecutar(api.resumenResidente(), resultado);
    }
}
