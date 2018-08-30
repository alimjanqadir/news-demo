package com.example.alimjan.news.ui.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.alimjan.news.model.News;
import com.example.alimjan.news.databinding.NewsListItemBinding;


/**
 * A RecyclerViewAdapter for NewsListFragment, uses {@link OnNewsItemClickListener} to notify
 * news click event to fragment.
 */
public class NewsListRecyclerViewAdapter extends PagedListAdapter<News, NewsListRecyclerViewAdapter.ViewHolder> {

    // A callback interface that is used to inform news item click event.
    private final OnNewsItemClickListener mNewsItemClickListener;

    public NewsListRecyclerViewAdapter(OnNewsItemClickListener newsItemClickListener) {
        super(DIFF_CALLBACK);
        this.mNewsItemClickListener = newsItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsListItemBinding newsListItemBinding = NewsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(newsListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.binding.setNews(getItem(position));
        holder.binding.getRoot().setOnClickListener(v -> mNewsItemClickListener.onNewsClick(getItem(position)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final NewsListItemBinding binding;

        ViewHolder(final NewsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * A callback interface that is used to inform news item click event.
     */
    public interface OnNewsItemClickListener {
        void onNewsClick(News news);
    }

    private static DiffUtil.ItemCallback<News> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<News>() {
                // News details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(News oldNews, News newNews) {
                    return oldNews.getAid() == newNews.getAid();
                }

                @Override
                public boolean areContentsTheSame(News oldNews,
                                                  @NonNull News newNews) {
                    return oldNews.equals(newNews);
                }
            };

}
