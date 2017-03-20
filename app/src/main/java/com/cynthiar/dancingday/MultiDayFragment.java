package com.cynthiar.dancingday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.filter.MultiDaySpinner;
import com.cynthiar.dancingday.filter.MultiDaySpinnerAdapter;
import com.cynthiar.dancingday.filter.SpinnerItemsSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

/**
 * A fragment representing a list of items across multiple days.
 * <p/>
 */
public class MultiDayFragment extends Fragment {

    private Spinner mViewBySpinner;
    private Spinner mSchoolSpinner;
    private Spinner mLevelSpinner;
    private HashMap<String, List<DummyItem>> mAllItemMap;
    private DanceClassPropertySelector mCurrentPropertySelector;

    public static final String TAG = "MultiDayFragment";
    public static final String ALL_KEY = "All";
    public static final String SCHOOL_SPINNER_PREFIX = "SCHOOL";
    public static final String LEVEL_SPINNER_PREFIX = "LEVEL";
    public static final String VIEW_BY_SPINNER_PREFIX = "VIEW_BY";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MultiDayFragment() {
    }

    public DanceClassPropertySelector getCurrentPropertySelector() {
        return mCurrentPropertySelector;
    }

    public void setCurrentPropertySelector(DanceClassPropertySelector propertySelector) {
        mCurrentPropertySelector = propertySelector;
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

        // Retrieve list of items
        TodayActivity parentActivity = (TodayActivity)getActivity();
        List<DummyItem> dummyItemList = parentActivity.getCurrentList();

        // Setup spinners
        // View by spinner
        mViewBySpinner = (Spinner)parentActivity.findViewById(R.id.viewBySpinner);
        List<String> viewByList = new ArrayList<>();
        for (String viewBy:getResources().getStringArray(R.array.view_by_array)
             ) {
            viewByList.add(viewBy);
        }
        this.setupSpinner(parentActivity, mViewBySpinner, viewByList);

        // School spinner
        mSchoolSpinner = (Spinner)parentActivity.findViewById(R.id.schoolSpinner);
        List<String> schoolList = Extractors.getInstance(parentActivity).getSchoolList();
        this.setupSpinner(parentActivity, mSchoolSpinner, schoolList);

        // Level spinner
        mLevelSpinner = (Spinner)parentActivity.findViewById(R.id.levelSpinner);
        List<String> levelList = Extractors.getInstance(parentActivity).getLevelList();
        this.setupSpinner(parentActivity, mLevelSpinner, levelList);

        // Get the list view and the empty state view
        ExpandableListView expandableListView = (ExpandableListView) parentActivity.findViewById(R.id.multi_day_list_view);
        TextView emptyStateTextView = (TextView) parentActivity.findViewById(R.id.emptyList);

        // Display empty state if no results
        if (0 == dummyItemList.size() && parentActivity.areAllListsLoaded()) {
            expandableListView.setVisibility(GONE);
            emptyStateTextView.setVisibility(View.VISIBLE);
        }
        else {
            // Otherwise show the expandable list view
            expandableListView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);

            // Mark favorite items
            //parentActivity.markFavorites(dummyItemList);

            // Group by the selector - default: by day
            DanceClassPropertySelector danceClassPropertySelector = new DayPropertySelector();
            HashMap<String, List<DummyItem>> dummyItemMap = DummyUtils.GroupBy(danceClassPropertySelector, dummyItemList);
            DummyUtils.sortItemMap(dummyItemMap);
            mAllItemMap = dummyItemMap;
            List<String> groupList = DummyUtils.sortAndRotateGroups(getActivity(), dummyItemMap, danceClassPropertySelector);

            // Set up list view
            MultiDayListViewAdapter adapter = new MultiDayListViewAdapter(groupList, dummyItemMap, mAllItemMap, parentActivity);
            expandableListView.setAdapter(adapter);

            // Expand groups the first time
            int groupCount = groupList.size();
            for (int i=0; i < groupCount; i++) {
                expandableListView.expandGroup(i);
            }

            // Setup spinners listener
            MultiDaySpinner[] spinners = {
                    new MultiDaySpinner(mSchoolSpinner, MultiDayFragment.SCHOOL_SPINNER_PREFIX),
                    new MultiDaySpinner(mLevelSpinner, MultiDayFragment.LEVEL_SPINNER_PREFIX),
                    new MultiDaySpinner(mViewBySpinner, MultiDayFragment.VIEW_BY_SPINNER_PREFIX)
            };
            SpinnerItemsSelectedListener spinnerItemsSelectedListener =
                    new SpinnerItemsSelectedListener(spinners, adapter);
            mSchoolSpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
            mLevelSpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
            mViewBySpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
        }

        // Set title
        parentActivity.setTitle(2);
    }

    /**
     * Setups a spinner layout and adapter.
     */
    private void setupSpinner(Context context, Spinner spinner, List<String> spinnerItemList) {
        MultiDaySpinnerAdapter spinnerAdapter = new MultiDaySpinnerAdapter(
                context, R.layout.spinner_item, spinnerItemList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
