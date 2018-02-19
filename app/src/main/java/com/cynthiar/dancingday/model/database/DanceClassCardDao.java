package com.cynthiar.dancingday.model.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.Schools;

import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.List;

/**
 * Created by CynthiaR on 12/27/2017.
 */

public class DanceClassCardDao extends AppDao<DanceClassCard> {

    public List<DanceClassCard> getClassCardList() {
        // Sort by purchase date, with no filter
        String sortOrder = DanceClassCard.COLUMN_PURCHASE_DATE + " ASC";

        // Retrieve the cards
        return this.retrieveEntities(null, null, null, null, sortOrder);
    }

    public DanceClassCard getClassCardById(long cardId) {
        // Filter by ID
        String selection = DanceClassCard._ID + " = ?";
        String[] selectionArgs = { Long.toString(cardId) };

        // Retrieve the card
        List<DanceClassCard> danceClassCardList = this.retrieveEntities(selection, selectionArgs, null, null, null);
        if (!danceClassCardList.isEmpty())
            return danceClassCardList.get(0);
        return null;
    }

    public DanceClassCard getCompatibleCard(Schools.DanceSchool school) {
        // Retrieve the list of current class cards
        List<DanceClassCard> classCardList = new DanceClassCardDao().getClassCardList();
        // TODO filter by company directly

        // Return if no cards are available
        if (null == classCardList ||classCardList.isEmpty())
            return null;

        // Try to find a compatible card for this class
        DanceClassCard cardToUse = null;
        Iterator<DanceClassCard> danceClassCardIterator = classCardList.iterator();
        do {
            DanceClassCard danceClassCard = danceClassCardIterator.next();
            if (school.isInCompany(danceClassCard.getCompany())
                    && danceClassCard.isValid())
                cardToUse = danceClassCard;
        }
        while (null == cardToUse && danceClassCardIterator.hasNext());
        return cardToUse;
    }

    public long saveCard(DanceClassCard danceClassCard){
        long id = this.insertEntity(danceClassCard);
        danceClassCard.setId(id);
        return id;
    }

    public int updateCard(DanceClassCard danceClassCard) {
        return this.updateEntity(danceClassCard);
    }

    public int deleteCard(DanceClassCard danceClassCard) {
        return this.deleteEntity(danceClassCard);
    }

    @Override
    protected String getTableName() {
        return DanceClassCard.TABLE;
    }

    @Override
    protected String[] getTableColumns() {
        return new String[] {
                DanceClassCard._ID,
                DanceClassCard.COLUMN_COMPANY,
                DanceClassCard.COLUMN_COUNT,
                DanceClassCard.COLUMN_EXPIRATION_DATE,
                DanceClassCard.COLUMN_PURCHASE_DATE
        };
    }

    @Override
    protected String[] getTableColumnTypes() {
        return new String[] {
                "INTEGER PRIMARY KEY",
                "TEXT",
                "INTEGER",
                "TEXT",
                "TEXT"
        };
    }

    @Override
    protected long getRowId(DanceClassCard danceClassCard) {
        return danceClassCard.getId();
    }

    @Override
    protected DanceClassCard readRow(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(DanceClassCard._ID));
        String companyKey = cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_COMPANY));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_COUNT));
        DateTime expirationDate = DateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_EXPIRATION_DATE)), DanceClassCard.dateTimeFormatter);
        DateTime purchaseDate = DateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(DanceClassCard.COLUMN_PURCHASE_DATE)), DanceClassCard.dateTimeFormatter);
        return new DanceClassCard(id, Schools.DanceCompany.fromString(companyKey), count, purchaseDate, expirationDate);
    }

    @Override
    protected ContentValues createRow(DanceClassCard danceClassCard) {
        ContentValues values = new ContentValues();
        values.put(DanceClassCard.COLUMN_COMPANY, danceClassCard.getCompanyKey());
        values.put(DanceClassCard.COLUMN_COUNT, danceClassCard.getCount());
        values.put(DanceClassCard.COLUMN_EXPIRATION_DATE, danceClassCard.getExpirationDate().toString(DanceClassCard.dateTimeFormatter));
        values.put(DanceClassCard.COLUMN_PURCHASE_DATE, danceClassCard.getPurchaseDate().toString(DanceClassCard.dateTimeFormatter));
        return values;
    }
}
