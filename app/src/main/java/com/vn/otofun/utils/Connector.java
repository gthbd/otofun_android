package com.vn.otofun.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by AnhVT on 6/4/15.
 */
public class Connector {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void post(Context context, HashMap params, final SecretHttpResponseHandler responseHandler) {
        VLog.d("POST Func: " + params.get(Configs.k_api_func));

        HashMap defaultParams = Commons.getDefaultAPIParams(context);
        String bodyString = "";

        try {
            HashMap allParamMap = new HashMap();
            allParamMap.putAll(params);
            allParamMap.putAll(defaultParams);

            Gson gson = new Gson();
            JSONObject dataJSON = new JSONObject(gson.toJson(allParamMap));
            bodyString = dataJSON.toString().trim();

        } catch (JSONException je) {
            VLog.d("Exception 1");
        }

        if (bodyString == null || bodyString.length() <= 0) {
            VLog.d("Error bodyString empty/null");
            if (responseHandler != null) {
                mainHandler.post(() -> responseHandler.handleFailure(0, null, "Invalid request body"));
            }
            return;
        }

        VLog.d("Request:" + bodyString);
        String bodyEncryptString;
        try {
            bodyEncryptString = AES.encryptWithDefPwd(bodyString);

            if (bodyEncryptString == null) {
                VLog.d("Error bodyEncryptString null");
                if (responseHandler != null) {
                    mainHandler.post(() -> responseHandler.handleFailure(0, null, "Encryption error"));
                }
                return;
            }
        } catch (Exception je) {
            VLog.d("Exception 2");
            if (responseHandler != null) {
                mainHandler.post(() -> responseHandler.handleFailure(0, null, "Encryption exception"));
            }
            return;
        }

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(bodyEncryptString, MEDIA_TYPE_JSON);

        Request request = new Request.Builder()
                .url(Commons.API_ROOT_NEW)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                VLog.d("OkHttp onFailure: " + e.getMessage());
                if (responseHandler != null) {
                    mainHandler.post(() -> responseHandler.handleFailure(-1, null, e.getMessage()));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int code = response.code();
                final String responseBody = response.body() != null ? response.body().string() : "";
                if (response.isSuccessful()) {
                    if (responseHandler != null) {
                        mainHandler.post(() -> responseHandler.handleSuccess(code, responseBody));
                    }
                } else {
                    if (responseHandler != null) {
                        mainHandler.post(() -> responseHandler.handleFailure(code, null, "HTTP Error " + code));
                    }
                }
            }
        });
    }

    public static void cancelAllRequests() {
    }

    public static void cancelRequest(Context context) {
    }
}
