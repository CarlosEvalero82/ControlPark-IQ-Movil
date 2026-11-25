package co.edu.sena.controlparkiq.movil.data.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import co.edu.sena.controlparkiq.movil.util.SesionManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Crea el cliente de la API (Retrofit + OkHttp + Gson).
 * Patrón Singleton: la app usa una sola conexión configurada.
 *
 * @author Carlos Eduardo Valero
 */
public final class ApiClient {

    private static ControlParkApi instancia;

    private ApiClient() {
        // Fábrica estática
    }

    /** Cliente de la app, que toma el token guardado en el celular. */
    public static synchronized ControlParkApi getInstancia(Context contexto) {
        if (instancia == null) {
            SesionManager sesion = SesionManager.getInstancia(contexto);
            instancia = crear(ApiConfig.URL_BASE, sesion::getToken);
        }
        return instancia;
    }

    /**
     * Crea un cliente para cualquier URL. Lo usan las pruebas unitarias
     * con un servidor simulado (MockWebServer).
     */
    public static ControlParkApi crear(String urlBase, ProveedorToken proveedorToken) {
        HttpLoggingInterceptor registro = new HttpLoggingInterceptor();
        registro.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient http = new OkHttpClient.Builder()
                .addInterceptor(new InterceptorToken(proveedorToken))
                .addInterceptor(registro)
                .connectTimeout(ApiConfig.SEGUNDOS_CONEXION, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.SEGUNDOS_LECTURA, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(urlBase)
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ControlParkApi.class);
    }
}
