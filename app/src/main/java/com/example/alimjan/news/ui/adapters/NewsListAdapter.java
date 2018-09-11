package com.example.alimjan.news.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.alimjan.news.api.NetworkState;
import com.example.alimjan.news.databinding.ItemNetworkStatesBinding;
import com.example.alimjan.news.databinding.ItemNewsListBinding;
import com.example.alimjan.news.model.News;
import com.example.alimjan.news.ui.callbacks.OnItemClickListener;
import com.example.alimjan.news.ui.callbacks.OnRetryListener;
import com.example.alimjan.news.ui.fragments.NewsListFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link RecyclerView.Adapter} implementation for {@link NewsListFragment},
 * {@link NewsListAdapter} uses {@link OnItemClickListener} and {@link OnRetryListener} to notify
 * item click event to fragment.
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // A threshold for item count that approximately fills the list.
    public static final int VISIBLE_ITEM_THRESHOLD = 7;

    // View types
    private static final int VIEW_TYPE_NEWS = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    // Listener which used to inform item click and retry events.
    private OnItemClickListener mOnItemClickListener;
    private OnRetryListener mOnRetryListener;

    // Data shows on the list.
    private List<News> mData = new ArrayList<>();

    // Network state for footer item.
    private NetworkState mNetworkState = NetworkState.LOADING;
    private int mSelectedItemPosition = -1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // News item.
        if (viewType == VIEW_TYPE_NEWS) {
            ItemNewsListBinding binding = ItemNewsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new NewsItemViewHolder(binding);
        }
        // Network state
        ItemNetworkStatesBinding networkStatesBinding = ItemNetworkStatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NetworkStateItemViewHolder(networkStatesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsItemViewHolder) {
            NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder) holder;
            newsItemViewHolder.binding.setNews(mData.get(position));
            newsItemViewHolder.binding.setOnItemClickListener(mOnItemClickListener);
            newsItemViewHolder.binding.setPosition(position);

            // Select item this would work only in double pane layout.
            if (position == mSelectedItemPosition) {
                newsItemViewHolder.binding.getRoot().setSelected(true);
            } else {
                newsItemViewHolder.binding.getRoot().setSelected(false);
            }
        } else if (holder instanceof NetworkStateItemViewHolder) {
            NetworkStateItemViewHolder networkStateItemViewHolder = (NetworkStateItemViewHolder) holder;
            networkStateItemViewHolder.binding.setNetworkState(mNetworkState);
            networkStateItemViewHolder.binding.setOnRetryListener(mOnRetryListener);
        }
    }

    @Override
    public int getItemCount() {
        // if list has footer add one to total item count for showing footer.
        return hasFooter() ? this.mData.size() + 1 /* For footer */ : this.mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return hasFooter() && position == getItemCount() - 1 /* For footer */ ? VIEW_TYPE_FOOTER :
                super.getItemViewType(position);
    }

    private boolean hasFooter() {
        return !this.mData.isEmpty() && this.mData.size() > VISIBLE_ITEM_THRESHOLD;
    }


    /**
     * Set network state and refresh the footer.
     *
     * @param networkState Network state.
     */
    public void setNetworkState(NetworkState networkState) {
        this.mNetworkState = networkState;
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * Removes an item from the list.
     */
    public void removeNews(int position) {
        if (!mData.isEmpty()) {
            this.mData.remove(position);
            notifyItemRemoved(position);
        }

    }

    /**
     * Add an item to the list.
     */
    public void addDataToPosition(News news, int position) {
        this.mData.add(position, news);
        notifyItemInserted(position);
    }

    /**
     * Returns News by given position. This method especially used when removing swiped item
     * from the list.
     */
    public News getNewsItem(int position) {
        if (!mData.isEmpty()) {
            return mData.get(position);
        }
        return null;
    }


    /**
     * Set data and notify data set change.
     */
    public void setData(List<News> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * Append data to the list and notify change.
     */
    public void addData(List<News> data) {
        int sizeBeforeInvalidation = this.mData.size();
        this.mData = data;
        int newlyInsertedItemCount = this.mData.size() - sizeBeforeInvalidation;
        notifyItemRangeInserted(sizeBeforeInvalidation, newlyInsertedItemCount);
    }

    /**
     * Set specific item selected and {@link RecyclerView.Adapter#notifyItemChanged(int)}.
     */
    public void setItemSelected(int position) {
        int lastSelectedItemPosition = this.mSelectedItemPosition;
        this.mSelectedItemPosition = position;
        notifyItemChanged(this.mSelectedItemPosition);
        notifyItemChanged(lastSelectedItemPosition);
    }

    /**
     * Returns selected item position, if no item selected return -1.
     */
    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    /**
     * Sets a listener callback for item click event.
     *
     * @param mOnItemClickListener A listener callback.
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * Sets a listener callback for retry event.
     *
     * @param mOnRetryListener A listener callback.
     */
    public void setOnRetryListener(OnRetryListener mOnRetryListener) {
        this.mOnRetryListener = mOnRetryListener;
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder {
        final ItemNewsListBinding binding;

        NewsItemViewHolder(final ItemNewsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
        final ItemNetworkStatesBinding binding;

        NetworkStateItemViewHolder(@NonNull final ItemNetworkStatesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
