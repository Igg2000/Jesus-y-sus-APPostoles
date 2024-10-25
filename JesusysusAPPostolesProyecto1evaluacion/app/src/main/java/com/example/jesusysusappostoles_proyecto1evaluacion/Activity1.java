package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity1 extends AppCompatActivity {

    private Button btn;
    private TextView txt;
    private static final String PREFS_NAME = "AppPreferences";
    private static final String ACCESS_COUNT_KEY = "contador_accesos";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        // Inicializamos el campo de texto
        txt = findViewById(R.id.campoDeTexto);

        // Obtenemos SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Cargamos el valor almacenado del contador, comenzando en 0 si es la primera vez
        int accessCount = preferences.getInt(ACCESS_COUNT_KEY, -1); // Cambiado a 0

        // Incrementamos el contador
        accessCount++;

        // Mostramos el valor en el TextView (REQ-011 y REQ-013)
        txt.setText("Número de accesos a la app: " + accessCount);

        // Guardamos el nuevo valor en SharedPreferences (REQ-016 y REQ-020)
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ACCESS_COUNT_KEY, accessCount); // Guardamos el nuevo valor
        editor.apply(); // Aplicamos los cambios
    }
}