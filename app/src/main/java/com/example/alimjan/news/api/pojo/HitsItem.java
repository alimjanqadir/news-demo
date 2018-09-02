package com.example.alimjan.news.api.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class HitsItem {

    @SerializedName("objectID")
    private long id;

    @SerializedName("author")
    private String author;

    @SerializedName("created_at_i")
    private String createdAtI;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;
    @SerializedName("story_title")
    private String storyTitle;

    @SerializedName("story_url")
    private String storyUrl;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setCreatedAtI(String createdAtI) {
        this.createdAtI = createdAtI;
    }

    public String getCreatedAtI() {
        return createdAtI;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }

    public String getStoryUrl() {
        return storyUrl;
    }

    @Override
    public String toString() {
        return
                "HitsItem{" +
                        ",author = '" + author + '\'' +
                        ",created_at_i = '" + createdAtI + '\'' +
                        ",title = '" + title + '\'' +
                        ",url = '" + url + '\'' +
                        ",story_title = '" + storyTitle + '\'' +
                        ",story_url = '" + storyUrl + '\'' +
                        "}";
    }
}