package com.cynthiar.dancingday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cynthiar.dancingday.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 */
public class SingleDayRecyclerViewAdapter extends RecyclerView.Adapter<SingleDayViewHolder> {

    private final List<DummyItem> mValues;
    private Context context;

    public SingleDayRecyclerViewAdapter(List<DummyItem> items, Context context) {
        mValues = items;
        context = context;
    }

    @Override
    public SingleDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_day_fragment_item, parent, false);
        return new SingleDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleDayViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTimeView.setText(mValues.get(position).time);
        holder.mSchoolView.setText(mValues.get(position).school);

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, DummyItem data) {
        mValues.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(DummyItem data) {
        int position = mValues.indexOf(data);
        mValues.remove(position);
        notifyItemRemoved(position);
    }
}
