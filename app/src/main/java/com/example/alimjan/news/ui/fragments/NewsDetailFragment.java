package com.example.alimjan.news.ui.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.alimjan.news.R;
import com.example.alimjan.news.api.NetworkState;
import com.example.alimjan.news.databinding.FragmentNewsDetailBinding;
import com.example.alimjan.news.model.News;
import com.example.alimjan.news.ui.callbacks.OnRetryListener;
import com.example.alimjan.news.ui.viewmodels.NewsViewModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;


/**
 * A simple Fragment implementation which shows a web page in a {@link WebView}. This fragment can
 * show in two different ways, single pane mode and double pane mode which would create a
 * Master/Detail flow. In single pane mode fragment has entire screen to show it self and data would
 * be delivered via intent. In double pane mode there are two fragment shown side by side, one is
 * list of news and this show detail of selected news, in this situation data would not delivered
 * by intent it would get from a shared viewmodel {@link NewsViewModel}.
 */
public class NewsDetailFragment extends Fragment implements OnRetryListener {

    private static final String ARG_NEWS = "news";
    private static final String HACKER_NEWS_ITEM_URL_FORMAT = "https://news.ycombinator.com/item?id=%d";

    // Binding instance for the fragment ui
    private FragmentNewsDetailBinding mBinding;

    // News to be loaded
    private News mNews;

    // Flag that indicates error received or not
    private boolean mIsErrorReceivedFlag;
    // Flag that indicates whether clear history or not when page finish.
    private boolean mClearHistoryFlag;
    /* Errors will received even when page finish, this flag used to prevent error show up even page
     * visible. */
    private boolean mIsPageVisible;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Url that is used for WebView inside the fragment.
     * @return An instance of NewsDetailFragment.
     */
    public static NewsDetailFragment newInstance(News param) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNews = getArguments().getParcelable(ARG_NEWS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the with data binding.
        mBinding = FragmentNewsDetailBinding.inflate(inflater);
        // Provide a retry listener callback to data binding.
        mBinding.setOnRetryListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Webview setup
        setupWebView();

        // Load page which data delivered from intent.
        loadUrl();

        // Show loading before page is visible
        mBinding.setNetworkState(NetworkState.LOADING);

        /* Instantiate viewmodel and observe current selected item. This snippet just for double
         * pane layout mode.
         */
        if (getActivity() != null) {
            NewsViewModel newsViewModel = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
            newsViewModel.getSelectedItem().observe(this, news -> {
                // Check if news same with last one, if same no need to load
                if (!sameWithLastNews(news)) {
                    mNews = news;
                    /* When loaded new page history should be deleted, otherwise back to previous
                     * page does not function correctly.
                     */
                    mClearHistoryFlag = true;

                    // Load page which data observed from viewmodel.
                    loadUrl();
                }

            });
        }
    }

    /**
     * Checks is current news same with last news.
     */
    private boolean sameWithLastNews(News news) {
        return mNews != null && mNews.getObjectId() == (news != null ? news.getObjectId() : -1);
    }

    /**
     * Load page from url.
     */
    private void loadUrl() {
        if (mNews != null) {
            /* Url would be null if the url is from HackerNews, when url not null url itself would
             * be used, when url is null which means story is belong to HackerNews then make a
             * HackerNews url by news object id then load.
             * */
            final String urlStr;
            if (mNews.getUrl() != null) {
                urlStr = mNews.getUrl();
            } else {
                urlStr = String.format(Locale.getDefault(), HACKER_NEWS_ITEM_URL_FORMAT, mNews.getId());
            }

            // Check url format
            try {
                final URL url = new URL(urlStr);
                mBinding.webView.loadUrl(url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mBinding.setNetworkState(NetworkState.error(getString(R.string.msg_wrong_url_format)));
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = mBinding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mBinding.setNetworkState(NetworkState.LOADING);
                clearFlags();
                // Set scroll position to origin this is useful in double pane mode.
                mBinding.scrollContainer.scrollTo(0, 0);
            }


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (!mIsPageVisible) {
                    mIsErrorReceivedFlag = true;
                    mBinding.setNetworkState(NetworkState.error(getString(R.string.msg_request_failed)));
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!mIsPageVisible) {
                    mIsErrorReceivedFlag = true;
                    mBinding.setNetworkState(NetworkState.error(getString(R.string.msg_request_failed)));
                }
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                mIsPageVisible = true;

                // Prevent failed request to marked as finished successfully
                if (!mIsErrorReceivedFlag) {
                    mBinding.setNetworkState(NetworkState.SUCCESS);
                }

                // Clear history, this operation only take effect when page loaded
                if (mClearHistoryFlag) {
                    mClearHistoryFlag = false;
                    view.clearHistory();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mIsPageVisible = true;

                // Prevent failed request to marked as finished successfully
                if (!mIsErrorReceivedFlag) {
                    mBinding.setNetworkState(NetworkState.SUCCESS);
                }

                // Clear history, this operation only take effect when page loaded
                if (mClearHistoryFlag) {
                    mClearHistoryFlag = false;
                    view.clearHistory();
                }
            }
        });
    }

    /**
     * Clear all request flags.
     */
    private void clearFlags() {
        mIsErrorReceivedFlag = false;
        mIsPageVisible = false;
    }

    /**
     * Retry the webview request.
     */
    @Override
    public void onRetry() {
        mBinding.webView.reload();
    }


    /**
     * Called from activity to ask permission to finish itself.
     *
     * @return Returns true when back press used by fragment false otherwise.
     */
    public boolean onBackPressed() {
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
            return true;
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Webview lifecycle callbacks mapped to fragment lifecycle
        mBinding.webView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        // Webview lifecycle callbacks mapped to fragment lifecycle
        mBinding.webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Webview lifecycle callbacks mapped to fragment lifecycle
        mBinding.webView.destroy();
    }
}
