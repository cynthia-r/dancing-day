package com.cynthiar.dancingday.model.classActivity;

import android.content.Context;

import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Preferences;
import com.cynthiar.dancingday.model.database.DanceClassCardDao;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by CynthiaR on 9/24/2017.
 */

public class ClassActivity {
    public final DummyItem dummyItem;
    public final DateTime date;
    public final PaymentType paymentType;
    public final DanceClassCard danceClassCard;
    public boolean isConfirmed;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmm");
    private static final String KEY_SEPARATOR = "_";
    private static final String CLASS_SEPARATOR = "#";

    public ClassActivity(DummyItem dummyItem, DateTime activityDate, PaymentType paymentType, DanceClassCard danceClassCard){
        this.dummyItem = dummyItem;
        this.date = activityDate;
        this.paymentType = paymentType;
        this.danceClassCard = danceClassCard;
        this.isConfirmed = false;
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
        if (null == context || null == danceClass)
            return null;

        // Determine the payment type and if to use a card
        PaymentType paymentType;
        DanceClassCard cardToUse = null;

        // Retrieve the list of current class cards
        List<DanceClassCard> classCardList = new DanceClassCardDao().getClassCardList();

        // If there are no cards available, a single ticket is used
        if (null == classCardList || classCardList.isEmpty())
            paymentType = PaymentType.SingleTicket;
        else {
            // Try to find a compatible card for this class
            Iterator<DanceClassCard> danceClassCardIterator = classCardList.iterator();
            do {
                DanceClassCard danceClassCard = danceClassCardIterator.next();
                if (danceClass.school.isInCompany(danceClassCard.getCompany())
                        && danceClassCard.isValid())
                    cardToUse = danceClassCard;
            }
            while (null == cardToUse && danceClassCardIterator.hasNext());

            // If a card was found use it
            if (null == cardToUse)
                paymentType = PaymentType.SingleTicket;
            else
                paymentType = PaymentType.PunchCard;
        }

        // Build and return the new activity
        return new ClassActivity(danceClass, DateTime.now(), paymentType, cardToUse);
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
