package co.edu.sena.controlparkiq.movil.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/** Pruebas unitarias del formato de dinero, horas y duraciones. */
public class FormatoTest {

    @Test
    public void monedaEnPesosColombianos() {
        assertEquals("$ 6.000", Formato.moneda(new BigDecimal("6000")));
        assertEquals("$ 480.000", Formato.moneda(new BigDecimal("480000.00")));
        assertEquals("$ 0", Formato.moneda(null));
    }

    @Test
    public void porcentajeConUnDecimal() {
        assertEquals("14,3 %", Formato.porcentaje(14.2857));
    }

    @Test
    public void horaYFechaDesdeLaApi() {
        assertEquals("22:30", Formato.hora("2026-11-24T22:30:15.123"));
        assertEquals("24/11/2026 22:30", Formato.fechaHora("2026-11-24T22:30:15"));
        assertEquals("--:--", Formato.hora(null));
    }

    @Test
    public void duracionEnHorasYMinutos() {
        assertEquals("40 min", Formato.duracion(40));
        assertEquals("1 h 5 min", Formato.duracion(65));
    }

    @Test
    public void minutosEntreLaEntradaYUnMomento() {
        Date entrada = Formato.leerFecha("2026-11-24T20:00:00");
        Date despues = new Date(entrada.getTime() + 65 * 60000L);
        assertEquals(65, Formato.minutosDesde("2026-11-24T20:00:00", despues));
    }
}
