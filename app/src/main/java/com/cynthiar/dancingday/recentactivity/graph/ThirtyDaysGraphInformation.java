package com.cynthiar.dancingday.recentactivity.graph;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;

/**
 * Created by CynthiaR on 4/5/2018.
 */

public class ThirtyDaysGraphInformation implements GraphInformation {
    @Override
    public int numberOfDays() {
        return 30;
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
        return new GraphSeriesLabelFormatter(RecentActivityGraphActivity.dateTimeFormatter2);
    }
}
