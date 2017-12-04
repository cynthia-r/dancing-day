package com.cynthiar.dancingday.model;

import com.cynthiar.dancingday.card.CardListViewAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class DanceClassCard {

    private Schools.DanceCompany company;
    private int count;
    private final DateTime expirationDate;
    private final DateTime purchaseDate;
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
    private static final String KEY_SEPARATOR = "-";

    public DanceClassCard(Schools.DanceCompany company, int count, DateTime purchaseDate, DateTime expirationDate) {
        this.company = company;
        this.count = count;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
    }

    public Schools.DanceCompany getCompany() {
        return this.company;
    }

    public int getCount() {
        return this.count;
    }

    public DateTime getExpirationDate() {
        return this.expirationDate;
    }

    public boolean isValid() {
        return this.count > 0 && this.expirationDate.isAfterNow();
    }

    public void debit() throws Exception {
        if (!this.isValid())
            throw new Exception("Cannot debit expired card for: " + this.company.Key + ". Card expired on: " + this.expirationDate.toString(CardListViewAdapter.ExpirationDateFormatter));
        this.count--;
    }

    public void credit() {
        this.count++;
    }

    /*
        Returns a unique representation of this dance class card.
     */
    public String toKey() {
        return DummyUtils.join(DanceClassCard.KEY_SEPARATOR, this.company.toString(), Integer.toString(this.count), this.purchaseDate.toString(DanceClassCard.dateTimeFormatter), this.expirationDate.toString(DanceClassCard.dateTimeFormatter));
    }

    /*
        Parses the specified string into a dance class card.
     */
    public static DanceClassCard fromKey(String danceClassCard) {
        if (null == danceClassCard || danceClassCard.isEmpty())
            return null;

        String[] keyElements = danceClassCard.split(DanceClassCard.KEY_SEPARATOR);
        if (null == keyElements || 4 != keyElements.length)
            return null;

        DanceClassCard danceClassCardParsed = null;
        try {
            danceClassCardParsed = new DanceClassCard(Schools.DanceCompany.fromString(keyElements[0]),
                    Integer.parseInt(keyElements[1]),
                    DateTime.parse(keyElements[2], DanceClassCard.dateTimeFormatter),
                    DateTime.parse(keyElements[3], DanceClassCard.dateTimeFormatter));
        }
        catch (Exception e){
            return null;
        }
        return danceClassCardParsed;
    }
}
