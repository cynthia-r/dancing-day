package com.cynthiar.dancingday.model.time;

/**
 * Created by CynthiaR on 2/26/2017.
 */

public class TimeParts {
    public int hours;
    public int minutes;
    public TimeHalf timeHalf;

    public TimeParts(int pHours, int pMinutes, TimeHalf pTimeHalf) {
        hours = pHours;
        minutes = pMinutes;
        timeHalf = pTimeHalf;
    }
}

