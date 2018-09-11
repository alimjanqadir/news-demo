package com.example.alimjan.news.ui.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alimjan.news.R;
import com.example.alimjan.news.api.NetworkState;
import com.example.alimjan.news.databinding.FragmentNewsListBinding;
import com.example.alimjan.news.model.News;
import com.example.alimjan.news.ui.activities.NewsActivity;
import com.example.alimjan.news.ui.activities.NewsDetailActivity;
import com.example.alimjan.news.ui.adapters.NewsListAdapter;
import com.example.alimjan.news.ui.callbacks.OnItemClickListener;
import com.example.alimjan.news.ui.callbacks.OnRetryListener;
import com.example.alimjan.news.ui.callbacks.OnSwipeListener;
import com.example.alimjan.news.ui.viewmodels.NewsViewModel;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.alimjan.news.ui.adapters.NewsListAdapter.VISIBLE_ITEM_THRESHOLD;

/**
 * A {@link Fragment} implementation that shows a list of news in a {@link RecyclerView}. This
 * fragment handles 4 types of events {@link OnRefreshListener#onRefresh(RefreshLayout)},
 * {@link OnItemClickListener#onItemClick(Object, int)}, {@link OnRetryListener#onRetry()}, and
 * {@link OnSwipeListener#onSwiped(RecyclerView.ViewHolder, int, int)} which fires from
 * {@link SmartRefreshLayout}, {@link NewsListAdapter}, Binding and {@link ItemTouchHelper}
 * respectively. Tricky part of this class is to handle observed data from database, because
 * {@link LiveData} observer receives all data inside database and receives again whenever a change
 * occurs to data source. To handle data differently we use flags to determine various request
 * types.
 */
