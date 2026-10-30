package com.vn.otofun.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Window;

import com.vn.otofun.BaseActivity;
import com.vn.otofun.R;
import com.vn.otofun.fragments.ThreadsFragment;

public class ThreadsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setupActionBar("",
				getResources().getColor(R.color.text_normal),
				getResources().getColor(R.color.background_main));

//		initGoogleAdmob();

	}


	@Override
	protected Fragment createFragment() {
		return new ThreadsFragment();
	}
}
