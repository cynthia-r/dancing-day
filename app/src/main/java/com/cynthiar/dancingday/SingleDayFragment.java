package com.cynthiar.dancingday;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of single day items.
 * <p/>
 * interface.
 */
public class SingleDayFragment extends Fragment {
    public static final String TAG = "SingleDayFragment";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_NUMBER = "number";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    //private DataCache<List<DummyItem>> mDataCache;
    //private OnListFragmentIn
    // teractionListener mListener;
    private List<DummyItem> mItemList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SingleDayFragment() {
    }


    public static SingleDayFragment newInstance(int position/*, DataCache<List<DummyItem>> dataCache*/, List<DummyItem> dummyItemList) {
        SingleDayFragment fragment = new SingleDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, position);
        fragment.setArguments(args);
        //fragment.setDataCache(dataCache);
        fragment.setData(dummyItemList);
        return fragment;
    }

    /*private void setDataCache(DataCache<List<DummyItem>> dataCache) {
        mDataCache = dataCache;
    }*/
    public void setData(List<DummyItem> data) {
        mItemList = data;
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

        TodayActivity parentActivity = (TodayActivity)getActivity();

        int position;
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            position = getArguments().getInt(ARG_NUMBER);
        }
        else
            position = 0;

        // Retrieve list of items
        List<DummyItem> dummyItemList = parentActivity.getCurrentList();

        // Filter for single day results
        dummyItemList = this.filterList(position, dummyItemList);

        // Sort list
        dummyItemList = DummyUtils.sortItemList(dummyItemList);

        // Setup list adapter
        ListView mListView = (ListView) parentActivity.findViewById(R.id.single_day_list_view);
        SingleDayListViewAdapter adapter = new SingleDayListViewAdapter(dummyItemList, parentActivity);
        mListView.setAdapter(adapter);
    }

    private List<DummyItem> filterList(int position, List<DummyItem> unfilteredList) {
        String dayToKeep = (1 == position) ?
                DummyUtils.getTomorrow()
                : DummyUtils.getCurrentDay();
        List<DummyItem> filteredList = new ArrayList<>();
        for (DummyItem dummyItem:unfilteredList
                ) {
            if (dayToKeep.equals(dummyItem.day))
                filteredList.add(dummyItem);
        }
        return filteredList;
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        return view;
    }*/


    /*@Override
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
