package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private final int DURACION_SPLASH = 3000; // 3 segundos



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pase el tiempo, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(SplashActivity.this, AutenticacionActivity.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}