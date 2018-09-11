package com.example.alimjan.news.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class that represents a news, could be seen as integral part of this application.
 */
@SuppressWarnings("unused")
@Entity
public class News implements Parcelable {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "object_id")
    private long objectId;

    @ColumnInfo(name = "title")
    private String newsTitle;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "created_date")
    private String createdDate;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "isInTrash")
    private int isInTrash;

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public News(long objectId, String newsTitle, String author, String createdDate, String url) {
        this.id = objectId;
        this.objectId = objectId;
        this.newsTitle = newsTitle;
        this.author = author;
        this.createdDate = createdDate;
        this.url = url;
    }

    protected News(Parcel in) {
        id = in.readLong();
        objectId = in.readLong();
        newsTitle = in.readString();
        author = in.readString();
        createdDate = in.readString();
        url = in.readString();
        isInTrash = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
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

    public void setIsInTrash(int isInTrash) {
        this.isInTrash = isInTrash;
    }

    public int getIsInTrash() {
        return this.isInTrash;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(objectId);
        dest.writeString(newsTitle);
        dest.writeString(author);
        dest.writeString(createdDate);
        dest.writeString(url);
        dest.writeInt(isInTrash);
    }

    @Override
    public String toString() {
        return "News{" +
                "id = '" + id + '\'' +
                ",newsTitle = '" + newsTitle + '\'' +
                ",author = '" + author + '\'' +
                ",createdDate = '" + createdDate + '\'' +
                ",url = '" + url + '\'' +
                ",isInTrash = '" + isInTrash + '\'' +
                "}";
    }
}
