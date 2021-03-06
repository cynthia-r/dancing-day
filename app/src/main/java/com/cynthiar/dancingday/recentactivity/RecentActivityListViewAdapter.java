package com.cynthiar.dancingday.recentactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.TodayActivity;
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
    private Fragment mTargetFragment;

    public RecentActivityListViewAdapter(List<ClassActivity> items, Context context, Fragment targetFragment) {
        mValues = items;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTargetFragment = targetFragment;
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

        // Make text italic if the activity is pending confirmation
        if (!classActivity.isConfirmed()) {
            activityDateView.setTypeface(activityDateView.getTypeface(), Typeface.BOLD_ITALIC);
            activityTimeView.setTypeface(activityTimeView.getTypeface(), Typeface.ITALIC);
        }

        // Set payment type view
        ImageView paymentTypeCardView = (ImageView)convertView.findViewById(R.id.payment_card);
        ImageView paymentTypeTicketView = (ImageView)convertView.findViewById(R.id.payment_ticket);
        if (PaymentType.PunchCard == classActivity.getPaymentType()) {
            paymentTypeCardView.setVisibility(View.VISIBLE);
            paymentTypeTicketView.setVisibility(View.GONE);
        }
        else {
            paymentTypeTicketView.setVisibility(View.VISIBLE);
            paymentTypeCardView.setVisibility(View.GONE);
        }

        // Setup on-click event for the date
        activityDateView.setClickable(true);
        activityDateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditActivityFragment editActivityFragment = EditActivityFragment.newInstance(classActivity);
                editActivityFragment.setTargetFragment(mTargetFragment, EditActivityFragment.REQUEST_CODE);
                editActivityFragment.show(mTargetFragment.getFragmentManager(), EditActivityFragment.TAG);
            }
        });

        // Setup on-click event for the list item
        convertView.setClickable(true);
        convertView.setFocusable(true);

        // Start the recent activity details activity
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TodayActivity parentActivity = (TodayActivity)mContext;
                Intent intent = new Intent(parentActivity, RecentActivityDetailsActivity.class);
                intent.putExtra(ClassActivityNotification.CLASS_ACTIVITY_ID_KEY, classActivity.getId());

                parentActivity.startActivity(intent);
            }

        });
        return convertView;
    }
}
