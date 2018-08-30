package com.example.alimjan.news.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alimjan.news.NewsDetailActivity;
import com.example.alimjan.news.R;
import com.example.alimjan.news.adapters.NewsListRecyclerViewAdapter;
import com.example.alimjan.news.data.News;
import com.example.alimjan.news.network.ServiceGenerator;
import com.example.alimjan.news.network.pojo.HitsItem;
import com.example.alimjan.news.network.service.HackNewsService;

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

        // Request data from api
        requestNewsListData();

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


    private class NewsRequestCallBack implements Callback<com.example.alimjan.news.network.pojo.News> {
        @Override
        public void onResponse(Call<com.example.alimjan.news.network.pojo.News> call, Response<com.example.alimjan.news.network.pojo.News> response) {
            if (response.isSuccessful()) {
                List<News> newsList = new ArrayList<>();
                com.example.alimjan.news.network.pojo.News news = response.body();
                assert news != null;
                for (HitsItem hitsItem : news.getHits()) {
                    newsList.add(new News(hitsItem.getTitle() != null ? hitsItem.getTitle() : hitsItem.getStoryTitle(), hitsItem.getAuthor(), hitsItem.getCreatedAtI(), hitsItem.getStoryUrl()));
                }

                mAdapter.putData(newsList);
            }
        }

        @Override
        public void onFailure(Call<com.example.alimjan.news.network.pojo.News> call, Throwable t) {
            System.out.println("Error");
        }
    }
}
