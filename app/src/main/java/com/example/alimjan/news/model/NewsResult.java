package com.example.alimjan.news.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

public class NewsResult {
    private final LiveData<List<News>> mNews;
    private final MutableLiveData<String> mNetworkError;

    public NewsResult(LiveData<List<News>> news, MutableLiveData<String> networkError) {
        this.mNews = news;
        this.mNetworkError = networkError;
    }

    public LiveData<List<News>> getNews() {
        return mNews;
    }


    public LiveData<String> getNetworkError() {
        return mNetworkError;
    }
}
