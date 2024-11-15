package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ImageView imageView;
    private Bitmap imageBitmap;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.captured_image);
        Button saveButton = findViewById(R.id.save_button);

        // Verificar permisos antes de abrir la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }

        saveButton.setOnClickListener(v -> {
            if (photoUri != null) {
                saveImageAndReturn();
            } else {
                Toast.makeText(this, "No hay imagen para guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Crear un archivo temporal para guardar la imagen
            File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imagen_capturada.jpg");

            // Obtener la URI usando FileProvider
            photoUri = FileProvider.getUriForFile(this, "com.example.jesusysusappostoles_proyecto1evaluacion.fileprovider", photoFile);

            // Pasar la URI al intent
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No se encontró una aplicación de cámara", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Mostrar la imagen en el ImageView desde la URI
            imageView.setImageURI(photoUri);
        } else {
            Toast.makeText(this, "Error al capturar la imagen", Toast.LENGTH_SHORT).show();
        }
    }



    private void saveImageAndReturn() {
        try {
            // Convertir la URI en un Bitmap
            InputStream inputStream = getContentResolver().openInputStream(photoUri);
            imageBitmap = BitmapFactory.decodeStream(inputStream);  // Convertir URI a Bitmap

            // Crear el archivo en el almacenamiento interno de la app
            // Obtener la fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss", Locale.getDefault());
            String currentDate = sdf.format(new Date()); 

            String nombreArchivo =currentDate+".jpg";
            Log.println(Log.DEBUG,"hola","Se ha guardado la imagen: " + nombreArchivo);
            File file = new File(getFilesDir(),nombreArchivo );
            OutputStream outputStream = new FileOutputStream(file);

            // Comprimir y guardar la imagen en el archivo
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // Cerrar los streams
            inputStream.close();
            outputStream.close();

            Toast.makeText(this, "Imagen guardada exitosamente en " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }

        // Finaliza esta actividad y regresa a MainActivity
        finish();
    }





    //METODO PARA GUARDAR LA IMAGEN
    private void saveImageAndReturn2() {
        // Ruta en el almacenamiento interno específico de la aplicación
        File directory = new File(getFilesDir(), "imagenes");
        if (!directory.exists()) {
            directory.mkdirs(); // Crea la carpeta si no existe
        }

        // Nombre y ubicación del archivo
        File file = new File(directory, "imagen_capturada.jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(this, "Imagen guardada exitosamente en " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }

        // Finaliza esta actividad y regresa a MainActivity
        finish();
    }

}