public class NewsListFragment extends Fragment implements OnRefreshListener, OnItemClickListener<News>, OnRetryListener,
        OnSwipeListener {
    // Binding instance for the fragment ui
    private FragmentNewsListBinding mBinding;

    // Views and adapter
    private NewsListAdapter mAdapter;

    // ViewModel instance for storing UI data
    private NewsViewModel mNewsViewModel;

    // Current request result
    private List<News> mCurrentRequestResult = new ArrayList<>();

    // Flags for differing request
    private boolean mRefreshRequestFlag;
    private boolean mLoadMoreRequestFlag;
    private boolean mAddToTrashRequestFlag;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Returns news instance of {@link NewsListFragment}.
     */
    @SuppressWarnings("unused")
    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout with data binding.
        mBinding = FragmentNewsListBinding.inflate(inflater);
        /* Provide a retry listener callback retry button which shows when initial
         * data load failed, not confuse it with retry button inside the RecyclerView.
         */
        mBinding.setOnRetryListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup SmartRefreshLayout
        setSmartRefreshLayout();

        // Setup RecyclerView
        setRecyclerView();

        // ViewModel created and essential data observed
        if (getActivity() != null) {
            mNewsViewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
            mNewsViewModel.getNews().observe(this, this::handleData);
            mNewsViewModel.getNetworkState().observe(this, this::handleNetworkState);
        }
    }


    /**
     * Setup {@link SmartRefreshLayout}.
     */
    private void setSmartRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(this);
        mBinding.refreshLayout.setEnableOverScrollDrag(false);
        mBinding.refreshLayout.setEnableHeaderTranslationContent(true);
        MaterialHeader refreshHeader = (MaterialHeader) mBinding.refreshLayout.getRefreshHeader();
        if (refreshHeader != null) {
            refreshHeader.setColorSchemeColors(getResources().getColor(R.color.primaryLightColor));
        }
    }

    /**
     * Setup {@link RecyclerView}.
     */
    private void setRecyclerView() {
        // Prepare adapter
        mAdapter = new NewsListAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnRetryListener(this);
        // Prepare RecyclerView
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.list.setAdapter(mAdapter);
        setScrollListener();
        // Swipe to delete achieved by RecyclerView ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(this));
        itemTouchHelper.attachToRecyclerView(mBinding.list);

        // Divider not added for respecting original Hacker News theme
        // recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
    }

    /**
     * Set scroll listener for achieving load more.
     */
    private void setScrollListener() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.list.getLayoutManager();
        mBinding.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager != null) {
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    // Load more request don't send while another request is on the way
                    if (!mRefreshRequestFlag && !mLoadMoreRequestFlag && !mAddToTrashRequestFlag) {
                        if (firstVisibleItemPosition + lastVisibleItemPosition + VISIBLE_ITEM_THRESHOLD >= totalItemCount) {
                            loadMore();
                        }
                    }
                }
            }
        });
    }

    /**
     * Change {@link RecyclerView}  scroll position to top.
     */
    private void scrollToTop() {
        mBinding.list.scrollToPosition(0);
    }

    /**
     * Starts the {@link NewsDetailActivity}.
     */
    private void startNewsDetailActivity(News news) {
        if (getActivity() != null) {
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra(NewsDetailActivity.EXTRA_NEWS, news);
            getActivity().startActivity(intent);
        }
    }

    /**
     * Refresh data source.
     */
    private void refresh() {
        mRefreshRequestFlag = true;
        mNewsViewModel.refresh();
    }

    /**
     * Load more data from data source.
     */
    private void loadMore() {
        mLoadMoreRequestFlag = true;
        mNewsViewModel.loadMore();
    }

    /**
     * Retries failed request.
     */
    private void retry() {
        mNewsViewModel.retry();
    }

    /**
     * Handles request data from data source.
     */
    private void handleData(@NonNull List<News> news) {
        mCurrentRequestResult = news;

        // Adding news to trash request handled separately because of request conflict.
        if (mAddToTrashRequestFlag) {
            mAddToTrashRequestFlag = false;
            if (isRequestResultEmpty()) {
                loadMore();
            }
            return;
        }

        // Select first item as default when in double pane layout mode.
        if (isDoublePane() && !isRequestResultEmpty()) {
            if (mRefreshRequestFlag) {
                setItemSelected(0, news.get(0));
            } else {
                int selectedItemPosition = mNewsViewModel.getSelectedItemPosition();
                setItemSelected(selectedItemPosition, news.get(selectedItemPosition));
            }
        }


        // Other request data handled
        if (mRefreshRequestFlag) {
            mRefreshRequestFlag = false;
            if (isRequestResultSmallerThanVisibleThreshold()) {
                mAdapter.setData(news);

                // Scroll to top every time data refreshes
                scrollToTop();
            } else {
                loadMore();
            }
        } else if (mLoadMoreRequestFlag) {
            mLoadMoreRequestFlag = false;
            if (isRequestResultSmallerThanVisibleThreshold()) {
                mAdapter.addData(news);
            } else {
                loadMore();
            }
        } else {
            /* If request is not refresh nor load more then it would be initial data from database
             * or configuration change.
             */
            mAdapter.setData(news);

            /* This condition is used to prevent data refresh when configuration changes, if
             * initial data loaded we don't have to refresh data again because data already refreshed
             * in the initial.*/
            if (!mNewsViewModel.isInitialDataLoaded()) {
                // Refresh even there is a data in cache
                mNewsViewModel.setInitialDataLoaded();
                if (isRequestResultEmpty()) {
                    refresh();
                } else {
                    mBinding.refreshLayout.autoRefresh(500);
                }
            }
        }
    }


    /**
     * Handles network state, deliberately used to update page state.
     */
    private void handleNetworkState(@NonNull NetworkState networkState) {
        if (networkState.getStatus() == NetworkState.State.LOADING) {
            if (IsAdapterEmpty()) {
                mBinding.setNetworkState(networkState);
            } else {
                mAdapter.setNetworkState(networkState);
            }
        } else if (networkState.getStatus() == NetworkState.State.SUCCESS) {
            if (mRefreshRequestFlag) {
                if (IsAdapterEmpty()) {
                    mBinding.setNetworkState(networkState);
                } else {
                    mBinding.refreshLayout.finishRefresh();
                }
            } else {
                if (IsAdapterEmpty()) {
                    mBinding.setNetworkState(networkState);
                } else {
                    mAdapter.setNetworkState(networkState);
                }
            }
        } else if (networkState.getStatus() == NetworkState.State.END) {
            mAdapter.setNetworkState(networkState);
        } else if (networkState.getStatus() == NetworkState.State.ERROR) {
            // Change unfriendly error message from http clint to more understandable message
            networkState.setMessage(getString(R.string.msg_request_failed));
            if (mRefreshRequestFlag) {
                if (IsAdapterEmpty()) {
                    mBinding.setNetworkState(networkState);
                } else {
                    mBinding.refreshLayout.finishRefresh();
                    //noinspection ConstantConditions
                    Snackbar.make(mBinding.getRoot(), networkState.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, v -> mBinding.refreshLayout.autoRefresh()).show();
                }
            } else {
                if (IsAdapterEmpty()) {
                    mBinding.setNetworkState(networkState);
                } else {
                    mAdapter.setNetworkState(networkState);
                }
            }
        }
    }


    /**
     * Is current request data size enough to fill screen, visible item threshold is least item count fills
     * the screen. This method is used determine should data visible to user or later
     * when there is enough data to fill the screen.
     */
    private boolean isRequestResultSmallerThanVisibleThreshold() {
        return mCurrentRequestResult.size() > VISIBLE_ITEM_THRESHOLD;
    }


    /**
     * Used to determine current request data empty or not.
     */
    private boolean isRequestResultEmpty() {
        return mCurrentRequestResult.size() == 0;
    }

    /**
     * Used to determine adapter empty or not which means are there data visible to user in
     * {@link RecyclerView}.
     */
    private boolean IsAdapterEmpty() {
        return mAdapter.getItemCount() == 0;
    }

    /**
     * A callback invocation from {@link NewsListAdapter} to inform a list item clicked.
     *
     * @param news A news data associated with clicked item.
     */
    @Override
    public void onItemClick(News news, int position) {
        if (isDoublePane()) {
            setItemSelected(position, news);
        } else {
            startNewsDetailActivity(news);
        }
    }

    /**
     * A callback invocation from {@link NewsListAdapter}.
     */
    @Override
    public void onRetry() {
        retry();
    }

    /**
     * A callback invocation from {@link ItemTouchHelper.Callback} which informs an list item
     * swiped by user.
     *
     * @param viewHolder Swiped item {@link RecyclerView.ViewHolder} instance.
     * @param direction  Swipe direction.
     * @param position   Swiped item position.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        mAddToTrashRequestFlag = true;

        // Add news to trash
        News news = mAdapter.getNewsItem(position);
        mAdapter.removeNews(position);
        mNewsViewModel.addToTrash(news);

        int selectedItemPosition = mAdapter.getSelectedItemPosition();
        if (position <= selectedItemPosition) {
            setItemSelected(position, mAdapter.getNewsItem(position));
        }


        // Show Snackbar for undo.
        Snackbar.make(mBinding.getRoot(), R.string.msg_news_undo, Snackbar.LENGTH_SHORT).setAction(R.string.undo, v -> {
            // Restore deleted item
            mAdapter.addDataToPosition(news, position);
            mNewsViewModel.restoreFromTrash(news);

            if (position == selectedItemPosition) {
                // Change selected item if needed
                setItemSelected(position, news);
            }


            /* if the deleted item is in position 0, restore action would not visible to user. After
             * first item in position 0 deleted, item in position 1 would become position 0 and at
             * same time list size would change. When restore action performed list size back to
             * before but scroll position is not. So changing scroll position to 0 solve this
             * problem.
             */
            if (position == 0) {
                scrollToTop();
            }
        }).show();
    }

    private void setItemSelected(int position, News news) {
        mNewsViewModel.setSelectItem(news, position);
        mAdapter.setItemSelected(position);
    }

    /**
     * A callback invocation from {@link SmartRefreshLayout} which informs refresh action
     * takes place.
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refresh();
    }

    /**
     * Returns activity is in double pane layout mode or not.
     */
    private boolean isDoublePane() {
        if (getActivity() != null) {
            NewsActivity newsActivity = (NewsActivity) getActivity();
            return newsActivity.isDoublePane();
        }
        return false;
    }
}
