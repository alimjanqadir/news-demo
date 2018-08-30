package com.example.alimjan.news.data;


import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NewsRepository {

    private final NewsCache mCache;

    public NewsRepository(NewsCache cache) {
        this.mCache = cache;
    }

    public LiveData<PagedList<News>> getNews() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(20)
                .setPrefetchDistance(5)
                .setEnablePlaceholders(false)
                .build();

        DataSource.Factory<Integer, News> dataSource = this.mCache.getNews();

        return new LivePagedListBuilder(dataSource, config)
                .setBoundaryCallback(new PagedList.BoundaryCallback() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        List<News> newsList = new ArrayList<>();

                        for (int i = 0; i < 1000; i++) {
                            newsList.add(new News(i,"This is a test title", "Alimjan", "20 minutes ago", "https://bing.com"));
                        }

                        mCache.insertAll(newsList);
                    }
                })
                .build();
    }
}
