package com.cynthiar.dancingday.model.database;

/**
 * Created by CynthiaR on 12/27/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AppDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DancingDay.db";

    private static AppDatabase mDatabaseInstance;

    private static final AppDao[] appDaos = new AppDao[] {
        new DanceClassCardDao(),
        new ClassActivityDao()
    };

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, AppDatabase.DATABASE_VERSION);
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
        for (AppDao appDao:appDaos
                ) {
            String sqlCreateEntries = AppDatabase.getSqlCreateEntries(appDao);
            db.execSQL(sqlCreateEntries);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recreate the whole database // TODO check if should do anything
        for (AppDao appDao:appDaos
                ) {
            String sqlDeleteEntries = AppDatabase.getSqlDeleteEntries(appDao);
            db.execSQL(sqlDeleteEntries);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Call upgrade with the versions swapped
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void clearDb() {
        SQLiteDatabase db = mDatabaseInstance.getWritableDatabase();
        for (AppDao appDao:appDaos
                ) {
            db.execSQL(AppDatabase.getSqlClearEntries(appDao));
        }
        db.execSQL("VACUUM;");
    }

    private static String getSqlCreateEntries(AppDao appDao) {
        StringBuilder sqlCreateDatabaseScript = new StringBuilder();
        sqlCreateDatabaseScript.append("CREATE TABLE " + appDao.getTableName() + " (");
        String[] tableColumns = appDao.getTableColumns();
        String[] tableColumnTypes = appDao.getTableColumnTypes();
        for (int i=0; i < tableColumns.length - 1; i++) {
            sqlCreateDatabaseScript.append(tableColumns[i] + " " + tableColumnTypes[i] + ",");
        }
        sqlCreateDatabaseScript.append(
                tableColumns[tableColumns.length - 1] + " " + tableColumnTypes[tableColumns.length - 1] + ");");

        return sqlCreateDatabaseScript.toString();
    }

    private static String getSqlDeleteEntries(AppDao appDao) {
        return "DROP TABLE IF EXISTS " + appDao.getTableName() + ";";
    }

    private static String getSqlClearEntries(AppDao appDao) {
        return "DELETE FROM " + appDao.getTableName() + ";";
    }
}
