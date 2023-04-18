package com.example.custom.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.custom.R;

public class Prefs1Fragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_prefs1fragment);

    }
}
