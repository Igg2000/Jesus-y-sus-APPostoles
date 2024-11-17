package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<File> imageFiles;

    public ImageAdapter(Context context, List<File> imageFiles) {
        this.context = context;
        this.imageFiles = imageFiles;
    }

    //Crear la vista para cada elemento del RecyclerView
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflar el layout de cada item (imagen)
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    //Vincular cada imagen con la vista
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        Uri imageUri = Uri.fromFile(imageFile);  //Convertir el archivo en un Uri
        holder.imageView.setImageURI(imageUri);  // ostrar la imagen en el ImageView
    }

    //Devolver la cantidad de elementos (imagenes) que tenemos
    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    //Clase interna que representa la vista de cada item (imagen)
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);  //Obtener el ImageView del layout
        }
    }
}
