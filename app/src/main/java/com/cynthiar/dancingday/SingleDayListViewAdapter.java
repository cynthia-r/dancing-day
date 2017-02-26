package com.cynthiar.dancingday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.time.DanceClassTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by CynthiaR on 2/5/2017.
 */

public class SingleDayListViewAdapter extends BaseAdapter{
    private List<DummyItem> mValues;
    private Context mContext;
    private LayoutInflater mInflater;

    public SingleDayListViewAdapter(List<DummyItem> items, Context context) {
        mValues = items;
        mContext = context;
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
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.single_day_fragment_item, parent, false);
        }

        TextView mTimeView = (TextView) convertView.findViewById(R.id.time);
        TextView mSchoolView = (TextView) convertView.findViewById(R.id.school);
        TextView mTeacherView = (TextView) convertView.findViewById(R.id.teacher);
        TextView mLevelView = (TextView) convertView.findViewById(R.id.level);

        DanceClassTime danceClassTime = mValues.get(position).danceClassTime;
        String time = null == danceClassTime ? "" : danceClassTime.toString();

        mTimeView.setText(time);
        mSchoolView.setText(mValues.get(position).school);
        mLevelView.setText(mValues.get(position).level.toString());
        mTeacherView.setText(mValues.get(position).teacher);

        convertView.setClickable(true);
        convertView.setFocusable(true);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DummyUtils.toast(mContext, "Doing something");
            }

        });
        return convertView;
    }
}
