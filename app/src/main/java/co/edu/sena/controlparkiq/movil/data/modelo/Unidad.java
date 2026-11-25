package co.edu.sena.controlparkiq.movil.data.modelo;

/** Unidad residencial (torre y apartamento) a la que pertenece un residente. */
public class Unidad {

    private Long id;
    private String torre;
    private String apartamento;

    public Long getId() {
        return id;
    }

    public String getTorre() {
        return torre;
    }

    public String getApartamento() {
        return apartamento;
    }

    /** "Torre 1 · Apto 302". */
    public String getDescripcion() {
        return "Torre " + torre + " · Apto " + apartamento;
    }
}
