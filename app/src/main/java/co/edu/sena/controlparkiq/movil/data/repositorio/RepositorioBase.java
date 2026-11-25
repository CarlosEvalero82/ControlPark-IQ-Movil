package co.edu.sena.controlparkiq.movil.data.repositorio;

import co.edu.sena.controlparkiq.movil.data.api.ControlParkApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Comportamiento común de los repositorios: hacer la petición a la API
 * y entregar a la pantalla el dato o un mensaje de error.
 * Patrón Repository: las pantallas no conocen Retrofit ni la API.
 *
 * @author Carlos Eduardo Valero
 */
public abstract class RepositorioBase {

    protected final ControlParkApi api;

    protected RepositorioBase(ControlParkApi api) {
        this.api = api;
    }

    /** Ejecuta la petición en segundo plano y responde en el hilo principal. */
    protected <T> void ejecutar(Call<T> peticion, Resultado<T> resultado) {
        peticion.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> llamada, Response<T> respuesta) {
                if (respuesta.isSuccessful()) {
                    resultado.exito(respuesta.body());
                } else if (respuesta.code() == 401) {
                    resultado.sesionVencida();
                } else {
                    resultado.error(MensajesError.desdeRespuesta(respuesta.code(), respuesta.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<T> llamada, Throwable falla) {
                resultado.error(MensajesError.desdeFalla(falla));
            }
        });
    }
}
