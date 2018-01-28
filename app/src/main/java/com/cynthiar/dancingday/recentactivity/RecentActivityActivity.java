package com.cynthiar.dancingday.recentactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.database.ClassActivityDao;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by cynthiar on 1/27/2018.
 */
/*
    Activity showing the past activity.
 */
public class RecentActivityActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ClassActivityDao classActivityDao = new ClassActivityDao();
    private RecentActivityListViewAdapter recentActivityListViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activity);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_recent_activity);
        setSupportActionBar(myToolbar);

        // Setup action bar buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Retrieve list of items
        List<ClassActivity> classActivityList = classActivityDao.getActivityList();

        // Get the list view and the empty state view
        ListView listView = (ListView) this.findViewById(R.id.recent_activity_list_view);
        TextView emptyStateTextView = (TextView) this.findViewById(R.id.emptyList);

        // Display empty state if no results
        if (0 == classActivityList.size()) {
            listView.setVisibility(GONE);
            emptyStateTextView.setText(R.string.activities_empty_state);
            emptyStateTextView.setVisibility(View.VISIBLE);
            return;
        }

        // Otherwise show the list view
        listView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);

        // Setup list adapter
        recentActivityListViewAdapter = new RecentActivityListViewAdapter(classActivityList, this);
        listView.setAdapter(recentActivityListViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
