package com.vn.otofun.fragments.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vn.otofun.R;

/**
 * Created by AnhVT on 6/19/15.
 */
public class LoadingDialogFragment extends DialogFragment {
    public final static String MESSAGE = "message";

    boolean mIsShowing = false;

    public static LoadingDialogFragment newInstance(String message) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        fragment.setCancelable(false);

        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);

        TextView textView = (TextView) view.findViewById(R.id.dialog_loading_textView);

        String message = getArguments().getString(MESSAGE);
        if (message != null) {
            textView.setText(message);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (mIsShowing) return;

        super.show(manager, tag);
        mIsShowing = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mIsShowing = false;
        super.onDismiss(dialog);
    }
}
