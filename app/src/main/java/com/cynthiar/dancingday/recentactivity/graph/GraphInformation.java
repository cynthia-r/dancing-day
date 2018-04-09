package com.cynthiar.dancingday.recentactivity.graph;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;

/**
 * Created by CynthiaR on 4/5/2018.
 */

public interface GraphInformation {

    int numberOfDays();
    int dayInterval();
    int minY();
    int maxY();
    int numXs();
    int numYs();

    Series getSeries(DataPoint[] dataPoints);

    LabelFormatter getLabelFormatter();
}

