package com.example.alimjan.news.ui.callbacks;

import android.support.v7.widget.RecyclerView;

/**
 * A listener callback for {@link RecyclerView} item click event.
 *
 * @param <T> Generic type that would defined when implementation.
 */
public interface OnItemClickListener<T> {
    void onItemClick(T type, int position);
}
