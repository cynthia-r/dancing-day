package com.cynthiar.dancingday;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DummyContent;

/**
 * Created by CynthiaR on 2/5/2017.
 */

public class SingleDayViewHolder extends RecyclerView.ViewHolder {
    //public final View mView;
    public final TextView mTimeView;
    public final TextView mSchoolView;
    public DummyContent.DummyItem mItem;

    public SingleDayViewHolder(View view) {
        super(view);
        //mView = view;
        mTimeView = (TextView) view.findViewById(R.id.time);
        mSchoolView = (TextView) view.findViewById(R.id.school);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mSchoolView.getText() + "'";
    }
}
