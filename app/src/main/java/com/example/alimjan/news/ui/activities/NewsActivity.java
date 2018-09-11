package com.example.alimjan.news.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alimjan.news.R;
import com.example.alimjan.news.ui.fragments.NewsDetailFragment;

public class NewsActivity extends AppCompatActivity {

    private NewsDetailFragment mNewsDetailFragment;
    private boolean mIsDoublePane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // This particular fragment instance needed for passing back press events to
        // NewsListFragment.
        mNewsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.news_detail_fragment);

        // Activity is in double pane layout mode or not
        mIsDoublePane = findViewById(R.id.news_detail_fragment) != null;
    }

    @Override
    public void onBackPressed() {
        // if NewsFragment allows to back then proceed, if not then let NewsFragment handle the
        // back press.
        if (mNewsDetailFragment != null && mNewsDetailFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }


    /**
     * Returns activity is in double pane layout mode or not.
     */
    public boolean isDoublePane() {
        return mIsDoublePane;
    }
}
