package com.cynthiar.dancingday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.filter.MultiDaySpinner;
import com.cynthiar.dancingday.filter.MultiDaySpinnerAdapter;
import com.cynthiar.dancingday.filter.SpinnerItemsSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class MultiDayFragment extends Fragment {

    // TODO: Customize parameters
    private Spinner mSchoolSpinner;
    private Spinner mLevelSpinner;
    private HashMap<String, List<DummyItem>> mAllItemMap;

    public static final String TAG = "MultiDayFragment";
    public static final String ALL_KEY = "All";
    public static final String SCHOOL_SPINNER_PREFIX = "SCHOOL";
    public static final String LEVEL_SPINNER_PREFIX = "LEVEL";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MultiDayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multi_day_fragment_item_list, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Retrieve list of items
        List<DummyItem> dummyItemList = parentActivity.getCurrentList();

        // Display empty state if no results
        if (0 == dummyItemList.size()) {
            parentActivity.displayEmptyList();
            return;
        }

        // Group by the selector
        DanceClassPropertySelector danceClassPropertySelector = new DayPropertySelector(); // TODO select in UI
        HashMap<String, List<DummyItem>> dummyItemMap = DummyUtils.GroupBy(danceClassPropertySelector, dummyItemList);
        sortItemMap(dummyItemMap);
        mAllItemMap = dummyItemMap;
        List<String> groupList = sortAndRotateGroups(dummyItemMap, danceClassPropertySelector);

        // Set up list view
        ExpandableListView mListView = (ExpandableListView) parentActivity.findViewById(R.id.multi_day_list_view);
        MultiDayListViewAdapter adapter = new MultiDayListViewAdapter(groupList, dummyItemMap, mAllItemMap, parentActivity, danceClassPropertySelector);
        mListView.setAdapter(adapter);

        // Expand groups the first time
        for (int i=0; i < groupList.size(); i++) {
            mListView.expandGroup(i);
        }

        // Setup spinners
        // School spinner
        mSchoolSpinner = (Spinner)parentActivity.findViewById(R.id.schoolSpinner);
        List<String> schoolList = Extractors.getInstance(parentActivity).getSchoolList();
        this.setupSpinner(parentActivity, mSchoolSpinner, schoolList);

        // Level spinner
        mLevelSpinner = (Spinner)parentActivity.findViewById(R.id.levelSpinner);
        List<String> levelList = Extractors.getInstance(parentActivity).getLevelList();
        this.setupSpinner(parentActivity, mLevelSpinner, levelList);

        // Setup spinners listener
        MultiDaySpinner[] spinners = {
             new MultiDaySpinner(mSchoolSpinner, MultiDayFragment.SCHOOL_SPINNER_PREFIX),
             new MultiDaySpinner(mLevelSpinner, MultiDayFragment.LEVEL_SPINNER_PREFIX)
        };
        SpinnerItemsSelectedListener spinnerItemsSelectedListener =
                new SpinnerItemsSelectedListener(parentActivity, spinners, adapter);
        mSchoolSpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
        mLevelSpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
    }

    private List<String> sortAndRotateGroups(HashMap<String, List<DummyItem>> dummyItemMap, DanceClassPropertySelector propertySelector) {
        List<String> groupList = new ArrayList<>(dummyItemMap.keySet());

        // Sort the list
        String[] unsortedGroups = new String[groupList.size()];
        String[] sortedGroups = dummyItemMap.keySet().toArray(unsortedGroups);
        new DummyUtils<>(sortedGroups, propertySelector.getComparer()).quickSort();

        // Rotate days (tomorrow should be first)
        if (propertySelector instanceof DayPropertySelector) {
            String tomorrow = DummyUtils.getTomorrow();
            // Find the position of tomorrow
            int k=0;
            while (k < sortedGroups.length && !sortedGroups[k].equals(tomorrow)) {
                k++;
            }
            if (k == sortedGroups.length) {
                DummyUtils.toast(getActivity().getApplicationContext(), "Tomorrow not found");
            }

            // Copy to the list back again
            groupList = new ArrayList<>();
            for (int j=k; j < sortedGroups.length; j++
                    ) {
                String group = sortedGroups[j];
                groupList.add(group);
            }
            for (int j=0; j < k; j++
                    ) {
                String group = sortedGroups[j];
                groupList.add(group);
            }
        }
        else {
            // Just copy the list back again
            groupList = new ArrayList<>();
            for (int j=0; j < sortedGroups.length; j++
                    ) {
                String group = sortedGroups[j];
                groupList.add(group);
            }
        }

        // Return the group list
        return groupList;
    }

    private void sortItemMap(HashMap<String, List<DummyItem>> dummyItemMap) {
        for (String key:dummyItemMap.keySet()
             ) {
            List<DummyItem> sortedItemList = DummyUtils.sortItemList(dummyItemMap.get(key));
            dummyItemMap.put(key, sortedItemList);
        }
    }

    private MultiDaySpinnerAdapter setupSpinner(Context context, Spinner spinner, List<String> spinnerItemList) {
        MultiDaySpinnerAdapter spinnerAdapter = new MultiDaySpinnerAdapter(
                context, android.R.layout.simple_spinner_dropdown_item, spinnerItemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        return spinnerAdapter;
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }*/
}
