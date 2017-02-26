package com.cynthiar.dancingday;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyContent.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

import java.util.ArrayList;
import java.util.Calendar;
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
        //parentActivity.startDownload();

        int position;
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            position = getArguments().getInt(ARG_NUMBER);
        }
        else
            position = 0;

        // Set the adapter
        //setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, DummyContent.ITEMS));

        // Retrieve list of items
        List<DummyItem> dummyItemList = new ArrayList<>();
        /*DummyItem[] inputArray = itemCount == 1 ? DummyContent.ITEMS : DummyContent.ITEMS2;
        for (int i=0; i < inputArray.length; i++)
            dummyItemList.add(inputArray[i]);*/

        //dummyItemList = mDataCache.Load(TodayActivity.TODAY_KEY);
        /*for (int i=0; i < mItemList.size(); i++)
            dummyItemList.add(mItemList.get(i));*/
        dummyItemList = parentActivity.getCurrentList();


        // Filter for single day results
        dummyItemList = this.filterList(position, dummyItemList);


        ListView mListView = (ListView) parentActivity.findViewById(R.id.single_day_list_view);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dummyItemList);
        SingleDayListViewAdapter adapter = new SingleDayListViewAdapter(dummyItemList, parentActivity);
        mListView.setAdapter(adapter);
        //mListView.setItemsCanFocus(true);
        //mListView.setOnItemClickListener(mMessageClickedHandler);
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Object item = adapter.getItemAtPosition(position);
                try {
                    String url = "waze://?ll=47.680279, -122.191947&z=10&navigate=yes";
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    startActivity( intent );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //}

            //@Override
            //public void onItemClick(AdapterView<?>adapter, View v, int position){


            }
        });*/
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle();
    }

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
            String a = "b";
            String b = a.concat("b");
        }
    };


    private List<DummyContent.DummyItem> filterList(int position, List<DummyContent.DummyItem> unfilteredList) {
        String dayToKeep = (1 == position) ?
                DummyUtils.getTomorrow()
                : DummyUtils.getCurrentDay();
        List<DummyContent.DummyItem> filteredList = new ArrayList<>();
        for (DummyContent.DummyItem dummyItem:unfilteredList
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
