package com.cynthiar.dancingday.model.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by CynthiaR on 12/28/2017.
 */

public class ClassActivityDao extends AppDao<ClassActivity> {
    private DanceClassCardDao danceClassCardDao = new DanceClassCardDao();

    public long registerActivity(ClassActivity classActivity) throws Exception {
        if (null == classActivity)
            return -1;

        // Debit the corresponding card if needed
        if (PaymentType.PunchCard == classActivity.getPaymentType()) {
            // Debit
            DanceClassCard cardToUse = classActivity.getDanceClassCard();
            cardToUse.debit();

            // Update the card
            danceClassCardDao.updateCard(cardToUse);
        }

        // Register the activity
        long id = this.insertEntity(classActivity);
        classActivity.setId(id);
        return id;
    }

    public void cancelActivity(ClassActivity classActivity) {
        if (null == classActivity)
            return;

        // Credit the corresponding card if needed
        if (PaymentType.PunchCard == classActivity.getPaymentType()) {
            // Credit
            DanceClassCard cardToUse = classActivity.getDanceClassCard();
            cardToUse.credit();

            // Update the card
            danceClassCardDao.updateCard(cardToUse);
        }

        // Cancel the activity
        this.deleteEntity(classActivity);
    }

    public List<ClassActivity> getActivityList() {
        // Sort by most recent activity
        String sortOrder = ClassActivity.COLUMN_DATE + " DESC";
        return this.retrieveEntities(null, null, null, null, sortOrder);
    }

    public ClassActivity getActivityById(long classActivityId) {
        // Filter by ID
        String selection = ClassActivity._ID + " = ?";
        String[] selectionArgs = { Long.toString(classActivityId) };

        // Retrieve the card
        List<ClassActivity> classActivityList = this.retrieveEntities(selection, selectionArgs, null, null, null);
        if (!classActivityList.isEmpty())
            return classActivityList.get(0);
        return null;
    }

    @Override
    protected String getTableName() {
        return ClassActivity.TABLE;
    }

    @Override
    protected String[] getTableColumns() {
        return new String[] {
                ClassActivity._ID,
                ClassActivity.COLUMN_CLASS,
                ClassActivity.COLUMN_DATE,
                ClassActivity.COLUMN_PAYMENT_TYPE,
                ClassActivity.COLUMN_CARD_ID,
                ClassActivity.COLUMN_IS_CONFIRMED
        };
    }

    @Override
    protected String[] getTableColumnTypes() {
        return new String[] {
                "INTEGER PRIMARY KEY",
                "TEXT",
                "TEXT",
                "TEXT",
                "INTEGER",
                "TEXT"
        };
    }

    @Override
    protected ContentValues createRow(ClassActivity classActivity) {
        ContentValues values = new ContentValues();
        values.put(ClassActivity.COLUMN_CLASS, classActivity.getDanceClass().toKey());
        values.put(ClassActivity.COLUMN_DATE, classActivity.getDate().toString(ClassActivity.dateTimeFormatter));
        values.put(ClassActivity.COLUMN_PAYMENT_TYPE, classActivity.getPaymentType().toString());
        values.put(ClassActivity.COLUMN_CARD_ID, classActivity.getDanceClassCard().getId());
        values.put(ClassActivity.COLUMN_IS_CONFIRMED, classActivity.isConfirmed());
        return values;
    }

    @Override
    protected ClassActivity readRow(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(ClassActivity._ID));
        DummyItem dummyItem = DummyItem.fromKey(cursor.getString(cursor.getColumnIndexOrThrow(ClassActivity.COLUMN_CLASS)));
        DateTime date = DateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(ClassActivity.COLUMN_DATE)), ClassActivity.dateTimeFormatter);
        PaymentType paymentType = PaymentType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ClassActivity.COLUMN_PAYMENT_TYPE)));
        long cardId = cursor.getLong(cursor.getColumnIndexOrThrow(ClassActivity.COLUMN_CARD_ID));
        boolean isConfirmed = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(ClassActivity.COLUMN_IS_CONFIRMED)));
        return new ClassActivity(id, dummyItem, date, paymentType, cardId, isConfirmed);
    }

    @Override
    protected long getRowId(ClassActivity entity) {
        return entity.getId();
    }
}
