package com.example.alimjan.news.data;


import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.alimjan.news.api.pojo.HitsItem;
import com.example.alimjan.news.api.pojo.NewsResponse;
import com.example.alimjan.news.api.service.HackNewsService;
import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple repository class for merging two data source to achieve source of truth principle.
 */
public class NewsRepository {
    // Page size for the DataSource to determine how to split a page.
    private static final int PAGE_SIZE = 20;
    private final NewsCache mCache;
    private final HackNewsService mService;

    public NewsRepository(NewsCache cache, HackNewsService service) {
        this.mCache = cache;
        this.mService = service;
    }

    /**
     * Returns a Live PagedList that linked to database,
     */
    public LiveData<PagedList<News>> getNews() {
        DataSource.Factory<Integer, News> dataSourceFactory = this.mCache.getNews();

        //noinspection unchecked
        return new LivePagedListBuilder(dataSourceFactory, PAGE_SIZE)
                .setBoundaryCallback(new NewsRepositoryBoundaryCallBack(this.mCache, this.mService))
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    /**
     * Request fresh data from start and invalidates all data from database.
     */
    public void refresh() {
        this.mService.getLatestNews(0).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HitsItem> hits = response.body().getHits();
                    List<News> newsList = new ArrayList<>();
                    for (HitsItem hit : hits) {
                        newsList.add(new News(hit.getTitle(), hit.getAuthor(),
                                hit.getCreatedAtI(), hit.getStoryUrl()));
                    }

                    mCache.refresh(newsList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Remove specific data from database.
     */
    public void remove(News news) {
        mCache.delete(news);
    }

}
