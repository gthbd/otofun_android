package com.vn.otofun;

import android.content.Context;

public final class AnalyticsTrackers {
    public enum Target {
        APP
    }
    private static AnalyticsTrackers sInstance;
    public static synchronized void initialize(Context context) {
        if (sInstance == null) {
            sInstance = new AnalyticsTrackers(context);
        }
    }
    public static synchronized AnalyticsTrackers getInstance() {
        if (sInstance == null) {
            sInstance = new AnalyticsTrackers(null);
        }
        return sInstance;
    }
    private AnalyticsTrackers(Context context) {}
    public Object get(Target target) {
        return null;
    }
}
