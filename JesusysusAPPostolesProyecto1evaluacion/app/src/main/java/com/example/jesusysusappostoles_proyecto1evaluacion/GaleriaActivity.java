package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.os.Bundle;
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

        //Usar GridLayoutManager para mostrar las imágenes en una cuadrícula
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columnas

        imageFiles = loadImagesFromStorage();  //Cargar las imágenes guardadas

        if (imageFiles.isEmpty()) {
            Toast.makeText(this, "No hay imágenes guardadas", Toast.LENGTH_SHORT).show();
        } else {
            imageAdapter = new ImageAdapter(this, imageFiles);
            recyclerView.setAdapter(imageAdapter);  //Asignar el adaptador al RecyclerView
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
