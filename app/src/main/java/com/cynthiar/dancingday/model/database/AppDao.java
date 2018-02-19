package com.cynthiar.dancingday.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CynthiaR on 12/28/2017.
 */

public abstract class AppDao<T> {

    protected abstract String getTableName();
    protected abstract String[] getTableColumns();
    protected abstract String[] getTableColumnTypes();
    protected abstract ContentValues createRow(T entity);
    protected abstract T readRow(Cursor cursor);
    protected abstract long getRowId(T entity);

    protected List<T> retrieveEntities(String selection, String[] selectionArgs,
                                       String groupBy, String having, String orderBy,
                                       int count) {
        // Get a readable instance of the database
        SQLiteDatabase db = AppDatabase.getInstance().getReadableDatabase();

        // Retrieve all columns
        String[] projection = this.getTableColumns();

        // Retrieve the entities
        Cursor cursor = db.query(this.getTableName(), projection, selection,
                selectionArgs, groupBy, having, orderBy);
        List<T> entityList = new ArrayList<>();
        int currentCount = 0;
        while (currentCount < count && cursor.moveToNext()) {
            entityList.add(this.readRow(cursor));
            currentCount++;
        }
        return entityList;
    }

    protected List<T> retrieveEntities(String selection, String[] selectionArgs,
                                       String groupBy, String having, String orderBy) {
        return this.retrieveEntities(selection, selectionArgs, groupBy, having, orderBy, Integer.MAX_VALUE);
    }

    protected T retrieveEntity(String selection, String[] selectionArgs,
                                       String groupBy, String having, String orderBy) {
        List<T> entityList = this.retrieveEntities(selection, selectionArgs, groupBy, having, orderBy, 1);
        if ((null != entityList) && !entityList.isEmpty())
            return entityList.get(0);
        else
            return null;
    }

    protected long insertEntity(T entity){
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = this.createRow(entity);

        // Insert the new row, returning the primary key value of the new row
        return db.insert(this.getTableName(), null, values);
    }

    protected int updateEntity(T entity) {
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // New values for all column
        ContentValues values = this.createRow(entity);

        // Which row to update, based on ID
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(this.getRowId(entity)) };

        // Update the row
        return db.update(
                this.getTableName(),
                values,
                selection,
                selectionArgs);
    }

    protected int deleteEntity(T entity) {
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // Which row to delete, based on ID
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(this.getRowId(entity)) };

        // Delete the row
        return db.delete(this.getTableName(), selection, selectionArgs);
    }
}
