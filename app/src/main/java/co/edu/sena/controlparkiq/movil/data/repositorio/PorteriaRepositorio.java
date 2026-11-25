package co.edu.sena.controlparkiq.movil.data.repositorio;

import java.util.List;

import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.modelo.EntradaPlaca;
import co.edu.sena.controlparkiq.movil.data.modelo.Factura;
import co.edu.sena.controlparkiq.movil.data.modelo.Pago;
import co.edu.sena.controlparkiq.movil.data.modelo.Salida;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import co.edu.sena.controlparkiq.movil.data.modelo.SolicitudFactura;
import co.edu.sena.controlparkiq.movil.util.Validador;

/**
 * Módulo de portería: entradas, salidas y cobro a visitantes.
 *
 * @author Carlos Eduardo Valero
 */
public class PorteriaRepositorio extends RepositorioBase {

    public PorteriaRepositorio(ControlParkApi api) {
        super(api);
    }

    /** Vehículos que están dentro del conjunto (sesiones abiertas). */
    public void vehiculosAdentro(Resultado<List<Sesion>> resultado) {
        ejecutar(api.listarSesiones(Sesion.ABIERTA), resultado);
    }

    /** Registra la entrada; el servidor asigna el espacio automáticamente. */
    public void registrarEntrada(String placa, Resultado<Sesion> resultado) {
        ejecutar(api.registrarEntradaPorPlaca(new EntradaPlaca(Validador.normalizarPlaca(placa))), resultado);
    }

    /** Registra la salida con la hora actual y libera el espacio. */
    public void registrarSalida(long sesionId, Resultado<Sesion> resultado) {
        ejecutar(api.registrarSalida(sesionId, new Salida()), resultado);
    }

    /** Genera la factura del visitante; el valor lo calcula el servidor. */
    public void generarFactura(long sesionId, Resultado<Factura> resultado) {
        ejecutar(api.generarFactura(new SolicitudFactura(sesionId)), resultado);
    }

    /** Registra el pago de la factura con el método elegido. */
    public void pagarFactura(long facturaId, String metodoPago, Resultado<Factura> resultado) {
        ejecutar(api.pagarFactura(facturaId, new Pago(metodoPago)), resultado);
    }
}
