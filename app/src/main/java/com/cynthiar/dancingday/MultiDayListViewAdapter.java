package com.cynthiar.dancingday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.List;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.List;

/**
 * Created by CynthiaR on 2/5/2017.
 */

public class MultiDayListViewAdapter extends BaseAdapter {
    private final List<DummyContent.DummyItem> mValues;
    private Context context;
    private LayoutInflater mInflater;

    public MultiDayListViewAdapter(List<DummyContent.DummyItem> items, Context context) {
        mValues = items;
        context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mValues.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mValues.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.multi_day_fragment_item, parent, false);

        TextView mTimeView = (TextView) rowView.findViewById(R.id.day_time);
        TextView mSchoolView = (TextView) rowView.findViewById(R.id.day_school);
        TextView mDayView = (TextView) rowView.findViewById(R.id.day);
        TextView mLevelView = (TextView) rowView.findViewById(R.id.day_level);
        TextView mTeacherView = (TextView) rowView.findViewById(R.id.day_teacher);

        //holder.mItem = mValues.get(position);
        mTimeView.setText(mValues.get(position).time);
        mSchoolView.setText(mValues.get(position).school);
        mDayView.setText(mValues.get(position).day);
        mLevelView.setText(mValues.get(position).teacher);
        mTeacherView.setText(mValues.get(position).level);
        return rowView;
    }
}
