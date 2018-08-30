package com.example.alimjan.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.alimjan.news.fragments.NewsDetailFragment;


/**
 * A simple activity that implements a NewsDetailFragment to show news article on a WebView.
 */
public class NewsDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NEWS_URL = "url";
    public static final String EXTRA_NEWS_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // Data extracted from intent.
        String url = null;
        String newsTitle = null;
        if (getIntent() != null) {
            url = getIntent().getStringExtra(EXTRA_NEWS_URL);
            newsTitle = getIntent().getStringExtra(EXTRA_NEWS_TITLE);
        }


        // NewsDetailFragment added to container
        if (url != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.news_detail_fragment_container,
                    NewsDetailFragment.newInstance(url)).commit();
        }
    }
}
