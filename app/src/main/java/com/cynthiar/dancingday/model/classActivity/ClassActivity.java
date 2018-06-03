package com.cynthiar.dancingday.model.classActivity;

import android.content.Context;
import android.provider.BaseColumns;

import com.cynthiar.dancingday.SettingsActivity;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.database.DanceClassCardDao;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Represents a class activity: i.e. when the user attended a dance class.
 */

public class ClassActivity implements BaseColumns {
    private long id;
    private DummyItem dummyItem;
    private DateTime date;
    private PaymentType paymentType;
    private DanceClassCard danceClassCard;
    private long danceClassCardId;
    private boolean isConfirmed;
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmm");
    private static final String KEY_SEPARATOR = "_";
    private static final String CLASS_SEPARATOR = "#";

    public static final String TABLE = "ClassActivity";
    public static final String COLUMN_CLASS = "class";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PAYMENT_TYPE = "paymentType";
    public static final String COLUMN_CARD_ID = "cardId";
    public static final String COLUMN_IS_CONFIRMED = "isConfirmed";

    private DanceClassCardDao danceClassCardDao;

    public ClassActivity(DummyItem dummyItem, DateTime activityDate, PaymentType paymentType, DanceClassCard danceClassCard){
        this.dummyItem = dummyItem;
        this.date = activityDate;
        this.paymentType = paymentType;
        this.danceClassCard = danceClassCard;
        this.isConfirmed = false;
        this.danceClassCardDao = new DanceClassCardDao();
    }

    public ClassActivity(long id, DummyItem dummyItem, DateTime activityDate, PaymentType paymentType, long danceClassCardId, boolean isConfirmed){
        this(dummyItem, activityDate, paymentType, null);
        this.id = id;
        this.danceClassCardId = danceClassCardId;
        this.isConfirmed = isConfirmed;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DummyItem getDanceClass() {
        return dummyItem;
    }

    public DateTime getDate() {
        return date;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public DanceClassCard getDanceClassCard() {
        if (null != this.danceClassCard)
            return danceClassCard;
        else if (0 != this.danceClassCardId)
            return this.danceClassCardDao.getClassCardById(this.danceClassCardId);
        else
            return null;
    }

    public long getDanceClassCardId() {
        if (0 != this.danceClassCardId)
            return this.danceClassCardId;
        else if (null != this.danceClassCard)
            return this.danceClassCard.getId();
        else
            return 0;
    }

    public void setDanceClassCard(DanceClassCard danceClassCard) {
        this.danceClassCard = danceClassCard;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void confirm() {
        this.isConfirmed = true;
    }

    /*
        Checks whether this activity happened recently.
        The time range is 1h30 before the current time to 10 minutes after.
     */
    public boolean isCurrent() {
        // Check the day and time
        DateTime currentTime = DateTime.now();
        return (currentTime.minusMinutes(90).isBefore(this.date)
                && currentTime.plusMinutes(10).isAfter(this.date));
    }

    public static ClassActivity buildActivity(Context context, DummyItem danceClass) {
        // Build and return the new activity
        return ClassActivity.buildActivity(context, danceClass, DateTime.now());
    }

    public static ClassActivity buildActivity(Context context, DummyItem danceClass, DateTime activityDate) {
        if (null == context || null == danceClass)
            return null;

        // Determine the payment type and if to use a card
        PaymentType paymentType;
        DanceClassCard cardToUse = new DanceClassCardDao().getCompatibleCard(danceClass.school);

        // If there are no cards available, a single ticket is used
        if (null == cardToUse)
            paymentType = PaymentType.SingleTicket;
        else
            paymentType = PaymentType.PunchCard;

        // Build and return the new activity
        return new ClassActivity(danceClass, activityDate, paymentType, cardToUse);
    }

    /*
        Returns a unique representation of this class activity.
     */
    public String toKey() {
        String classKey = this.dummyItem.toKey();
        String cardKey = this.danceClassCard.toKey();
        String activityKey = DummyUtils.join(ClassActivity.KEY_SEPARATOR, this.date.toString(ClassActivity.dateTimeFormatter), this.paymentType.toString());
        return DummyUtils.join(ClassActivity.CLASS_SEPARATOR, classKey, cardKey, activityKey);
    }

    /*
        Parses the specified string into a class activity.
     */
    public static ClassActivity fromKey(String classActivity) {
        if (null == classActivity || classActivity.isEmpty())
            return null;

        String[] mainElements = classActivity.split(ClassActivity.CLASS_SEPARATOR);
        if (null == mainElements || 3 != mainElements.length)
            return null;

        String classKey = mainElements[0];
        String cardKey = mainElements[1];
        String activityKey = mainElements[2];

        String[] keyElements = activityKey.split(ClassActivity.KEY_SEPARATOR);
        if (null == keyElements || 2 != keyElements.length)
            return null;

        ClassActivity classActivityParsed = null;
        try {
            DummyItem dummyItemParsed = DummyItem.fromKey(classKey);
            DanceClassCard danceClassCard = DanceClassCard.fromKey(cardKey);
            classActivityParsed = new ClassActivity(dummyItemParsed,
                    DateTime.parse(keyElements[0], ClassActivity.dateTimeFormatter),
                    PaymentType.valueOf(keyElements[1]), danceClassCard);
        } catch (Exception e) {
            return null;
        }
        return classActivityParsed;
    }
}
