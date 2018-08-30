package com.example.alimjan.news.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.alimjan.news.data.NewsRepository;
import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.db.NewsDatabase;

import java.util.concurrent.Executors;

public class NewsViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;

    public NewsViewModelFactory(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            NewsDatabase database = NewsDatabase.getInMemoryDatabase(this.mContext);
            NewsCache cache = new NewsCache(database.getNewsDao(), Executors.newSingleThreadExecutor());
            NewsRepository newsRepository = new NewsRepository(cache);

            return (T) new NewsViewModel(newsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
