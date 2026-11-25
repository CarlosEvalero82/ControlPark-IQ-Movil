package co.edu.sena.controlparkiq.movil.ui.comun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.sena.controlparkiq.movil.R;
import co.edu.sena.controlparkiq.movil.data.modelo.Espacio;
import co.edu.sena.controlparkiq.movil.data.modelo.Persona;
import co.edu.sena.controlparkiq.movil.data.modelo.Sesion;
import co.edu.sena.controlparkiq.movil.data.modelo.Vehiculo;
import co.edu.sena.controlparkiq.movil.util.Formato;

/**
 * Adaptador reutilizable para mostrar sesiones de parqueo en una lista.
 * Portería lo usa con el botón de salida; el residente, solo para consultar.
 * Patrón Adapter (RecyclerView.Adapter).
 *
 * @author Carlos Eduardo Valero
 */
public class SesionAdapter extends RecyclerView.Adapter<SesionAdapter.VistaSesion> {

    /** Acción que se ejecuta al tocar "Registrar salida". */
    public interface AlRegistrarSalida {
        void registrarSalida(Sesion sesion);
    }

    private final List<Sesion> sesiones = new ArrayList<>();
    private final AlRegistrarSalida accionSalida;

    /** @param accionSalida acción del botón de salida, o null para una lista solo de consulta */
    public SesionAdapter(AlRegistrarSalida accionSalida) {
        this.accionSalida = accionSalida;
    }

    public void actualizar(List<Sesion> nuevas) {
        sesiones.clear();
        if (nuevas != null) {
            sesiones.addAll(nuevas);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VistaSesion onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_sesion, padre, false);
        return new VistaSesion(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull VistaSesion vista, int posicion) {
        vista.mostrar(sesiones.get(posicion));
    }

    @Override
    public int getItemCount() {
        return sesiones.size();
    }

    class VistaSesion extends RecyclerView.ViewHolder {

        private final TextView txtPlaca;
        private final TextView txtTipoPersona;
        private final TextView txtVehiculo;
        private final TextView txtEspacio;
        private final TextView txtHoras;
        private final MaterialButton btnSalida;

        VistaSesion(View vista) {
            super(vista);
            txtPlaca = vista.findViewById(R.id.txtPlaca);
            txtTipoPersona = vista.findViewById(R.id.txtTipoPersona);
            txtVehiculo = vista.findViewById(R.id.txtVehiculo);
            txtEspacio = vista.findViewById(R.id.txtEspacio);
            txtHoras = vista.findViewById(R.id.txtHoras);
            btnSalida = vista.findViewById(R.id.btnSalida);
        }

        void mostrar(Sesion sesion) {
            Context contexto = itemView.getContext();
            Vehiculo vehiculo = sesion.getVehiculo();
            Espacio espacio = sesion.getEspacio();

            txtPlaca.setText(vehiculo != null ? vehiculo.getPlaca() : "");

            boolean visitante = sesion.esDeVisitante();
            txtTipoPersona.setText(visitante ? R.string.sesion_visitante : R.string.sesion_residente);
            txtTipoPersona.setBackgroundColor(ContextCompat.getColor(contexto,
                    visitante ? R.color.cp_etiqueta_visitante : R.color.cp_etiqueta_residente));

            String descripcion = "";
            if (vehiculo != null) {
                descripcion = vehiculo.getDescripcion();
                Persona propietario = vehiculo.getPropietario();
                if (propietario != null) {
                    descripcion += " · " + propietario.getNombreCompleto();
                }
            }
            txtVehiculo.setText(descripcion);

            if (espacio != null) {
                String zona = espacio.getZona() != null ? espacio.getZona().getNombre() : "";
                txtEspacio.setText(contexto.getString(R.string.sesion_espacio, espacio.getCodigo(), zona));
            }

            if (sesion.estaAbierta()) {
                long minutos = Formato.minutosDesde(sesion.getHoraEntrada(), new Date());
                txtHoras.setText(contexto.getString(R.string.sesion_entrada_abierta,
                        Formato.hora(sesion.getHoraEntrada()), Formato.duracion(minutos)));
            } else {
                txtHoras.setText(contexto.getString(R.string.sesion_entrada_cerrada,
                        Formato.fechaHora(sesion.getHoraEntrada()), Formato.hora(sesion.getHoraSalida())));
            }

            boolean conSalida = accionSalida != null && sesion.estaAbierta();
            btnSalida.setVisibility(conSalida ? View.VISIBLE : View.GONE);
            btnSalida.setOnClickListener(conSalida ? v -> accionSalida.registrarSalida(sesion) : null);
        }
    }
}
