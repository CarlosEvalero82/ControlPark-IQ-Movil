package co.edu.sena.controlparkiq.movil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Actividad principal de ControlPark-IQ móvil.
 * Muestra la pantalla de bienvenida (activity_main.xml) y un mensaje Toast.
 * Evidencia GA8-220501096-AA2-EV01.
 *
 * @author Carlos Eduardo Valero
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        // Une esta actividad con su diseño XML
        setContentView(R.layout.activity_main);

        // Respeta las barras del sistema (estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mensaje emergente (Toast) al abrir la aplicación
        Toast.makeText(this, "Bienvenido a ControlPark-IQ", Toast.LENGTH_SHORT).show();

        // Evento Listener del botón Ingresar
        Button btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(v ->
                Toast.makeText(this, "¡Hola, ADSO! Primera app Android de ControlPark-IQ",
                        Toast.LENGTH_LONG).show());
    }
}