package com.example.alimjan.news.db;

import android.arch.paging.DataSource;

import com.example.alimjan.news.model.News;

import java.util.List;
import java.util.concurrent.Executor;

public class NewsCache {
    private final NewsDao mNewsDao;
    private final Executor mExecutor;

    public NewsCache(NewsDao newsDao, Executor executor) {
        this.mNewsDao = newsDao;
        this.mExecutor = executor;
    }

    public DataSource.Factory<Integer, News> getNews() {
        return this.mNewsDao.getNews();
    }

    public void insertAll(List<News> newsList) {
        this.mExecutor.execute(() -> mNewsDao.insertAll(newsList));
    }

}
