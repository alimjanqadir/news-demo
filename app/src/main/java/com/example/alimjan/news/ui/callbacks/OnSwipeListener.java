package com.example.alimjan.news.ui.callbacks;

import android.support.v7.widget.RecyclerView;

/**
 * A listener callback for {@link RecyclerView} item swipe event.
 */
public interface OnSwipeListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}