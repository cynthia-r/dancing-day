package com.cynthiar.dancingday.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.database.AppDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CynthiaR on 12/27/2017.
 */

public class DanceClassCardDao {

    public List<DanceClassCard> getClassCardList(){
        // Get a readable instance of the database
        SQLiteDatabase db = AppDatabase.getInstance().getReadableDatabase();

        // Retrieve all columns
        String[] projection = {
                DanceClassCard._ID,
                DanceClassCard.COLUMN_COMPANY,
                DanceClassCard.COLUMN_COUNT,
                DanceClassCard.COLUMN_EXPIRATION_DATE,
                DanceClassCard.COLUMN_PURCHASE_DATE
        };

        // No filter

        // Sort by purchase date
        String sortOrder = DanceClassCard.COLUMN_PURCHASE_DATE + " ASC";

        // Retrieve the cards
        Cursor cursor = db.query(
                DanceClassCard.TABLE,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        List<DanceClassCard> danceClassCardList = new ArrayList<>();
        while (cursor.moveToNext()) {
            danceClassCardList.add(this.readRow(cursor));
        }
        return danceClassCardList;
    }

    public long saveCard(DanceClassCard danceClassCard){
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = this.createRow(danceClassCard);

        // Insert the new row, returning the primary key value of the new row
        return db.insert(DanceClassCard.TABLE, null, values);
    }

    public int updateCard(DanceClassCard danceClassCard) {
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // New values for all column
        ContentValues values = this.createRow(danceClassCard);

        // Which row to update, based on ID
        String selection = DanceClassCard._ID + " = ?";
        String[] selectionArgs = { Long.toString(danceClassCard.getId()) };

        // Update the row
        return db.update(
                DanceClassCard.TABLE,
                values,
                selection,
                selectionArgs);
    }

    public int deleteCard(DanceClassCard danceClassCard) {
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // Which row to delete, based on ID
        String selection = DanceClassCard._ID + " = ?";
        String[] selectionArgs = { Long.toString(danceClassCard.getId()) };

        // Delete the row
        return db.delete(DanceClassCard.TABLE, selection, selectionArgs);
    }

    public DanceClassCard readRow(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DanceClassCard._ID));
        String companyKey = cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_COMPANY));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_COUNT));
        DateTime expirationDate = DateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_EXPIRATION_DATE)), DanceClassCard.dateTimeFormatter);
        DateTime purchaseDate = DateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_PURCHASE_DATE)), DanceClassCard.dateTimeFormatter);
        return new DanceClassCard(id, Schools.DanceCompany.fromString(companyKey), count, purchaseDate, expirationDate);
    }

    public ContentValues createRow(DanceClassCard danceClassCard) {
        ContentValues values = new ContentValues();
        values.put(DanceClassCard.COLUMN_COMPANY, danceClassCard.getCompanyKey());
        values.put(DanceClassCard.COLUMN_COUNT, danceClassCard.getCount());
        values.put(DanceClassCard.COLUMN_EXPIRATION_DATE, danceClassCard.getExpirationDate().toString(DanceClassCard.dateTimeFormatter));
        values.put(DanceClassCard.COLUMN_PURCHASE_DATE, danceClassCard.getPurchaseDate().toString(DanceClassCard.dateTimeFormatter));
        return values;
    }
}
