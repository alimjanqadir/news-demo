package com.example.alimjan.news.ui.activities;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.alimjan.news.R;
import com.example.alimjan.news.databinding.ActivityNewsDetailBinding;
import com.example.alimjan.news.model.News;
import com.example.alimjan.news.ui.fragments.NewsDetailFragment;


/**
 * A simple activity that implements a NewsDetailFragment to show news article on a WebView.
 */
public class NewsDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NEWS = "news";
    private NewsDetailFragment mNewsDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNewsDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        News news = null;
        // Data extracted from intent.
        if (getIntent() != null) {
            news = getIntent().getParcelableExtra(EXTRA_NEWS);
            addFragment(news);
        }

        // Set title
        if (news != null) {
            binding.setTitle(news.getNewsTitle());
        }

        // Set toolbar to actionbar
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Adds fragment to it's container.
     *
     * @param param Parameter passed to {@link android.support.v4.app.Fragment}
     */
    private void addFragment(News param) {
        // This particular fragment instance needed for passing back press events to
        this.mNewsDetailFragment = NewsDetailFragment.newInstance(param);
        // NewsDetailFragment added to container
        getSupportFragmentManager().beginTransaction().add(R.id.news_detail_fragment_container,
                this.mNewsDetailFragment).commit();
    }

    @Override
    public void onBackPressed() {
        // if NewsFragment allows to back then proceed, if not then let NewsFragment handle the
        // back press.
        if (this.mNewsDetailFragment != null) {
            if (this.mNewsDetailFragment.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }
}
