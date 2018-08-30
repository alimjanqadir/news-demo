package com.example.alimjan.news.db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.alimjan.news.model.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM News")
    DataSource.Factory<Integer, News> getNews();

    @Insert
    void insertAll(List<News> newsList);

    @Delete
    void delete(News news);

}
