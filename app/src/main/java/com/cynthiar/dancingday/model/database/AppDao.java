package com.cynthiar.dancingday.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    protected Iterator<T> retrieveEntityIterator(String selection, String[] selectionArgs,
                                                 String groupBy, String having, String orderBy) {
        // Get a readable instance of the database
        SQLiteDatabase db = AppDatabase.getInstance().getReadableDatabase();

        // Retrieve all columns
        String[] projection = this.getTableColumns();

        // Retrieve the entities
        Cursor cursor = db.query(this.getTableName(), projection, selection,
                selectionArgs, groupBy, having, orderBy);
        return new EntityIterator(this, cursor);
    }

    protected List<T> retrieveEntities(String selection, String[] selectionArgs,
                                       String groupBy, String having, String orderBy,
                                       int count) {

        // Get an iterator on the retrieved entities
        Iterator<T> entityIterator = this.retrieveEntityIterator(selection, selectionArgs, groupBy, having, orderBy);

        // Populate and return the list of results
        List<T> entityList = new ArrayList<>();
        int currentCount = 0;
        while (currentCount < count && entityIterator.hasNext()) {
            entityList.add(entityIterator.next());
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

        // New values for all columns
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

    protected int updateColumns(HashMap<String, String> columnValues, String selection, String[] selectionArgs) {
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // New values for the specified columns
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> entry:columnValues.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }

        // Update the row
        return db.update(
                this.getTableName(),
                values,
                selection,
                selectionArgs);
    }

    protected int deleteEntity(T entity) {
        // Select which row to delete, based on ID
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(this.getRowId(entity)) };

        // Delete the row
        return this.deleteEntities(selection, selectionArgs);
    }

    protected int deleteEntities(String selection, String[] selectionArgs) {
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // Delete the rows
        return db.delete(this.getTableName(), selection, selectionArgs);
    }
}
