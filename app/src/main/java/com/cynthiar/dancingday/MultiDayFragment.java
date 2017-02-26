package com.cynthiar.dancingday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.cynthiar.dancingday.dummy.comparer.DayComparer;
import com.cynthiar.dancingday.dummy.comparer.SingleDayDummyItemComparer;
import com.cynthiar.dancingday.dummy.comparer.StringComparer;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyContent.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

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
    private int mColumnCount = 1;

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

        // Only keep items starting fro, tomorrow
        dummyItemList = this.filterList(dummyItemList);

        DanceClassPropertySelector danceClassPropertySelector = new DayPropertySelector(); // TODO select in UI
        HashMap<String, List<DummyItem>> dummyItemMap = DummyUtils.GroupBy(danceClassPropertySelector, dummyItemList);
        List<String> groupList = sortAndRotateGroups(dummyItemMap, danceClassPropertySelector);

        // Set up list view
        ExpandableListView mListView = (ExpandableListView) parentActivity.findViewById(R.id.multi_day_list_view);
        MultiDayListViewAdapter adapter = new MultiDayListViewAdapter(groupList, dummyItemMap, parentActivity);
        mListView.setAdapter(adapter);

        // Expand groups the first time
        for (int i=0; i < groupList.size(); i++)
        {
            mListView.expandGroup(i);
        }
    }

    private List<DummyContent.DummyItem> filterList(List<DummyContent.DummyItem> unfilteredList) {
        Calendar calendar = Calendar.getInstance();
        int dayToFilter = calendar.get(Calendar.DAY_OF_WEEK);

        String dayToExclude = DummyUtils.getCurrentDay(dayToFilter);
        List<DummyContent.DummyItem> filteredList = new ArrayList<>();
        for (DummyContent.DummyItem dummyItem:unfilteredList
                ) {
            if (!dayToExclude.equals(dummyItem.day))
                filteredList.add(dummyItem);
        }
        return filteredList;
    }

    private List<String> sortAndRotateGroups(HashMap<String, List<DummyItem>> dummyItemMap, DanceClassPropertySelector propertySelector) {
        List<String> groupList = new ArrayList<>(dummyItemMap.keySet());

        // Sort the list
        String[] unsortedGroups = new String[groupList.size()];
        String[] sortedGroups = dummyItemMap.keySet().toArray(unsortedGroups);
        new DummyUtils<String>(sortedGroups, propertySelector.getComparer()).quickSort();

        // Rotate days (tomorrow should be first)
        if (propertySelector instanceof DayPropertySelector) {
            String currentDay = DummyUtils.getCurrentDay();
            int k=0;
            while (k < sortedGroups.length && !sortedGroups[k].equals(currentDay)) { // Find current day position
                k++;
            }
            if (k == sortedGroups.length) {
                DummyUtils.toast(getActivity().getApplicationContext());
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
