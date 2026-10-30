package com.vn.otofun;

import android.app.Application;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import com.onesignal.OneSignal;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.Connector;
import com.vn.otofun.utils.SecretHttpResponseHandler;
import com.vn.otofun.utils.VLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.analytics.Tracker;

/**
 * Created by AnhVT on 6/24/15.
 */
public class OtofunApplication extends Application {
//    private static OtofunApplication singleton;
    private static JSONObject pushNotifyObject;

    Map<String, Fragment.SavedState> savedStateMap;
    final HashMap<String, Object> data = new HashMap<>();
    final long MAX_ACTIVITY_TRANSITION_TIME_MS = 10000;

    Timer mActivityTransitionTimer;
    TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground = true;

    @Override
    public void onCreate() {
        super.onCreate();

//        savedStateMap = new HashMap<String, Fragment.SavedState>();
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        Configs.IS_FIRST_USAGE = preferences.getBoolean("IS_FIRST_USAGE", false);

        pushNotifyObject = null;

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new pushNotificationOpenedHandler())
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                VLog.d("User:" + userId);
                if (registrationId != null)
                    VLog.d("registrationId:" + registrationId);

                requestUpdateOnesignalToken(userId, registrationId);
            }
        });

        // AnalyticsTrackers.initialize(this);
        // Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        // tracker.setClientId(Utils.getUUID(getApplicationContext()));

    }

//    public void setFragmentSavedState(String key, Fragment.SavedState state) {
//        savedStateMap.put(key, state);
//    }
//
//    public Fragment.SavedState getFragmentSavedState(String key) {
//        return savedStateMap.get(key);
//    }
//
//    public void putData(String key, Object value) {
//        data.put(key, value);
//    }
//
//    public Object getData(String key) {
//        return data.get(key);
//    }

    public void setWasInBackground(boolean isSet) {
        wasInBackground = isSet;
    }

    public void startActivityTransitionTimer() {
        mActivityTransitionTimer = new Timer();
        mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                wasInBackground = true;
                VLog.d("wasInBackground = "+ wasInBackground);

            }
        };

        mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (mActivityTransitionTimerTask != null) {
            mActivityTransitionTimerTask.cancel();
        }

        if (mActivityTransitionTimer != null) {
            mActivityTransitionTimer.cancel();
        }

        wasInBackground = false;
        VLog.d("wasInBackground = "+ wasInBackground);
    }

//    public static synchronized OtofunApplication getInstance() {
//        return singleton;
//    }

    public static synchronized JSONObject getPushNotifyObject() {
        return pushNotifyObject;
    }
    public static void resetPushNotifyObject() {
        pushNotifyObject = null;
    }

    private class pushNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                int newsID = 0;
                if (additionalData != null) {
                    String deeplink = null;

                    try {
                        deeplink = additionalData.getString("deeplink");
                    } catch (Exception e) {
                    }

                    //Phan tich lay ra ID cua News
                    if (deeplink != null) {
                        String[] paramArray = deeplink.split("/");
                        if (paramArray.length >= 2) {
                            newsID = Integer.parseInt(paramArray[paramArray.length - 1]);
                        }
                    }
                }

                VLog.d("OpenLink to push with id: " + newsID);
                pushNotifyObject = new JSONObject();
                pushNotifyObject.put("news_id", String.valueOf(newsID));
                pushNotifyObject.put("message", message);

                Handler mainUIHandler = new Handler();
                mainUIHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BaseActivity baseActivity = BaseActivity.getInstance();
                            if (baseActivity != null) {
                                baseActivity.showPushNotification();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        };
                    }
                }, 1000);


            } catch (Throwable t) {
                t.printStackTrace();
            }

            // The following can be used to open an Activity of your choice.
      /*
      Intent intent = new Intent(getApplication(), YourActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      */
            // Follow the instructions in the link below to prevent the launcher Activity from starting.
            // https://documentation.onesignal.com/docs/android-notification-customizations#changing-the-open-action-of-a-notification
        }
    }

    public void requestUpdateOnesignalToken(String onesignalID, String pushID) {
        if (onesignalID == null) {
            return;
        }
        if (onesignalID.length() <= 0) {
            return;
        }
        final HashMap<String, String> params = new HashMap<>();
        params.put(Configs.k_api_func, Configs.api_UpdateOnesignalToken);
        params.put(Configs.k_onesignal_id, onesignalID);

        if (pushID != null) {
            params.put(Configs.k_push_id, pushID);
        }


        Connector.post(this, params, new SecretHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Object result, String errString) {

            }
            @Override
            public void onSuccess(int statusCode, Object result) {
            }
        });

    }


//    public synchronized Tracker getGoogleAnalyticsTracker() {
//        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
//        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
//    }

}
