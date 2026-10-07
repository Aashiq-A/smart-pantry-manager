package com.abrahams.smartpantrymanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.abrahams.smartpantrymanager.util.PantryPreferences;
import com.example.smartpantrymanager.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_settings, container, false);
    }

    // it will show the saved choice on the switch and saves it again when the switch changes.
    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        SwitchCompat switchShowExpiryDates = view.findViewById(
                R.id.switchShowExpiryDates);

        switchShowExpiryDates.setChecked(
                PantryPreferences.shouldShowExpiryDates(
                        requireContext()));

        switchShowExpiryDates.setOnCheckedChangeListener(
                (button, isChecked) ->
                        PantryPreferences.setShowExpiryDates(
                                requireContext(), isChecked));
    }
}