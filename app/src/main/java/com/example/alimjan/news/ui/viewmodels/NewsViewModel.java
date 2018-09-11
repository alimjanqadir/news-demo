package com.example.alimjan.news.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.alimjan.news.api.ServiceGenerator;
import com.example.alimjan.news.api.service.HackNewsService;
import com.example.alimjan.news.api.NetworkState;
import com.example.alimjan.news.data.NewsRepository;
import com.example.alimjan.news.db.NewsCache;
import com.example.alimjan.news.db.NewsDatabase;
import com.example.alimjan.news.model.News;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * ViewModel class for {@link com.example.alimjan.news.ui.fragments.NewsListFragment} and
 * {@link com.example.alimjan.news.ui.fragments.NewsDetailFragment}.
 */
public class NewsViewModel extends AndroidViewModel {

    // Repository class for achieving source of truth.
    private final NewsRepository mRepository;
    // A single data source which connected to database, inform observer whenever data changes.
    private final LiveData<List<News>> mNews;

    // Current selected item and it's position, this field is used for double pane layout.
    private final MutableLiveData<News> mSelectedItem = new MutableLiveData<>();
    private int mSelectedItemPosition;

    /* Indicates initial data loaded or not this field is saved inside viewmodel to survive
     * configuration change
     * */
    private boolean mInInitialDataLoaded;


    NewsViewModel(Application application) {
        super(application);
        this.mRepository = setUpRepository(application);
        this.mNews = this.mRepository.getNews();
    }

    /**
     * Prepare needed dependencies for {@link NewsRepository} and return new instance.
     */
    private NewsRepository setUpRepository(Application application) {
        NewsCache cache = new NewsCache(NewsDatabase.getInstance(application.getApplicationContext()).getNewsDao(),
                Executors.newSingleThreadExecutor());
        HackNewsService service = ServiceGenerator.createService(HackNewsService.class);
        return new NewsRepository(cache, service);
    }

    /**
     * An observable LiveData for observing room database result.
     */
    public LiveData<List<News>> getNews() {
        return mNews;
    }


    /**
     * Refresh repository by send request to webservice.
     */
    public void refresh() {
        mRepository.refresh();
    }

    /**
     * Load more data from webservice .
     */
    public void loadMore() {
        this.mRepository.loadMore();
    }

    /**
     * Add the corresponding news to trash by updating it's property.
     */
    public void addToTrash(News news) {
        this.mRepository.addToTrash(news);
    }

    /**
     * Restores the corresponding from trash by updating it's property.
     */
    public void restoreFromTrash(News news) {
        this.mRepository.restoreNews(news);
    }

    /**
     * Retry webservice request when not successful.
     */
    public void retry() {
        this.mRepository.retry();
    }

    /**
     * Returns the {@link NetworkState} which indicates current network state.
     */
    public MutableLiveData<NetworkState> getNetworkState() {
        return mRepository.getNetworkState();
    }

    /**
     * Return current selected item.
     */
    public LiveData<News> getSelectedItem() {
        return mSelectedItem;
    }

    /**
     * This method invoke when {@link android.arch.lifecycle.ViewModel} destroys, reason
     * {@link NewsViewModel} override this method is to inform repository to clean it's resource.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        this.mRepository.onCleared();
    }

    /**
     * Set selected item data and it's position.
     *
     * @param news     Selected item data.
     * @param position Selected item position.
     */
    public void setSelectItem(News news, int position) {
        this.mSelectedItem.setValue(news);
        this.mSelectedItemPosition = position;
    }

    /**
     * Returns selected item position.
     */
    public int getSelectedItemPosition() {
        return mSelectedItemPosition;
    }

    /**
     * Returns whether initial data loaded or not.
     */
    public boolean isInitialDataLoaded() {
        return mInInitialDataLoaded;
    }

    /**
     * Set initial data loading flag to true.
     */
    public void setInitialDataLoaded() {
        this.mInInitialDataLoaded = true;
    }


}
