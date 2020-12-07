package com.example.to_doapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeHelperList  extends ItemTouchHelper.SimpleCallback{

    private TaskAdapter mAdapter;
    private Drawable toggleIcon, deleteIcon;
    private final ColorDrawable toggleBackground, deleteBackground;

    public SwipeHelperList(TaskAdapter adapter) {
        super(0, ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT);
        this.mAdapter = adapter;
        this.deleteIcon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_delete_24);
        this.deleteBackground = new ColorDrawable(Color.RED);

        this.toggleIcon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_delete_24);
        this.toggleBackground = new ColorDrawable(Color.GREEN);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            mAdapter.deleteItem(position);
        } else if(direction == ItemTouchHelper.RIGHT){
            mAdapter.toggleCheck(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin, iconTop, iconBottom;

        if (dX > 0) { // Swiping to the right           Toggle
            iconMargin = (itemView.getHeight() -  this.toggleIcon.getIntrinsicHeight()) / 2;
            iconTop = itemView.getTop() + (itemView.getHeight() -  this.toggleIcon.getIntrinsicHeight()) / 2;
            iconBottom = iconTop +  this.toggleIcon.getIntrinsicHeight();

            int iconLeft = itemView.getLeft() + iconMargin + this.toggleIcon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            this.toggleIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            this.toggleBackground.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left             Delete
            iconMargin = (itemView.getHeight() -  this.deleteIcon.getIntrinsicHeight()) / 2;
            iconTop = itemView.getTop() + (itemView.getHeight() -  this.deleteIcon.getIntrinsicHeight()) / 2;
            iconBottom = iconTop +  this.deleteIcon.getIntrinsicHeight();

            int iconLeft = itemView.getRight() - iconMargin - this.deleteIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            this.deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            this.deleteBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            this.deleteBackground.setBounds(0, 0, 0, 0);
            this.toggleBackground.setBounds(0, 0, 0, 0);
        }

        this.toggleBackground.draw(c);
        this.toggleIcon.draw(c);
        this.deleteBackground.draw(c);
        this.deleteIcon.draw(c);
    }
}
