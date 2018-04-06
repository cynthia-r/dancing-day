package com.cynthiar.dancingday.recentactivity;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.SpinnerAdapter;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Activity displaying the graph of recent activity.
 */
public class RecentActivityGraphActivity extends AppCompatActivity {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEE dd");
    public static DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("H'h'mm");
    public static DateTimeFormatter localTimeFormatterNoMinutes = DateTimeFormat.forPattern("H'h'");

    private Toolbar myToolbar;
    private Spinner graphSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_graph);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.title_graph));
        setSupportActionBar(myToolbar);

        // Setup action bar buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Setup spinner
        graphSpinner = (Spinner)this.findViewById(R.id.graphSpinner);
        List<String> dropDownList = new ArrayList<>();
        for (String graphView:getResources().getStringArray(R.array.graph_array)
                ) {
            dropDownList.add(graphView);
        }
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                this, R.layout.graph_spinner_item, R.layout.graph_spinner_dropdown_item, dropDownList);
        graphSpinner.setAdapter(spinnerAdapter);
        graphSpinner.setOnItemSelectedListener(new GraphSpinnerSelectedListener(this));

        // Setup graph
        GraphView graph = (GraphView) findViewById(R.id.graph);

        // Get the activity over the past 7 days by default
        // Display the graph of activity
        GraphInformation graphInformation = new SevenDaysGraphInformation();
        GraphData graphData = this.getDataPoints(graphInformation.numberOfDays(), graphInformation.dayInterval());
        this.displayGraph(graph, graphData, graphInformation);
    }

    public void resetGraph(GraphInformation graphInformation) {
        // Reset graph
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.clearSecondScale();

        // Display the graph of activity
        GraphData graphData = this.getDataPoints(graphInformation.numberOfDays(), graphInformation.dayInterval());
        this.displayGraph(graph, graphData, graphInformation);
    }

    private GraphData getDataPoints(int numberOfDays, int dayCumul) {
        // Get the activity over the past X days
        HashMap<Date, Pair<Integer, Integer>> minutesPerIntervalMap = this.getPastDays(numberOfDays, dayCumul);
        if (minutesPerIntervalMap.isEmpty()) {
            return new GraphData(new Date(), new Date(), new DataPoint[0]);
        }

        // Convert the map information to data points
        DataPoint[] dataPoints = new DataPoint[minutesPerIntervalMap.keySet().size()];
        Date minDate = new Date(Long.MAX_VALUE);
        Date maxDate = new Date(Long.MIN_VALUE);
        for (Date day:minutesPerIntervalMap.keySet()
                ) {
            if (day.before(minDate))
                minDate = day;
            else if (day.after(maxDate))
                maxDate = day;
            Pair<Integer, Integer> pair = minutesPerIntervalMap.get(day);
            dataPoints[pair.first / dayCumul] = new DataPoint(day, pair.second);
        }
        return new GraphData(minDate, maxDate, dataPoints);
    }

    private class GraphData {
        public Date minDate;
        public Date maxDate;
        public DataPoint[] dataPoints;
        public GraphData(Date minDate, Date maxDate, DataPoint[] dataPoints) {
            this.minDate = minDate;
            this.maxDate = maxDate;
            this.dataPoints = dataPoints;
        }
    }

    /*
    7 days:
    minY = 0
    max Y = 180.0 // 0h to 3h
    numHorizontalLabels = 5 // show 5 dates
    numVerticalLabels = 7 // show every half-hour
    */

    private void displayGraph(GraphView graph, GraphData graphData, GraphInformation graphInformation) {
        // Create a graph series
        DataPoint[] dataPoints = graphData.dataPoints;
        Series series = graphInformation.getSeries(dataPoints);
        graph.addSeries(series);

        // Set label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // Format x values as dates
                    DateTime dateTime = new DateTime((long) value);
                    return dateTime.toString(RecentActivityGraphActivity.dateTimeFormatter);
                } else {
                    // Format y values as amounts of time
                    int hours = (int)value / 60;
                    int minutes = (int)value % 60;
                    LocalTime localTime = new LocalTime(hours, minutes);
                    return minutes > 0 ? localTime.toString(RecentActivityGraphActivity.localTimeFormatter)
                            : localTime.toString(RecentActivityGraphActivity.localTimeFormatterNoMinutes);
                }
            }
        });

        // Set viewport to last X days
        graph.getViewport().setMinX(graphData.minDate.getTime());
        graph.getViewport().setMaxX(graphData.maxDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // Set viewport to 0h to 3h
        graph.getViewport().setMinY(graphInformation.minY());
        graph.getViewport().setMaxY(graphInformation.maxY());
        graph.getViewport().setYAxisBoundsManual(true);

        // Show 5 dates
        graph.getGridLabelRenderer().setNumHorizontalLabels(graphInformation.numXs());

        // Show every half hour range, and set width to fully display hours and minutes
        graph.getGridLabelRenderer().setLabelVerticalWidth(150);
        graph.getGridLabelRenderer().setNumVerticalLabels(graphInformation.numYs());

        // Disable the human rounding to avoid pre-calculating labels
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private HashMap<Date, Pair<Integer, Integer>> getPastDays(int numberOfDays, int dayInterval) {
        // Retrieve the activities over the past specified days
        ClassActivityDao classActivityDao = new ClassActivityDao();
        Iterator<ClassActivity> classActivityIterator = classActivityDao.getActivityHistory(numberOfDays);

        // Group the activities per day
        HashMap<Date, Pair<Integer, Integer>> minutesPerDayMap = new HashMap();

        // Retrieve the current day
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -numberOfDays + 1); // add 1 to include current day

        // Go through each day
        int dayIndex = 0;
        boolean goToNextActivity = classActivityIterator.hasNext();
        Date currentIntervalBaseDate = calendar.getTime();
        while (dayIndex < numberOfDays) {
            // Get current day string
            Date currentDay = calendar.getTime();
            int currentActivityAmount = 0;

            // Check if there is any activity left
            if (goToNextActivity) {
                // Retrieve the current activity
                ClassActivity classActivity = classActivityIterator.next();

                // Retrieve the day this activity was
                DateTime activityDate = classActivity.getDate();

                // Check if this activity was on the current day
                if (this.compareDates(calendar, activityDate)) {
                    currentActivityAmount = classActivity.getDanceClass().danceClassTime.getDurationInMinutes();

                    // Go to next activity if any (might be on the same day)
                    goToNextActivity = classActivityIterator.hasNext();
                }
            }

            if (dayIndex % dayInterval == 0) {
                currentIntervalBaseDate = currentDay;
            }

            // Initialize the amount for this day interval
            if (!minutesPerDayMap.containsKey(currentIntervalBaseDate)) {
                minutesPerDayMap.put(currentIntervalBaseDate, new Pair(dayIndex, 0));
            }

            // Add the amount of minutes for that day interval
            if (currentActivityAmount > 0) {
                Pair<Integer, Integer> currentPair = minutesPerDayMap.get(currentIntervalBaseDate);
                minutesPerDayMap.put(currentIntervalBaseDate, new Pair(dayIndex, currentPair.second + currentActivityAmount));

                // Continue to see if there were other activities on that same day
                continue;
            }

            // Move to the next day in the calendar
            calendar.add(Calendar.DATE, 1);
            dayIndex ++;
        }

        // Return the map
        return minutesPerDayMap;
    }

    private boolean compareDates(Calendar calendar, DateTime dateTime) {
        int calendarDay = (calendar.get(Calendar.DAY_OF_MONTH));
        int jodaDay = dateTime.getDayOfMonth();
        int calendarMonth = calendar.get(Calendar.MONTH) + 1; // calendar month is zero-based
        int jodaMonth = dateTime.getMonthOfYear();
        int calendarYear =calendar.get(Calendar.YEAR);
        int jodaYear = dateTime.getYear();

        return calendarDay == jodaDay && calendarMonth == jodaMonth && calendarYear == jodaYear;
    }
}
