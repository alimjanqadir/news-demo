package com.example.alimjan.news.ui.fragments;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.alimjan.news.R;
import com.example.alimjan.news.ui.adapters.NewsListAdapter;

/**
 * A callback to achieve {@link RecyclerView} swipe to delete. Class did
 * not extend the {@link android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback} because
 * be red background to indicate swipe to delete action, implementing this class is easy you just
 * need to map a method to associated {@link ItemTouchHelper.Callback#getDefaultUIUtil()} couple.
 */
public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private RecyclerItemTouchHelperListener mListener;

    SwipeToDeleteCallback(RecyclerItemTouchHelperListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
            getDefaultUIUtil().onSelected(foregroundView);
        }

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder != null) {
            final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }


    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}