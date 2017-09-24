package com.cynthiar.dancingday;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

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
public class NewCardFragment extends DialogFragment {
    public static final String TAG = "NewCardFragment";
    private Schools.DanceSchool mSchool;
    private int mNumberOfClasses;
    private DateTime mExpirationDate;
    private NewCardDialogListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewCardFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NewCardDialogListener so we can send events to the host
            mListener = (NewCardDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NewCardDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the parent activity
        final CardsActivity parentActivity = (CardsActivity)getActivity();

        // Get the new card view
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        final View newCardView = inflater.inflate(R.layout.new_card_fragment, null);

        // Setup the number of classes editor
        TextView numberOfClassesView = (TextView) newCardView.findViewById(R.id.count);
        numberOfClassesView.addTextChangedListener(new NewCardTextWatcher());

        // Setup the expiration date picker
        DatePicker datePicker = (DatePicker) newCardView.findViewById(R.id.expiration_date_picker);
        final DateTime currentDate = DateTime.now();
        datePicker.init(currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth(), new NewCardDatePickerListener());
        // The date picker's day of month is zero-based

        // Setup the school spinner
        final Spinner schoolSpinner = (Spinner) newCardView.findViewById(R.id.schoolSpinner);
        final List<String> schoolSpinnerItemList = Extractors.getInstance(parentActivity).getSchoolList(false);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                parentActivity, R.layout.school_spinner_item, R.layout.spinner_dropdown_item, schoolSpinnerItemList);
        schoolSpinner.setAdapter(spinnerAdapter);
        schoolSpinner.setOnItemSelectedListener(new NewCardSchoolSpinnerListener(schoolSpinnerItemList));

        // Build the new card dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_new_card_dialog)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
                .setView(newCardView)
                .setPositiveButton(R.string.confirm_new_card, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Save the new card
                        DanceClassCard danceClassCard = new DanceClassCard(mSchool, mNumberOfClasses, currentDate, mExpirationDate);
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

    /*
        Listener for the "New card" school spinner.
     */
    private class NewCardSchoolSpinnerListener implements AdapterView.OnItemSelectedListener {
        private List<String> mSchoolList;
        public NewCardSchoolSpinnerListener(List<String> schoolList) {
            mSchoolList = schoolList;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSchool = Schools.DanceSchool.fromString(mSchoolList.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }

    /*
        Listener interface for events coming from this dialog.
     */
    public interface NewCardDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    /*
        Text watcher for the "New card" number of classes editor.
     */
    private class NewCardTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            mNumberOfClasses = Integer.parseInt(s.toString());
        }
    }

    /*
        Listener for the "New card" date picker.
     */
    private class NewCardDatePickerListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mExpirationDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 00, 00);
            // The date picker's day of month is zero-based
        }
    }
}
