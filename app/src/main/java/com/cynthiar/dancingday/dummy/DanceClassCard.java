package com.cynthiar.dancingday.dummy;

import org.joda.time.DateTime;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class DanceClassCard {

    public Schools.DanceSchool school;
    public final int count;
    public final DateTime expirationDate;
    public final DateTime purchaseDate;

    public DanceClassCard(Schools.DanceSchool school, int count, DateTime purchaseDate, DateTime expirationDate) {
        this.school = school;
        this.count = count;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
    }

}
