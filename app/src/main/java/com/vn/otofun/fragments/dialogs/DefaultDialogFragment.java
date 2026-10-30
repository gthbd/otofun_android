package com.vn.otofun.fragments.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.vn.otofun.R;
import com.vn.otofun.TwoButtonsDialogClickListener;
import com.rey.material.widget.Button;

/**
 * Created by AnhVT on 9/22/15.
 */
public class DefaultDialogFragment extends DialogFragment {
    public final static String TAG = "tag";
    public final static String TITLE = "title";
    public final static String TITLE_COLOR = "title_color";
    public final static String CONTENT = "content";
    public final static String CONTENT_COLOR = "content_color";
    public final static String POSITIVE_TITLE = "positive_title";
    public final static String NEGATIVE_TITLE = "negative_title";

    boolean mIsShowing = false;

    public String userString = "";


    public static DefaultDialogFragment newInstance(String tag, String title, int titleColor, String content, int contentColor, String positiveTitle, String negativeTitle) {
        DefaultDialogFragment fragment = new DefaultDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putString(TAG, tag);
        args.putString(TITLE, title);
        args.putInt(TITLE_COLOR, titleColor);
        args.putString(CONTENT, content);
        args.putInt(CONTENT_COLOR, contentColor);
        args.putString(POSITIVE_TITLE, positiveTitle);
        args.putString(NEGATIVE_TITLE, negativeTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.DIALOG_WIDTH);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_default, container, false);

        final String tag = getArguments().getString(TAG);
        String title = getArguments().getString(TITLE);
        int titleColor = getArguments().getInt(TITLE_COLOR);
        String content = getArguments().getString(CONTENT);
        int contentColor = getArguments().getInt(CONTENT_COLOR);
        String positive = getArguments().getString(POSITIVE_TITLE);
        String negative = getArguments().getString(NEGATIVE_TITLE);

        TextView titleTextView = (TextView) view.findViewById(R.id.dialog_default_title_textView);
        TextView contentTextView = (TextView) view.findViewById(R.id.dialog_default_content_textView);
        Button positiveButton = (Button) view.findViewById(R.id.dialog_default_positive_button);
        Button negativeButton = (Button) view.findViewById(R.id.dialog_default_negative_button);

        titleTextView.setText(title);
        titleTextView.setTextColor(titleColor);
        contentTextView.setText(content);
        contentTextView.setTextColor(contentColor);
        positiveButton.setText(positive);
        negativeButton.setText(negative);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Fragment fragment = getTargetFragment();
                Activity activity = getActivity();
                if (fragment != null && fragment instanceof TwoButtonsDialogClickListener) {
                    ((TwoButtonsDialogClickListener) fragment).onClickPositiveButton(tag, userString);
                } else if (activity != null && activity instanceof TwoButtonsDialogClickListener) {
                    ((TwoButtonsDialogClickListener) activity).onClickPositiveButton(tag, userString);
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Fragment fragment = getTargetFragment();
                Activity activity = getActivity();
                if (fragment != null && fragment instanceof TwoButtonsDialogClickListener) {
                    ((TwoButtonsDialogClickListener) fragment).onClickNegativeButton(tag, userString);
                } else if (activity != null && activity instanceof TwoButtonsDialogClickListener) {
                    ((TwoButtonsDialogClickListener) activity).onClickNegativeButton(tag, userString);
                }
            }
        });

        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (mIsShowing) return;

        try {
            super.show(manager, tag);
        } catch (Exception e) {
        }

        mIsShowing = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mIsShowing = false;
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        String tag = getArguments().getString(TAG);
        Fragment fragment = getTargetFragment();
        Activity activity = getActivity();
        if (fragment != null && fragment instanceof TwoButtonsDialogClickListener) {
            ((TwoButtonsDialogClickListener) fragment).onClickNegativeButton(tag, userString);
        } else if (activity != null && activity instanceof TwoButtonsDialogClickListener) {
            ((TwoButtonsDialogClickListener) activity).onClickNegativeButton(tag, userString);
        }
    }
}
