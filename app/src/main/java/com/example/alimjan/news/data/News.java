package com.example.alimjan.news.data;

/**
 * Data class for RecyclerView data binding.
 */
public class News {
    private final String newsTitle;
    private final String author;
    private final String timeOffset;
    private final String url;

    public News(String newsTitle, String author, String timeOffset, String url) {
        this.newsTitle = newsTitle;
        this.author = author;
        this.timeOffset = timeOffset;
        this.url = url;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getTimeOffset() {
        return timeOffset;
    }

    public String getUrl() {
        return url;
    }
}
