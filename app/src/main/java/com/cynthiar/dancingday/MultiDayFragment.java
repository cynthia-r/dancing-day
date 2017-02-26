package com.cynthiar.dancingday;

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
import com.cynthiar.dancingday.dummy.DummyContent.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class MultiDayFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Spinner mSchoolSpinner;
    private HashMap<String, List<DummyItem>> mAllItemMap;

    public static final String ALL_KEY = "All";

//    private OnListFragmentInteractionListener mListener;

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

        // Set the adapter
        //setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, DummyContent.ITEMS2));
    }

    @Override
    public void onStart() {
        super.onStart();

        int itemCount;
        if (getArguments() != null) {
            //mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        else
            itemCount = 1;

        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Retrieve list of items
        List<DummyItem> dummyItemList = parentActivity.getCurrentList();

        // Group by the selector
        DanceClassPropertySelector danceClassPropertySelector = new DayPropertySelector(); // TODO select in UI
        HashMap<String, List<DummyItem>> dummyItemMap = DummyUtils.GroupBy(danceClassPropertySelector, dummyItemList);
        mAllItemMap = dummyItemMap;
        List<String> groupList = sortAndRotateGroups(dummyItemMap, danceClassPropertySelector);

        // Set up list view
        ExpandableListView mListView = (ExpandableListView) parentActivity.findViewById(R.id.multi_day_list_view);
        MultiDayListViewAdapter adapter = new MultiDayListViewAdapter(groupList, dummyItemMap, mAllItemMap, parentActivity);
        mListView.setAdapter(adapter);

        // Expand groups the first time
        for (int i=0; i < groupList.size(); i++)
        {
            mListView.expandGroup(i);
        }

        // Setup spinners
        mSchoolSpinner = (Spinner)parentActivity.findViewById(R.id.schoolSpinner);
        List<String> schoolList = Extractors.getSchoolList();
        SchoolSpinnerAdapter schoolSpinnerAdapter = new SchoolSpinnerAdapter(
                parentActivity, android.R.layout.simple_spinner_dropdown_item, schoolList);
        schoolSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSchoolSpinner.setAdapter(schoolSpinnerAdapter);
        mSchoolSpinner.setOnItemSelectedListener(
                new SpinnerItemsSelectedListener(parentActivity, schoolSpinnerAdapter, adapter));
    }

    private List<String> sortAndRotateGroups(HashMap<String, List<DummyItem>> dummyItemMap, DanceClassPropertySelector propertySelector) {
        List<String> groupList = new ArrayList<>(dummyItemMap.keySet());

        // Sort the list
        String[] unsortedGroups = new String[groupList.size()];
        String[] sortedGroups = dummyItemMap.keySet().toArray(unsortedGroups);
        new DummyUtils<String>(sortedGroups, propertySelector.getComparer()).quickSort();

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
