package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity1 extends AppCompatActivity {
    // Hace falta para el contador de accesos
    private static final String PREFS_NAME = "AppPrefs";
    private static final String COUNTER_KEY = "LaunchCounter";
    private int launchCounter;
    private Button btn;
    private TextView txtAccesos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        // Referencia al TextView en tu layout para mostrar el contador
        txtAccesos = findViewById(R.id.counterTextView);

        // Accede a SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Lee el contador de lanzamientos
        launchCounter = preferences.getInt(COUNTER_KEY, -1);

        // Incrementa el contador
        launchCounter++;

        // Muestra el contador actualizado en el TextView
        txtAccesos.setText("Veces accedidas: " + launchCounter);

        // Guarda el contador actualizado en SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COUNTER_KEY, launchCounter);
        editor.apply(); // Aplica los cambios

    }


}
