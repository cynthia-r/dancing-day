package com.cynthiar.dancingday.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CynthiaR on 12/27/2017.
 */

public class DanceClassCardDao {

    public List<DanceClassCard> getClassCardList(){
        // "SELECT * FROM DanceClassCard"
        SQLiteDatabase db = AppDatabase.getInstance().getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DanceClassCard.COLUMN_COMPANY,
                DanceClassCard.COLUMN_COUNT
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
          //      FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

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
            String companyKey = cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_COMPANY));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_COUNT));
            danceClassCardList.add(new DanceClassCard(Schools.DanceCompany.fromString(companyKey), count, new DateTime(), new DateTime()));
        }
        return danceClassCardList;
    }

    public long saveCard(DanceClassCard danceClassCard){
        // Gets the data repository in write mode
        SQLiteDatabase db = AppDatabase.getInstance().getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DanceClassCard.COLUMN_COMPANY, danceClassCard.getCompanyKey());
        values.put(DanceClassCard.COLUMN_COUNT, danceClassCard.getCount());

        // Insert the new row, returning the primary key value of the new row
        return db.insert(DanceClassCard.TABLE, null, values);
    }

    /*public void updateCard(String oldCardKey, DanceClassCard danceClassCard) {
        this.preferencesModel.updateCard(oldCardKey, danceClassCard);
    }

    public void deleteCard(String cardKey) {
        this.preferencesModel.deleteCard(cardKey);
    }
*/
}
