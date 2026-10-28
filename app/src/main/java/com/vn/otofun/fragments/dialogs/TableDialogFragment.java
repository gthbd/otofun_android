package com.vn.otofun.fragments.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TextView;

import com.vn.otofun.R;
import com.rey.material.widget.Button;

/**
 * Created by AnhVT on 6/26/15.
 */
public class TableDialogFragment extends DialogFragment {

    public final static String TITLE = "title";
    public final static String TAG = "tag";

    boolean mIsShowing = false;
    String mTag;
    TableLayout mTableLayout;

    public interface ViewInjector {
        void injectViews(TableLayout tableLayout, String tag);

        void onViewCreated(View view, String tag);
    }

    public static TableDialogFragment newInstance(String title, String tag) {
        TableDialogFragment fragment = new TableDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(TAG, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.LARGE_DIALOG_WIDTH);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_table_chooser, container, false);

        String title = getArguments().getString(TITLE);
        mTag = getArguments().getString(TAG);

        TextView textView = (TextView) view.findViewById(R.id.dialog_table_choose_title_textView);
        textView.setText(title);
        Button closeButton = (Button) view.findViewById(R.id.dialog_table_choose_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mTableLayout = (TableLayout) view.findViewById(R.id.dialog_table_choose_table);
        if (getTargetFragment() instanceof ViewInjector) {
            ((ViewInjector) (getTargetFragment())).injectViews(mTableLayout, mTag);
        } else {
            throw new IllegalArgumentException(getTargetFragment().getClass().getSimpleName() + " must implement ViewInjector");
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getTargetFragment() instanceof ViewInjector) {
            ((ViewInjector) (getTargetFragment())).onViewCreated(mTableLayout, mTag);
        } else {
            throw new IllegalArgumentException(getTargetFragment().getClass().getSimpleName() + " must implement ViewInjector");
        }
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
