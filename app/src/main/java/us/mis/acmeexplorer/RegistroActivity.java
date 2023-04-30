package us.mis.acmeexplorer;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistroActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    public static final String CORREO= "correo";
    private EditText et_correo;
    private EditText et_contra;
    private EditText et_confirm;
    private Button registro_btn;
    private Usuario usuario;
    private ImageView ver_contra;
    private Boolean viendo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        fAuth = FirebaseAuth.getInstance();

        String correoParam = getIntent().getStringExtra(CORREO);
        et_correo = findViewById(R.id.editTextCorreo);
        et_contra = findViewById(R.id.editTextContraseña);
        et_confirm = findViewById(R.id.editTextConfirmacion);
        registro_btn = findViewById(R.id.registro_btn);
        ver_contra = findViewById(R.id.ver_contraseña_registro);

        et_correo.setText(correoParam);

        ver_contra.setOnClickListener(v -> {
            if(viendo == false) {
                et_contra.setInputType(InputType.TYPE_CLASS_TEXT);
                et_confirm.setInputType(InputType.TYPE_CLASS_TEXT);
                viendo = true;
            } else if(viendo == true) {
                et_contra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                viendo = false;
            }
        });

        registro_btn.setOnClickListener(l -> {
            if (et_correo.getText().toString().length() == 0) {
                et_correo.setError("Introduzca una dirección de correo válida");
            } else if (et_contra.getText().length() == 0) {
                et_contra.setError("Introduzca una contraseña");
            } else if (et_contra.getText().length() < 6) {
                et_contra.setError("La contraseña debe tener al menos 6 caracteres");
            } else if (et_confirm.getText().length() == 0) {
                et_confirm.setError("Repita la contraseña en el campo 'confirmación contraseña'");
            } else if(!et_confirm.getText().toString().equals(et_contra.getText().toString())) {
                et_confirm.setError("Las contraseñas deben ser iguales");
            } else {
                fAuth.createUserWithEmailAndPassword(et_correo.getText().toString(), et_contra.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
                        RegistroActivity.this.finish();
                    } else if(task.getException() != null && task.getException().getClass().equals(FirebaseAuthUserCollisionException.class)) {
                        Toast.makeText(this, "Ya existe una cuenta con este correo", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

    }

}
