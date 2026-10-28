package com.vn.otofun.fragments.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;

import com.vn.otofun.R;

/**
 * Created by AnhVThd on 10/18/16.
 */

public class NameDialogFragment extends DialogFragment {

    boolean mIsShowing = false;
    EditText mEditText;
    TextView mErrorTextView;
    String mOldQuoteNameText = "";

    public interface InputNameListener {
        void onConfirm(String quoteName, int requestCode);
    }

    public void setEditText(String text) {
        mOldQuoteNameText = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

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
        View view = inflater.inflate(R.layout.dialog_input, container, false);

        mEditText = (EditText) view.findViewById(R.id.dialog_input_editText);
        TextView dialogTitleView = (TextView) view.findViewById(R.id.dialog_input_title_textView);
//        TextView editTitleView = (TextView) view.findViewById(R.id.dialog_input_title_edit_textView);
        mErrorTextView = (TextView) view.findViewById(R.id.dialog_input_error_textView);
        Button closeButton = (Button) view.findViewById(R.id.dialog_input_cancel_button);
        Button doneButton = (Button) view.findViewById(R.id.dialog_input_ok_button);
        Button clearButton = (Button) view.findViewById(R.id.dialog_input_clear_button);

        if (this.getTargetRequestCode() > 0) {
            dialogTitleView.setText(getResources().getString(R.string.buttonEditQuote));
            mEditText.setText(mOldQuoteNameText);

        } else {
            dialogTitleView.setText(getResources().getString(R.string.buttonCreateQuote));
        }
        mEditText.setHint(getResources().getString(R.string.labelInputQuoteName));
        mErrorTextView.setText("");
        mErrorTextView.setVisibility(View.VISIBLE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(null);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getTargetFragment();
                if (fragment != null && fragment instanceof InputNameListener) {
                    String text = mEditText.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        mErrorTextView.setText(getResources().getString(R.string.msgErrorQuoteNameEmpty));
                    } else if (text.length() > 64) {
                        mErrorTextView.setText(getResources().getString(R.string.msgErrorQuoteNameLong));
                    } else {
                        ((InputNameListener) fragment).onConfirm(text, getTargetRequestCode());
                        dismiss();
                    }
                }
            }
        });
        mEditText.addTextChangedListener(mTextChangeListener);

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

    private TextWatcher mTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            mEditText.removeTextChangedListener(this);
            mErrorTextView.setText("");
//            mEditText.addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
