package us.mis.acmeexplorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViajeActivity extends AppCompatActivity {

    private Viaje viaje;
    private TextView viaje_destino;
    private TextView viaje_salida;
    private TextView viaje_inicio;
    private TextView viaje_fin;
    private ImageView viaje_foto;
    private TextView viaje_regimen;
    private TextView viaje_incluido1;
    private TextView viaje_incluido2;
    private TextView viaje_incluido3;
    private TextView viaje_precio;
    private ImageView viaje_favorito;
    private ImageView btn_mapa;
    private Button btn_comprar;
    private static Context context;
    private Usuario usuarioPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viaje);

        ViajeActivity.context = getApplicationContext();
        viaje_destino = findViewById(R.id.viaje_titulo);
        viaje_salida = findViewById(R.id.viaje_salida);
        viaje_inicio = findViewById(R.id.viaje_inicio);
        viaje_fin = findViewById(R.id.viaje_fin);
        viaje_foto = findViewById(R.id.viaje_foto);
        viaje_regimen = findViewById(R.id.viaje_regimen);
        viaje_incluido1 = findViewById(R.id.viaje_incluye1);
        viaje_incluido2 = findViewById(R.id.viaje_incluye2);
        viaje_incluido3 = findViewById(R.id.viaje_incluye3);
        viaje_precio = findViewById(R.id.viaje_precio);
        viaje_favorito = findViewById(R.id.viaje_favorito);
        btn_comprar = findViewById(R.id.btn_comprar);
        btn_mapa = findViewById(R.id.verMapa);

        ViajeActivity.context = getApplicationContext();

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

        try{
            viaje = getIntent().getParcelableExtra(DatosViajes.intentViaje);
            usuarioPrincipal = getIntent().getParcelableExtra(DatosViajes.USUARIO_PRINCIPAL);

            if(viaje != null) {
                viaje_destino.setText(viaje.getDestino());
                viaje_salida.setText(viaje.getSalida());
                viaje_inicio.setText(AdaptadorViaje.formatearFecha(viaje.getInicio()));
                viaje_fin.setText(AdaptadorViaje.formatearFecha(viaje.getFin()));

                if(!viaje.getUrl().isEmpty()) {
                    Picasso.get()
                            .load(viaje.getUrl())
                            .error(R.drawable.sin_foto)
                            .into(viaje_foto);
                }
                viaje_regimen.setText(viaje.getRegimen());
                viaje_incluido1.setText((viaje.getIncluido()).get(0));
                viaje_incluido2.setText((viaje.getIncluido()).get(1));
                viaje_incluido3.setText((viaje.getIncluido()).get(2));
                viaje_precio.setText(viaje.getPrecio().toString()+"€");

                if(viaje.isFavorito()) {
                    firebaseDatabaseService.setViajeAsFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            viaje_favorito.setImageResource(R.drawable.corazon_lleno);
                        } else {
                            Log.e("AcmeExplorer", "Error al guardar viaje como favorito: " + databaseError.getMessage());
                        }
                    });
                }else{
                    firebaseDatabaseService.setViajeAsNoFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            viaje_favorito.setImageResource(R.drawable.corazon_vacio);
                        } else {
                            Log.e("AcmeExplorer", "Error al descartar viaje como favorito: " + databaseError.getMessage());
                        }
                    });
                }
            }

            viaje_favorito.setOnClickListener(view -> {
                if(viaje.isFavorito()) {
                    firebaseDatabaseService.setViajeAsNoFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            viaje_favorito.setImageResource(R.drawable.corazon_vacio);
                        } else {
                            Log.e("AcmeExplorer", "Error al descartar viaje como favorito: " + databaseError.getMessage());
                        }
                    });
                }else{
                    firebaseDatabaseService.setViajeAsFavorito(usuarioPrincipal.getId(), viaje.getId(), (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            viaje_favorito.setImageResource(R.drawable.corazon_lleno);
                        } else {
                            Log.e("AcmeExplorer", "Error al guardar viaje como favorito: " + databaseError.getMessage());
                        }
                    });
                }
            });

            btn_comprar.setOnClickListener(v ->
                    Toast.makeText(ViajeActivity.context, "En construcción", Toast.LENGTH_LONG).show());

            btn_mapa.setOnClickListener(v -> {
                Intent intent = new Intent(ViajeActivity.this, MapsDestinosActivity.class);
                intent.putExtra("latitud", viaje.getLatitud());
                intent.putExtra("longitud", viaje.getLongitud());
                startActivity(intent);
            });

        }catch (Exception e){
            Toast.makeText(this, "Se ha producido un error.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id_viaje", viaje.getId());
        intent.putExtra("viaje_favorito", viaje.isFavorito());
        setResult(RESULT_OK, intent);
        finish();
    }
}
