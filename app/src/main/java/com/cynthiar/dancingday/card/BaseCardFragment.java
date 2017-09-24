package com.cynthiar.dancingday.card;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.cynthiar.dancingday.dummy.Schools;
import com.cynthiar.dancingday.dummy.extractor.Extractors;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by CynthiaR on 9/23/2017.
 */

public abstract class BaseCardFragment extends DialogFragment {
    protected Schools.DanceSchool mSchool;
    protected int mNumberOfClasses;
    protected DateTime mExpirationDate;
    protected NewCardDialogListener mListener;

    /*
        Listener interface for events coming from this dialog.
     */
    public interface NewCardDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NewCardDialogListener so we can send events to the host
            mListener = (BaseCardFragment.NewCardDialogListener) activity;
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

        // Initialize the data
        this.initializeData();

        // Get the new card view
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        final View newCardView = inflater.inflate(this.getViewResourceId(), null);

        // Setup the number of classes editor
        TextView numberOfClassesView = (TextView) newCardView.findViewById(R.id.count);
        numberOfClassesView.addTextChangedListener(new CardTextWatcher());
        if (mNumberOfClasses > 0)
            numberOfClassesView.setText(Integer.toString(mNumberOfClasses));

        // Setup the expiration date picker
        DatePicker datePicker = (DatePicker) newCardView.findViewById(R.id.expiration_date_picker);
        datePicker.init(mExpirationDate.getYear(), mExpirationDate.getMonthOfYear() - 1, mExpirationDate.getDayOfMonth(), new CardDatePickerListener());
        // The date picker's day of month is zero-based

        // Setup the school spinner
        final Spinner schoolSpinner = (Spinner) newCardView.findViewById(R.id.schoolSpinner);
        final List<String> schoolSpinnerItemList = Extractors.getInstance(parentActivity).getSchoolList(false);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                parentActivity, R.layout.school_spinner_item, R.layout.spinner_dropdown_item, schoolSpinnerItemList);
        schoolSpinner.setAdapter(spinnerAdapter);
        schoolSpinner.setOnItemSelectedListener(new CardSchoolSpinnerListener(schoolSpinnerItemList));
        schoolSpinner.setSelection(schoolSpinnerItemList.indexOf(mSchool.Key));

        // Build and return the dialog
        return this.buildDialog(newCardView, parentActivity);
    }

    protected abstract int getViewResourceId();

    protected abstract void initializeData();

    protected abstract Dialog buildDialog(View view, final Activity parentActivity);

    /*
        Listener for the card's school spinner.
     */
    private class CardSchoolSpinnerListener implements AdapterView.OnItemSelectedListener {
        private List<String> mSchoolList;
        public CardSchoolSpinnerListener(List<String> schoolList) {
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
        Text watcher for the card's number of classes editor.
     */
    private class CardTextWatcher implements TextWatcher {
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
            try {
                mNumberOfClasses = Integer.parseInt(s.toString());
            }
            catch (Exception e){
                mNumberOfClasses = 0;
            }
        }
    }

    /*
        Listener for the card's date picker.
     */
    protected class CardDatePickerListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mExpirationDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 00, 00);
            // The date picker's day of month is zero-based
        }
    }
}
