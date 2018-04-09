package com.cynthiar.dancingday.recentactivity.graph;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Fragment for displaying the graph of recent activity.
 */
public class GraphFragment extends Fragment {
    public static String TAG = "GraphFragment";
    private GraphInformation graphInformation;

    public GraphFragment() {
        graphInformation = new SevenDaysGraphInformation();
    }

    public static GraphFragment newInstance(GraphInformation graphInformation) {
        GraphFragment graphFragment = new GraphFragment();
        graphFragment.setGraphInformation(graphInformation);
        return graphFragment;
    }

    private void setGraphInformation(GraphInformation graphInformation) {
        this.graphInformation = graphInformation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup graph
        RecentActivityGraphActivity parentActivity = (RecentActivityGraphActivity)getActivity();
        GraphView graph = (GraphView) parentActivity.findViewById(R.id.graph);

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

    private void displayGraph(GraphView graph, GraphData graphData, GraphInformation graphInformation) {
        // Create a graph series
        DataPoint[] dataPoints = graphData.dataPoints;
        Series series = graphInformation.getSeries(dataPoints);
        graph.addSeries(series);

        // Set label formatter
        graph.getGridLabelRenderer().setLabelFormatter(graphInformation.getLabelFormatter());

        // Set viewport to last X days
        graph.getViewport().setMinX(graphData.minDate.getTime());
        graph.getViewport().setMaxX(graphData.maxDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // Set viewport from min date to max date
        graph.getViewport().setMinY(graphInformation.minY());
        graph.getViewport().setMaxY(graphInformation.maxY());
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().calcCompleteRange();

        // Show some dates
        graph.getGridLabelRenderer().setNumHorizontalLabels(graphInformation.numXs());

        // Show every range, and set width to fully display hours and minutes
        graph.getGridLabelRenderer().setLabelVerticalWidth(150);
        graph.getGridLabelRenderer().setNumVerticalLabels(graphInformation.numYs());

        // Disable the human rounding to avoid pre-calculating labels
        graph.getGridLabelRenderer().setHumanRounding(false);
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
}
