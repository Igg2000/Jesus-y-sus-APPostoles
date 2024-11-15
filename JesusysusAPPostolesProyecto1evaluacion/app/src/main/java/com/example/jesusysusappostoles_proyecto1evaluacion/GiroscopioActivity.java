package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GiroscopioActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gyroSensor;
    TextView textoZ;
    TextView textoY;
    TextView textoX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giroscopioactivity); // Asegúrate de que este archivo XML exista


        // Inicializar SensorManager y obtener el giroscopio
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Verificar si el dispositivo tiene un giroscopio
        if (gyroSensor == null) {
            // Mostrar mensaje al usuario
            Toast.makeText(this, "El dispositivo no tiene un giroscopio disponible.", Toast.LENGTH_LONG).show();
            // Finalizar la actividad si no hay giroscopio
            finish();
            return;
        }

        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);


        textoX = findViewById(R.id.txtX);
        textoY = findViewById(R.id.txtY);
        textoZ = findViewById(R.id.txtZ);
    }


    private long lastTimestamp = 0;  // Variable para almacenar el tiempo anterior
    private float totalX = 0;  // Rotación acumulada en X
    private float totalY = 0;  // Rotación acumulada en Y
    private float totalZ = 0;  // Rotación acumulada en Z

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            long currentTimestamp = System.nanoTime();  // Tiempo actual en nanosegundos

            // Calculamos el tiempo transcurrido entre las lecturas en segundos
            if (lastTimestamp != 0) {
                float deltaTime = (currentTimestamp - lastTimestamp) / 1000000000.0f; // convertimos nanosegundos a segundos

                // Calculamos la rotación acumulada (integración de la velocidad angular)
                totalX += event.values[0] * deltaTime;
                totalY += event.values[1] * deltaTime;
                totalZ += event.values[2] * deltaTime;

                // Mostramos los valores
                textoX.setText(String.format("X: %.3f", totalX));
                textoY.setText(String.format("Y: %.3f", totalY));
                textoZ.setText(String.format("Z: %.3f", totalZ));
            }

            lastTimestamp = currentTimestamp;  // Actualizamos el tiempo del último evento
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

}
