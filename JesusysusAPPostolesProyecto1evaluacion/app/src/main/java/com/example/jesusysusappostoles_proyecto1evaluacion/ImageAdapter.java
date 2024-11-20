package com.example.jesusysusappostoles_proyecto1evaluacion;

import static com.example.jesusysusappostoles_proyecto1evaluacion.GaleriaActivity.FULL_SCREEN_IMAGE_REQUEST_CODE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File imageFile = imageFiles.get(position);
        Bitmap thumbnail = decodeSampledBitmapFromFile(imageFile.getAbsolutePath(), 200, 200);
        holder.imageView.setImageBitmap(thumbnail);

        // Suponiendo que esto ocurre cuando el usuario hace clic en una imagen
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenImageActivity.class);
            intent.putExtra("image_path", imageFile.getAbsolutePath()); // Pasar la ruta de la imagen
            ((AppCompatActivity) context).startActivityForResult(intent, GaleriaActivity.FULL_SCREEN_IMAGE_REQUEST_CODE);
        });

    }



    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    // Clase interna para el ViewHolder
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    // Método para decodificar un Bitmap optimizado
    private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        // Obtener las dimensiones originales
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calcular el factor de escalado
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decodificar el Bitmap reducido
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    // Método para calcular el inSampleSize
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
