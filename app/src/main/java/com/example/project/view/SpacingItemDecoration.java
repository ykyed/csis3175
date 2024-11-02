package com.example.project.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecoration  extends RecyclerView.ItemDecoration {
    private final int verticalSpacing;
    private final int horizontalSpacing;

    public SpacingItemDecoration(int verticalSpacing, int horizontalSpacing) {
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = horizontalSpacing;
        outRect.right = horizontalSpacing;
        outRect.top = verticalSpacing;
        outRect.bottom = verticalSpacing;
    }
}
