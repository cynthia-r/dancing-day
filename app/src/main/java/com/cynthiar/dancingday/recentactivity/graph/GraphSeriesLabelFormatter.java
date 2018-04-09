package com.cynthiar.dancingday.recentactivity.graph;

import com.jjoe64.graphview.DefaultLabelFormatter;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by CynthiaR on 4/8/2018.
 */

public class GraphSeriesLabelFormatter extends DefaultLabelFormatter {
    private DateTimeFormatter dateTimeFormatter;
    public GraphSeriesLabelFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            // Format x values as dates
            DateTime dateTime = new DateTime((long) value);
            return dateTime.toString(dateTimeFormatter);
        } else {
            // Format y values as amounts of time
            int hours = (int)value / 60;
            int minutes = (int)value % 60;
            LocalTime localTime = new LocalTime(hours, minutes);
            return minutes > 0 ? localTime.toString(RecentActivityGraphActivity.localTimeFormatter)
                    : localTime.toString(RecentActivityGraphActivity.localTimeFormatterNoMinutes);
        }
    }
}
