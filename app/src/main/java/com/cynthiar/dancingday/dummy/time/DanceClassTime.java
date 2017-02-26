package com.cynthiar.dancingday.dummy.time;

import com.cynthiar.dancingday.dummy.DummyUtils;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by CynthiaR on 2/26/2017.
 */

public class DanceClassTime {
    public LocalTime startTime;
    public LocalTime endTime;
    private DateTimeFormatter timeWithHalfFormatter = DateTimeFormat.forPattern("h:m a");
    private DateTimeFormatter timeWithoutHalfFormatter = DateTimeFormat.forPattern("h:m");
    private DateTimeFormatter timeWithoutHalfWithoutMinutesFormatter = DateTimeFormat.forPattern("h");
    private DateTimeFormatter timeWithHalfWithoutMinutesFormatter = DateTimeFormat.forPattern("h a");


    public DanceClassTime(LocalTime pStartTime, LocalTime pEndTime) {
        startTime = pStartTime;
        endTime = pEndTime;
    }

    public DanceClassTime(int startHours, int startMinutes, int endHours, int endMinutes) {
        startTime = new LocalTime(startHours, startMinutes);
        endTime = new LocalTime(endHours, endMinutes);
    }

    public static DanceClassTime create(String timeString) {
        return DummyUtils.parseTime(timeString);
    }

    @Override
    public String toString() {
        int startMinutes = startTime.getMinuteOfHour();
        int endMinutes = endTime.getMinuteOfHour();

        // Generate start time string
        String startTimeString;
        int startHours = startTime.getHourOfDay();
        int endHours = endTime.getHourOfDay();

        // Start and end are in same half
        if ((startHours < 12 && endHours < 12) || (startHours >= 12 && endHours >= 12)) {
            startTimeString = startTime.toString(
                startMinutes > 0 ? timeWithoutHalfFormatter : timeWithoutHalfWithoutMinutesFormatter);
        }
        // Start and end are not in same half
        else {
            startTimeString = startTime.toString(
                startMinutes > 0 ? timeWithHalfFormatter : timeWithHalfWithoutMinutesFormatter);
        }

        // Generate end time string
        String endTimeString = endTime.toString(
            endMinutes > 0 ? timeWithHalfFormatter : timeWithHalfWithoutMinutesFormatter);

        // Concatenate start and end
        return startTimeString.concat("-").concat(endTimeString);
    }
}
