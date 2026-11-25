package co.edu.sena.controlparkiq.movil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import co.edu.sena.controlparkiq.movil.ui.comun.ActividadBase;
import co.edu.sena.controlparkiq.movil.ui.comun.Navegador;
import co.edu.sena.controlparkiq.movil.ui.login.LoginActivity;

/**
 * Pantalla de bienvenida de ControlPark-IQ móvil (creada en la evidencia
 * GA8-220501096-AA2-EV01). Si ya hay una sesión guardada, abre directamente
 * la pantalla del rol; si no, el botón Ingresar lleva al inicio de sesión.
 *
 * @author Carlos Eduardo Valero
 */
public class MainActivity extends ActividadBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sesion.estaActiva()) {
            Navegador.irSegunRol(this, sesion.getRol());
            return;
        }

        setContentView(R.layout.activity_main);
        aplicarMargenesSistema(findViewById(R.id.main));

        Toast.makeText(this, "Bienvenido a ControlPark-IQ", Toast.LENGTH_SHORT).show();

        // Evento Listener del botón Ingresar: abre el inicio de sesión
        Button btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }
}
