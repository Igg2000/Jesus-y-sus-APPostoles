package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    ImageView imageView;

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
        imageView = findViewById(R.id.imageView);
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

                rotateImageView();
            }

            lastTimestamp = currentTimestamp;  // Actualizamos el tiempo del último evento
        }


    }

    private void rotateImageView() {
        // Calcular los ángulos de rotación en grados alrededor de los ejes X, Y y Z
        final float rotationX = totalX * (180 / (float) Math.PI); // Convertimos la rotación en radianes a grados (eje X)
        final float rotationY = totalY * (180 / (float) Math.PI); // Convertimos la rotación en radianes a grados (eje Y)
        final float rotationZ = totalZ * (180 / (float) Math.PI); // Convertimos la rotación en radianes a grados (eje Z)

        // Si no hay movimiento detectado (por ejemplo, si la diferencia es menor que un umbral), reiniciar la rotación
        if (Math.abs(rotationX - imageView.getRotationX()) < 1f &&
                Math.abs(rotationY - imageView.getRotationY()) < 1f &&
                Math.abs(rotationZ - imageView.getRotation()) < 1f) {
            // Si los valores son iguales, significa que la imagen no está en movimiento, y podemos restablecer la rotación
            totalX = 0;
            totalY = 0;
            totalZ = 0;
        }

        // Usamos ValueAnimator para interpolar entre el valor actual y el nuevo valor de rotación
        ValueAnimator animatorX = ValueAnimator.ofFloat(imageView.getRotationX(), rotationX);
        animatorX.setDuration(100);  // Duración de la animación (ajústalo según tus necesidades)
        animatorX.addUpdateListener(animation -> imageView.setRotationX((Float) animation.getAnimatedValue()));

        ValueAnimator animatorY = ValueAnimator.ofFloat(imageView.getRotationY(), rotationY);
        animatorY.setDuration(100);  // Duración de la animación (ajústalo según tus necesidades)
        animatorY.addUpdateListener(animation -> imageView.setRotationY((Float) animation.getAnimatedValue()));

        ValueAnimator animatorZ = ValueAnimator.ofFloat(imageView.getRotation(), rotationZ);
        animatorZ.setDuration(100);  // Duración de la animación (ajústalo según tus necesidades)
        animatorZ.addUpdateListener(animation -> imageView.setRotation((Float) animation.getAnimatedValue()));

        // Iniciar las animaciones
        animatorX.start();
        animatorY.start();
        animatorZ.start();
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
