package com.cynthiar.dancingday.recentactivity;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RecentActivityGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_graph);

        // Get the activity over the past 7 days - todo
        int numberOfDays = 20;
        HashMap<String, Pair<Integer, Integer>> minutesPerDayMap = this.getPastDays(numberOfDays);

        // Display the graph of activity
        if (!minutesPerDayMap.isEmpty()) {
            DataPoint[] dataPoints = new DataPoint[numberOfDays];
            for (String day:minutesPerDayMap.keySet()
                    ) {
                Pair<Integer, Integer> pair = minutesPerDayMap.get(day);
                dataPoints[pair.first] = new DataPoint(pair.first, pair.second);
            }
            GraphView graph = (GraphView) findViewById(R.id.graph);
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
            graph.addSeries(series);

            // Set viewport to last 7 days
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(numberOfDays);
            graph.getViewport().setMinY(0.0);
            graph.getViewport().setMaxY(180.0);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
        }
    }

    private HashMap<String, Pair<Integer, Integer>> getPastDays(int numberOfDays) {
        // Retrieve the activities over the past specified days
        ClassActivityDao classActivityDao = new ClassActivityDao();
        Iterator<ClassActivity> classActivityIterator = classActivityDao.getActivityHistory(numberOfDays);

        // Group the activities per day
        HashMap<String, Pair<Integer, Integer>> minutesPerDayMap = new HashMap();

        // Retrieve the current day
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -numberOfDays);

        // Go through each day
        int dayIndex = 0;
        boolean goToNextActivity = classActivityIterator.hasNext();
        while (dayIndex < numberOfDays) {
            // Get current day string
            String currentDayString = this.getDayString(calendar);
            int currentActivityAmount = 0;

            // Check if there is any activity left
            if (goToNextActivity) {
                // Retrieve the current activity
                ClassActivity classActivity = classActivityIterator.next();

                // Retrieve the day this activity was
                DateTime activityDate = classActivity.getDate();
                String dayString = this.getDayString(activityDate);

                // Check if this activity was on the current day
                if (dayString.equals(currentDayString)) {
                    currentActivityAmount = classActivity.getDanceClass().danceClassTime.getDurationInMinutes();

                    // Go to next activity if any (might be on the same day)
                    goToNextActivity = classActivityIterator.hasNext();
                }
            }

            // Initialize the amount for this day
            if (!minutesPerDayMap.containsKey(currentDayString)) {
                minutesPerDayMap.put(currentDayString, new Pair(dayIndex, 0));
            }

            // Add the amount of minutes for that day
            if (currentActivityAmount > 0) {
                Pair<Integer, Integer> currentPair = minutesPerDayMap.get(currentDayString);
                minutesPerDayMap.put(currentDayString, new Pair(dayIndex, currentPair.second + currentActivityAmount));

                // Continue to see if there were other activities on that same day
                continue;
            }

            // Move to the next day in the calendar
            calendar.add(Calendar.DATE, 1);
            dayIndex++;
        }

        // Return the map
        return minutesPerDayMap;
    }

    private String getDayString(Calendar calendar) {
        // Build day string: Day_Name Day_Of_Month
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dayName = DummyUtils.getDayName(calendar.get(Calendar.DAY_OF_WEEK));
        return dayName + " " + dayOfMonth;
    }
    private String getDayString(DateTime dateTime) {
        // Build day string: Day_Name Day_Of_Month
        int dayOfMonth = dateTime.getDayOfMonth();
        int calendarDayOfWeek = (dateTime.getDayOfWeek() + 1);
        if (calendarDayOfWeek > 7)
            calendarDayOfWeek = calendarDayOfWeek - 7; // calendar day starts at 1=Sunday and 7=Saturday
        String dayName = DummyUtils.getDayName(calendarDayOfWeek);
        return dayName + " " + dayOfMonth;
    }
}
