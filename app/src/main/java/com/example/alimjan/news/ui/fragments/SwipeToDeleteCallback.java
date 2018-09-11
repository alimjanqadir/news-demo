package com.example.alimjan.news.ui.fragments;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.alimjan.news.R;
import com.example.alimjan.news.ui.adapters.NewsListAdapter;
import com.example.alimjan.news.ui.callbacks.OnSwipeListener;

/**
 * A callback to achieve swipe to delete in {@link RecyclerView}. Class involves an interface
 * that is used to inform swipe detection.
 */
public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private OnSwipeListener mListener;

    SwipeToDeleteCallback(OnSwipeListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // set the swipe direction
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null && viewHolder instanceof NewsListAdapter.NewsItemViewHolder) {
            final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
            getDefaultUIUtil().onSelected(foregroundView);
        }

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof NewsListAdapter.NewsItemViewHolder) {
            final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof NewsListAdapter.NewsItemViewHolder) {
            final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof NewsListAdapter.NewsItemViewHolder) {
            final View foregroundView = ((NewsListAdapter.NewsItemViewHolder) viewHolder).itemView.findViewById(R.id.foreground_view);
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // inform swipe action via listener interface.
        mListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }
}