package com.cynthiar.dancingday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DanceClassCard;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.Preferences;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.LevelPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.SchoolPropertySelector;
import com.cynthiar.dancingday.dummy.time.DanceClassTime;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by CynthiaR on 2/5/2017.
 */

public class CardListViewAdapter extends BaseAdapter{
    private List<DanceClassCard> mValues;
    private Context mContext;
    private LayoutInflater mInflater;

    private DateTimeFormatter mExpirationDateFormatter = DateTimeFormat.forPattern("MMM d, YYYY");

    public CardListViewAdapter(List<DanceClassCard> items, Context context) {
        mValues = items;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_item, parent, false);
        }

        TextView mSchoolView = (TextView) convertView.findViewById(R.id.school);
        TextView mCountView = (TextView) convertView.findViewById(R.id.count);
        TextView mExpirationDateView = (TextView) convertView.findViewById(R.id.expiration_date);

        final DanceClassCard danceClassCard = mValues.get(position);

        // Set school, count and expiration date view texts
        mSchoolView.setText(danceClassCard.school.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(danceClassCard.count);
        sb.append(" classes");
        mCountView.setText(sb.toString());
        mExpirationDateView.setText(danceClassCard.expirationDate.toString(mExpirationDateFormatter));

        convertView.setClickable(true);
        convertView.setFocusable(true);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DummyUtils.toast(mContext, "Coming soon...");
            }

        });
        return convertView;
    }
}
