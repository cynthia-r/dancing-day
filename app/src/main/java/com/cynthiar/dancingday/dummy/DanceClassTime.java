package com.cynthiar.dancingday.dummy;

import org.joda.time.LocalTime;

/**
 * Created by CynthiaR on 2/26/2017.
 */

public class DanceClassTime {
    public LocalTime startTime;
    public LocalTime endTime;
    public DanceClassTime(LocalTime pStartTime, LocalTime pEndTime) {
        startTime = pStartTime;
        endTime = pEndTime;
    }

    public DanceClassTime(int startHours, int startMinutes, int endHours, int endMinutes) {
        startTime = new LocalTime(startHours, startMinutes);
        endTime = new LocalTime(endHours, endMinutes);
    }
}
