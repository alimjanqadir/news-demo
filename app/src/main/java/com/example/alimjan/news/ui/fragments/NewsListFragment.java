package com.example.alimjan.news.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alimjan.news.R;
import com.example.alimjan.news.model.News;
import com.example.alimjan.news.ui.activities.NewsDetailActivity;
import com.example.alimjan.news.ui.adapters.NewsListAdapter;
import com.example.alimjan.news.ui.viewmodels.NewsViewModel;
import com.example.alimjan.news.ui.viewmodels.NewsViewModelFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * * A simple Fragment shows a list of news in a {@link RecyclerView}.
 */
public class NewsListFragment extends Fragment implements NewsListAdapter.OnNewsItemClickListener,
        SwipeToDeleteCallback.RecyclerItemTouchHelperListener, OnRefreshListener {


    private NewsListAdapter mAdapter;
    private NewsViewModel mNewsViewModel;

    public NewsListFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("unused")
    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        // Setup SmartRefreshLayout
        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableOverScrollDrag(false);

        // Setup RecyclerView
        setupRecyclerView(view);

        // ViewModel created
        //noinspection ConstantConditions
        mNewsViewModel = ViewModelProviders.of(this, new NewsViewModelFactory(
                getContext().getApplicationContext())).get(NewsViewModel.class);
        mNewsViewModel.getNews().observe(this, news -> {
            mAdapter.submitList(news);
            // SmartRefreshLayout should also finish refresh state when new data observed.
            refreshLayout.finishRefresh();
        });
        return view;
    }


    /**
     * Setup {@link RecyclerView}.
     */
    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NewsListAdapter(this);

        // Swipe to delete achieved by RecyclerView ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);
    }

    /**
     * A callback invocation from {@link NewsListAdapter} to inform a list item clicked.
     *
     * @param news A news data associated with clicked item.
     */
    @Override
    public void onNewsClick(News news) {
        if (getFragmentManager() != null) {
            boolean isDoublePane = getFragmentManager().findFragmentById(R.id.news_detail_fragment) != null;
            if (isDoublePane) {
                // if double pane submit event to NewsDetailFragment
            } else {
                startNewsDetailActivity(news);
            }
        }
    }

    /**
     * Starts the {@link NewsDetailActivity}.
     */
    private void startNewsDetailActivity(News news) {
        if (getActivity() != null) {
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra(NewsDetailActivity.EXTRA_NEWS_URL, news.getUrl());
            intent.putExtra(NewsDetailActivity.EXTRA_NEWS_TITLE, news.getNewsTitle());
            getActivity().startActivity(intent);
        }
    }

    /**
     * A callback invocation from {@link ItemTouchHelper.Callback} which informs an list item
     * swiped by user.
     *
     * @param viewHolder Swiped item {@link RecyclerView.ViewHolder} instance.
     * @param direction  Swiped direction.
     * @param position   Swiped item position.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//        mAdapter.notifyItemRemoved(position);
//        Snackbar.make(viewHolder.itemView, "this is a test", Snackbar.LENGTH_SHORT).show();

        mNewsViewModel.removeNews(mAdapter.getNewsItem(position));
    }

    /**
     * A callback invocation from {@link SmartRefreshLayout} which informs refresh action
     * takes place.
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mNewsViewModel.refresh();
    }
}
