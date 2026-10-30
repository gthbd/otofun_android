package com.vn.otofun.fragments.dialogs;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vn.otofun.R;
import com.vn.otofun.views.YearPicker;
import com.rey.material.app.DialogFragment;

/**
 * Created by AnhVT on 8/7/15.
 */
public class NumberPickerDialogFragment extends DialogFragment {
    final static String EXTRA_MIN = "extra_min";
    final static String EXTRA_MAX = "extra_max";

    YearPicker mPicker;
    YearPicker.OnYearChangedListener mListener;

    public static NumberPickerDialogFragment newInstance(Builder builder, int min, int max) {
        NumberPickerDialogFragment fragment = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_MIN, min);
        args.putInt(EXTRA_MAX, max);
        fragment.setArguments(args);
        fragment.mBuilder = builder;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int min = getArguments().getInt(EXTRA_MIN);
        int max = getArguments().getInt(EXTRA_MAX);

        mPicker = (YearPicker) getDialog().findViewById(R.id.dialog_number_picker_picker);
        mPicker.setYearRange(min, max);
        mPicker.setOnYearChangedListener(mListener);
        mPicker.setSelection(0);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setOnNumberChangeListener(YearPicker.OnYearChangedListener listener) {
        mListener = listener;
    }

    public int getSelectedNumber() {
        return mPicker.getYear();
    }
}
