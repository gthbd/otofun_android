package com.vn.otofun.fragments.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.vn.otofun.OneButtonDialogClickListener;
import com.vn.otofun.R;

/**
 * Created by AnhVT on 6/4/15.
 */
public class NoticeDialogFragment extends DialogFragment {

    public final static String TAG = "tag";
    public final static String TITLE = "title";
    public final static String TITLE_COLOR = "title_color";
    public final static String ICON = "icon";
    public final static String ICON_COLOR = "icon_color";
    public final static String CONTENT = "content";
    public final static String CONTENT_COLOR = "content_color";
    public final static String BUTTON = "button";

    boolean mIsShowing = false;

    public static NoticeDialogFragment newInstance(String tag, String title, int titleColor, String icon, int iconColor, String content, int contentColor) {
        NoticeDialogFragment fragment = new NoticeDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        Bundle args = new Bundle();
        args.putString(TAG, tag);
        args.putString(TITLE, title);
        args.putString(ICON, icon);
        args.putInt(TITLE_COLOR, titleColor);
        args.putInt(ICON_COLOR, iconColor);
        args.putString(CONTENT, content);
        args.putInt(CONTENT_COLOR, contentColor);
        fragment.setArguments(args);

        return fragment;
    }

    public void setContent(String message) {
        getArguments().putString(CONTENT, message);
    }

    public void setTitle(String title) {
        getArguments().putString(TITLE, title);
    }

    public void setIcon(String icon) {
        getArguments().putString(ICON, icon);
    }

    public void setContentColor(int color) {
        getArguments().putInt(CONTENT_COLOR, color);
    }

    public void setTitleColor(int color) {
        getArguments().putInt(TITLE_COLOR, color);
    }

    public void setIconColor(int color) {
        getArguments().putInt(ICON_COLOR, color);
    }

    public void setTitleButton(String title) {
        getArguments().putString(BUTTON, title);
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
        View view = inflater.inflate(R.layout.dialog_confirm, container, false);

        final String tag = getArguments().getString(TAG);
        String title = getArguments().getString(TITLE);
        String icon = getArguments().getString(ICON);
        String content = getArguments().getString(CONTENT);
        String buttonStr = getArguments().getString(BUTTON);
        int titleColor = getArguments().getInt(TITLE_COLOR);
        int iconColor = getArguments().getInt(ICON_COLOR);
        int contentColor = getArguments().getInt(CONTENT_COLOR);

        TextView titleTextView = (TextView) view.findViewById(R.id.dialog_confirm_title_textView);
        titleTextView.setText(title);
        titleTextView.setTextColor(titleColor);

        TextView iconTextView = (TextView) view.findViewById(R.id.dialog_confirm_icon);
        if (icon != null) {
            iconTextView.setText(icon);
            iconTextView.setTextColor(iconColor);
        } else {
            iconTextView.setVisibility(View.GONE);
        }

        TextView contentTextView = (TextView) view.findViewById(R.id.dialog_confirm_content_textView);
        contentTextView.setText(content);
        contentTextView.setTextColor(contentColor);

        Button button = (Button) view.findViewById(R.id.dialog_confirm_button);
        if (!TextUtils.isEmpty(buttonStr)) button.setText(buttonStr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Fragment fragment = getTargetFragment();
                Activity activity = getActivity();
                if (fragment != null && fragment instanceof OneButtonDialogClickListener) {
                    ((OneButtonDialogClickListener) fragment).onClickDialogButton(tag);
                } else if (activity != null && activity instanceof OneButtonDialogClickListener) {
                    ((OneButtonDialogClickListener) activity).onClickDialogButton(tag);
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
}
