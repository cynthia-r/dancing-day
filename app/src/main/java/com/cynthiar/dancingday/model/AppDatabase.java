package com.cynthiar.dancingday.model;

/*
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by CynthiaR on 12/27/2017.
 */
/*
@Database(entities=DanceClassCard.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DanceClassCardDao getClassCardDao();
}
*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.content.Context.MODE_PRIVATE;

public class AppDatabase extends SQLiteOpenHelper {
    private static AppDatabase mDatabaseInstance;
    //private static volatile Object syncObject = new Object();

    public static final String DATABASE_NAME = "DancingDay.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DanceClassCard.TABLE + " (" +
                    DanceClassCard._ID + " INTEGER PRIMARY KEY," +
                    DanceClassCard.COLUMN_COMPANY + " TEXT," +
                    DanceClassCard.COLUMN_COUNT + " INTEGER)";
    // TODO implement schema

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DanceClassCard.TABLE;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /*private AppDatabase(Context context) {
        mContext = context;
        mSqLiteDatabase = openOrCreateDatabase("your database name",MODE_PRIVATE,null);
    }*/

    public static void initializeDb(Context context) {
        mDatabaseInstance = new AppDatabase(context);
    }

    public static void finalizeDb() {
        mDatabaseInstance.close();
    }

    public static AppDatabase getInstance() {
        return mDatabaseInstance;
    }

    /*public static AppDatabase getInstance(Context context) {
        if (null != mDatabaseInstance)
            return mDatabaseInstance;

        synchronized (syncObject) {
            if (null == mDatabaseInstance) {
                mDatabaseInstance = new AppDatabase(context);
            }
        }
        return mDatabaseInstance;
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the tables
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recreate the whole database
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Call upgrade with the versions swapped
        onUpgrade(db, oldVersion, newVersion);
    }
}
