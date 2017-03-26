package com.cynthiar.dancingday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.filter.MultiDayFilterState;
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
    private ImageButton mStarButton;
    private HashMap<String, List<DummyItem>> mAllItemMap;
    private DanceClassPropertySelector mCurrentPropertySelector;

    private boolean mKeepFavorites = false;

    public static final String TAG = "MultiDayFragment";
    public static final String ALL_KEY = "All";
    public static final String SCHOOL_SPINNER_PREFIX = "SCHOOL";
    public static final String LEVEL_SPINNER_PREFIX = "LEVEL";
    public static final String VIEW_BY_SPINNER_PREFIX = "VIEW_BY";
    public static final String FAVORITE_BUTTON_PREFIX = "STARRED_ONLY";

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

        // Star button
        mStarButton = (ImageButton)parentActivity.findViewById(R.id.favoriteFilter);
        mStarButton.setPressed(mKeepFavorites);

        // Initialize filter state
        MultiDaySpinner[] spinners = {
                new MultiDaySpinner(mSchoolSpinner, MultiDayFragment.SCHOOL_SPINNER_PREFIX),
                new MultiDaySpinner(mLevelSpinner, MultiDayFragment.LEVEL_SPINNER_PREFIX),
                new MultiDaySpinner(mViewBySpinner, MultiDayFragment.VIEW_BY_SPINNER_PREFIX)
        };
        MultiDayFilterState filterState = new MultiDayFilterState(spinners, mStarButton);

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
            SpinnerItemsSelectedListener spinnerItemsSelectedListener =
                    new SpinnerItemsSelectedListener(filterState, adapter);
            mSchoolSpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
            mLevelSpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);
            mViewBySpinner.setOnItemSelectedListener(spinnerItemsSelectedListener);

            // Setup favorite filter listener
            mStarButton.setOnTouchListener(new FavoriteFilterTouchListener(filterState, adapter));
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

    /**
     * Touch listener for the favorite filter button.
     */
    private class FavoriteFilterTouchListener implements View.OnTouchListener {
        private MultiDayFilterState mFilterState;
        private MultiDayListViewAdapter mMultiDayListViewAdapter;

        public FavoriteFilterTouchListener(MultiDayFilterState filterState, MultiDayListViewAdapter multiDayListViewAdapter) {
            mMultiDayListViewAdapter = multiDayListViewAdapter;
            mFilterState = filterState;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Show interest in events resulting from ACTION_DOWN
            if(event.getAction()== MotionEvent.ACTION_DOWN) return true;

            // Don't handle event unless its ACTION_UP so "doSomething()" only runs once.
            if(event.getAction() != MotionEvent.ACTION_UP) return false;

            // Change the state of the favorite button
            mKeepFavorites = !mKeepFavorites;
            v.setPressed(mKeepFavorites);

            // Filter the results
            String filterString = mFilterState.getFilterString();
            mMultiDayListViewAdapter.getFilter().filter(filterString);
            return true;
        }
    }
}
