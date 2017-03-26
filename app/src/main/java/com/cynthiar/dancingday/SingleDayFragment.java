package com.cynthiar.dancingday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.View.GONE;

/**
 * A fragment representing a list of single day items.
 */
public class SingleDayFragment extends Fragment {
    public static final String TAG = "SingleDayFragment";
    public static final String ARG_POSITION = "position";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SingleDayFragment() {
    }

    public static SingleDayFragment newInstance(int position) {
        SingleDayFragment fragment = new SingleDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.single_day_fragment_item_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the parent activity
        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Retrieve the fragment position
        int position;
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
        else
            position = 0;

        // Retrieve list of items
        List<DummyItem> dummyItemList = parentActivity.getCurrentList();

        // Filter for single day results
        dummyItemList = this.filterList(position, dummyItemList);

        // Get the list view and the empty state view
        ListView listView = (ListView) parentActivity.findViewById(R.id.single_day_list_view);
        TextView emptyStateTextView = (TextView) parentActivity.findViewById(R.id.emptyList);

        // Display empty state if no results
        if (0 == dummyItemList.size() && parentActivity.areAllListsLoaded()) {
            listView.setVisibility(GONE);
            emptyStateTextView.setVisibility(View.VISIBLE);

            // Set title
            parentActivity.setTitle(position);
            return;
        }

        // Otherwise show the list view
        listView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);

        // Sort list
        dummyItemList = DummyUtils.sortItemList(dummyItemList);

        // Setup list adapter
        SingleDayListViewAdapter adapter = new SingleDayListViewAdapter(dummyItemList, parentActivity);
        listView.setAdapter(adapter);

        // Set title
        parentActivity.setTitle(position);
    }

    /**
     * Filters the list of items to the selected day.
     */
    private List<DummyItem> filterList(int position, List<DummyItem> unfilteredList) {
        // Get the day to keep
        String dayToKeep = (1 == position) ? DummyUtils.getTomorrow() : DummyUtils.getCurrentDay();

        // Filter the list to the selected day
        List<DummyItem> filteredList = new ArrayList<>();
        LocalTime currentTime = new LocalTime();
        for (DummyItem dummyItem:unfilteredList
                ) {
            if (dayToKeep.equals(dummyItem.day) // only keep the current day (today/tomorrow)
                    // and if the day is today, only keep the classes that haven't started yet (with a 10 min margin)
                    && ((1 == position || (dummyItem.danceClassTime.startTime.plusMinutes(10).isAfter(currentTime)))))
                filteredList.add(dummyItem);
        }
        return filteredList;
    }
}
