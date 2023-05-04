package us.mis.acmeexplorer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth fAuth;
    private ImageView cerrarSesion;
    private ImageView imageView_buscar;
    private ImageView imageView_favoritos;
    private ImageView iv_perfil;
    private TextView txt_perfil;
    public static Usuario usuario;
    public static String USUARIO_PRINCIPAL = "usuarioPrincipal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("799577653974-q481mam10uipplgqeuujhb4jih1i6p9l.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        cerrarSesion = findViewById(R.id.cerrarSesion);
        imageView_buscar = findViewById(R.id.imageView_buscar);
        imageView_favoritos = findViewById(R.id.imageView_favoritos);
        iv_perfil = findViewById(R.id.iv_perfil);
        txt_perfil = findViewById(R.id.txt_perfil);

        usuario = getIntent().getParcelableExtra(USUARIO_PRINCIPAL);

        if(usuario != null && usuario.getNombre() != null && !usuario.getNombre().equals("")) {
            txt_perfil.setText(usuario.getNombre());
        } else if (usuario != null) {
            txt_perfil.setText(usuario.getCorreo());
        }

        if (usuario != null && usuario.getFoto() != null && !usuario.getFoto().equals("")) {
            Picasso.get()
                    .load(usuario.getFoto())
                    .placeholder(R.drawable.perfil)
                    .error(R.drawable.perfil)
                    .into(iv_perfil);
        }

        cerrarSesion.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("¿Quiere terminar la sesión?")
                    .setPositiveButton("Cerrar sesión", ((dialog, which) -> {
                        fAuth.signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, AutenticacionActivity.class));
                    })).setNegativeButton("Cancelar", (dialog, which) -> {
                    }).show();
        });

        imageView_buscar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BuscarViajesActivity.class);
            intent.putExtra(PerfilUsuarioActivity.USUARIO_PRINCIPAL, usuario);
            startActivity(intent);
        });

        imageView_favoritos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViajesFavoritosActivity.class);
            intent.putExtra(PerfilUsuarioActivity.USUARIO_PRINCIPAL, usuario);
            startActivity(intent);
        });

        iv_perfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilUsuarioActivity.class);
            intent.putExtra(PerfilUsuarioActivity.USUARIO_PRINCIPAL, usuario);
            startActivity(intent);
        });

        txt_perfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilUsuarioActivity.class);
            intent.putExtra(PerfilUsuarioActivity.USUARIO_PRINCIPAL, usuario);
            startActivity(intent);
        });

    }

}