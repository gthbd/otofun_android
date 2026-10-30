package com.vn.otofun.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.vn.otofun.BaseActivity;
import com.vn.otofun.R;
import com.vn.otofun.fragments.WebFragment;

/**
 * Created by AnhVT on 11/20/15.
 */
public class WebActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar("",
                getResources().getColor(R.color.text_bright),
                getResources().getColor(R.color.status_bar_blue));
    }

    @Override
    protected Fragment createFragment() {
        return new WebFragment();
    }

}
