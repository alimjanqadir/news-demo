package com.example.alimjan.news.api.service;

import com.example.alimjan.news.api.pojo.NewsResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HackNewsService {


    /**
     * Get latest news from API service;
     *
     * @return The latest news.
     */
    @GET("search_by_date?query=android&tags=story&hitsPerPage=20")
    Single<NewsResponse> getNews(@Query("page") int page);
}
