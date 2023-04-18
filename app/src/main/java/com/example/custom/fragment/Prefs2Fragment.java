package com.example.custom.fragment;

import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

import com.example.custom.R;

public class Prefs2Fragment extends PreferenceFragment {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.root_preferences);
//
//        String name = ((EditTextPreference) findPreference("name")).getText();
////        Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference preference = findPreference("first");
        preference.setTitle("8888");
    }
}
