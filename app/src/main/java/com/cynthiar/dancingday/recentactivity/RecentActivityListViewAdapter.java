package com.cynthiar.dancingday.recentactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.card.CardListViewAdapter;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;

import java.util.List;

/**
 * Created by cynthiar on 1/27/2018.
 */

public class RecentActivityListViewAdapter extends BaseAdapter {
    private List<ClassActivity> mValues;
    private Context mContext;
    private LayoutInflater mInflater;

    public RecentActivityListViewAdapter(List<ClassActivity> items, Context context) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.recent_activity_item, parent, false);
        }

        // Retrieve class activity information
        final ClassActivity classActivity = mValues.get(position);
        DummyItem danceClass = classActivity.getDanceClass();

        // Set date, time and school view texts
        TextView activityDateView = (TextView) convertView.findViewById(R.id.activity_date);
        TextView activityTimeView = (TextView) convertView.findViewById(R.id.activity_time);
        TextView schoolView = (TextView) convertView.findViewById(R.id.school);
        activityDateView.setText(classActivity.getDate().toString(CardListViewAdapter.ExpirationDateFormatter));
        activityTimeView.setText(danceClass.danceClassTime.toString());
        schoolView.setText(danceClass.school.Key);

        // Set payment type view
        ImageView paymentTypeCardView = (ImageView)convertView.findViewById(R.id.payment_card);
        ImageView paymentTypeTicketView = (ImageView)convertView.findViewById(R.id.payment_ticket);
        //if (PaymentType.PunchCard == classActivity.getPaymentType()) {
        if (position % 2 == 0) { // todo
            paymentTypeCardView.setVisibility(View.VISIBLE);
            paymentTypeTicketView.setVisibility(View.GONE);
        }
        else {
            paymentTypeTicketView.setVisibility(View.VISIBLE);
            paymentTypeCardView.setVisibility(View.GONE);
        }

        // TODO make clickable, on-click open recent activity details view
        return convertView;
    }
}
