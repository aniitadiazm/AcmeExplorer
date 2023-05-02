package us.mis.acmeexplorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarViajesActivity extends AppCompatActivity {

    private RecyclerView recyclerView_viajes;
    private AdaptadorViaje adaptadorViaje;
    private Switch switch_nColumnas;
    private ImageView img_nColumnas;
    private GridLayoutManager gridLayoutManager;
    private SharedPreferences sharedPreferencesFiltro;
    List<Viaje> viajes = new ArrayList<>();
    List<Viaje> viajesFiltrados = new ArrayList<>();
    private Usuario usuarioPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscarviajes);

        recyclerView_viajes = findViewById(R.id.recyclerView_viajes);
        switch_nColumnas = findViewById(R.id.switch_nColumnas);
        img_nColumnas = findViewById(R.id.img_nColumnas);

        sharedPreferencesFiltro = getSharedPreferences(DatosViajes.filtro, MODE_PRIVATE);
        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

        if(firebaseDatabaseService.getAllViajes() == null){
            DatosViajes.VIAJES = Viaje.generarViajes(40);
            for(int i=0; i<DatosViajes.VIAJES.size(); i++){
                firebaseDatabaseService.guardarViaje(DatosViajes.VIAJES.get(i), (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Log.i("AcmeExplorer", "Los viajes se han creado correctamente");
                    } else {
                        Log.e("AcmeExplorer", "Error al crear los viaje: " + databaseError.getMessage());
                    }
                });
            }
        }

        usuarioPrincipal = getIntent().getParcelableExtra(MainActivity.USUARIO_PRINCIPAL);
        firebaseDatabaseService.getUsuario(usuarioPrincipal.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UsuarioDTO usuarioDTO = dataSnapshot.getValue(UsuarioDTO.class);
                    if (usuarioDTO != null) {
                        usuarioPrincipal = new Usuario(usuarioDTO);
                        obtenerViajes();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });

        /*adaptadorViaje = new AdaptadorViaje(viajes, this, true, usuarioPrincipal);
        switch_nColumnas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int spanCount = isChecked ? 2 : 1;
                gridLayoutManager.setSpanCount(spanCount);
                if(spanCount == 2){
                    img_nColumnas.setImageResource(R.drawable.img_1columna);
                } else {
                    img_nColumnas.setImageResource(R.drawable.img_2columnas);
                }
            }
        });
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView_viajes.setLayoutManager(gridLayoutManager);
        recyclerView_viajes.setAdapter(adaptadorViaje);*/
    }

    private void obtenerViajes() {
        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
        firebaseDatabaseService.getAllViajes().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    viajes.clear();
                    for (DataSnapshot viajeDS : dataSnapshot.getChildren()) {
                        ViajeDTO viajeDTO = viajeDS.getValue(ViajeDTO.class);
                        if (viajeDTO != null) {
                            Viaje viaje = new Viaje(viajeDTO);
                            viaje.setFavorito(usuarioPrincipal.getViajesFavoritos().containsValue(viaje.getId()));
                            viajes.add(viaje);
                        }
                    }
                    viajesFiltrados = comprobarFiltros(viajes);

                    adaptadorViaje = new AdaptadorViaje(viajesFiltrados, BuscarViajesActivity.this, true, usuarioPrincipal);
                    switch_nColumnas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int spanCount = isChecked ? 2 : 1;
                            gridLayoutManager.setSpanCount(spanCount);
                            if(spanCount == 2){
                                img_nColumnas.setImageResource(R.drawable.img_1columna);
                            } else {
                                img_nColumnas.setImageResource(R.drawable.img_2columnas);
                            }
                        }
                    });
                    gridLayoutManager = new GridLayoutManager(BuscarViajesActivity.this, 1);
                    recyclerView_viajes.setLayoutManager(gridLayoutManager);
                    recyclerView_viajes.setAdapter(adaptadorViaje);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("La lectura de la BD ha fallado: ", databaseError.getMessage());
            }
        });
    }


    public List<Viaje> comprobarFiltros(List<Viaje> VIAJES) {

        List<Viaje> viajesFiltrados = null;
        Calendar fechaInicio = Calendar.getInstance(), fechaFin = Calendar.getInstance();
        int maxPrecio, minPrecio;

        long inicio = sharedPreferencesFiltro.getLong(DatosViajes.inicio,0);
        long fin = sharedPreferencesFiltro.getLong(DatosViajes.fin, 0);
        String precioMax = sharedPreferencesFiltro.getString(DatosViajes.precioMax, "");
        String precioMin = sharedPreferencesFiltro.getString(DatosViajes.precioMin, "");

        fechaInicio.setTimeInMillis(inicio * 1000);
        fechaFin.setTimeInMillis(fin * 1000);

        if(inicio != 0 && fin == 0){
            viajesFiltrados = VIAJES.stream().filter(t -> t.getInicio().after(fechaInicio)).collect(Collectors.toList());

        }else if(inicio == 0 && fin != 0) {
            viajesFiltrados = VIAJES.stream().filter(t -> t.getFin().before(fechaFin)).collect(Collectors.toList());

        } else if(inicio != 0) {
            viajesFiltrados = VIAJES.stream().filter(t -> t.getInicio().after(fechaInicio) && t.getFin().before(fechaFin)).collect(Collectors.toList());

        } else{
            viajesFiltrados = VIAJES;
        }

        if(!precioMax.equals("") && precioMin.equals("")){
            maxPrecio = Integer.parseInt(precioMax);
            viajesFiltrados = viajesFiltrados.stream().filter(t -> t.getPrecio() < maxPrecio).collect(Collectors.toList());

        }else if(precioMax.equals("") && !precioMin.equals("")) {
            minPrecio = Integer.parseInt(precioMin);
            viajesFiltrados = viajesFiltrados.stream().filter(t -> t.getPrecio() > minPrecio).collect(Collectors.toList());

        } else if(!precioMax.equals("") && !precioMin.equals("")) {
            maxPrecio = Integer.parseInt(precioMax);
            minPrecio = Integer.parseInt(precioMin);
            viajesFiltrados = viajesFiltrados.stream().filter(t -> t.getPrecio() > minPrecio &&
                    t.getPrecio() < maxPrecio).collect(Collectors.toList());
        }

        return viajesFiltrados;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                usuarioPrincipal = intent.getParcelableExtra(DatosViajes.USUARIO_PRINCIPAL);
                obtenerViajes();
            }
        } else if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                boolean filtro = intent.getBooleanExtra("filtro", false);
                if(filtro) {
                    viajesFiltrados = comprobarFiltros(viajes);
                    if(viajesFiltrados.size() == 0){
                        Toast.makeText(this, "Lo sentimos, no hay viajes para los filtros establecidos", Toast.LENGTH_LONG).show();
                    }
                    adaptadorViaje = new AdaptadorViaje(viajesFiltrados, this, true, usuarioPrincipal);
                    recyclerView_viajes.swapAdapter(adaptadorViaje, false);
                }
            }
        }
    }

    public void abrirFiltroActivity(View view) {
        Intent intent = new Intent(BuscarViajesActivity.this, FiltroActivity.class);
        intent.putExtra("Filtro", false);
        startActivityForResult(intent, 2);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USUARIO_PRINCIPAL, usuarioPrincipal);
        startActivity(intent);
    }
}