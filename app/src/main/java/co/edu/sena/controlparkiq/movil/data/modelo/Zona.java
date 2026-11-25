package co.edu.sena.controlparkiq.movil.data.modelo;

/** Zona del parqueadero (residentes o visitantes). */
public class Zona {

    private Long id;
    private String nombre;
    private String tipo;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
}
