package com.example.alimjan.news.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.example.alimjan.news.data.NewsRepository;
import com.example.alimjan.news.model.News;

/**
 * ViewModel class for ui of {@link com.example.alimjan.news.ui.fragments.NewsListFragment}
 */
public class NewsViewModel extends ViewModel {

    // Repository class for achieving source of truth.
    private final NewsRepository mRepository;
    // Live PagedList for observing records from room data source.
    private final LiveData<PagedList<News>> mNews;

    NewsViewModel(NewsRepository repository) {
        this.mNews = repository.getNews();
        this.mRepository = repository;
    }

    /**
     * An observable LiveData for observing room database result.
     */
    public LiveData<PagedList<News>> getNews() {
        return mNews;
    }

    /**
     * Remove corresponding news from database.
     */
    public void removeNews(News news) {
        this.mRepository.remove(news);
    }

    public void refresh() {
        this.mRepository.refresh();
    }
}
