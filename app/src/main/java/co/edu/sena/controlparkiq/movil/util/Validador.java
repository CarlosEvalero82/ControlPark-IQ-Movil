package co.edu.sena.controlparkiq.movil.util;

import java.util.Locale;

/**
 * Validaciones de los datos que escribe el usuario, antes de enviarlos a la API.
 * Son las mismas reglas que aplica el servidor de ControlPark-IQ.
 *
 * @author Carlos Eduardo Valero
 */
public final class Validador {

    /** Placa de carro: tres letras y tres números (ABC123). */
    private static final String PATRON_PLACA_CARRO = "^[A-Z]{3}[0-9]{3}$";
    /** Placa de moto: tres letras, dos números y una letra (ABC12D). */
    private static final String PATRON_PLACA_MOTO = "^[A-Z]{3}[0-9]{2}[A-Z]$";

    private Validador() {
        // Clase de utilidades: no se instancia
    }

    /** Quita espacios y guiones y pasa la placa a mayúsculas: "hjk-219" → "HJK219". */
    public static String normalizarPlaca(String placa) {
        if (placa == null) {
            return "";
        }
        return placa.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    public static boolean esPlacaValida(String placa) {
        String limpia = normalizarPlaca(placa);
        return limpia.matches(PATRON_PLACA_CARRO) || limpia.matches(PATRON_PLACA_MOTO);
    }

    /**
     * Revisa los datos del inicio de sesión.
     *
     * @return el mensaje de error, o null si los datos están completos
     */
    public static String validarCredenciales(String usuario, String contrasena) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return "Escriba el usuario";
        }
        if (contrasena == null || contrasena.isEmpty()) {
            return "Escriba la contraseña";
        }
        return null;
    }
}
