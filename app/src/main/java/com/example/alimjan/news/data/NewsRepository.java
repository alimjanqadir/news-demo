package com.example.alimjan.news.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.alimjan.news.api.NetworkState;
import com.example.alimjan.news.api.pojo.Hit;
import com.example.alimjan.news.api.service.HackNewsService;
import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.model.News;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple repository class for merging two data source to achieve source of truth principle.
 */
public class NewsRepository {
    // A data cache that is implemented by sqlite database.
    private final NewsCache mNewsCache;
    private final HackNewsService mService; // Webservice
    // Disposable that contains all requests which would release when repository no longer valid.
    private final CompositeDisposable mCompositeDisposable;
    // Network state of the current webservice request.
    private MutableLiveData<NetworkState> mNetWorkState = new MutableLiveData<>();
    // Current requested page increment every successful request.
    private int mPage = 0;

    public NewsRepository(NewsCache cache, HackNewsService service) {
        this.mNewsCache = cache;
        this.mService = service;
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * Returns a Live PagedList that linked to database,
     */
    public LiveData<List<News>> getNews() {
        return this.mNewsCache.getNews();
    }

    /**
     * Send request that fetch page 0 from webservice.
     */
    public void refresh() {
        mPage = 0;
        mNetWorkState.postValue(NetworkState.LOADING);
        mCompositeDisposable.add(this.mService.getNews(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            this.mNetWorkState.setValue(NetworkState.SUCCESS);
                            mNewsCache.refresh(mapHitsToNews(response.getHits()));
                            mPage++;
                            // If it's the last page set network state would set to end
                            if (response.getPage() == response.getNbPages()) {
                                this.mNetWorkState.setValue(NetworkState.END);
                            }
                        }, throwable -> this.mNetWorkState.setValue(NetworkState.error(throwable.getMessage()))
                ));
    }

    /**
     * Load more data from webservice.
     */
    public void loadMore() {
        mNetWorkState.postValue(NetworkState.LOADING);
        mCompositeDisposable.add(this.mService.getNews(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            this.mNetWorkState.setValue(NetworkState.SUCCESS);
                            mNewsCache.insertAll(mapHitsToNews(response.getHits()));
                            mPage++;
                        }, throwable -> this.mNetWorkState.setValue(NetworkState.error(throwable.getMessage()))));
    }

    /**
     * retry failed request.
     */
    public void retry() {
        if (mPage == 0) {
            refresh();
        } else {
            loadMore();
        }
    }

    /**
     * Add news to trash.
     */
    public void addToTrash(News news) {
        mNewsCache.addToTrash(news);
    }

    /**
     * Restore news from trash.
     */
    public void restoreNews(News news) {
        mNewsCache.restoreFromTrash(news);
    }

    /**
     * Return current network state.
     */
    public MutableLiveData<NetworkState> getNetworkState() {
        return mNetWorkState;
    }

    /**
     * Map {@link Hit} to {@link News} to adapt webservice data to local use.
     */
    @NonNull
    private List<News> mapHitsToNews(@NonNull List<Hit> hitList) {
        List<News> newsList = new ArrayList<>();
        for (Hit hit : hitList) {
            newsList.add(new News(hit.getId(), hit.getTitle(), hit.getAuthor(), hit.getCreatedAtI(), hit.getUrl()));
        }
        return newsList;
    }


    /**
     * Called from viewmodel when destroys to inform repository to clear it's resource.
     */
    public void onCleared() {
        mCompositeDisposable.dispose();
    }
}
