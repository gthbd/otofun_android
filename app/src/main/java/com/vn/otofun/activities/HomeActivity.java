package com.vn.otofun.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

import com.vn.otofun.BaseActivity;
import com.vn.otofun.R;
import com.vn.otofun.fragments.HomeFragment;

public class HomeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setupActionBar(getResources().getString(R.string.naviTitleHome),
				getResources().getColor(R.color.text_normal),
				getResources().getColor(R.color.background_main));

		initGoogleAdmob();

	}


	@Override
	protected Fragment createFragment() {
		return new HomeFragment();
	}
}
