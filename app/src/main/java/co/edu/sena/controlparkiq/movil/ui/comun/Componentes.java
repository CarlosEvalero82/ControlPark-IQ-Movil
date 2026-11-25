package co.edu.sena.controlparkiq.movil.ui.comun;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import co.edu.sena.controlparkiq.movil.R;

/**
 * Fábrica de componentes visuales reutilizables: filas, indicadores y zonas.
 *
 * @author Carlos Eduardo Valero
 */
public final class Componentes {

    private Componentes() {
        // Clase de utilidades
    }

    /** Agrega una fila de dos columnas (item_fila.xml) al contenedor. */
    public static void agregarFila(LinearLayout contenedor, String izquierda, String derecha) {
        View fila = LayoutInflater.from(contenedor.getContext()).inflate(R.layout.item_fila, contenedor, false);
        ((TextView) fila.findViewById(R.id.txtIzquierda)).setText(izquierda);
        ((TextView) fila.findViewById(R.id.txtDerecha)).setText(derecha);
        contenedor.addView(fila);
    }

    /** Crea una tarjeta de indicador (item_indicador.xml) dentro de una fila. */
    public static void agregarIndicador(ViewGroup fila, String etiqueta, String valor, String detalle) {
        View tarjeta = LayoutInflater.from(fila.getContext()).inflate(R.layout.item_indicador, fila, false);
        ((TextView) tarjeta.findViewById(R.id.txtEtiqueta)).setText(etiqueta);
        ((TextView) tarjeta.findViewById(R.id.txtValor)).setText(valor);
        ((TextView) tarjeta.findViewById(R.id.txtDetalle)).setText(detalle);
        fila.addView(tarjeta);
    }

    /** Crea una fila horizontal vacía para poner dos indicadores lado a lado. */
    public static LinearLayout nuevaFilaIndicadores(LinearLayout contenedor) {
        LinearLayout fila = new LinearLayout(contenedor.getContext());
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contenedor.addView(fila);
        return fila;
    }

    /** Agrega la ocupación de una zona con su barra de progreso (item_zona.xml). */
    public static void agregarZona(LinearLayout contenedor, String nombre, String detalle, int porcentaje) {
        View zona = LayoutInflater.from(contenedor.getContext()).inflate(R.layout.item_zona, contenedor, false);
        ((TextView) zona.findViewById(R.id.txtZona)).setText(nombre);
        ((TextView) zona.findViewById(R.id.txtZonaDetalle)).setText(detalle);
        ((ProgressBar) zona.findViewById(R.id.barraZona)).setProgress(porcentaje);
        contenedor.addView(zona);
    }
}
