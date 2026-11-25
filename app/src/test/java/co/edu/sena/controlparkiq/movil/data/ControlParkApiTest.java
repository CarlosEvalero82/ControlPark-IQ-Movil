package co.edu.sena.controlparkiq.movil.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import co.edu.sena.controlparkiq.movil.data.api.ApiClient;
import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.modelo.Credenciales;
import co.edu.sena.controlparkiq.movil.data.modelo.EntradaPlaca;
import co.edu.sena.controlparkiq.movil.data.modelo.LoginRespuesta;
import co.edu.sena.controlparkiq.movil.data.modelo.ResumenOperacion;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

/**
 * Pruebas de la capa de datos contra un servidor simulado (MockWebServer):
 * rutas, métodos HTTP, cabecera del token y lectura del JSON de la API.
 */
public class ControlParkApiTest {

    private MockWebServer servidor;
    private AlmacenSesionEnMemoria sesion;
    private ControlParkApi api;

    @Before
    public void iniciar() throws Exception {
        servidor = new MockWebServer();
        servidor.start();
        sesion = new AlmacenSesionEnMemoria();
        api = ApiClient.crear(servidor.url("/").toString(), sesion::getToken);
    }

    @After
    public void detener() throws Exception {
        servidor.shutdown();
    }

    @Test
    public void loginEnviaCredencialesYLeeElToken() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "{\"exito\":true,\"mensaje\":\"Inicio de sesión exitoso\",\"token\":\"abc123\","
                        + "\"usuario\":\"admin.torres\",\"rol\":\"ADMINISTRADOR\",\"expira\":\"2026-11-25T06:00:00\"}"));

        Response<LoginRespuesta> respuesta = api.iniciarSesion(new Credenciales("admin.torres", "Parque2026")).execute();

        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("POST", peticion.getMethod());
        assertEquals("/api/auth/login", peticion.getPath());
        assertTrue(peticion.getBody().readUtf8().contains("\"usuario\":\"admin.torres\""));
        assertNull("El login no lleva token", peticion.getHeader("Authorization"));
        assertEquals("abc123", respuesta.body().getToken());
        assertEquals("ADMINISTRADOR", respuesta.body().getRol());
    }

    @Test
    public void lasPeticionesLlevanElTokenBearer() throws Exception {
        sesion.guardar("token-de-prueba", "porteria.torre1", "PORTERIA");
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));

        api.listarSesiones(Sesion.ABIERTA).execute();

        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("Bearer token-de-prueba", peticion.getHeader("Authorization"));
        assertEquals("/api/sesiones?estado=ABIERTA", peticion.getPath());
    }

    @Test
    public void entradaPorPlacaLeeElEspacioAsignado() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(201).setBody(
                "{\"id\":9,\"estado\":\"ABIERTA\",\"horaEntrada\":\"2026-11-24T22:30:00\","
                        + "\"vehiculo\":{\"id\":3,\"placa\":\"HJK219\",\"tipo\":\"CARRO\","
                        + "\"propietario\":{\"id\":2,\"nombres\":\"Laura\",\"apellidos\":\"Gómez\",\"tipo\":\"RESIDENTE\"}},"
                        + "\"espacio\":{\"id\":1,\"codigo\":\"R-01\",\"zona\":{\"id\":1,\"nombre\":\"Sótano 1\"}}}"));

        Response<Sesion> respuesta = api.registrarEntradaPorPlaca(new EntradaPlaca("HJK219")).execute();

        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("/api/sesiones/entrada-placa", peticion.getPath());
        assertEquals(201, respuesta.code());
        assertEquals("R-01", respuesta.body().getEspacio().getCodigo());
        assertTrue(respuesta.body().estaAbierta());
        assertTrue(!respuesta.body().esDeVisitante());
    }

    @Test
    public void resumenDelTableroSeLeeCompleto() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "{\"totalEspacios\":22,\"espaciosPorEstado\":{\"DISPONIBLE\":17,\"OCUPADO\":3},"
                        + "\"porcentajeOcupacion\":14.3,\"vehiculosAdentro\":3,\"entradasHoy\":5,"
                        + "\"ingresosHoy\":9000,\"ingresosMes\":9000,\"facturasPendientes\":1,"
                        + "\"valorPendiente\":1500,\"vehiculosResidentes\":7,\"mensualidadesProyectadas\":480000,"
                        + "\"ocupacionPorZona\":[{\"zona\":\"Sótano 1\",\"tipo\":\"RESIDENTES\",\"total\":14,"
                        + "\"ocupados\":2,\"disponibles\":10}]}"));

        ResumenOperacion resumen = api.resumenOperacion().execute().body();

        assertEquals("/api/reportes/resumen", servidor.takeRequest().getPath());
        assertEquals(22, resumen.getTotalEspacios());
        assertEquals(Long.valueOf(3), resumen.getEspaciosPorEstado().get("OCUPADO"));
        assertEquals(0, new BigDecimal("480000").compareTo(resumen.getMensualidadesProyectadas()));
        assertEquals("Sótano 1", resumen.getOcupacionPorZona().get(0).getZona());
    }
}
