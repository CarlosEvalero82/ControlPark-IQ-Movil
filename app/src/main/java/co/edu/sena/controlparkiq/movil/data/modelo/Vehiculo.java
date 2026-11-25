package co.edu.sena.controlparkiq.movil.data.modelo;

/** Vehículo registrado (carro o moto) con su propietario. */
public class Vehiculo {

    private Long id;
    private String placa;
    private String tipo;
    private String marca;
    private String color;
    private Persona propietario;

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMarca() {
        return marca;
    }

    public String getColor() {
        return color;
    }

    public Persona getPropietario() {
        return propietario;
    }

    /** "Renault Duster · Gris". */
    public String getDescripcion() {
        return marca + " · " + color;
    }
}
