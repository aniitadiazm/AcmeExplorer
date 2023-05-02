package us.mis.acmeexplorer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class FiltroActivity extends AppCompatActivity {

    private Calendar inicio_calendario;
    private Calendar fin_calendario;
    private Integer precioMax;
    private Integer precioMin;
    private TextView txt_inicio;
    private TextView txt_fin;
    private EditText txt_precioMax;
    private EditText txt_precioMin;
    Button btn_verResultados;
    Button btn_quitarFiltros;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        btn_verResultados = findViewById(R.id.btn_verResultados);
        btn_quitarFiltros = findViewById(R.id.btn_quitarFiltros);
        txt_precioMax = findViewById(R.id.editText_precioMax);
        txt_precioMin = findViewById(R.id.editText_precioMin);
        txt_inicio = findViewById(R.id.textView_fechaInicio);
        txt_fin = findViewById(R.id.textView_fechaFin);
        filtro = false;

        inicializeVariablesFromSharedPreferences();

        if (inicio_calendario != null)
            txt_inicio.setText(formatearFecha(inicio_calendario));
        if (fin_calendario != null)
            txt_fin.setText(formatearFecha(fin_calendario));
        if (precioMax != null)
            txt_precioMax.setText(precioMax.toString()+" €");
        if (precioMin != null)
            txt_precioMin.setText(precioMin.toString()+" €");

        btn_verResultados.setOnClickListener(v -> {

            String precioMaxString;
            String precioMinString;

            filtro = true;

            if(precioMax == null)
                precioMaxString = txt_precioMax.getText().toString();
            else
                precioMaxString = precioMax.toString();

            if(precioMin == null)
                precioMinString = txt_precioMin.getText().toString();
            else
                precioMinString = precioMin.toString();

            editor = sharedPreferences.edit();
            editor.putLong(DatosViajes.inicio, calendario(inicio_calendario));
            editor.putLong(DatosViajes.fin, calendario(fin_calendario));
            editor.putString(DatosViajes.precioMax, precioMaxString);
            editor.putString(DatosViajes.precioMin, precioMinString);
            editor.apply();

            onBackPressed();
        });

        btn_quitarFiltros.setOnClickListener(v -> {

            filtro = true;

            editor = sharedPreferences.edit();
            editor.putLong(DatosViajes.inicio, 0);
            editor.putLong(DatosViajes.fin, 0);
            editor.putString(DatosViajes.precioMax, "");
            editor.putString(DatosViajes.precioMin, "");
            editor.apply();

            txt_inicio.setText("dd/mm/aaaa");
            txt_fin.setText("dd/mm/aaaa");
            txt_precioMax.setHint("Máximo (€)");
            txt_precioMin.setHint("Mínimo (€)");

            onBackPressed();
        });

    }

    public static String formatearFecha(Calendar calendar){
        String fecha_formateada = "";
        int dia = calendar.get(Calendar.DAY_OF_MONTH);;
        int mes = calendar.get(Calendar.MONTH);;
        int año = calendar.get(Calendar.YEAR);;

        fecha_formateada = "" + dia + "/" + mes + "/" + año;

        return  fecha_formateada;
    }

    public static long calendario(Calendar fecha) {

        if (fecha == null)
            return 0;

        return (fecha.getTimeInMillis()/1000);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("filtro", filtro);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void inicializeVariablesFromSharedPreferences() {

        sharedPreferences = getSharedPreferences(DatosViajes.filtro, MODE_PRIVATE);
        long fechaInicioSP = sharedPreferences.getLong(DatosViajes.inicio,0);
        long fechaFinSP = sharedPreferences.getLong(DatosViajes.fin, 0);
        String precioMaxSP = sharedPreferences.getString(DatosViajes.precioMax, "");
        String precioMinSP = sharedPreferences.getString(DatosViajes.precioMin, "");
        Calendar inicioSP_calendario = Calendar.getInstance();
        Calendar finSP_calendario = Calendar.getInstance();

        inicioSP_calendario.setTimeInMillis(fechaInicioSP * 1000);
        finSP_calendario.setTimeInMillis(fechaFinSP * 1000);

        if(fechaInicioSP != 0 && fechaFinSP == 0) {
            inicio_calendario = inicioSP_calendario;

        } else if(fechaInicioSP == 0 && fechaFinSP != 0) {
            fin_calendario = finSP_calendario;

        } else if(fechaInicioSP != 0 && fechaFinSP != 0) {
            inicio_calendario = inicioSP_calendario;
            fin_calendario = finSP_calendario;
        }

        if(!precioMaxSP.equals("") && precioMinSP.equals("")) {
            precioMax = Integer.parseInt(precioMaxSP);

        } else if(precioMaxSP.equals("") && !precioMinSP.equals("")) {
            precioMin = Integer.parseInt(precioMinSP);

        } else if(!precioMaxSP.equals("") && !precioMinSP.equals("")) {
            precioMax = Integer.parseInt(precioMaxSP);
            precioMin = Integer.parseInt(precioMinSP);

        }
    }

    public void fechaFinClick(View view) {
        if(fin_calendario == null)
            fin_calendario = Calendar.getInstance();
        int yy = fin_calendario.get(Calendar.YEAR);
        int mm = fin_calendario.get(Calendar.MONTH);
        int dd = fin_calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view1, año, mes, dia) -> {
            inicio_calendario = Calendar.getInstance();
            fin_calendario.set(año, mes, dia);
            txt_fin.setText(dia + "/" + (mes+1) + "/" + año);
        }, yy, mm, dd);
        dialog.show();
    }

    public void fechaInicioClick(View view) {
        if(inicio_calendario == null)
            inicio_calendario = Calendar.getInstance();
        int yy = inicio_calendario.get(Calendar.YEAR);
        int mm = inicio_calendario.get(Calendar.MONTH);
        int dd = inicio_calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view1, año, mes, dia) -> {
            inicio_calendario = Calendar.getInstance();
            inicio_calendario.set(año, mes, dia);
            txt_inicio.setText(dia + "/" + (mes+1) + "/" + año);
        }, yy, mm, dd);
        dialog.show();
    }

}
