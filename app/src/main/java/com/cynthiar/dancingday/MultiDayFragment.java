package com.cynthiar.dancingday;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
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

        // Set the adapter
        //setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, DummyContent.ITEMS));

        // Retrieve list of items
        List<DummyItem> dummyItemList = new ArrayList<>();
        for (int i=0; i < DummyContent.ITEMS3.length; i++)
            dummyItemList.add(DummyContent.ITEMS3[i]);

        // Set up list view
        Activity parentActivity = getActivity();
        ListView mListView = (ListView) parentActivity.findViewById(R.id.multi_day_list_view);
        MultiDayListViewAdapter adapter = new MultiDayListViewAdapter(dummyItemList, parentActivity);
        mListView.setAdapter(adapter);
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
