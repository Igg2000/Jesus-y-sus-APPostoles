package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.Intent;
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
import java.util.Collections;
import java.util.List;

public class GaleriaActivity extends AppCompatActivity {

    public static final int FULL_SCREEN_IMAGE_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<File> imageFiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeriaactivity);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));



        // Obtener el espaciado definido en dimens.xml
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);

        // Añadir el espaciado al RecyclerView
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spacing));

        imageFiles = loadImagesFromStorage();
        imageAdapter = new ImageAdapter(this, imageFiles);
        recyclerView.setAdapter(imageAdapter);


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

        // Ordenar las imágenes por nombre
        Collections.sort(imageList, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));

        return imageList;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FULL_SCREEN_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            String deletedImagePath = data.getStringExtra("deleted_image_path");
            if (deletedImagePath != null) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    if (imageFiles.get(i).getAbsolutePath().equals(deletedImagePath)) {
                        int finalI = i;

                        // Animación antes de eliminar
                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                        if (viewHolder != null) {
                            viewHolder.itemView.animate()
                                    .translationX(recyclerView.getWidth()) // Mover fuera de pantalla
                                    .alpha(0f) // Desvanecer
                                    .setDuration(300) // Duración de la animación
                                    .withEndAction(() -> {
                                        imageFiles.remove(finalI);
                                        imageAdapter.notifyItemRemoved(finalI);

                                        // Verificar si la lista está vacía
                                        if (imageFiles.isEmpty()) {
                                            TextView emptyMessage = new TextView(this);
                                            emptyMessage.setText("No hay ninguna imagen");
                                            emptyMessage.setTextColor(Color.LTGRAY);
                                            emptyMessage.setGravity(Gravity.CENTER);
                                            emptyMessage.setTextSize(24);
                                            setContentView(emptyMessage);
                                        }
                                    }).start();
                        } else {
                            imageFiles.remove(i);
                            imageAdapter.notifyItemRemoved(i);

                            if (imageFiles.isEmpty()) {
                                TextView emptyMessage = new TextView(this);
                                emptyMessage.setText("No hay ninguna imagen");
                                emptyMessage.setTextColor(Color.LTGRAY);
                                emptyMessage.setGravity(Gravity.CENTER);
                                emptyMessage.setTextSize(24);
                                setContentView(emptyMessage);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

}
