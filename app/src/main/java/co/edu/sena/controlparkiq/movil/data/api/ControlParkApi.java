package co.edu.sena.controlparkiq.movil.data.api;

import java.util.List;

import co.edu.sena.controlparkiq.movil.data.modelo.Credenciales;
import co.edu.sena.controlparkiq.movil.data.modelo.EntradaPlaca;
import co.edu.sena.controlparkiq.movil.data.modelo.Factura;
import co.edu.sena.controlparkiq.movil.data.modelo.LoginRespuesta;
import co.edu.sena.controlparkiq.movil.data.modelo.Pago;
import co.edu.sena.controlparkiq.movil.data.modelo.Respuesta;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenOperacion;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenResidente;
import co.edu.sena.controlparkiq.movil.data.modelo.Salida;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import co.edu.sena.controlparkiq.movil.data.modelo.SolicitudFactura;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Servicios de la API REST de ControlPark-IQ que usa la app móvil.
 * Retrofit genera la implementación de esta interfaz.
 *
 * @author Carlos Eduardo Valero
 */
public interface ControlParkApi {

    // ---------- Autenticación ----------
    @POST("api/auth/login")
    Call<LoginRespuesta> iniciarSesion(@Body Credenciales credenciales);

    @POST("api/auth/logout")
    Call<Respuesta> cerrarSesion();

    // ---------- Portería: sesiones de parqueo ----------
    @GET("api/sesiones")
    Call<List<Sesion>> listarSesiones(@Query("estado") String estado);

    @POST("api/sesiones/entrada-placa")
    Call<Sesion> registrarEntradaPorPlaca(@Body EntradaPlaca entrada);

    @PUT("api/sesiones/{id}")
    Call<Sesion> registrarSalida(@Path("id") long sesionId, @Body Salida salida);

    // ---------- Portería: cobro a visitantes ----------
    @POST("api/facturas")
    Call<Factura> generarFactura(@Body SolicitudFactura solicitud);

    @PUT("api/facturas/{id}")
    Call<Factura> pagarFactura(@Path("id") long facturaId, @Body Pago pago);

    // ---------- Administrador ----------
    @GET("api/reportes/resumen")
    Call<ResumenOperacion> resumenOperacion();

    // ---------- Residente ----------
    @GET("api/residente/resumen")
    Call<ResumenResidente> resumenResidente();
}
