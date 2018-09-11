package com.example.alimjan.news.api.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Hit {

    @SerializedName("objectID")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("author")
    private String author;

    @SerializedName("created_at_i")
    private String createdAtI;

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


    @Override
    public String toString() {
        return
                "Hit{" +
                        ",author = '" + author + '\'' +
                        ",created_at_i = '" + createdAtI + '\'' +
                        ",title = '" + title + '\'' +
                        ",url = '" + url + '\'' +
                        "}";
    }
}