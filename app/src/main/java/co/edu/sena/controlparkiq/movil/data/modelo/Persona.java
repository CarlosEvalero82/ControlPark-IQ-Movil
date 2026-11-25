package co.edu.sena.controlparkiq.movil.data.modelo;

/** Persona registrada en el conjunto: residente o visitante. */
public class Persona {

    public static final String TIPO_VISITANTE = "VISITANTE";

    private Long id;
    private String documento;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String tipo;
    private Unidad unidad;

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTipo() {
        return tipo;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public boolean esVisitante() {
        return TIPO_VISITANTE.equals(tipo);
    }
}
