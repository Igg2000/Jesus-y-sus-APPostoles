package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GaleriaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<File> imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeriaactivity);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        imageFiles = loadImagesFromStorage();

        if (imageFiles.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No hay ninguna imagen");
            emptyMessage.setTextColor(Color.LTGRAY);
            emptyMessage.setGravity(Gravity.CENTER);
            emptyMessage.setTextSize(24);
            setContentView(emptyMessage);
        } else {
            imageAdapter = new ImageAdapter(this, imageFiles);
            recyclerView.setAdapter(imageAdapter);
        }
    }


    //Método para cargar las imágenes desde el almacenamiento interno de la aplicación
    private List<File> loadImagesFromStorage() {
        List<File> imageList = new ArrayList<>();

        //Obtener todos los archivos del directorio de la app
        File directory = getFilesDir();  //Directorio privado de la app
        File[] files = directory.listFiles();  //Listar todos los archivos en ese directorio

        if (files != null) {
            for (File file : files) {
                //Filtrar solo las imágenes (por ejemplo, aquellos con extensión .jpg)
                if (file.getName().endsWith(".jpg")) {
                    imageList.add(file);
                }
            }
        }

        return imageList;
    }
}
