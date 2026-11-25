package co.edu.sena.controlparkiq.movil.data.modelo;

/** Espacio de parqueo, por ejemplo R-01, con su estado y su zona. */
public class Espacio {

    private Long id;
    private String codigo;
    private String tipoVehiculo;
    private String estado;
    private Zona zona;

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public String getEstado() {
        return estado;
    }

    public Zona getZona() {
        return zona;
    }
}
