package com.vn.otofun.views;

import android.content.Context;

/**
 * Created by AnhVT on 7/27/15.
 */
public class MenuItem {
    String mIcon;
    String mTitle;

    public MenuItem(String icon, String title) {
        mIcon = icon;
        mTitle = title;
    }

    public MenuItem(Context context, int iconId, int titleId) {
        this(context.getResources().getString(iconId), context.getResources().getString(titleId));
    }

    public String getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return mTitle;
    }

}
