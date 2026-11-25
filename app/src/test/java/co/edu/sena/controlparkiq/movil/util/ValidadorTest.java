package co.edu.sena.controlparkiq.movil.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Pruebas unitarias de las validaciones de datos (login y portería). */
public class ValidadorTest {

    @Test
    public void placaDeCarroValida() {
        assertTrue(Validador.esPlacaValida("HJK219"));
    }

    @Test
    public void placaDeMotoValida() {
        assertTrue(Validador.esPlacaValida("ABC12D"));
    }

    @Test
    public void placaEnMinusculasYConGuionSeNormaliza() {
        assertEquals("HJK219", Validador.normalizarPlaca(" hjk-219 "));
        assertTrue(Validador.esPlacaValida("hjk 219"));
    }

    @Test
    public void placasInvalidas() {
        assertFalse(Validador.esPlacaValida("HJ219"));
        assertFalse(Validador.esPlacaValida("1234AB"));
        assertFalse(Validador.esPlacaValida(""));
        assertFalse(Validador.esPlacaValida(null));
    }

    @Test
    public void credencialesCompletasNoTienenError() {
        assertNull(Validador.validarCredenciales("admin.torres", "Parque2026"));
    }

    @Test
    public void credencialesIncompletasDevuelvenMensaje() {
        assertEquals("Escriba el usuario", Validador.validarCredenciales("  ", "Parque2026"));
        assertEquals("Escriba la contraseña", Validador.validarCredenciales("admin.torres", ""));
    }
}
