package com.cynthiar.dancingday.card;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.SpinnerAdapter;
import com.cynthiar.dancingday.dummy.DanceClassCard;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.Preferences;
import com.cynthiar.dancingday.dummy.Schools;
import com.cynthiar.dancingday.dummy.extractor.Extractors;

import org.joda.time.DateTime;

import java.util.List;

/*
    Fragment for the new card dialog.
 */
public class NewCardFragment extends BaseCardFragment {
    public static final String TAG = "NewCardFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewCardFragment() {
    }

    @Override
    protected int getViewResourceId() {
        return R.layout.new_card_fragment;
    }

    @Override
    protected void initializeData(Activity parentActivity, Spinner schoolSpinner, TextView textView, DatePicker datePicker) {
        final DateTime currentDate = DateTime.now();
        datePicker.init(currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth(), new CardDatePickerListener());
        // The date picker's day of month is zero-based
    }

    @Override
    protected Dialog buildDialog(View view, final Activity parentActivity) {
        // Build the new card dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_new_card_dialog)
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(view)
                .setPositiveButton(R.string.confirm_new_card, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Save the new card
                        DanceClassCard danceClassCard = new DanceClassCard(mSchool, mNumberOfClasses, DateTime.now(), mExpirationDate);
                        Preferences.getInstance(parentActivity).saveCard(danceClassCard);
                        mListener.onDialogPositiveClick(NewCardFragment.this);
                        DummyUtils.toast(getActivity(), "New card created");
                    }
                })
                .setNegativeButton(R.string.cancel_new_card, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
