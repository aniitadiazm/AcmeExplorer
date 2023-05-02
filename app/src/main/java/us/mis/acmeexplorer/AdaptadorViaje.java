package us.mis.acmeexplorer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import us.mis.acmeexplorer.Viaje;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.squareup.picasso.Picasso;

public class AdaptadorViaje extends RecyclerView.Adapter<AdaptadorViaje.ViajeViewHolder> {

    private List<Viaje> viajes;
    private Context context;
    private boolean columnasSwitch;
    private Usuario usuarioPrincipal;

    FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

    public AdaptadorViaje(List<Viaje> viajes,  Context context, boolean columnasSwitch, Usuario usuarioPrincipal) {
        this.viajes = viajes;
        this.context = context;
        this.columnasSwitch = columnasSwitch;
        this.usuarioPrincipal = usuarioPrincipal;
    }

    @NonNull
    @Override
    public ViajeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.ficha_viaje, viewGroup, false);
        return new ViajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViajeViewHolder viajeViewHolder, int i) {
        Viaje viaje = viajes.get(i);
        String inicio = formatearFecha(viaje.getInicio());
        String fin = formatearFecha(viaje.getFin());

        viajeViewHolder.destino.setText(viaje.getDestino());
        viajeViewHolder.inicio.setText(inicio);
        viajeViewHolder.fin.setText(fin);
        viajeViewHolder.precio.setText(viaje.getPrecio()+"€ por persona");

        if(!viaje.getUrl().isEmpty()) {
            Picasso.get()
                    .load(viaje.getUrl())
                    .error(R.drawable.sin_foto)
                    .into(viajeViewHolder.foto);
        }

        if(viaje.isFavorito()) {
            firebaseDatabaseService.setViajeAsFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    viajeViewHolder.corazon.setImageResource(R.drawable.corazon_lleno);
                } else {
                    Log.e("AcmeExplorer", "Error al guardar viaje como favorito: " + databaseError.getMessage());
                }
            });
        }else{
            firebaseDatabaseService.setViajeAsFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    viajeViewHolder.corazon.setImageResource(R.drawable.corazon_vacio);
                } else {
                    Log.e("AcmeExplorer", "Error al descartar viaje como favorito: " + databaseError.getMessage());
                }
            });
        }

        ImageView viaje_favorito = viajeViewHolder.corazon.findViewById(R.id.viaje_corazon);
        viaje_favorito.setOnClickListener(v -> {
            if(viaje.isFavorito()) {
                firebaseDatabaseService.setViajeAsNoFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        viajeViewHolder.corazon.setImageResource(R.drawable.corazon_vacio);
                    } else {
                        Log.e("AcmeExplorer", "Error al descartar viaje como favorito: " + databaseError.getMessage());
                    }
                });
            }else{
                firebaseDatabaseService.setViajeAsFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        viajeViewHolder.corazon.setImageResource(R.drawable.corazon_lleno);
                    } else {
                        Log.e("AcmeExplorer", "Error al guardar viaje como favorito: " + databaseError.getMessage());
                    }
                });
            }
        });

        viajeViewHolder.cardView.setOnClickListener(view -> {

            Intent intent = new Intent(context, ViajeActivity.class);
            intent.putExtra(DatosViajes.intentViaje, viaje);
            intent.putExtra("columnasSwitch", columnasSwitch);
            intent.putExtra(DatosViajes.USUARIO_PRINCIPAL, usuarioPrincipal);
            intent.putExtra("viaje_favorito", viaje.isFavorito());
            ((Activity) context).startActivityForResult(intent, 1);

        });

    }

    public class ViajeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView foto;
        private ImageView corazon;
        private TextView destino;
        private TextView precio;
        private TextView inicio;
        private TextView fin;

        public ViajeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.viaje_cardview);
            foto = itemView.findViewById(R.id.viaje_foto);
            corazon = itemView.findViewById(R.id.viaje_corazon);
            destino = itemView.findViewById(R.id.viaje_destino);
            precio = itemView.findViewById(R.id.viaje_precio);
            inicio = itemView.findViewById(R.id.viaje_inicio);
            fin = itemView.findViewById(R.id.viaje_fin);
        }
    }

    @Override
    public int getItemCount() {
        return viajes.size();
    }

    public static String formatearFecha(Calendar calendar) {

        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        String fechaFormateada = "";
        fechaFormateada =  "" + dia + "/" + mes + "/" + año;

        return fechaFormateada;

    }
}
