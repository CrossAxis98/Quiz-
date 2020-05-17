package com.example.quizam;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SecondFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.preferences);
    }
}