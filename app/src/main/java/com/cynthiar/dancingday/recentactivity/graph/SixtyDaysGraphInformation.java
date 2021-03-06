package com.cynthiar.dancingday.recentactivity.graph;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

/**
 * Created by CynthiaR on 4/5/2018.
 */

public class SixtyDaysGraphInformation implements GraphInformation {
    @Override
    public int numberOfDays() {
        return 60;
    }

    @Override
    public int dayInterval() {
        return 7;
    }

    @Override
    public int minY() {
        return 0;
    }

    @Override
    public int maxY() {
        return 480;
    }

    @Override
    public int numXs() {
        return 5;
    }

    @Override
    public int numYs() {
        return 5;
    }

    @Override
    public Series getSeries(DataPoint[] dataPoints) {
        return new LineGraphSeries(dataPoints);
    }

    @Override
    public LabelFormatter getLabelFormatter() {
        return new GraphSeriesLabelFormatter(RecentActivityGraphActivity.dateTimeFormatter2);
    }
}
