package com.vn.otofun.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by AnhVT on 6/10/15.
 */
public abstract class SecretHttpResponseHandler {
    final static String TAG = Configs.TAG_LOG;
    private Context mContext = null;

    public SecretHttpResponseHandler(Context context) {
        mContext = context;
    }

    public void handleSuccess(int statusCode, String responseString) {
        VLog.d("response: " + responseString);

        if (responseString == null) {
            handleFailure(statusCode, null, "Response is null");
            return;
        }

        Object result;
        try {
            result = new Gson().fromJson(responseString.trim(), LinkedTreeMap.class);
        } catch (Exception e) {
            handleFailure(statusCode, null, "JSON parse error");
            return;
        }

        if (result == null) {
            handleFailure(statusCode, null, "Result is null");
            return;
        }

        long errCode = (long) MapHelper.getValue((LinkedTreeMap)result, Configs.k_err_code, 0D);
        if (errCode != 0) {
            String errDesc = MapHelper.getValue((LinkedTreeMap)result, Configs.k_err_desc, null);
            onFailure((int) errCode, null, errDesc);
        } else {
            onSuccess((int) errCode, result);
        }
    }

    public void handleFailure(int statusCode, Object response, String errString) {
        VLog.d("Error code:" + statusCode + " res:" + errString);
        onFailure(statusCode, response, errString);
    }

    public void onSuccess(int statusCode, Object response) {
    }

    public void onFailure(int statusCode, Object response, String errString) {
    }
}
