package com.example.alimjan.news.data;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.alimjan.news.api.pojo.HitsItem;
import com.example.alimjan.news.api.pojo.NewsResponse;
import com.example.alimjan.news.api.service.HackNewsService;
import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.model.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A callback which part of The Paging Library for creating Database + Networking model, when no
 * data and last data loaded from {@link android.arch.paging.DataSource}
 * {@link PagedList.BoundaryCallback#onZeroItemsLoaded()} and
 * {@link PagedList.BoundaryCallback#onItemAtEndLoaded(Object)} ()} called respectively to
 * give us chance to request data from web service and save it to local database.
 */
class NewsRepositoryBoundaryCallBack extends PagedList.BoundaryCallback<News> {
    // Two type of data source which represents database and network.
    private final NewsCache mCache;
    private final HackNewsService mService;
    // indicate current page used when send request to web service.
    private int mPage = 0;
    // For preventing repetitive request
    private boolean mIsLoading = false;


    NewsRepositoryBoundaryCallBack(NewsCache cache, HackNewsService service) {
        this.mService = service;
        this.mCache = cache;
    }

    @Override
    public void onZeroItemsLoaded() {
        super.onZeroItemsLoaded();
        if (!mIsLoading) {
            requestAndSave();
        }
    }

    @Override
    public void onItemAtEndLoaded(@NonNull News itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);
        if (!mIsLoading) {
            requestAndSave();
        }
    }

    /**
     * Request data from web service insert result to database.
     */
    private void requestAndSave() {
        mIsLoading = true; // For preventing repetitive request
        this.mService.getLatestNews(mPage).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Result from web service adapted to local use.
                    List<HitsItem> hits = response.body().getHits();
                    List<News> newsList = new ArrayList<>();
                    for (HitsItem hit : hits) {
                        newsList.add(new News(hit.getTitle(), hit.getAuthor(),
                                hit.getCreatedAtI(), hit.getStoryUrl()));
                    }

                    // Received data insert to database.
                    mCache.insertAll(newsList);

                    mIsLoading = false;
                    mPage++;
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                mIsLoading = false;
            }
        });

    }
}
