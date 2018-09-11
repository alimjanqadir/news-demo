package com.example.alimjan.news.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alimjan.news.model.News;

import java.util.List;

/**
 * A simple data access class for {@link NewsDatabase}.
 */
@Dao
public interface NewsDao {

    @Query("SELECT * FROM News WHERE isInTrash != 1 ORDER BY created_date DESC")
    LiveData<List<News>> getNews();

    @Query("SELECT * FROM News WHERE isInTrash = 1")
    List<News> getNewsInTrash();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<News> newsList);

    @Delete
    void delete(News news);

    @Update
    void update(News... news);

    @Query("DELETE FROM NEWS WHERE isInTrash != 1")
    void deleteAll();
}
