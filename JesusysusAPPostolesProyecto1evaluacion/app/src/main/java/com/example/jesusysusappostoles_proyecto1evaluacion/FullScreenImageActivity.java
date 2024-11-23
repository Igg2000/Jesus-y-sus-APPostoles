package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class FullScreenImageActivity extends AppCompatActivity {

    private ZoomableImageView imageView;
    private Button deleteButton;
    private File imageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configurar las transiciones compartidas
        getWindow().setSharedElementEnterTransition(new android.transition.TransitionSet()
                .addTransition(new android.transition.ChangeBounds())
                .addTransition(new android.transition.ChangeTransform())
                .addTransition(new android.transition.ChangeImageTransform()));

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
            imageView.setImageURI(Uri.fromFile(imageFile));
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

}
