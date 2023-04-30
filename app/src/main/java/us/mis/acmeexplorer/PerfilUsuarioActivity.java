package us.mis.acmeexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class PerfilUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    public static String USUARIO = "usuario";
    private ImageView iv_foto;
    private ImageView ubicacion;
    private TextView tv_nombre;
    private TextView tv_apellidos;
    private TextView tv_correo;
    private Button btn_editar;
    private Button btn_cambiarContra;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilusuario);

        iv_foto = findViewById(R.id.imageViewPerfil);
        ubicacion = findViewById(R.id.actual);
        tv_nombre = findViewById(R.id.textViewNombre);
        tv_apellidos = findViewById(R.id.textViewApellidos);
        tv_correo = findViewById(R.id.textViewCorreo);
        btn_editar = findViewById(R.id.buttonEditar);
        btn_cambiarContra = findViewById(R.id.buttonCambiarContra);

        fAuth = FirebaseAuth.getInstance();
        usuario = getIntent().getParcelableExtra(USUARIO);

        if (usuario != null) {
            tv_nombre.setText(usuario.getNombre());
            tv_apellidos.setText(usuario.getApellidos());
            tv_correo.setText(usuario.getCorreo());

            if (usuario.getFoto() != null && !usuario.getFoto().equals("")) {
                Picasso.get()
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.perfil)
                        .error(R.drawable.perfil)
                        .into(iv_foto);
            }

            ubicacion.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilUsuarioActivity.this, MapsActivity.class);
                startActivity(intent);
            });

            btn_editar.setOnClickListener(v -> {
                Intent intent = new Intent(PerfilUsuarioActivity.this, EditarUsuarioActivity.class);
                intent.putExtra(USUARIO, usuario);
                startActivity(intent);
            });

            btn_cambiarContra.setOnClickListener(v -> cambiarContraseña());

        } else {
            Toast.makeText(PerfilUsuarioActivity.this, "Error al obtener los datos del perfil del usuario", Toast.LENGTH_LONG).show();
            btn_editar.setVisibility(View.GONE);
            btn_cambiarContra.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USUARIO, usuario);
        startActivity(intent);
    }

    public void cambiarContraseña(){
        fAuth.setLanguageCode("es");
        fAuth.sendPasswordResetEmail(usuario.getCorreo()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(PerfilUsuarioActivity.this, "Se le ha enviado un correo para reestablecer la contraseña", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(PerfilUsuarioActivity.this, "No se ha podido enviar el correo para cambiar la contraseña", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
