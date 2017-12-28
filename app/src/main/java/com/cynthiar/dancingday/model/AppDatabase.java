package com.cynthiar.dancingday.model;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by CynthiaR on 12/27/2017.
 */

@Database(entities=DanceClassCard.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DanceClassCardDao getClassCardDao();
}
