package com.cynthiar.dancingday;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cynthiar.dancingday.dummy.DummyContent.DummyItem;

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


    public static SingleDayFragment newInstance(int columnCount/*, DataCache<List<DummyItem>> dataCache*/, List<DummyItem> dummyItemList) {
        SingleDayFragment fragment = new SingleDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
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

        int itemCount;
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            itemCount = getArguments().getInt(ARG_NUMBER);
        }
        else
            itemCount = 1;

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

        // Set up recycler view
        //RecyclerView recyclerView = (RecyclerView) parentActivity.findViewById(R.id.singledayrecyclerview);
        //SingleDayRecyclerViewAdapter adapter = new SingleDayRecyclerViewAdapter(
        //        dummyItemList, parentActivity.getApplication());
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));*/

        ListView mListView = (ListView) parentActivity.findViewById(R.id.single_day_list_view);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dummyItemList);
        SingleDayListViewAdapter adapter = new SingleDayListViewAdapter(dummyItemList, parentActivity);
        mListView.setAdapter(adapter);

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle();
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
