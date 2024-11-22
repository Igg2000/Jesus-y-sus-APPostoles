package com.example.jesusysusappostoles_proyecto1evaluacion;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    // Constructor que recibe el valor del espaciado
    public GridSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Establecer espaciado alrededor de las imágenes
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.top = spacing;
        outRect.bottom = spacing;

        // Evitar que haya espacio extra en la última fila
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.bottom = 0;
        }
    }
}
