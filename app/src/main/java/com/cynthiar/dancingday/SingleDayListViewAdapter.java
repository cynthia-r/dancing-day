package com.cynthiar.dancingday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.Preferences;
import com.cynthiar.dancingday.model.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.model.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.model.propertySelector.LevelPropertySelector;
import com.cynthiar.dancingday.model.propertySelector.SchoolPropertySelector;
import com.cynthiar.dancingday.model.time.DanceClassTime;

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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_day_fragment_item, parent, false);
        }

        TextView mTimeView = (TextView) convertView.findViewById(R.id.time);
        TextView mClassItemTopRightView = (TextView) convertView.findViewById(R.id.class_item_top_right);
        TextView mTeacherView = (TextView) convertView.findViewById(R.id.teacher);
        TextView mClassItemBottomLeftView = (TextView) convertView.findViewById(R.id.class_item_bottom_left);

        final DummyItem dummyItem = mValues.get(position);

        DanceClassTime danceClassTime = dummyItem.danceClassTime;
        String time = null == danceClassTime ? "" : danceClassTime.toString();

        // Set time and teacher view texts
        mTimeView.setText(time);
        mTeacherView.setText(dummyItem.teacher);

        // Set corner view texts
        DanceClassPropertySelector currentPropertySelector = ((TodayActivity) mContext).getCurrentPropertySelector();
        String topRightViewText = "";
        String bottomLeftViewText = "";

        if (null == currentPropertySelector || currentPropertySelector instanceof DayPropertySelector) {
            topRightViewText = dummyItem.school.Key;
            bottomLeftViewText = dummyItem.level.toString();
        }
        else if (currentPropertySelector instanceof SchoolPropertySelector) {
            topRightViewText = dummyItem.day;
            bottomLeftViewText = dummyItem.level.toString();
        }
        else if (currentPropertySelector instanceof LevelPropertySelector) {
            topRightViewText = dummyItem.school.Key;
            bottomLeftViewText = dummyItem.day;
        }

        mClassItemTopRightView.setText(topRightViewText);
        mClassItemBottomLeftView.setText(bottomLeftViewText);

        // Display whether the item is a favorite
        ImageView starView = (ImageView)convertView.findViewById(R.id.star);
        if (Preferences.getInstance(mContext).isFavorite(dummyItem.toKey())) {
            starView.setVisibility(View.VISIBLE);
        }
        else
            starView.setVisibility(View.GONE);

        convertView.setClickable(true);
        convertView.setFocusable(true);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TodayActivity parentActivity = (TodayActivity)mContext;
                Intent intent = new Intent(parentActivity, DetailsActivity.class);
                Bundle bundle = DetailsActivity.toBundle(dummyItem);
                intent.putExtra(DetailsActivity.DANCE_CLASS_KEY, bundle);

                parentActivity.startActivity(intent);
            }

        });
        return convertView;
    }
}
