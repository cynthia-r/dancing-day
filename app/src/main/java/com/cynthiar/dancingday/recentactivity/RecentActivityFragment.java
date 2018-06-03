package com.cynthiar.dancingday.recentactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.TodayActivity;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.database.ClassActivityDao;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by cynthiar on 1/27/2018.
 */
/*
    Activity showing the past activity.
 */
public class RecentActivityFragment extends Fragment implements BaseRecentActivityFragment.RecentActivityDialogListener {
    public static final String TAG = "RecentActivityFragment";
    public static final int POSITION = 4;

    private ClassActivityDao classActivityDao = new ClassActivityDao();
    private RecentActivityListViewAdapter recentActivityListViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recent_activity_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the parent activity
        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Get the list
        this.refreshList(parentActivity);

        // Set title
        parentActivity.setTitle(RecentActivityFragment.POSITION);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        TodayActivity parentActivity = (TodayActivity)getActivity();
        this.refreshList(parentActivity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recent_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_record_activity:
                // User chose the "Record card" item, launch the record card fragment...
                RecordActivityFragment newCardFragment = new RecordActivityFragment();
                newCardFragment.setTargetFragment(this, RecordActivityFragment.REQUEST_CODE);
                newCardFragment.show(getFragmentManager(), RecordActivityFragment.TAG);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /*
        Refreshes the list of cards
     */
    private void refreshList(TodayActivity parentActivity) {
        // Retrieve list of items
        List<ClassActivity> classActivityList = classActivityDao.getRecentActivityList();

        // Get the list view and the empty state view
        ListView listView = (ListView) parentActivity.findViewById(R.id.recent_activity_list_view);
        TextView emptyStateTextView = (TextView) parentActivity.findViewById(R.id.emptyList);
        Button graphButton = (Button) parentActivity.findViewById(R.id.show_graph);

        // Display empty state if no results
        if (0 == classActivityList.size()) {
            listView.setVisibility(GONE);
            graphButton.setVisibility(GONE);
            emptyStateTextView.setText(R.string.activities_empty_state);
            emptyStateTextView.setVisibility(View.VISIBLE);
            return;
        }

        // Otherwise show the list view
        listView.setVisibility(View.VISIBLE);
        graphButton.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);

        // Setup list adapter
        recentActivityListViewAdapter = new RecentActivityListViewAdapter(classActivityList, parentActivity, this);
        listView.setAdapter(recentActivityListViewAdapter);
    }
}
