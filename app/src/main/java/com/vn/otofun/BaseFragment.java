package com.vn.otofun;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import com.vn.otofun.utils.Commons;

import com.vn.otofun.fragments.dialogs.NoticeDialogFragment;

/**
 * Created by AnhVT on 6/22/15.
 */
public class BaseFragment extends Fragment implements OnBackPressListener {
    protected final static String DIALOG_ERROR = "dialog_error";
    private static final String SAVED_STATE_KEY = BaseFragment.class.getSimpleName();
    public Handler mMainUIHandler = new Handler();
    public AlertDialog mAlertDialog;
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        ((vStockApplication) getActivity().getApplication()).setFragmentSavedState(SAVED_STATE_KEY, getFragmentManager().saveFragmentInstanceState(this));
        Commons.hideSoftKeyboard(getActivity()); //anhvt add new

        mMainUIHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ((vStockApplication) getActivity().getApplication()).setFragmentSavedState(SAVED_STATE_KEY, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Commons.setupViewListenSoftKeyboard(view, getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mMainUIHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onBackPressed() {
        return mBackPressImpl.onBackPressed();
    }


//    protected void showFailureDialog() {
//        Activity activity = getActivity();
//        if (mFailureDialog == null && activity != null) {
//            mFailureDialog = NoticeDialogFragment.newInstance(
//                    DIALOG_FAILURE,
//                    activity.getResources().getString(R.string.common_title_error),
//                    activity.getResources().getColor(R.color.dialog_failure),
//                    activity.getResources().getString(R.string.ion_android_warning),
//                    activity.getResources().getColor(R.color.dialog_failure),
//                    activity.getResources().getString(R.string.common_description_error),
//                    Color.BLACK);
//        }
//        if (mFailureDialog != null) {
//            mFailureDialog.show(getFragmentManager(), DIALOG_FAILURE);
//        }
//    }

    protected void showErrorDialog(String title, String description) {
        Activity activity = getActivity();
        NoticeDialogFragment errorDialog;

        errorDialog = NoticeDialogFragment.newInstance(
                    DIALOG_ERROR,
                    title,
                    activity.getResources().getColor(R.color.dialog_failure),
                    activity.getResources().getString(R.string.ion_android_warning),
                    activity.getResources().getColor(R.color.dialog_failure),
                    description,
                    Color.BLACK);
        errorDialog.setContent(description);
        errorDialog.show(getFragmentManager(), DIALOG_ERROR);
    }

//    protected void showSuccessDialog(String title, String description) {
//        Activity activity = getActivity();
//        if (mSuccessDialog == null && activity != null) {
//            mSuccessDialog = NoticeDialogFragment.newInstance(DIALOG_SUCCESS,
//                    title,
//                    activity.getResources().getColor(R.color.dialog_success),
//                    activity.getResources().getString(R.string.ion_android_checkmark_circle),
//                    activity.getResources().getColor(R.color.dialog_success),
//                    description,
//                    Color.BLACK);
//        }
//        if (mSuccessDialog != null) {
//            mSuccessDialog.setContent(description);
//            mSuccessDialog.show(getFragmentManager(), DIALOG_SUCCESS);
//        }
//    }


//    protected void autoFill(String[] data, TextView[] views) {
//        int size = Math.min(data.length, views.length);
//        for (int i = 0; i < size; i++) {
//            views[i].setText(data[i]);
//        }
//    }

    final Fragment self = this;
    OnBackPressListener mBackPressImpl = new OnBackPressListener() {
        @Override
        public boolean onBackPressed() {
            if (self == null) return false;

            int childCount = self.getChildFragmentManager().getBackStackEntryCount();

            if (childCount == 0) {
                // it has no child Fragment
                // can not handle the onBackPressed task by itself
                return false;

            } else {
                // get the child Fragment
                FragmentManager childFragmentManager = self.getChildFragmentManager();
                OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);

                // propagate onBackPressed method call to the child Fragment
                if (!childFragment.onBackPressed()) {
                    // child Fragment was unable to handle the task
                    // It could happen when the child Fragment is last last leaf of a chain
                    // removing the child Fragment from stack
                    childFragmentManager.popBackStackImmediate();
                }

                // either this Fragment or its child handled the task
                // either way we are successful and done here
                return true;
            }
        }
    };
}
