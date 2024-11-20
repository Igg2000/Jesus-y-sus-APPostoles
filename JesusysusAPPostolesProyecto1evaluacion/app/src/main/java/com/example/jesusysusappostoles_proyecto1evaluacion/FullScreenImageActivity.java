package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ZoomableImageView imageView = findViewById(R.id.full_screen_image);

        // Obtener la ruta de la imagen desde el Intent
        String imagePath = getIntent().getStringExtra("image_path");

        if (imagePath != null) {
            // Decodificar y mostrar la imagen
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }
    }
}
