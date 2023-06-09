package us.mis.acmeexplorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AutenticacionActivity extends AppCompatActivity {

    FirebaseUser usuario;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth fAuth;
    FirebaseAuth.AuthStateListener listener;
    private EditText et_correo;
    private EditText et_contra;
    private Button btn_Correo;
    private Button btn_Google;
    private Button btn_registro;
    private ImageView ver_contra;
    private Boolean viendo = false;
    private ProgressBar progressBar;
    private static final int RC_SIGN_IN = 121;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        fAuth = FirebaseAuth.getInstance();
        et_correo = findViewById(R.id.editTextCorreoLogin);
        et_contra = findViewById(R.id.editTextContraseñaLogin);
        btn_Correo = findViewById(R.id.btn_correo);
        btn_Google = findViewById(R.id.btn_google);
        btn_registro = findViewById(R.id.btn_registrar);
        ver_contra = findViewById(R.id.ver_contraseña);
        progressBar = findViewById(R.id.login_progress);
        progressBar.setVisibility(View.GONE);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("799577653974-q481mam10uipplgqeuujhb4jih1i6p9l.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        listener = firebaseAuth -> {
            usuario = fAuth.getCurrentUser();
            Toast.makeText(getApplicationContext(), "Bienvenido a Acme Explorer", Toast.LENGTH_SHORT).show();
        };

        ver_contra.setOnClickListener(v -> {
            if(viendo == false) {
                et_contra.setInputType(InputType.TYPE_CLASS_TEXT);
                ver_contra.setImageResource(R.drawable.no_visibilidad);
                viendo = true;
            } else if(viendo == true) {
                et_contra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ver_contra.setImageResource(R.drawable.visibilidad);
                viendo = false;
            }
        });

        btn_Correo.setOnClickListener(l -> attempAuthCorreo());
        btn_Google.setOnClickListener(l -> attempAuthGoogle(googleSignInOptions));

        btn_registro.setOnClickListener(v -> {
            Intent intent = new Intent(AutenticacionActivity.this, RegistroActivity.class);
            intent.putExtra(RegistroActivity.CORREO, et_correo.getText().toString());
            startActivity(intent);
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        fAuth.addAuthStateListener(listener);
    }


    private void attempAuthCorreo() {

        progressBar.setVisibility(View.VISIBLE);

        if (et_correo.getText().length() == 0) {
            et_correo.setError("Introduzca la dirección de correo");
            progressBar.setVisibility(View.GONE);

        } else if(et_contra.getText().length() == 0) {
            et_contra.setError("Introduzca la contraseña");
            progressBar.setVisibility(View.GONE);

        } else {
            iniciarSesionCorreo();
        }

    }

    private void attempAuthGoogle(GoogleSignInOptions googleSignInOptions) {

        progressBar.setVisibility(View.VISIBLE);
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void iniciarSesionCorreo() {

        if (fAuth == null){
            fAuth = FirebaseAuth.getInstance();
        }

        if (fAuth != null) {
            fAuth.signInWithEmailAndPassword(et_correo.getText().toString(), et_contra.getText().toString()).addOnCompleteListener(this, task -> {

                if (!task.isSuccessful() || task.getResult().getUser() == null) {
                    ocultarLoginButton(false);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Autenticación incorrecta", Toast.LENGTH_SHORT).show();
                }
                else if(!task.getResult().getUser().isEmailVerified()) {
                    mostrarErrorVerificarCuenta(task.getResult().getUser());
                    progressBar.setVisibility(View.GONE);
                } else {
                    FirebaseUser usuario = task.getResult().getUser();
                    comprobarUsuarioBD(usuario);
                }
            });
        } else {
            mostrarErrorGooglePlayServices();
        }

    }

    private void ocultarLoginButton(boolean ocultar) {

        TransitionSet transitionSet = new TransitionSet();
        Transition layoutFade = new AutoTransition();
        layoutFade.setDuration(1000);
        transitionSet.addTransition(layoutFade);

        if (ocultar) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.autenticacion_main_layout), transitionSet);
            progressBar.setVisibility(View.VISIBLE);
            btn_Correo.setVisibility(View.GONE);
            btn_Google.setVisibility(View.GONE);
            btn_registro.setVisibility(View.GONE);
            et_correo.setEnabled(false);
            et_contra.setEnabled(false);
        } else {
            TransitionManager.beginDelayedTransition(findViewById(R.id.autenticacion_main_layout), transitionSet);
            progressBar.setVisibility(View.GONE);
            btn_Correo.setVisibility(View.VISIBLE);
            btn_Google.setVisibility(View.VISIBLE);
            btn_registro.setVisibility(View.VISIBLE);
            et_correo.setEnabled(true);
            et_contra.setEnabled(true);
        }
    }

    private void mostrarErrorVerificarCuenta(FirebaseUser usuario) {

        ocultarLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Aún no ha validado su cuenta. ¿Quiere recibir un correo de verificación?")
                .setPositiveButton("Enviar correo", ((dialog, which) -> {
                    usuario.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Snackbar.make(et_correo, "Se ha enviado un correo de verificación a su cuenta", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(et_correo, "No se ha podido enviar un correo de verificación a su cuenta", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                })).setNegativeButton("Cancelar", (dialog, which) -> {
                }).show();

    }

    private void comprobarUsuarioBD(FirebaseUser firebaseUsuario) {

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

        firebaseDatabaseService.getUsuario(firebaseUsuario.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    UsuarioDTO usuarioDTO = dataSnapshot.getValue(UsuarioDTO.class);
                    if (usuarioDTO != null) {
                        Usuario usuario = new Usuario(usuarioDTO);
                        Intent intent = new Intent(AutenticacionActivity.this, MainActivity.class);
                        intent.putExtra(MainActivity.USUARIO_PRINCIPAL, usuario);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AutenticacionActivity.this, "Error 2 al obtener el usuario de la BD", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Usuario usuario = new Usuario(firebaseUsuario);
                    guardarUsuarioDB(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AutenticacionActivity.this, "Error 1 al obtener el usuario de la BD", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void guardarUsuarioDB(Usuario usuario) {
        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

        firebaseDatabaseService.guardarUsuario(usuario, (databaseError, databaseReference) -> {
            if (databaseError == null) {
                Log.i("AcmeExplorer", "El usuario se ha guardado correctamente: " + usuario.getId());
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.USUARIO_PRINCIPAL, usuario);
                startActivity(intent);
            } else {
                Log.e("AcmeExplorer", "Error al guardar el usuario en la BD: " + databaseError.getMessage());
            }
        });
    }

    private void mostrarErrorGooglePlayServices() {

        Snackbar.make(btn_registro, "Debe actualizar Google Play Services", Snackbar.LENGTH_LONG).setAction("Actualizar", view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms")));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> result = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = result.getResult(ApiException.class);
                assert account != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                if (fAuth == null) {
                    fAuth = FirebaseAuth.getInstance();
                }
                if (fAuth != null) {
                    fAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            comprobarUsuarioBD(user);
                        } else {
                            ocultarLoginButton(false);
                            Toast.makeText(this, "Error al autenticarse con google", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    mostrarErrorGooglePlayServices();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Error "+e.getMessage()+ ": "+e.getCause() , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
