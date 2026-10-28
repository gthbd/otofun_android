package com.vn.otofun.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

import com.vn.otofun.BaseFragment;
import com.vn.otofun.R;

/**
 * Created by AnhVT on 7/2/15.
 */
public abstract class AbsLoadingFragment extends BaseFragment {
    protected View mLoadingView;
    protected View mContentView;
    protected ProgressView mLoadingProgressView;
    protected TextView mLoadingTextView;

    protected abstract int getLoadingLayoutResId();

    protected abstract int getContentLayoutResId();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getLoadingLayoutResId() > 0 && getContentLayoutResId() > 0) {
            mLoadingView = view.findViewById(getLoadingLayoutResId());
            mContentView = view.findViewById(getContentLayoutResId());

            mLoadingProgressView = (ProgressView) mLoadingView.findViewById(R.id.include_loading_view_progressView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mLoadingProgressView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mLoadingTextView = (TextView) mLoadingView.findViewById(R.id.include_loading_view_textView);
        }
    }

//    @Override
//    public void execute() {
//        if (mLoadingView.getVisibility() == View.VISIBLE) return;
//    }

//    protected void showContentView() {
//        if (mLoadingView != null)
//            mLoadingView.setVisibility(View.GONE);
//        if (mLoadingProgressView != null)
//            mLoadingProgressView.stop();
//        if (mContentView != null)
//            mContentView.setVisibility(View.VISIBLE);
//    }
//
//    protected void showLoadingView() {
//        if (mLoadingView != null)
//            mLoadingView.setVisibility(View.VISIBLE);
//        if (mLoadingProgressView != null)
//            mLoadingProgressView.start();
//        if (mContentView != null)
//            mContentView.setVisibility(View.GONE);
//    }
//
//    protected void setupLoadingText(String text, int color) {
//        mLoadingTextView.setVisibility(View.VISIBLE);
//        mLoadingTextView.setText(text);
//        mLoadingTextView.setTextColor(color);
//    }
}
