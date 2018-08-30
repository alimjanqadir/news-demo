package com.example.alimjan.news.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Model class for data binding and room table entity.
 */
@Entity
public class News {

    @PrimaryKey
    private long aid;

    @ColumnInfo(name = "title")
    private String newsTitle;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "created_date")
    private String createdDate;

    @ColumnInfo(name = "url")
    private String url;

    public News(long aid, String newsTitle, String author, String createdDate, String url) {
        this.aid = aid;
        this.newsTitle = newsTitle;
        this.author = author;
        this.createdDate = createdDate;
        this.url = url;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }
}
