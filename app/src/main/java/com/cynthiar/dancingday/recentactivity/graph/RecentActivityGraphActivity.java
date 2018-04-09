package com.cynthiar.dancingday.recentactivity.graph;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.SpinnerAdapter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity displaying the graph of recent activity.
 */
public class RecentActivityGraphActivity extends AppCompatActivity {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEE d");
    public static DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("MMM d");
    public static DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("H'h'mm");
    public static DateTimeFormatter localTimeFormatterNoMinutes = DateTimeFormat.forPattern("H'h'");

    private Toolbar myToolbar;
    private Spinner graphSpinner;
    private GraphSpinnerSelectedListener graphSpinnerSelectedListener;

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

        // Set on selected listener
        graphSpinnerSelectedListener = new GraphSpinnerSelectedListener(this);
        graphSpinner.setOnItemSelectedListener(graphSpinnerSelectedListener);

        // Setup the graph fragment
        // Get the recent activity over the past 7 days by default
        GraphFragment graphFragment = GraphFragment.newInstance(new SevenDaysGraphInformation());
        getSupportFragmentManager().beginTransaction().add(R.id.graphContainer, graphFragment, GraphFragment.TAG).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void resetGraph(GraphInformation graphInformation) {
        // Swap the graph fragment with a new one to force reloading the graph
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        GraphFragment graphFragment = GraphFragment.newInstance(graphInformation);
        fragmentTransaction.replace(R.id.graphContainer, graphFragment, GraphFragment.TAG);
        fragmentTransaction.commit();
    }
}
