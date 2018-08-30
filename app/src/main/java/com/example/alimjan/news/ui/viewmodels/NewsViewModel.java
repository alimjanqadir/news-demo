package com.example.alimjan.news.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.example.alimjan.news.model.News;
import com.example.alimjan.news.data.NewsRepository;

public class NewsViewModel extends ViewModel {

    public final LiveData<PagedList<News>> news;

    public NewsViewModel(NewsRepository repository) {
        this.news = repository.getNews();
    }
}
