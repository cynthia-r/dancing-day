package com.cynthiar.dancingday.recentactivity.graph;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/*
    7 days:
    minY = 0
    max Y = 180.0 // 0h to 3h
    numHorizontalLabels = 5 // show 5 dates
    numVerticalLabels = 7 // show every half-hour
    */
public class SevenDaysGraphInformation implements GraphInformation {

    @Override
    public int numberOfDays() {
        return 7;
    }

    @Override
    public int dayInterval() {
        return 1;
    }

    @Override
    public int minY() {
        return 0;
    }

    @Override
    public int maxY() {
        return 180;
    }

    @Override
    public int numXs() {
        return 5;
    }

    @Override
    public int numYs() {
        return 7;
    }

    @Override
    public Series getSeries(DataPoint[] dataPoints) {
        return new BarGraphSeries<>(dataPoints);
    }

    @Override
    public LabelFormatter getLabelFormatter() {
        return new GraphSeriesLabelFormatter(RecentActivityGraphActivity.dateTimeFormatter);
    }
}
