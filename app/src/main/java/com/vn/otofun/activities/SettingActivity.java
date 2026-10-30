package com.vn.otofun.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Window;

import com.vn.otofun.BaseActivity;
import com.vn.otofun.R;
import com.vn.otofun.fragments.SettingFragment;

/**
 * Created by DucChinh on 7/3/2015.
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

//        Commons.changeStatusBarColor(this, getResources().getColor(R.color.status_bar_blue));
        setupActionBar(getString(R.string.naviTitleSettings),
                getResources().getColor(R.color.text_bright),
                getResources().getColor(R.color.status_bar_blue));
    }

    @Override
    protected Fragment createFragment() {
        return new SettingFragment();
    }
}