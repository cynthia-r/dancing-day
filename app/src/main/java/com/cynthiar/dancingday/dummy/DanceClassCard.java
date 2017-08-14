package com.cynthiar.dancingday.dummy;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class DanceClassCard {

    public Schools.DanceSchool school;
    public final int count;
    public final DateTime expirationDate;
    public final DateTime purchaseDate;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
    private static final String KEY_SEPARATOR = "-";

    public DanceClassCard(Schools.DanceSchool school, int count, DateTime purchaseDate, DateTime expirationDate) {
        this.school = school;
        this.count = count;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
    }

    /*
        Returns a unique representation of this dance class card.
     */
    public String toKey() {
        return DummyUtils.join(DanceClassCard.KEY_SEPARATOR, this.school.toString(), this.purchaseDate.toString(DanceClassCard.dateTimeFormatter), this.expirationDate.toString(DanceClassCard.dateTimeFormatter));
    }

    /*
        Parses the specified string into a dance class card.
     */
    public static DanceClassCard fromKey(String danceClassCard) {
        if (null == danceClassCard || danceClassCard.isEmpty())
            return null;

        String[] keyElements = danceClassCard.split(DanceClassCard.KEY_SEPARATOR);
        if (null == keyElements || 3 != keyElements.length)
            return null;

        return new DanceClassCard(Schools.DanceSchool.fromString(keyElements[0]), Integer.parseInt(keyElements[1]),
                DateTime.parse(keyElements[2], DanceClassCard.dateTimeFormatter),
                DateTime.parse(keyElements[3], DanceClassCard.dateTimeFormatter));
    }
}
