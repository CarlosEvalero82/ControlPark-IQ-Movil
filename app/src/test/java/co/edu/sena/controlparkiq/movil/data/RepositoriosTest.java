package co.edu.sena.controlparkiq.movil.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import co.edu.sena.controlparkiq.movil.data.api.ApiClient;
import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.modelo.LoginRespuesta;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import co.edu.sena.controlparkiq.movil.data.repositorio.AuthRepositorio;
import co.edu.sena.controlparkiq.movil.data.repositorio.PorteriaRepositorio;
import co.edu.sena.controlparkiq.movil.data.repositorio.Resultado;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Pruebas de los repositorios de los módulos de autenticación y portería:
 * qué se guarda, qué mensaje recibe la pantalla y cómo se maneja el 401.
 */
public class RepositoriosTest {

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
    public void loginCorrectoGuardaLaSesion() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "{\"exito\":true,\"token\":\"tk1\",\"usuario\":\"laura.gomez\",\"rol\":\"RESIDENTE\"}"));
        Espera<LoginRespuesta> espera = new Espera<>();

        new AuthRepositorio(api, sesion).iniciarSesion("laura.gomez", "Residente2026", espera);
        espera.esperar();

        assertEquals("tk1", sesion.getToken());
        assertEquals("RESIDENTE", sesion.getRol());
        assertNull(espera.mensajeError.get());
    }

    @Test
    public void loginIncorrectoMuestraMensajeYNoGuarda() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(401)
                .setBody("{\"exito\":false,\"mensaje\":\"Usuario o contraseña incorrectos\"}"));
        Espera<LoginRespuesta> espera = new Espera<>();

        new AuthRepositorio(api, sesion).iniciarSesion("laura.gomez", "mala", espera);
        espera.esperar();

        assertEquals("Usuario o contraseña incorrectos", espera.mensajeError.get());
        assertNull(sesion.getToken());
    }

    @Test
    public void entradaEnviaLaPlacaNormalizada() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(201).setBody("{\"id\":1,\"estado\":\"ABIERTA\"}"));
        Espera<Sesion> espera = new Espera<>();

        new PorteriaRepositorio(api).registrarEntrada("hjk-219", espera);
        espera.esperar();

        assertTrue(servidor.takeRequest().getBody().readUtf8().contains("\"placa\":\"HJK219\""));
    }

    @Test
    public void errorDeValidacionLlegaComoMensaje() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(400).setBody(
                "{\"exito\":false,\"mensaje\":\"Datos inválidos\",\"errores\":[\"El vehículo ya está dentro del conjunto\"]}"));
        Espera<Sesion> espera = new Espera<>();

        new PorteriaRepositorio(api).registrarEntrada("HJK219", espera);
        espera.esperar();

        assertEquals("El vehículo ya está dentro del conjunto", espera.mensajeError.get());
    }

    @Test
    public void tokenVencidoAvisaALaPantalla() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(401)
                .setBody("{\"exito\":false,\"mensaje\":\"Debe iniciar sesión para usar este servicio\"}"));
        Espera<Sesion> espera = new Espera<>();

        new PorteriaRepositorio(api).registrarSalida(5, espera);
        espera.esperar();

        assertEquals("PUT", servidor.takeRequest().getMethod());
        assertEquals(Resultado.MENSAJE_SESION_VENCIDA, espera.mensajeError.get());
    }

    /** Resultado que permite esperar la respuesta asíncrona en la prueba. */
    private static class Espera<T> implements Resultado<T> {

        private final CountDownLatch listo = new CountDownLatch(1);
        final AtomicReference<T> datos = new AtomicReference<>();
        final AtomicReference<String> mensajeError = new AtomicReference<>();

        @Override
        public void exito(T valor) {
            datos.set(valor);
            listo.countDown();
        }

        @Override
        public void error(String mensaje) {
            mensajeError.set(mensaje);
            listo.countDown();
        }

        void esperar() throws InterruptedException {
            assertTrue("La API no respondió", listo.await(5, TimeUnit.SECONDS));
        }
    }
}
