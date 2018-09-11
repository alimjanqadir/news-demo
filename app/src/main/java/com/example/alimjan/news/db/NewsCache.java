package com.example.alimjan.news.db;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.alimjan.news.model.News;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * A wrapper class of database playing role of single source of truth.
 */
public class NewsCache {
    private final NewsDao mNewsDao;
    private final Executor mExecutor;

    public NewsCache(NewsDao newsDao, Executor executor) {
        this.mNewsDao = newsDao;
        this.mExecutor = executor;
    }

    /**
     * Returns a {@link LiveData} that informs observer to data change.
     */
    public LiveData<List<News>> getNews() {
        return this.mNewsDao.getNews();
    }

    /**
     * Delete old data and insert new one.
     */
    public void refresh(List<News> newsList) {
        this.mExecutor.execute(() -> {
            filterNewsInTrash(newsList);
            mNewsDao.deleteAll();
            mNewsDao.insertAll(newsList);
        });
    }

    /**
     * Insert data to database.
     */
    public void insertAll(List<News> newsList) {
        this.mExecutor.execute(() -> {
            filterNewsInTrash(newsList);
            mNewsDao.insertAll(newsList);
        });
    }

    /**
     * Update the given news inTrash property to 1 which means specified news would not returned.
     */
    public void addToTrash(News news) {
        this.mExecutor.execute(() -> {
            news.setIsInTrash(1);
            this.mNewsDao.update(news);
        });
    }

    /**
     * Update the given news inTrash property to 0 which means specified news would returned.
     */
    public void restoreFromTrash(News news) {
        this.mExecutor.execute(() -> {
            news.setIsInTrash(0);
            this.mNewsDao.update(news);
        });
    }

    /**
     * Deletes all the data in database.
     */
    @SuppressWarnings("unused")
    public void deleteAll() {
        this.mExecutor.execute(mNewsDao::deleteAll);
    }

    /**
     * Compare given data with news which inTrash property is 1, and set new data inTrash property
     * to 1. This is because if newly inserted data is already received and marked inTrash to 1
     * the data should still be in trash and should not be returned.
     */
    private void filterNewsInTrash(@NonNull List<News> newsList) {
        List<News> newsInTrash = mNewsDao.getNewsInTrash();
        for (int i = 0; i < newsList.size(); i++) {
            for (int j = 0; j < newsInTrash.size(); j++) {
                if (newsList.get(i).getObjectId() == newsInTrash.get(j).getObjectId()) {
                    newsList.get(i).setIsInTrash(1);
                }
            }
        }
    }
}
