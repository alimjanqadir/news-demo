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

    public static NewsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, NewsDatabase.class, "news.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unused")
    public static void destroyDatabaseInstance() {
        INSTANCE = null;
    }
}
