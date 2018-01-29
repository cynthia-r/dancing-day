package com.cynthiar.dancingday.recentactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.TodayActivity;
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
public class RecentActivityFragment extends Fragment {
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
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the parent activity
        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Retrieve list of items
        List<ClassActivity> classActivityList = classActivityDao.getActivityList();

        // Get the list view and the empty state view
        ListView listView = (ListView) parentActivity.findViewById(R.id.recent_activity_list_view);
        TextView emptyStateTextView = (TextView) parentActivity.findViewById(R.id.emptyList);

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
        recentActivityListViewAdapter = new RecentActivityListViewAdapter(classActivityList, parentActivity);
        listView.setAdapter(recentActivityListViewAdapter);

        // Set title
        parentActivity.setTitle(RecentActivityFragment.POSITION);
    }
}
