package com.example.alimjan.news.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.alimjan.news.data.News;
import com.example.alimjan.news.databinding.NewsListItemBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * A RecyclerViewAdapter for NewsListFragment, uses {@link OnNewsItemClickListener} to notify
 * news click event to fragment.
 */
public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<NewsListRecyclerViewAdapter.ViewHolder> {

    // News data that showed on Recycleview.
    private List<News> mData = new ArrayList<>();

    // A callback interface that is used to inform news item click event.
    private final OnNewsItemClickListener mNewsItemClickListener;

    public NewsListRecyclerViewAdapter(OnNewsItemClickListener newsItemClickListener) {
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
        holder.binding.setNews(mData.get(position));
        holder.binding.getRoot().setOnClickListener(v -> {
            mNewsItemClickListener.onNewsClick(mData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Puts data to a collection and notify adapter to change data.
     *
     * @param data news data from repository.
     */
    public void putData(List<News> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
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
}
