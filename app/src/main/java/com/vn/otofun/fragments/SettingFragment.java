package com.vn.otofun.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.vn.otofun.BaseActivity;
import com.vn.otofun.BaseFragment;
import com.vn.otofun.OtofunApplication;
import com.vn.otofun.R;
import com.vn.otofun.activities.SettingActivity;
import com.vn.otofun.data.Prefs;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.Connector;
import com.vn.otofun.utils.MapHelper;
import com.vn.otofun.utils.SecretHttpResponseHandler;
import com.vn.otofun.utils.Utils;
import com.vn.otofun.utils.VLog;
import com.vn.otofun.utils.Commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import com.vn.otofun.BuildConfig;

/**
 * Created by AnhVT on 11/20/15.
 */
public class SettingFragment extends BaseFragment {
    View mEnableView;
    View mMuaxebanxeView;
    View mDodientuView;
    View mCanbanView;
    View mCanmuaView;
    View mCanthueView;
    View mChothueView;

    TextView mEnableIconView;
    TextView mMuaxebanxeIconView;
    TextView mDodientuIconView;

    TextView mCanbanIconView;
    TextView mCanmuaIconView;
    TextView mCanthueIconView;
    TextView mChothueIconView;

    TextView mPhoneTextView;
    TextView mEmailTextView;

    Button mUpdateButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        VLog.d("");
        super.onCreate(savedInstanceState);
        Commons.sendTrackerScreen(getActivity(), "Setting");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        VLog.d("");
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mPhoneTextView = (TextView) view.findViewById(R.id.fragment_setting_phone_textview);
        mEmailTextView = (TextView) view.findViewById(R.id.fragment_setting_email_textview);

        Prefs p = new Prefs(getActivity());

        mPhoneTextView.setText("Phone: " + p.getConfigByKey(Configs.k_support_mobile_num));
        mEmailTextView.setText("Email: " + p.getConfigByKey(Configs.k_support_email));


