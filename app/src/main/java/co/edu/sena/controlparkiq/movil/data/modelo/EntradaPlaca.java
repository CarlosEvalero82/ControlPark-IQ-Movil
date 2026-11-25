package co.edu.sena.controlparkiq.movil.data.modelo;

/** Datos para registrar una entrada por placa (DTO EntradaPlacaDTO del servidor). */
public class EntradaPlaca {

    private final String placa;

    public EntradaPlaca(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }
}
