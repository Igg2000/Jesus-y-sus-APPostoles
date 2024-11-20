package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.Nullable;

public class ZoomableImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Matrix matrix = new Matrix();
    private float scale = 1f;
    private ScaleGestureDetector scaleDetector;

    public ZoomableImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setScaleType(ScaleType.MATRIX); // Permitir transformaciones con la matriz
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event); // Detectar gestos de escala
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetZoom(); // Restablecer zoom cuando cambie el tamaño del contenedor
    }

    private void resetZoom() {
        // Restablecer la matriz al estado inicial
        matrix.reset();
        scale = 1f;

        // Centrar la imagen dentro del contenedor
        RectF drawableRect = new RectF(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        RectF viewRect = new RectF(0, 0, getWidth(), getHeight());
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);

        setImageMatrix(matrix);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scale *= scaleFactor;

            // Limitar el nivel de zoom
            scale = Math.max(1f, Math.min(scale, 5f));

            // Obtener el centro de la vista que contiene la imagen
            float viewCenterX = getWidth() / 2f;
            float viewCenterY = getHeight() / 2f;

            // Aplicar el zoom basado en el centro de la vista
            matrix.postScale(scaleFactor, scaleFactor, viewCenterX, viewCenterY);
            setImageMatrix(matrix);
            return true;
        }
    }
}
