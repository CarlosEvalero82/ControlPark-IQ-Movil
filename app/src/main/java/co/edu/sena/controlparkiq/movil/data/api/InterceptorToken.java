package co.edu.sena.controlparkiq.movil.data.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Agrega a cada petición la cabecera "Authorization: Bearer token",
 * que la API exige en todos los servicios menos en el inicio de sesión.
 *
 * @author Carlos Eduardo Valero
 */
public class InterceptorToken implements Interceptor {

    private final ProveedorToken proveedor;

    public InterceptorToken(ProveedorToken proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public Response intercept(Chain cadena) throws IOException {
        Request original = cadena.request();
        String token = proveedor.getToken();
        if (token == null || token.isEmpty()) {
            return cadena.proceed(original);
        }
        Request conToken = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        return cadena.proceed(conToken);
    }
}
