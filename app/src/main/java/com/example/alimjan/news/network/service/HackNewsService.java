package com.example.alimjan.news.network.service;

import com.example.alimjan.news.network.pojo.News;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HackNewsService {


    /**
     * Get latest news from API service;
     * @return The latest news.
     */
    @GET("search_by_date")
    Call<News> getLatestNews();
}
