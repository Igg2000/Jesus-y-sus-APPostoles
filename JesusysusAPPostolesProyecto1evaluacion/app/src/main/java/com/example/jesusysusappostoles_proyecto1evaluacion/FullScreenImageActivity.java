package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import java.io.File;

public class FullScreenImageActivity extends AppCompatActivity {

    private ZoomableImageView imageView;
    private Button deleteButton;
    private File imageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configurar las transiciones compartidas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Configurar transiciones para dispositivos compatibles
            TransitionSet transitionSet = new TransitionSet()
                    .addTransition(new ChangeBounds())
                    .addTransition(new ChangeTransform())
                    .addTransition(new ChangeImageTransform())
                    .setDuration(400)
                    .setInterpolator(new FastOutSlowInInterpolator());

            getWindow().setSharedElementEnterTransition(transitionSet);
            getWindow().setSharedElementReturnTransition(transitionSet);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);



        // Configurar el callback para manejar el botón de retroceso (API 33+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> finishAfterTransition() // Realiza la animación de cierre
            );
        }

        imageView = findViewById(R.id.full_screen_image);
        deleteButton = findViewById(R.id.delete_button);

        // Obtener el archivo de imagen y el transitionName
        String imagePath = getIntent().getStringExtra("image_path");
        String transitionName = getIntent().getStringExtra("transition_name");

        if (imagePath != null) {
            imageFile = new File(imagePath);
            // Aquí puedes establecer el tamaño máximo que deseas para la imagen (ej., 1000x1000)
            Bitmap bitmap = decodeSampledBitmapFromFile(imagePath, 1000, 1000);
            imageView.setImageBitmap(bitmap);
        }

        if (transitionName != null) {
            imageView.setTransitionName(transitionName); // Sincronizar la animación
        }

        // Configurar el botón de borrar
        deleteButton.setOnClickListener(v -> deleteImage());
    }



    private void deleteImage() {
        if (imageFile != null && imageFile.exists()) {
            // Animación de desvanecimiento y desplazamiento
            imageView.animate()
                    .translationX(imageView.getWidth()) // Desplazar hacia la derecha
                    .alpha(0f) // Desvanecer
                    .setDuration(300) // Duración de la animación
                    .withEndAction(() -> {
                        boolean deleted = imageFile.delete();
                        if (deleted) {
                            Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show();

                            // Devolver el resultado a la actividad anterior
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("deleted_image_path", imageFile.getAbsolutePath());
                            setResult(RESULT_OK, resultIntent);

                            // Finalizar con animación inversa
                            finishAfterTransition();
                        } else {
                            Toast.makeText(this, "Error al eliminar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }).start();
        }
    }

    private Bitmap decodeSampledBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {
        // Primero, obtener las dimensiones de la imagen
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calcular la escala adecuada
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decodificar la imagen con el tamaño adecuado
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw altura y ancho de la imagen
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calcular la mejor escala
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
