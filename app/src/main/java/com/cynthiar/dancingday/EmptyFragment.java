package com.cynthiar.dancingday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class EmptyFragment extends Fragment {

    public static final String TAG = "EmptyFragment";
    private static final String POSITION = "Position";
    private int mPosition;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EmptyFragment() {}

    public static EmptyFragment newInstance(int position) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_list, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Set title
        TodayActivity parentActivity = (TodayActivity) getActivity();
        parentActivity.setTitle(mPosition);
    }
}
