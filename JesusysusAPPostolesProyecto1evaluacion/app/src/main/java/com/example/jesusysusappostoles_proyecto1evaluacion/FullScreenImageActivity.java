package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class FullScreenImageActivity extends AppCompatActivity {

    private ZoomableImageView imageView;
    private Button deleteButton;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imageView = findViewById(R.id.full_screen_image);
        deleteButton = findViewById(R.id.delete_button);

        // Obtener el archivo de imagen desde el intent
        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null) {
            imageFile = new File(imagePath);
            imageView.setImageURI(Uri.fromFile(imageFile));
        }

        // Configurar el botón de borrar
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });
    }

    private void deleteImage() {
        if (imageFile != null && imageFile.exists()) {
            boolean deleted = imageFile.delete();
            if (deleted) {
                Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show();

                // Devolver el resultado a la actividad anterior
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deleted_image_path", imageFile.getAbsolutePath());
                setResult(RESULT_OK, resultIntent);

                // Finalizar la actividad
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
