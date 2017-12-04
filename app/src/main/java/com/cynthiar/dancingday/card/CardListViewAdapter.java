package com.cynthiar.dancingday.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.DanceClassCard;

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

    public static DateTimeFormatter ExpirationDateFormatter = DateTimeFormat.forPattern("MMM d, YYYY");

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

        // Set company, count and expiration date view texts
        mSchoolView.setText(danceClassCard.getCompany().toString());
        StringBuilder sb = new StringBuilder();
        sb.append(danceClassCard.getCount());
        sb.append(" classes");
        mCountView.setText(sb.toString());
        mExpirationDateView.setText(danceClassCard.getExpirationDate().toString(CardListViewAdapter.ExpirationDateFormatter));

        convertView.setClickable(true);
        convertView.setFocusable(true);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //DummyUtils.toast(mContext, "Coming soon...");
                EditDeleteCardFragment.newInstance(danceClassCard.toKey())
                        .show(((CardsActivity)mContext).getSupportFragmentManager(), EditDeleteCardFragment.TAG);
            }

        });
        return convertView;
    }
}
