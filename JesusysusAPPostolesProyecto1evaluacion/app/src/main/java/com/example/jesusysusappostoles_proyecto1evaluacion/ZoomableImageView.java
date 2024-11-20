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
    private float[] matrixValues = new float[9];

    private float scale = 1f;
    private ScaleGestureDetector scaleDetector;

    private float lastTouchX;
    private float lastTouchY;
    private boolean isDragging = false;

    private float minScale = 1f;
    private float maxScale = 5f;

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

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;

                    if (scale > minScale) { // Solo permitir desplazamiento si hay zoom
                        matrix.postTranslate(dx, dy);
                        constrainTranslation(); // Limitar el movimiento dentro de los bordes
                        setImageMatrix(matrix);
                    }

                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                break;
        }

        return true;
    }

    private void constrainTranslation() {
        RectF rect = getTransformedRect();

        float offsetX = 0f;
        float offsetY = 0f;

        // Limitar la traslación horizontal
        if (rect.width() > getWidth()) { // Si la imagen es más ancha que la vista
            if (rect.left > 0) offsetX = -rect.left;
            if (rect.right < getWidth()) offsetX = getWidth() - rect.right;
        } else { // Si la imagen es más pequeña que la vista, centrarla horizontalmente
            offsetX = (getWidth() - rect.width()) / 2f - rect.left;
        }

        // Limitar la traslación vertical
        if (rect.height() > getHeight()) { // Si la imagen es más alta que la vista
            if (rect.top > 0) offsetY = -rect.top;
            if (rect.bottom < getHeight()) offsetY = getHeight() - rect.bottom;
        } else { // Si la imagen es más pequeña que la vista, centrarla verticalmente
            offsetY = (getHeight() - rect.height()) / 2f - rect.top;
        }

        matrix.postTranslate(offsetX, offsetY);
    }

    private RectF getTransformedRect() {
        RectF rect = new RectF(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        matrix.mapRect(rect);
        return rect;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetZoom(); // Restablecer zoom cuando cambie el tamaño del contenedor
    }

    private void resetZoom() {
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
            float newScale = scale * scaleFactor;

            if (newScale >= minScale && newScale <= maxScale) {
                scale *= scaleFactor;

                // Obtener el centro de la vista
                float viewCenterX = getWidth() / 2f;
                float viewCenterY = getHeight() / 2f;

                matrix.postScale(scaleFactor, scaleFactor, viewCenterX, viewCenterY);
                constrainTranslation(); // Limitar la traslación después del zoom
                setImageMatrix(matrix);
            }

            return true;
        }
    }
}
