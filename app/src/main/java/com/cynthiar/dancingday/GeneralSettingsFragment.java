package com.cynthiar.dancingday;


import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Fragment for the general settings.
 */
public class GeneralSettingsFragment extends PreferenceFragment {
    // Required empty public constructor
    public GeneralSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
