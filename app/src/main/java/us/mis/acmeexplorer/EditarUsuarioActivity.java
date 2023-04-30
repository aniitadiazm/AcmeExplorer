package us.mis.acmeexplorer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

public class EditarUsuarioActivity extends AppCompatActivity {

    private ImageView foto;
    private EditText nombre;
    private EditText apellidos;
    private TextView correo;
    private Button guardar;
    private Usuario usuario;
    private static final int CAMERA_PERMISSION_REQUEST = 0x512;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 0x513;
    private static final int TAKE_PHOTO_CODE = 0x514;
    private String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarusuario);

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

        usuario = getIntent().getParcelableExtra(DatosViajes.USUARIO);
        nombre = findViewById(R.id.editTextNombre);
        apellidos = findViewById(R.id.editTextApellidos);
        correo = findViewById(R.id.textViewCorreo);
        foto = findViewById(R.id.img_perfilusuario);
        guardar = findViewById(R.id.buttonGuardar);

        if (usuario != null) {
            if (usuario.getNombre() != null && !usuario.getNombre().equals("")) {
                nombre.setText(usuario.getNombre());
            }
            if (usuario.getApellidos() != null && !usuario.getApellidos().equals("")) {
                apellidos.setText(usuario.getApellidos());
            }
            correo.setText(usuario.getCorreo());
            if (usuario.getFoto() != null && !usuario.getFoto().equals("")) {
                Picasso.get()
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.perfil)
                        .error(R.drawable.perfil)
                        .into(foto);
            }
        } else {
            Toast.makeText(this, "Error al cargar los datos del usuario", Toast.LENGTH_LONG).show();
            guardar.setVisibility(View.GONE);
        }

        foto.setOnClickListener(l -> sacarFoto());

        guardar.setOnClickListener(v -> {
            if (!errorCamposVacios()) {
                usuario.setNombre(nombre.getText().toString());
                usuario.setApellidos(apellidos.getText().toString());
            }
            firebaseDatabaseService.guardarUsuario(usuario, ((databaseError, databaseReference) -> {
                if (databaseError == null) {
                    Log.i("AcmeExplorer", "El usuario se ha actualizado correctamente: " + usuario.getId());
                    Intent intent = new Intent(this, PerfilUsuarioActivity.class);
                    intent.putExtra(PerfilUsuarioActivity.USUARIO, usuario);
                    startActivity(intent);
                } else {
                    Log.i("AcmeExplorer", "Error al actualizar el usuario: " + databaseError.getMessage());
                }
            }));
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            File fileFoto = new File(file);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("usuarios").child(FirebaseAuth.getInstance().getUid()).child(fileFoto.getName());
            UploadTask uploadTask = storageReference.putFile(Uri.fromFile(fileFoto));
            uploadTask.addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Log.e("AcmeExplorer", "Firebase Storage: completado " + task1.getResult().getTotalByteCount());

                    storageReference.getDownloadUrl().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            usuario.setFoto(task2.getResult().toString());
                            FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

                            firebaseDatabaseService.guardarUsuario(usuario, ((databaseError, databaseReference) -> {
                                if (databaseError == null) {
                                    Log.i("AcmeExplorer", "La foto de perfil se ha actualizado correctamente");
                                    Picasso.get()
                                            .load(usuario.getFoto())
                                            .placeholder(R.drawable.perfil)
                                            .error(R.drawable.perfil)
                                            .into(foto);
                                } else {
                                    Log.i("AcmeExplorer", "Error al actualizar el usuario: " + databaseError.getMessage());
                                }
                            }));
                        }
                    });
                }
            });
            uploadTask.addOnFailureListener(e -> Log.e("AcmeExplorer", "Error de Firebase Storage: " + e.getMessage()));
        }
    }

    private void sacarFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(foto, "Se necesita acceso a la cámara del dispositivo", BaseTransientBottomBar.LENGTH_LONG).setAction("Permitir", click -> {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(foto, "Se necesita acceso al almacenamiento del dispositivo", BaseTransientBottomBar.LENGTH_LONG).setAction("Permitir", click -> {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                    });
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                }
            } else {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                String dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/misAcmeExplorerApp";
                File newFile = new File(dir);
                newFile.mkdirs();

                file = dir + "/" + Calendar.getInstance().getTimeInMillis() + ".png";
                File newFileFoto = new File(file);
                try {
                    newFileFoto.createNewFile();
                } catch (Exception ignore) {
                }

                Uri outputFileDir = Uri.fromFile(newFileFoto);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileDir);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        }
    }

    public boolean errorCamposVacios() {
        boolean result = false;

        if (nombre.getText().toString().isEmpty()) {
            nombre.setError("El nombre no es válido");
            result = true;
        }
        if (apellidos.getText().toString().isEmpty()) {
            apellidos.setError("Los apellidos no son válidos");
            result = true;
        }

        return result;
    }


}
