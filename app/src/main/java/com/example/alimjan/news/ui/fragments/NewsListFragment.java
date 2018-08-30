package com.example.alimjan.news.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alimjan.news.api.pojo.NewsResponse;
import com.example.alimjan.news.ui.activities.NewsDetailActivity;
import com.example.alimjan.news.R;
import com.example.alimjan.news.ui.adapters.NewsListRecyclerViewAdapter;
import com.example.alimjan.news.model.News;
import com.example.alimjan.news.api.ServiceGenerator;
import com.example.alimjan.news.api.pojo.HitsItem;
import com.example.alimjan.news.api.service.HackNewsService;
import com.example.alimjan.news.ui.viewmodels.NewsViewModel;
import com.example.alimjan.news.ui.viewmodels.NewsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * * A simple Fragment shows a list of news items in a RecyclerView.
 */
public class NewsListFragment extends Fragment implements NewsListRecyclerViewAdapter.OnNewsItemClickListener {


    private NewsListRecyclerViewAdapter mAdapter;

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

        // Setup RecyclerView
        if (view instanceof RecyclerView) {
            setupRecyclerView((RecyclerView) view);
        }

        NewsViewModel newsViewModel = ViewModelProviders.of(this,new NewsViewModelFactory(getContext())).get(NewsViewModel.class);
        newsViewModel.news.observe(this, news -> {
            mAdapter.submitList(news);
        });

        return view;
    }

    /**
     * Requests data from api endpoint.
     */
    private void requestNewsListData() {
        HackNewsService service = ServiceGenerator.createService(HackNewsService.class);
        service.getLatestNews().enqueue(new NewsRequestCallBack());
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NewsListRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onNewsClick(News news) {
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.EXTRA_NEWS_URL, news.getUrl());
        intent.putExtra(NewsDetailActivity.EXTRA_NEWS_TITLE, news.getNewsTitle());
        getContext().startActivity(intent);
    }


    private class NewsRequestCallBack implements Callback<NewsResponse> {
        @Override
        public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
            if (response.isSuccessful()) {
                List<News> newsList = new ArrayList<>();
                NewsResponse newsResponse = response.body();
                for (HitsItem hitsItem : newsResponse.getHits()) {
                    newsList.add(new News(0,hitsItem.getTitle() != null ? hitsItem.getTitle() : hitsItem.getStoryTitle(), hitsItem.getAuthor(), hitsItem.getCreatedAtI(), hitsItem.getStoryUrl()));
                }
            }
        }

        @Override
        public void onFailure(Call<NewsResponse> call, Throwable t) {
            System.out.println("Error");
        }
    }
}
