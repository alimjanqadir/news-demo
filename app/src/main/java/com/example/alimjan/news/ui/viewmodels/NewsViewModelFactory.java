package com.example.alimjan.news.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.alimjan.news.api.ServiceGenerator;
import com.example.alimjan.news.api.service.HackNewsService;
import com.example.alimjan.news.data.NewsRepository;
import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.db.NewsDatabase;

import java.util.concurrent.Executors;

/**
 * Factory class for ViewModel instantiation.
 */
public class NewsViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;

    public NewsViewModelFactory(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            // Necessary dependencies prepared to NewsRepository.
            NewsDatabase database = NewsDatabase.getInstance(this.mContext);
            NewsCache cache = new NewsCache(database.getNewsDao(), Executors.newSingleThreadExecutor());
            HackNewsService service = ServiceGenerator.createService(HackNewsService.class);

            //noinspection unchecked
            return (T) new NewsViewModel(new NewsRepository(cache, service));
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
