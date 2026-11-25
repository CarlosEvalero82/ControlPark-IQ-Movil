package co.edu.sena.controlparkiq.movil.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Da formato a los datos que llegan de la API para mostrarlos en pantalla:
 * dinero en pesos colombianos, horas, fechas y duraciones.
 *
 * @author Carlos Eduardo Valero
 */
public final class Formato {

    private static final TimeZone ZONA_BOGOTA = TimeZone.getTimeZone("America/Bogota");
    private static final String PATRON_API = "yyyy-MM-dd'T'HH:mm:ss";

    private Formato() {
        // Clase de utilidades: no se instancia
    }

    /** 6000 → "$ 6.000" (pesos colombianos, sin decimales). */
    public static String moneda(BigDecimal valor) {
        if (valor == null) {
            return "$ 0";
        }
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.ROOT);
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        DecimalFormat formato = new DecimalFormat("#,##0", simbolos);
        return "$ " + formato.format(valor);
    }

    /** 14.2857 → "14,3 %". */
    public static String porcentaje(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.ROOT);
        simbolos.setDecimalSeparator(',');
        return new DecimalFormat("0.0", simbolos).format(valor) + " %";
    }

    /** "2026-11-24T22:30:15.123" → "22:30". */
    public static String hora(String fechaIso) {
        if (fechaIso == null || fechaIso.length() < 16) {
            return "--:--";
        }
        return fechaIso.substring(11, 16);
    }

    /** "2026-11-24T22:30:15" → "24/11/2026 22:30". */
    public static String fechaHora(String fechaIso) {
        if (fechaIso == null || fechaIso.length() < 16) {
            return "";
        }
        return fechaIso.substring(8, 10) + "/" + fechaIso.substring(5, 7) + "/"
                + fechaIso.substring(0, 4) + " " + fechaIso.substring(11, 16);
    }

    /** Minutos que han pasado desde una fecha de la API hasta el momento indicado. */
    public static long minutosDesde(String fechaIso, Date ahora) {
        Date inicio = leerFecha(fechaIso);
        if (inicio == null || ahora == null) {
            return 0;
        }
        long minutos = (ahora.getTime() - inicio.getTime()) / 60000L;
        return Math.max(minutos, 0);
    }

    /** 65 → "1 h 5 min"; 40 → "40 min". */
    public static String duracion(long minutos) {
        long horas = minutos / 60;
        long resto = minutos % 60;
        if (horas == 0) {
            return resto + " min";
        }
        return horas + " h " + resto + " min";
    }

    /** Convierte la fecha de la API (hora de Colombia) en un objeto Date. */
    static Date leerFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.length() < 19) {
            return null;
        }
        SimpleDateFormat lector = new SimpleDateFormat(PATRON_API, Locale.ROOT);
        lector.setTimeZone(ZONA_BOGOTA);
        try {
            return lector.parse(fechaIso.substring(0, 19));
        } catch (ParseException e) {
            return null;
        }
    }
}
