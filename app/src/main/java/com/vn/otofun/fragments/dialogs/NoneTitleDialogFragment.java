package com.vn.otofun.fragments.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.rey.material.widget.Button;

import com.vn.otofun.OneButtonDialogClickListener;
import com.vn.otofun.R;
import com.vn.otofun.TwoButtonsDialogClickListener;

/**
 * Created by AnhVT on 7/2/15.
 */
public class NoneTitleDialogFragment extends DialogFragment {
    public final static String TAG = "tag";
    public final static String CONTENT = "content";
    public final static String ICON = "icon";
    public final static String COLOR = "color";
    public final static String POSITIVE = "positive";
    public final static String NEGATIVE = "negative";
    public final static String TYPE = "type";

    public final static int TYPE_ONE_BUTTON = 0;
    public final static int TYPE_TWO_BUTTONS = 1;

    boolean mIsShowing = false;

    public static NoneTitleDialogFragment newInstance(String tag, String content, String positive, String negative, String icon, int color, int type) {
        NoneTitleDialogFragment fragment = new NoneTitleDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        Bundle args = new Bundle();
        args.putString(TAG, tag);
        args.putString(CONTENT, content);
        args.putString(POSITIVE, positive);
        args.putString(NEGATIVE, negative);
        args.putString(ICON, icon);
        args.putInt(COLOR, color);
        args.putInt(TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.SMALL_DIALOG_WIDTH);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setContent(String message) {
        getArguments().putString(CONTENT, message);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_none_title, container, false);

        final String tag = getArguments().getString(TAG);
        String content = getArguments().getString(CONTENT);
        String postive = getArguments().getString(POSITIVE);
        String negative = getArguments().getString(NEGATIVE);
        String icon = getArguments().getString(ICON);
        int color = getArguments().getInt(COLOR);
        int type = getArguments().getInt(TYPE);

        TextView contentTextView = (TextView) view.findViewById(R.id.none_title_dialog_textView);
        TextView iconTextView = (TextView) view.findViewById(R.id.none_title_dialog_icon);
        View oneButtonPane = view.findViewById(R.id.none_title_dialog_one_button_pane);
        View twoButtonsPane = view.findViewById(R.id.none_title_dialog_two_button_pane);
        Button okButton = (Button) view.findViewById(R.id.none_title_dialog_ok_button);
        Button positiveButton = (Button) view.findViewById(R.id.none_title_dialog_positive_button);
        Button negativeButton = (Button) view.findViewById(R.id.none_title_dialog_negative_button);

        if (type == TYPE_ONE_BUTTON) {
            oneButtonPane.setVisibility(View.VISIBLE);
            twoButtonsPane.setVisibility(View.GONE);
            okButton.setText(postive);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = getTargetFragment();
                    Activity activity = getActivity();
                    if (fragment != null && fragment instanceof OneButtonDialogClickListener) {
                        ((OneButtonDialogClickListener) fragment).onClickDialogButton(tag);
                    } else if (activity != null && activity instanceof OneButtonDialogClickListener) {
                        ((OneButtonDialogClickListener) activity).onClickDialogButton(tag);
                    }
                    dismiss();
                }
            });
        } else {
            oneButtonPane.setVisibility(View.GONE);
            twoButtonsPane.setVisibility(View.VISIBLE);
            positiveButton.setText(postive);
            negativeButton.setText(negative);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = getTargetFragment();
                    Activity activity = getActivity();
                    if (fragment != null && fragment instanceof TwoButtonsDialogClickListener) {
                        ((TwoButtonsDialogClickListener) fragment).onClickPositiveButton(tag, "");
                    } else if (activity != null && activity instanceof TwoButtonsDialogClickListener) {
                        ((TwoButtonsDialogClickListener) activity).onClickPositiveButton(tag, "");
                    }
                    dismiss();
                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = getTargetFragment();
                    Activity activity = getActivity();
                    if (fragment != null && fragment instanceof TwoButtonsDialogClickListener) {
                        ((TwoButtonsDialogClickListener) fragment).onClickNegativeButton(tag, "");
                    } else if (activity != null && activity instanceof TwoButtonsDialogClickListener) {
                        ((TwoButtonsDialogClickListener) activity).onClickNegativeButton(tag, "");
                    }
                    dismiss();
                }
            });
        }
        contentTextView.setText(Html.fromHtml(content));
        if (icon != null) {
            iconTextView.setVisibility(View.VISIBLE);
            iconTextView.setText(icon);
            iconTextView.setTextColor(color);
        } else {
            iconTextView.setVisibility(View.GONE);
        }

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
}
