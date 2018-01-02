package com.cynthiar.dancingday.model.database;

/**
 * Created by CynthiaR on 12/27/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AppDatabase extends SQLiteOpenHelper {
    private static AppDatabase mDatabaseInstance;

    public static final String DATABASE_NAME = "DancingDay.db";
    private static final AppDao[] appDaos = new AppDao[] {
        new DanceClassCardDao(),
        new ClassActivityDao()
    };

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static void initializeDb(Context context) {
        mDatabaseInstance = new AppDatabase(context);
    }

    public static void finalizeDb() {
        mDatabaseInstance.close();
    }

    public static AppDatabase getInstance() {
        return mDatabaseInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the tables
        db.execSQL(AppDatabase.getSqlCreateEntries());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recreate the whole database // TODO check if should do anything
        db.execSQL(AppDatabase.getSqlDeleteEntries());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Call upgrade with the versions swapped
        onUpgrade(db, oldVersion, newVersion);
    }

    private static String getSqlCreateEntries() {
        StringBuilder sqlCreateDatabaseScript = new StringBuilder();
        for (AppDao appDao:appDaos
                ) {
            sqlCreateDatabaseScript.append("CREATE TABLE " + appDao.getTableName() + " (");
            String[] tableColumns = appDao.getTableColumns();
            String[] tableColumnTypes = appDao.getTableColumnTypes();
            for (int i=0; i < tableColumns.length - 1; i++) {
                sqlCreateDatabaseScript.append(tableColumns[i] + " " + tableColumnTypes[i] + ",");
            }
            sqlCreateDatabaseScript.append(
                    tableColumns[tableColumns.length - 1] + " " + tableColumnTypes[tableColumns.length - 1] + ");");
        }
        return  sqlCreateDatabaseScript.toString();
    }

    private static String getSqlDeleteEntries() {
        StringBuilder sqlDeleteDatabaseScript = new StringBuilder();
        for (AppDao appDao:appDaos
                ) {
            sqlDeleteDatabaseScript.append("DROP TABLE IF EXISTS " + appDao.getTableName() + ";");
        }
        return sqlDeleteDatabaseScript.toString();
    }
}
