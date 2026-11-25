package co.edu.sena.controlparkiq.movil.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import co.edu.sena.controlparkiq.movil.R;
import co.edu.sena.controlparkiq.movil.data.modelo.LoginRespuesta;
import co.edu.sena.controlparkiq.movil.data.repositorio.AuthRepositorio;
import co.edu.sena.controlparkiq.movil.data.repositorio.Resultado;
import co.edu.sena.controlparkiq.movil.ui.comun.ActividadBase;
import co.edu.sena.controlparkiq.movil.ui.comun.Navegador;
import co.edu.sena.controlparkiq.movil.util.Validador;

/**
 * Módulo de autenticación: el usuario escribe sus credenciales, la app las
 * valida, las envía a la API y abre la pantalla que corresponde a su rol.
 *
 * @author Carlos Eduardo Valero
 */
public class LoginActivity extends ActividadBase {

    private TextInputLayout capaUsuario;
    private TextInputLayout capaContrasena;
    private TextInputEditText txtUsuario;
    private TextInputEditText txtContrasena;
    private MaterialButton btnIngresar;
    private ProgressBar progreso;
    private TextView txtEstado;
    private AuthRepositorio repositorio;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        setContentView(R.layout.activity_login);
        aplicarMargenesSistema(findViewById(R.id.raiz));

        capaUsuario = findViewById(R.id.capaUsuario);
        capaContrasena = findViewById(R.id.capaContrasena);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIngresar = findViewById(R.id.btnIngresar);
        progreso = findViewById(R.id.progreso);
        txtEstado = findViewById(R.id.txtEstado);
        repositorio = new AuthRepositorio(api, sesion);

        btnIngresar.setOnClickListener(v -> ingresar());
    }

    private void ingresar() {
        String usuario = texto(txtUsuario);
        String contrasena = texto(txtContrasena);
        capaUsuario.setError(null);
        capaContrasena.setError(null);

        String error = Validador.validarCredenciales(usuario, contrasena);
        if (error != null) {
            if (usuario.trim().isEmpty()) {
                capaUsuario.setError(error);
            } else {
                capaContrasena.setError(error);
            }
            return;
        }

        mostrarCargando(true);
        repositorio.iniciarSesion(usuario, contrasena, new Resultado<LoginRespuesta>() {
            @Override
            public void exito(LoginRespuesta datos) {
                mostrarCargando(false);
                Toast.makeText(LoginActivity.this,
                        getString(R.string.login_bienvenido, datos.getUsuario()), Toast.LENGTH_SHORT).show();
                Navegador.irSegunRol(LoginActivity.this, datos.getRol());
            }

            @Override
            public void error(String mensaje) {
                mostrarCargando(false);
                txtEstado.setVisibility(View.VISIBLE);
                txtEstado.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.cp_rojo));
                txtEstado.setText(mensaje);
            }
        });
    }

    private void mostrarCargando(boolean cargando) {
        btnIngresar.setEnabled(!cargando);
        progreso.setVisibility(cargando ? View.VISIBLE : View.GONE);
        if (cargando) {
            txtEstado.setVisibility(View.VISIBLE);
            txtEstado.setTextColor(ContextCompat.getColor(this, R.color.cp_texto_suave));
            txtEstado.setText(R.string.login_conectando);
        }
    }

    private static String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
