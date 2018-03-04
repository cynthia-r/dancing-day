package com.cynthiar.dancingday.database;

import android.database.Cursor;

import java.util.Iterator;

/**
 * Created by cynthiar on 2/25/2018.
 */
public class EntityIterator<T> implements Iterator<T> {

    private AppDao<T> dao;
    private Cursor cursor;

    public EntityIterator(AppDao dao, Cursor dbCursor) {
        this.dao = dao;
        this.cursor = dbCursor;
    }

    @Override
    public boolean hasNext() {
        return this.cursor.moveToNext();
    }

    @Override
    public T next() {
        return this.dao.readRow(this.cursor);
    }
}