package com.example.alimjan.news.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.alimjan.news.model.News;

/**
 * A simple implementation of RoomDatabase for data persistence through out the project.
 */
@Database(
        entities = News.class,
        version = 1,
        exportSchema = false
)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase INSTANCE;

    public abstract NewsDao getNewsDao();

    public static NewsDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context, NewsDatabase.class).build();
        }
        return INSTANCE;
    }

    public static void destroyDatabaseInstance() {
        INSTANCE = null;
    }
}
