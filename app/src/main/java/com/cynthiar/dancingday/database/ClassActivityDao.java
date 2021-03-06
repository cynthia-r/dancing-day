package com.cynthiar.dancingday.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CynthiaR on 12/28/2017.
 */

public class ClassActivityDao extends AppDao<ClassActivity> {
    private DanceClassCardDao danceClassCardDao = new DanceClassCardDao();

    public List<ClassActivity> getRecentActivityList() {
        // Sort by most recent activity
        String sortOrder = ClassActivity.COLUMN_DATE + " DESC";

        // Retrieve the activities up to 30 days ago
        DateTime dateThreshold = DateTime.now().minusDays(30);
        String selection = ClassActivity.COLUMN_DATE + " > ?";
        String[] selectionArgs = { dateThreshold.toString(ClassActivity.dateTimeFormatter) };
        return this.retrieveEntities(selection, selectionArgs, null, null, sortOrder);
    }

    public ClassActivity getActivityById(long classActivityId) {
        // Filter by ID
        String selection = ClassActivity._ID + " = ?";
        String[] selectionArgs = { Long.toString(classActivityId) };

        // Retrieve the class activity
        return this.retrieveEntity(selection, selectionArgs, null, null, null);
    }

    public List<ClassActivity> getActivityListByDanceClass(String danceClassKey) {
        // Filter by ID
        String selection = ClassActivity.COLUMN_CLASS + " = ?";
        String[] selectionArgs = { danceClassKey };

        // Sort by most recent activity
        String sortOrder = ClassActivity.COLUMN_DATE + " DESC";

        // Retrieve the class activities
        return this.retrieveEntities(selection, selectionArgs, null, null, sortOrder);
    }

    public Iterator<ClassActivity> getActivityHistory(int numberOfDays) {
        // Retrieve the activities up to the number of days specified
        DateTime dateThreshold = DateTime.now().minusDays(numberOfDays);
        String selection = ClassActivity.COLUMN_DATE + " > ?";
        String[] selectionArgs = { dateThreshold.toString(ClassActivity.dateTimeFormatter) };

        // Sort by oldest activity
        String sortOrder = ClassActivity.COLUMN_DATE + " ASC";

        // Retrieve the class activities
        return this.retrieveEntityIterator(selection, selectionArgs, null, null, sortOrder);
    }

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

    public void deleteActivity(ClassActivity classActivity) {
        if (null == classActivity)
            return;

        // Cancel the activity
        this.deleteEntity(classActivity);
    }

    public boolean confirmActivity(ClassActivity classActivity) {
        if (null == classActivity)
            return false;

        // Update the activity to be confirmed
        classActivity.confirm();
        return 1 == this.updateEntity(classActivity);
    }

    public void cancelActivity(long classActivityId) {
        if (classActivityId <= 0)
            return;

        ClassActivity classActivity = this.getActivityById(classActivityId);
        if (null == classActivity)
            return;

        this.cancelActivity(classActivity);
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

    public boolean editPaymentType(ClassActivity classActivity, PaymentType newPaymentType) throws Exception {
        // Check if there's a change needed
        PaymentType currentPaymentType = classActivity.getPaymentType();
        if ((null == classActivity) || (newPaymentType == currentPaymentType))
            return false;

        // If the current payment is a card, credit it
        if (PaymentType.PunchCard == currentPaymentType) {
            // Credit
            DanceClassCard cardToUse = classActivity.getDanceClassCard();
            cardToUse.credit();

            // Update the card
            danceClassCardDao.updateCard(cardToUse);

            // Unlink the activity from the card
            classActivity.setDanceClassCard(null);
        }
        // If the current payment is a ticket, find the new card and debit it
        else {
            // Debit and update the card
            DanceClassCard cardToUse = danceClassCardDao.getCompatibleCard(classActivity.getDanceClass().school);
            if (null != cardToUse) {
                cardToUse.debit();
                danceClassCardDao.updateCard(cardToUse);

                // Link the activity to the card
                classActivity.setDanceClassCard(cardToUse);
            }
            else
                throw new Exception("Cannot change payment ticket to a card: there was no compatible card available.");
        }

        // Set the payment type and update the class activity
        classActivity.setPaymentType(newPaymentType);
        return this.updateEntity(classActivity) > 0;
    }

    public int confirmPendingActivities() {
        DateTime thresholdDate = DateTime.now().minusDays(1);
        String selection = ClassActivity.COLUMN_IS_CONFIRMED + " = ? AND " + ClassActivity.COLUMN_DATE + " < ?";
        String[] selectionArgs = new String[] { Boolean.toString(false), thresholdDate.toString(ClassActivity.dateTimeFormatter) };

        HashMap<String, String> columnValues = new HashMap<>();
        columnValues.put(ClassActivity.COLUMN_IS_CONFIRMED, Boolean.toString(true));

        return this.updateColumns(columnValues, selection, selectionArgs);
    }

    public int updateActivityDate(long classActivityId, DateTime activityDate) {
        // Filter by ID
        String selection = ClassActivity._ID + " = ?";
        String[] selectionArgs = { Long.toString(classActivityId) };

        // Set the new activity date
        HashMap<String, String> columnValues = new HashMap<>();
        columnValues.put(ClassActivity.COLUMN_DATE, activityDate.toString(ClassActivity.dateTimeFormatter));

        // Update the activity
        return this.updateColumns(columnValues, selection, selectionArgs);
    }

    public int deleteOldActivities() {
        // Filter based on the threshold date
        DateTime oldActivityThresholdDate = DateTime.now().minusDays(60);
        String selection = ClassActivity.COLUMN_DATE + " < ?";
        String[] selectionArgs = { oldActivityThresholdDate.toString(ClassActivity.dateTimeFormatter) };

        // Delete the old class activities
        return this.deleteEntities(selection, selectionArgs);
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
        values.put(ClassActivity.COLUMN_CARD_ID, classActivity.getDanceClassCardId());
        values.put(ClassActivity.COLUMN_IS_CONFIRMED, Boolean.toString(classActivity.isConfirmed()));
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
