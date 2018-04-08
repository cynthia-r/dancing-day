package com.cynthiar.dancingday.model;

import android.provider.BaseColumns;

import com.cynthiar.dancingday.card.CardListViewAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class DanceClassCard implements BaseColumns {
    private long id;
    private String companyKey;
    private Schools.DanceCompany company;
    private int count;
    private DateTime expirationDate;
    private DateTime purchaseDate;

    public static final String TABLE = "DanceClassCard";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_EXPIRATION_DATE = "expirationDate";
    public static final String COLUMN_PURCHASE_DATE = "purchaseDate";

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
    private static final String KEY_SEPARATOR = "-";

    public DanceClassCard(Schools.DanceCompany company, int count, DateTime purchaseDate, DateTime expirationDate) {
        this.setCompanyKey(company.Key);
        this.count = count;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
    }

    public DanceClassCard(long id, Schools.DanceCompany company, int count, DateTime purchaseDate, DateTime expirationDate) {
        this(company, count, purchaseDate, expirationDate);
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyKey() {
        return this.companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
        this.company = Schools.DanceCompany.fromString(this.companyKey);
    }

    public Schools.DanceCompany getCompany() {
        if (null != this.company)
            return this.company;
        return Schools.DanceCompany.fromString(this.companyKey);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DateTime getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public DateTime getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isValid(StringBuilder validationMessageBuilder) {
        boolean validationSuccessful = true;
        if (this.count <= 0) {
            validationMessageBuilder.append("No available classes on card");
            validationSuccessful = false;
        }
        if (this.expirationDate.isBeforeNow()) {
            validationMessageBuilder.append("Card expired on: " + this.expirationDate.toString(CardListViewAdapter.ExpirationDateFormatter));
            validationSuccessful = false;
        }
        return validationSuccessful;
    }

    public void debit() throws Exception {
        StringBuilder validationMessageBuilder = new StringBuilder();
        if (!this.isValid(validationMessageBuilder))
            throw new Exception("Cannot debit expired card for " + this.company.Key + " " + validationMessageBuilder.toString());
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
