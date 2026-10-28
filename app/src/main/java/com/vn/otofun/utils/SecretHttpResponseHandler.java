package com.vn.otofun.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by AnhVT on 6/10/15.
 */
public class SecretHttpResponseHandler extends TextHttpResponseHandler {
    final static String TAG = Configs.TAG_LOG;
//    private String mApiFunction = "";
    private Context mContext = null;

    public SecretHttpResponseHandler(Context context) {
        super(DEFAULT_CHARSET);
        mContext = context;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        VLog.d("response: " + responseString);

        if (responseString == null) {
            onFailure(statusCode, null, null);
            return;
        }

        Object result = new Gson().fromJson(responseString.trim(), LinkedTreeMap.class);

        if (result == null) {
            onFailure(statusCode, null, null);
            return;
        }
//        mApiFunction = MapHelper.getValue((LinkedTreeMap)result, Configs.k_api_func, "");

        long errCode = (long) MapHelper.getValue((LinkedTreeMap)result, Configs.k_err_code, 0D);
        if (errCode != 0) {
            String errDesc = MapHelper.getValue((LinkedTreeMap)result, Configs.k_err_desc, null);
            onFailure((int) errCode, null, errDesc);
        } else {
            onSuccess((int) errCode, result);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        VLog.d("Error code:" + statusCode + " res:" + responseString);

        onFailure(statusCode, null, null);
    }

    public void onSuccess(int statusCode, Object response) {
    }

    public void onFailure(int statusCode, Object response, String errString) {
    }



//    private void showErrorDialog(String errString) {
//        //Bo qua khong thong bao loi mang voi cac api duoi:
//        if (mApiFunction.equals(Configs.api_GetFullQuoteRealtime)) {
//            return;
//        }
//
//        if (mContext != null && (mContext instanceof BaseActivity)) {
//            ((BaseActivity) mContext).showServerErrorDialog(errString);
//        }
//    }


}
