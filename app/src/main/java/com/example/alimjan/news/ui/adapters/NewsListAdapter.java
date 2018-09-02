package com.example.alimjan.news.ui.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.alimjan.news.databinding.ItemNewsListBinding;
import com.example.alimjan.news.model.News;


/**
 * A {@link PagedListAdapter} for NewsListFragment, this is a special adapter that utilizes uses
 * {@link DiffUtil.ItemCallback} to efficiently display paged data from data source. Another point
 * worth noting that {@link NewsListAdapter} uses {@link OnNewsItemClickListener} to notify
 * item click event to fragment.
 */
public class NewsListAdapter extends PagedListAdapter<News, NewsListAdapter.NewsItemViewHolder> {

    // A callback interface that is used to inform item click event.
    private final OnNewsItemClickListener mNewsItemClickListener;

    private final static DiffUtil.ItemCallback<News> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<News>() {
                // News details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(News oldNews, News newNews) {
                    return oldNews.getId() == newNews.getId();
                }

                @Override
                public boolean areContentsTheSame(News oldNews, @NonNull News newNews) {
                    return oldNews.equals(newNews);
                }

            };

    public NewsListAdapter(OnNewsItemClickListener newsItemClickListener) {
        super(DIFF_CALLBACK);
        this.mNewsItemClickListener = newsItemClickListener;
    }

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsListBinding binding = ItemNewsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewsItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, int position) {
        holder.binding.setNews(getItem(position));
        holder.binding.getRoot().setOnClickListener(v -> mNewsItemClickListener.onNewsClick(getItem(position)));
    }


    public class NewsItemViewHolder extends RecyclerView.ViewHolder {

        final ItemNewsListBinding binding;

        NewsItemViewHolder(final ItemNewsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Returns News by given position. This method especially used when removing swiped item
     * from the list.
     */
    public News getNewsItem(int position) {
        return getItem(position);
    }

    /**
     * A callback interface that is used to inform news item click event.
     */
    public interface OnNewsItemClickListener {
        void onNewsClick(News news);
    }
}
