package us.mis.acmeexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViajesFavoritosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorViaje adaptadorViaje;
    private GridLayoutManager gridLayoutManager;
    private List<Viaje> viajes_favoritos;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajesfavoritos);

        usuario = getIntent().getParcelableExtra(MainActivity.USUARIO);
        getViajesFavoritosFromUsuario();

      /*  if (DatosViajes.VIAJES == null || DatosViajes.VIAJES.isEmpty()){
            Toast.makeText(this, "No tienes viajes favoritos", Toast.LENGTH_LONG).show();

        } else {
            viajes_favoritos = DatosViajes.VIAJES.stream().filter(Viaje::isFavorito).collect(Collectors.toList());

            if(viajes_favoritos.size() == 0){
                Toast.makeText(this, "No tienes viajes favoritos", Toast.LENGTH_LONG).show();

            }else {
                recyclerView = findViewById(R.id.recyclerView_viajesFavoritos);
                adaptadorViaje = new AdaptadorViaje(viajes_favoritos, this, false);

                gridLayoutManager = new GridLayoutManager(this, 1);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adaptadorViaje);

            }
        }*/

    }

    private void getViajesFavoritosFromUsuario() {
        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
        firebaseDatabaseService.getViajesFavoritosFromUsuario(usuario.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long idCount = dataSnapshot.getChildrenCount();
                    for (DataSnapshot viajeIdDS : dataSnapshot.getChildren()) {
                        String viajeId = viajeIdDS.getValue(String.class);
                        if (viajeId != null && !viajeId.isEmpty()) {

                            firebaseDatabaseService.getViaje(viajeId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2.exists() && dataSnapshot2.getValue() != null) {
                                        ViajeDTO viajeDTO = dataSnapshot2.getValue(ViajeDTO.class);
                                        if (viajeDTO != null) {
                                            Viaje viaje = new Viaje(viajeDTO);
                                            viaje.setFavorito(true);
                                            viajes_favoritos.add(viaje);
                                        }
                                    }

                                    if (viajes_favoritos.size() == (idCount.intValue())) {
                                        montarRecyclerViewList();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError2) {
                                    Log.e("La lectura a la BD ha fallado: ", databaseError2.getMessage());
                                }
                            });

                        }
                    }
                } else {
                    montarRecyclerViewList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    private void montarRecyclerViewList() {
        if (viajes_favoritos.size() == 0) {
            Toast.makeText(this, "Aún no tienes viajes favoritos", Toast.LENGTH_LONG).show();
        }

        recyclerView = findViewById(R.id.recyclerView_viajesFavoritos);
        adaptadorViaje = new AdaptadorViaje(viajes_favoritos, this, false, usuario);

        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adaptadorViaje);
    }


  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                usuario = intent.getParcelableExtra(DatosViajes.USUARIO);
                getViajesFavoritosFromUsuario();
            }
        } else if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                boolean filtro = intent.getBooleanExtra("filtro", false);
                if(filtro) {
                    viajes_favoritos = DatosViajes.VIAJES.stream().filter(Viaje::isFavorito).collect(Collectors.toList());;
                    if(viajes_favoritos.size() == 0){
                        Toast.makeText(this, "No tienes viajes favoritos", Toast.LENGTH_LONG).show();
                    }
                    adaptadorViaje = new AdaptadorViaje(viajes_favoritos, this, true);
                    recyclerView.swapAdapter(adaptadorViaje, false);
                }
            }
        }
    }*/

}
