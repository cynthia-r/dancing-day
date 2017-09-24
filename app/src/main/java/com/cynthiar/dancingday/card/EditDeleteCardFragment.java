package com.cynthiar.dancingday.card;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.dummy.DanceClassCard;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.Preferences;
import com.cynthiar.dancingday.dummy.extractor.Extractors;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by CynthiaR on 9/23/2017.
 */

public class EditDeleteCardFragment extends BaseCardFragment {
    public static final String TAG = "EditDeleteCardFragment";
    private String mCardKey;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditDeleteCardFragment() {
    }

    public static EditDeleteCardFragment newInstance(String cardKey) {
        EditDeleteCardFragment editDeleteCardFragment = new EditDeleteCardFragment();
        editDeleteCardFragment.setCardKey(cardKey);
        return editDeleteCardFragment;
    }

    private void setCardKey(String cardKey) {
        mCardKey = cardKey;
    }

    @Override
    protected int getViewResourceId() {
        return R.layout.new_card_fragment;
    }

    @Override
    protected void initializeData(Activity parentActivity, Spinner schoolSpinner, TextView textView, DatePicker datePicker) {
        DanceClassCard danceClassCard = DanceClassCard.fromKey(mCardKey);

        Object[] schoolSpinnerItemList = Extractors.getInstance(parentActivity).getSchoolList(false).toArray();
        int i=0;
        while (i < schoolSpinnerItemList.length && (String)(schoolSpinnerItemList[i]) != danceClassCard.school.Key) {
            i++;
        }
        if (i < schoolSpinnerItemList.length)
            schoolSpinner.setSelection(i);

        textView.setText(Integer.toString(danceClassCard.count));
        DateTime expirationDate = danceClassCard.expirationDate;
        datePicker.init(expirationDate.getYear(), expirationDate.getMonthOfYear() - 1, expirationDate.getDayOfMonth(), new CardDatePickerListener());
        // The date picker's day of month is zero-based
    }

    @Override
    protected Dialog buildDialog(View view, final Activity parentActivity) {
        // Build the edit/delete card dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_edit_delete_card_dialog)
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(view)
                .setNeutralButton(R.string.confirm_new_card, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the card
                        DanceClassCard danceClassCard = new DanceClassCard(mSchool, mNumberOfClasses, DateTime.now(), mExpirationDate);
                        Preferences.getInstance(parentActivity).updateCard(mCardKey, danceClassCard);
                        mListener.onDialogPositiveClick(EditDeleteCardFragment.this);
                        DummyUtils.toast(getActivity(), "Card updated");
                    }
                })
                .setPositiveButton(R.string.delete_card, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the card
                        Preferences.getInstance(parentActivity).deleteCard(mCardKey);
                        mListener.onDialogPositiveClick(EditDeleteCardFragment.this);
                        DummyUtils.toast(getActivity(), "Card deleted");
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
