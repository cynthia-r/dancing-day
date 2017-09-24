package com.cynthiar.dancingday.card;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Preferences;

import org.joda.time.DateTime;

/**
 * Created by CynthiaR on 9/23/2017.
 */

public class EditDeleteCardFragment extends BaseCardFragment {
    public static final String TAG = "EditDeleteCardFragment";
    private String mCardKey;
    private DanceClassCard mDanceClassCard;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditDeleteCardFragment() {
    }

    public static EditDeleteCardFragment newInstance(String cardKey) {
        EditDeleteCardFragment editDeleteCardFragment = new EditDeleteCardFragment();
        editDeleteCardFragment.setCard(cardKey);
        return editDeleteCardFragment;
    }

    private void setCard(String cardKey) {
        mCardKey = cardKey;
        mDanceClassCard = DanceClassCard.fromKey(cardKey);
    }

    @Override
    protected void initializeData() {
        mCompany = mDanceClassCard.company;
        mNumberOfClasses = mDanceClassCard.count;
        mExpirationDate = mDanceClassCard.expirationDate;
    }

    @Override
    protected Dialog buildDialog(View view, final Activity parentActivity) {
        // Build the edit/delete card dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_edit_delete_card_dialog)
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(view)
                .setNeutralButton(R.string.edit_card, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the card
                        DanceClassCard danceClassCard = new DanceClassCard(mCompany, mNumberOfClasses, DateTime.now(), mExpirationDate);
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
                .setNegativeButton(R.string.cancel_card_action, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
