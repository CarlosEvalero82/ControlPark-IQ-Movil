package co.edu.sena.controlparkiq.movil.ui.comun;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import co.edu.sena.controlparkiq.movil.R;
import co.edu.sena.controlparkiq.movil.data.api.ApiClient;
import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import co.edu.sena.controlparkiq.movil.data.repositorio.AuthRepositorio;
import co.edu.sena.controlparkiq.movil.data.repositorio.Resultado;
import co.edu.sena.controlparkiq.movil.util.SesionManager;

/**
 * Comportamiento común de todas las pantallas de la app: acceso a la sesión
 * y a la API, márgenes del sistema, encabezado y cierre de sesión.
 *
 * @author Carlos Eduardo Valero
 */
public abstract class ActividadBase extends AppCompatActivity {

    protected SesionManager sesion;
    protected ControlParkApi api;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        // La paleta de ControlPark-IQ está pensada para el modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(estadoGuardado);
        WindowCompat.enableEdgeToEdge(getWindow());
        sesion = SesionManager.getInstancia(this);
        api = ApiClient.getInstancia(this);
    }

    /** Evita que el contenido quede debajo de la barra de estado, la de navegación o el teclado. */
    protected void aplicarMargenesSistema(View raiz) {
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (vista, margenes) -> {
            Insets barras = margenes.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return margenes;
        });
    }

    /** Llena el encabezado reutilizable (layout_encabezado.xml). */
    protected void configurarEncabezado(String titulo) {
        TextView txtTitulo = findViewById(R.id.txtTituloEncabezado);
        TextView txtUsuario = findViewById(R.id.txtUsuarioEncabezado);
        txtTitulo.setText(titulo);
        txtUsuario.setText(getString(R.string.usuario_rol, sesion.getUsuario(), Navegador.nombreRol(sesion.getRol())));
        findViewById(R.id.btnSalir).setOnClickListener(v -> confirmarCerrarSesion());
    }

    private void confirmarCerrarSesion() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.confirmar_cerrar_sesion)
                .setPositiveButton(R.string.si, (dialogo, boton) -> cerrarSesion())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    protected void cerrarSesion() {
        new AuthRepositorio(api, sesion).cerrarSesion(() -> Navegador.irAlLogin(this));
    }

    /** El token venció: se borra la sesión y se vuelve al inicio de sesión. */
    public void alVencerSesion() {
        mostrarMensaje(Resultado.MENSAJE_SESION_VENCIDA);
        sesion.cerrar();
        Navegador.irAlLogin(this);
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