        mUpdateButton = (Button) view.findViewById(R.id.fragment_setting_update_button);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePushOptionToServer();
            }
        });


        //===========================================================================================
        mEnableView = (View) view.findViewById(R.id.fragment_setting_enable_view);
        mEnableIconView = (TextView) mEnableView.findViewById(R.id.icon_textview);
        mEnableIconView.setTag(0);

        TextView enableTitleView = (TextView) mEnableView.findViewById(R.id.title_textview);
        enableTitleView.setText("Bật/tắt tất cả các thông báo");

        mEnableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mEnableIconView);
            }
        });

        //===========================================================================================
        mMuaxebanxeView = (View) view.findViewById(R.id.fragment_setting_muaxebanxe_view);
        mMuaxebanxeIconView = (TextView) mMuaxebanxeView.findViewById(R.id.icon_textview);
        mMuaxebanxeIconView.setTag(0);

        TextView muaxebanxeTitleView = (TextView) mMuaxebanxeView.findViewById(R.id.title_textview);
        muaxebanxeTitleView.setText("Mua xe bán xế");

        mMuaxebanxeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mMuaxebanxeIconView);
            }
        });

        //===========================================================================================
        mDodientuView = (View) view.findViewById(R.id.fragment_setting_dodientu_view);
        mDodientuIconView = (TextView) mDodientuView.findViewById(R.id.icon_textview);
        mDodientuIconView.setTag(0);

        TextView dodientuTitleView = (TextView) mDodientuView.findViewById(R.id.title_textview);
        dodientuTitleView.setText("Đồ điện tử");

        mDodientuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mDodientuIconView);
            }
        });

        //===========================================================================================
        mCanmuaView = (View) view.findViewById(R.id.fragment_setting_canmua_view);
        mCanmuaIconView = (TextView) mCanmuaView.findViewById(R.id.icon_textview);
        mCanmuaIconView.setTag(0);

        TextView canmuaTitleView = (TextView) mCanmuaView.findViewById(R.id.title_textview);
        canmuaTitleView.setText("Cần mua");

        mCanmuaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mCanmuaIconView);
            }
        });

        //===========================================================================================
        mCanbanView = (View) view.findViewById(R.id.fragment_setting_canban_view);
        mCanbanIconView = (TextView) mCanbanView.findViewById(R.id.icon_textview);
        mCanbanIconView.setTag(0);

        TextView canbanTitleView = (TextView) mCanbanView.findViewById(R.id.title_textview);
        canbanTitleView.setText("Cần bán");

        mCanbanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mCanbanIconView);
            }
        });

        //===========================================================================================
        mCanthueView = (View) view.findViewById(R.id.fragment_setting_canthue_view);
        mCanthueIconView = (TextView) mCanthueView.findViewById(R.id.icon_textview);
        mCanthueIconView.setTag(0);

        TextView canthueTitleView = (TextView) mCanthueView.findViewById(R.id.title_textview);
        canthueTitleView.setText("Cần thuê");

        mCanthueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mCanthueIconView);
            }
        });

        //===========================================================================================
        mChothueView = (View) view.findViewById(R.id.fragment_setting_chothue_view);
        mChothueIconView = (TextView) mChothueView.findViewById(R.id.icon_textview);
        mChothueIconView.setTag(0);

        TextView chothueTitleView = (TextView) mChothueView.findViewById(R.id.title_textview);
        chothueTitleView.setText("Cho thuê");

        mChothueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectTextView(mChothueIconView);
            }
        });


        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        VLog.d("");
        super.onResume();
        requestPushOptionFromServer();

    }

    @Override
    public void onPause() {
        super.onPause();
        VLog.d("");
    }

    void changeSelectTextView(TextView iconTextView) {
        if (iconTextView != mEnableIconView) {
            if ((int) mEnableIconView.getTag() == 0) {
                return;
            }
        }

        if ((int) iconTextView.getTag() > 0) {
            setSelectTextView(iconTextView, 0);

        } else {
            setSelectTextView(iconTextView, 1);
        }
    }

    void setSelectTextView(TextView iconTextView, int value) {
        iconTextView.setTag(value);
        if ((int) iconTextView.getTag() == 0) {
            iconTextView.setText(R.string.ion_android_checkbox_outline_blank);

            if (iconTextView == mEnableIconView) {
                float opacity = 0.3F;
                mMuaxebanxeView.setAlpha(opacity);
                mDodientuView.setAlpha(opacity);
                mCanmuaView.setAlpha(opacity);
                mCanbanView.setAlpha(opacity);
                mCanthueView.setAlpha(opacity);
                mChothueView.setAlpha(opacity);
            }
        } else {
            iconTextView.setText(R.string.ion_android_checkbox_outline);

            if (iconTextView == mEnableIconView) {
                mMuaxebanxeView.setAlpha(1.0F);
                mDodientuView.setAlpha(1.0F);
                mCanmuaView.setAlpha(1.0F);
                mCanbanView.setAlpha(1.0F);
                mCanthueView.setAlpha(1.0F);
                mChothueView.setAlpha(1.0F);
            }
        }
    }


    private void requestPushOptionFromServer() {
        VLog.d("");

        ((BaseActivity)getActivity()).showLoadingDialog("");
        final HashMap<String, String> params = new HashMap<>();
        params.put(Configs.k_api_func, Configs.api_GetPushOption);

        Connector.post(getActivity(), params, new SecretHttpResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Object result, String errString) {
                ((BaseActivity)getActivity()).hideLoadingDialog();
                ((BaseActivity)getActivity()).showServerErrorDialog(statusCode, errString);
                Commons.sendGAEvent(getActivity(), Configs.GA_EVENT_NETWORK_ERROR, (String) params.get(Configs.k_api_func), Utils.getDeviceName() + "_" + statusCode, 0);

            }
            @Override
            public void onSuccess(int statusCode, Object result) {
                ((BaseActivity)getActivity()).hideLoadingDialog();

                final LinkedTreeMap dataMap = (LinkedTreeMap) ((LinkedTreeMap) result).get(Configs.k_data);
                VLog.d(dataMap.toString());

                setSelectTextView (mEnableIconView, MapHelper.getValue(dataMap, Configs.k_enable, 0));
                setSelectTextView (mMuaxebanxeIconView, MapHelper.getValue(dataMap, Configs.k_topic_88, 0));
                setSelectTextView (mDodientuIconView, MapHelper.getValue(dataMap, Configs.k_topic_312, 0));
                setSelectTextView (mCanbanIconView, MapHelper.getValue(dataMap, Configs.k_type_canban, 0));
                setSelectTextView (mCanmuaIconView, MapHelper.getValue(dataMap, Configs.k_type_canmua, 0));
                setSelectTextView (mCanthueIconView, MapHelper.getValue(dataMap, Configs.k_type_canthue, 0));
                setSelectTextView (mChothueIconView, MapHelper.getValue(dataMap, Configs.k_type_chothue, 0));
            }
        });
    }

    private void updatePushOptionToServer() {
        VLog.d("");

        ((BaseActivity)getActivity()).showLoadingDialog("");
        final HashMap params = new HashMap<>();
        params.put(Configs.k_api_func, Configs.api_UpdatePushOption);

        params.put(Configs.k_enable, (int) mEnableIconView.getTag());
        params.put(Configs.k_topic_88, (int) mMuaxebanxeIconView.getTag());
        params.put(Configs.k_topic_312, (int) mDodientuIconView.getTag());
        params.put(Configs.k_type_canban, (int) mCanbanIconView.getTag());
        params.put(Configs.k_type_canmua, (int) mCanmuaIconView.getTag());
        params.put(Configs.k_type_canthue, (int) mCanthueIconView.getTag());
        params.put(Configs.k_type_chothue, (int) mChothueIconView.getTag());

        Connector.post(getActivity(), params, new SecretHttpResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Object result, String errString) {
                ((BaseActivity)getActivity()).hideLoadingDialog();
                ((BaseActivity)getActivity()).showServerErrorDialog(statusCode, errString);
                Commons.sendGAEvent(getActivity(), Configs.GA_EVENT_NETWORK_ERROR, (String) params.get(Configs.k_api_func), Utils.getDeviceName() + "_" + statusCode, 0);

            }
            @Override
            public void onSuccess(int statusCode, Object result) {
                ((BaseActivity)getActivity()).hideLoadingDialog();

//                final LinkedTreeMap dataMap = (LinkedTreeMap) ((LinkedTreeMap) result).get(Configs.k_data);
//                VLog.d(dataMap.toString());
                Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }
        });
    }

}
