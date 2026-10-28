package com.vn.otofun.utils;

import android.content.Context;

import com.google.gson.Gson;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AnhVT on 6/4/15.
 */
public class Connector {
//    final static String TAG = "Connector";
//    final static ArrayList<String> normalFunctions = new ArrayList<>();
    static AsyncHttpClient client = new AsyncHttpClient();
//    static String basicAuthUsername = null, basicAuthPassword = null;

//    public static void auth(String basicUser, String basicPassword) {
//        client.setBasicAuth(basicUser, basicPassword);
//    }

//    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        VLog.d("GET: " + url);
//        VLog.d("Request: " + params);
////        setupHeaders();
//        client.get(context, url, params, responseHandler);
//    }

    public static void post(Context context, HashMap params, AsyncHttpResponseHandler responseHandler) {
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

        if (bodyString == null) {
            VLog.d("Error bodyString null");
            return;
        }

        if (bodyString.length() <= 0) {
            VLog.d("Error bodyString empty");
            return;
        }


        VLog.d("Request:" + bodyString);
        StringEntity bodyEntity = null;
        try {
//            String bodyEncryptString = Commons.encrypt(bodyString, );
            String bodyEncryptString = AES.encryptWithDefPwd(bodyString);
//            VLog.d("bodyEncryptString:" + bodyEncryptString);

            if (bodyEncryptString == null) {
                VLog.d("Error bodyEncryptString null");
                return;
            }

            bodyEntity = new StringEntity(bodyEncryptString);
        } catch (Exception je) {
            VLog.d("Exception 2");
        }
        if (bodyEntity == null) {
            VLog.d("Error bodyEntity empty");
            return;
        }

        client.post(context, Commons.API_ROOT_NEW, bodyEntity, "application/json", responseHandler);
    }

    public static AsyncHttpClient client() {
        return client;
    }

    public static void cancelAllRequests() {
        client.cancelAllRequests(true);
    }

    public static void cancelRequest(Context context) {
        client.cancelRequests(context, true);
    }

//    private static void setupHeaders() {
//        client.addHeader("x-api-version","1.0.0");
//        client.addHeader("Content-Type","application/x-www-form-urlencoded");
//    }

//    private static void setupBasicAuth() {
//        if (basicAuthUsername == null && basicAuthPassword == null) {
//            try {
//                basicAuthUsername = Commons.encrypt(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD);
//                basicAuthPassword = Commons.encrypt(BuildConfig.BASIC_AUTH_PASSWORD, BuildConfig.BASIC_AUTH_PASSWORD);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            client.setBasicAuth(basicAuthUsername, basicAuthPassword);
//            client.setTimeout(60000);
//        }
//    }
}
