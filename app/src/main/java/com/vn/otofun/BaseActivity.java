package com.vn.otofun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.internal.LinkedTreeMap;
import com.onesignal.OneSignal;
import com.onesignal.shortcutbadger.ShortcutBadger;
import com.vn.otofun.activities.HomeActivity;
import com.vn.otofun.activities.SettingActivity;
import com.vn.otofun.activities.ThreadsActivity;
import com.vn.otofun.activities.WebActivity;
import com.vn.otofun.data.Prefs;
import com.vn.otofun.fragments.dialogs.NoticeDialogFragment;
import com.vn.otofun.utils.Commons;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.Connector;
import com.vn.otofun.utils.MapHelper;
import com.vn.otofun.utils.SecretHttpResponseHandler;
import com.vn.otofun.utils.Utils;
import com.vn.otofun.utils.VLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/*
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
*/
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
//import com.vn.otofun.utils.Connector;

/**
 * Ceated by hung on 6/4/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements OneButtonDialogClickListener, TwoButtonsDialogClickListener, View.OnClickListener {
    public final static int ID_FRAGMENT = R.id.fragment_container;
    NoticeDialogFragment mServerErrorDialog;
    private FirebaseAnalytics mFirebaseAnalytics;

    private InterstitialAd mInterstitialAd = null;
    private Intent mPendingIntent = null;
    private boolean mPendingIsFinish = false;
    private Handler mMainUIHandler = new Handler();

    protected Toolbar mToolBar;
    protected TableLayout mLeftDrawerList;
    protected ListView mRightDrawerList;
    protected DrawerLayout mDrawerLayout;
    protected View mLeftDrawer;
    protected View mRightDrawer;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected TextView mTitleTextView;
//    protected TextView mLeftButton;
    protected TextView mBackButton;

    public View mNavigationView = null;
    protected ProgressDialog mLoadingDialog = null;

    protected String[][] mMenuItemArray;
    protected String mLinkApp = null;
    boolean mInTransition;

    Activity self = this;
    private static BaseActivity singleton;

    public static synchronized BaseActivity getInstance() {
        return singleton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = this;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Xu ly ngon ngu
        Prefs p = new Prefs(this);
        Locale locale = p.getLanguage();
        if (locale != null) {
            Configuration newConfig = getBaseContext().getResources().getConfiguration();
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }

//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            if (bundle.containsKey(Configs.k_is_root_activity)) {
//                mIsRootActivity = (int) bundle.getSerializable(Configs.k_is_root_activity);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(false);
        }

//        if (savedInstanceState != null && savedInstanceState.getInt("SavedInstance") > 0) {
            // Prevent activity restart after another activity crash
//            try {
//                this.finishActivity(0);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(10);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return;

//        }

//        mMenuItemArray = new String[][]{
//                {Configs.LEFT_MENU_NEWS, getResources().getString(R.string.ion_ios_world_outline), getResources().getString(R.string.naviTitleNews)},
//                {Configs.LEFT_MENU_SETTING, getResources().getString(R.string.ion_ios_cog_outline), getResources().getString(R.string.naviTitleSettings)}};

        mMenuItemArray = new String[][]{};

        setContentView(R.layout.activity_base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_main_dark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.background_main_dark));
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        } else {
            overridePendingTransition(0, 0);
        }

        mToolBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(mToolBar);

        setupActionBar();

        setupDrawer();

        disableDrawer();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(ID_FRAGMENT);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(ID_FRAGMENT, fragment).commit();
        }

    }

    @Override
    protected void onResume() {
        VLog.d("");
        super.onResume();

        OtofunApplication myApp = (OtofunApplication) this.getApplication();
        if (myApp.wasInBackground) {

            OneSignal.clearOneSignalNotifications();
//            //Xoa tat ca push notification
//            String ns = Context.NOTIFICATION_SERVICE;
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(ns);
//            notificationManager.cancelAll();
//
//            //Xoa badge count tren icon app
//            ContentValues cv = new ContentValues();
//            cv.put("badgecount", 0);
//            getContentResolver().update(Uri.parse("content://com.sec.badge/apps"), cv, "package=?", new String[] {getPackageName()});


            requestAllConfigFromServer();
            showPushNotification();
            ShortcutBadger.removeCount(this);
        }
        myApp.stopActivityTransitionTimer();
        updateLeftDrawerItems();
    }

    @Override
    protected void onPause() {
        VLog.d("");
        super.onPause();
        ((OtofunApplication) this.getApplication()).startActivityTransitionTimer();
        Connector.cancelRequest(this);
        hideLoadingDialog();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SavedInstance", 1);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    protected void onDestroy() {
        VLog.d("");
        super.onDestroy();
        mMainUIHandler.removeCallbacksAndMessages(null);
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
            mDrawerLayout.closeDrawer(mLeftDrawer);
        } else if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
            mDrawerLayout.closeDrawer(mRightDrawer);
        } else if (!mInTransition) {
            if (isTaskRoot()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.labelWarning));
                builder.setMessage(getResources().getString(R.string.msgConfirmExitApp));
                builder.setCancelable(true);

                builder.setPositiveButton(getResources().getString(R.string.buttonOK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        performBackActivity();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.buttonCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else {
                performBackActivity();
            }
        }
    }

    public void performBackActivity() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(ID_FRAGMENT);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                && (fragment instanceof TransitionTemplate)
                && ((TransitionTemplate) fragment).exitTransition(new Runnable() {
            @Override
            public void run() {
                mInTransition = false;
                BaseActivity.super.onBackPressed();
                overridePendingTransition(0, 0);
            }
        })) {
            mInTransition = true;
        } else {
            super.onBackPressed();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            } else {
                overridePendingTransition(0, 0);
            }
        }

    }

    @Override
    public void onClickPositiveButton(String tag, String userString) {
    }

    @Override
    public void onClickNegativeButton(String tag, String userString) {

    }

    @Override
    public void onClickDialogButton(String tag) {
    }

    public void showServerErrorDialog(int statusCode, String errString) {
        String err = this.getResources().getString(R.string.msgErrorServerOrNetwork);
        if (errString != null) {
            err = errString;
        }
        err = err + "\nCode: " + statusCode;

        if (mServerErrorDialog != null && mServerErrorDialog.isVisible()) {
            mServerErrorDialog.dismiss();
        }

        mServerErrorDialog = NoticeDialogFragment.newInstance(
            "DIALOG_SERVER_ERROR",
            this.getResources().getString(R.string.labelWarning),
            this.getResources().getColor(R.color.dialog_failure),
            this.getResources().getString(R.string.ion_android_warning),
            this.getResources().getColor(R.color.dialog_failure),
            err,
            Color.BLACK);

        mServerErrorDialog.show(getSupportFragmentManager(), "DIALOG_SERVER_ERROR");
    }

    protected void disableDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void setupActionBar(final String title, int textColor, int backgroundColor) {
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(backgroundColor));
        mNavigationView = actionBar.getCustomView();
        mNavigationView.setBackgroundColor(getResources().getColor(R.color.background_main_dark));

        mTitleTextView = (TextView) mNavigationView.findViewById(R.id.actionbar_style1_title_textView);
//        mLeftButton = (TextView) mNavigationView.findViewById(R.id.actionbar_style1_left_button);
        mBackButton = (TextView) mNavigationView.findViewById(R.id.actionbar_style1_back_button);


//        updateStateActionBar();
//        mLeftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDrawerLayout.openDrawer(mLeftDrawer);
//            }
//        });

        mTitleTextView.setText(title);

        //Cho nay phai dat delay vi isTaskRoot khong dung o tren android 4.x
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            checkShowBackButton();
//        } else {
//            mBackButton.setVisibility(View.GONE);
//            mMainUIHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    checkShowBackButton();
//                }
//            }, 500);
//        }
    }

    private void checkShowBackButton() {
        if (this.isTaskRoot()) {
            mBackButton.setVisibility(View.GONE);
        } else {
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    protected abstract Fragment createFragment();

    protected void onDrawerOpened(View drawerView) {
    }

    private void setupActionBar() {
        int resId = R.layout.actionbar_style1;
        if (resId > 0) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(resId, null);
            actionBar.setCustomView(view);

        }
    }

    private void setupDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = findViewById(R.id.left_drawer);
        mRightDrawer = findViewById(R.id.right_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mRightDrawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                BaseActivity.this.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mLeftDrawerList = (TableLayout) findViewById(R.id.left_drawer_listView);
        mRightDrawerList = (ListView) findViewById(R.id.right_drawer_listView);
//        mDrawerProgress = (ProgressView) findViewById(R.id.right_drawer_progressView);
//        mDrawerEmptyTextView = (TextView) findViewById(R.id.right_drawer_empty_textView);
//        final Activity self = this;

    }

    private void updateLeftDrawerItems() {
        mLeftDrawerList.removeAllViews();
        for (int i = 0; i < mMenuItemArray.length; i++) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.drawer_item, null, false);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View rowView = row.findViewById(R.id.drawer_item_row_view);
            TextView iconView = (TextView) row.findViewById(R.id.drawer_item_row_icon);
            TextView titleView = (TextView) row.findViewById(R.id.drawer_item_row_text);
            iconView.setText(mMenuItemArray[i][1]);
            titleView.setText(mMenuItemArray[i][2]);
            rowView.setTag(i);
            rowView.setOnClickListener(this);
            mLeftDrawerList.addView(row);

        }
    }


    private void requestAllConfigFromServer() {
        VLog.d("");

        showLoadingDialog("");
        final HashMap<String, String> params = new HashMap<>();
        params.put(Configs.k_api_func, Configs.api_GetAllConfig);

        Connector.post(this, params, new SecretHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Object result, String errString) {
                hideLoadingDialog();
                showServerErrorDialog(statusCode, errString);
                Commons.sendGAEvent(self, Configs.GA_EVENT_NETWORK_ERROR, (String) params.get(Configs.k_api_func), Utils.getDeviceName() + "_" + statusCode, 0);

            }
            @Override
            public void onSuccess(int statusCode, Object result) {
                hideLoadingDialog();

                final LinkedTreeMap dataMap = (LinkedTreeMap) ((LinkedTreeMap) result).get(Configs.k_data);

                if (dataMap == null) {
                    showServerErrorDialog(statusCode, null);
                    return;
                }

                Prefs p = new Prefs(self);
                p.setAllConfig(dataMap);

                checkVersionApp(dataMap);
            }
        });
    }


    public void checkVersionApp(LinkedTreeMap dataMap) {
        final LinkedTreeMap androidMap = (LinkedTreeMap) dataMap.get(Configs.k_update_app_android);

        int type = MapHelper.getValue(androidMap, Configs.k_type, 1);
        String version = MapHelper.getValue(androidMap, Configs.k_version, "0.0.0");
        String message = MapHelper.getValue(androidMap, Configs.k_message, "");
        mLinkApp = MapHelper.getValue(androidMap, Configs.k_link_install, null);
        String show_time = MapHelper.getValue(androidMap, Configs.k_show_time, null);


        int versionCodeServer = 0, versionCodeApp = 0;
        try {
            versionCodeServer = Integer.parseInt(version.trim().replaceAll("\\s+", "").replaceAll("\\.", ""));
            versionCodeApp = Integer.parseInt(BuildConfig.VERSION_NAME.trim().replaceAll("\\s+", "").replaceAll("\\.", ""));
        } catch (Exception e) {
            e.printStackTrace();
//            mIsConnectFree = true;
        }
        VLog.d("check version app:" + versionCodeApp + "  server:" + versionCodeServer);

        if (versionCodeServer > versionCodeApp) {
            if (type == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.labelWarning));
                builder.setMessage(message);
                builder.setCancelable(false);

                builder.setPositiveButton(getResources().getString(R.string.buttonUpdate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigateToUpdateSite();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.buttonLater), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return;
            } else if (type == 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.labelMessage));
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton(getResources().getString(R.string.buttonUpdate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigateToUpdateSite();
                    }
                });
//                builder.setNegativeButton(null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return;
            }
        }
    }


    private void navigateToUpdateSite() {
//        String url = GlobalSettings.access().get("APP_VERSION").get("ANDROID").get("URL").export2String();

//        final String appPackageName = "vn.otofun"; //getPackageName(); // getPackageName() from Context or Activity object
        if (mLinkApp != null) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mLinkApp)));
            } catch (Exception e){
            }
        } else {

        }
    }

    @Override
    public void onClick(View view) {
        String itemName = mMenuItemArray[(int) view.getTag()][0];
        mDrawerLayout.closeDrawer(mLeftDrawer);
        if (itemName.equals(Configs.LEFT_MENU_NEWS)) {
            if (!(self instanceof ThreadsActivity)) {
                Intent intent = new Intent(this, ThreadsActivity.class);
                openNewActivity(intent, true);

            }
        } else if (itemName.equals(Configs.LEFT_MENU_SETTING)) {
            if (!(self instanceof SettingActivity)) {
                Intent intent = new Intent(this, SettingActivity.class);
                openNewActivity(intent, true);
            }
        }
    }

    public void openNewActivity(Intent intent, boolean isFinish) {
        //Mo activity 2 kieu khac nhau de chong bug thoat app dong may 4.x
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (isFinish) {
                TaskStackBuilder.create(self)
                        .addNextIntent(intent)
                        .startActivities();
            } else {
                startActivity(intent);
            }
        } else {
            startActivity(intent);
            if (isFinish) {
                finish();
            }
        }
    }

    public void showLoadingDialog(final String content) {
        VLog.d("");
        final Context context = this;
        mMainUIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog == null) {
                    if (content.length() > 0) {
                        mLoadingDialog = ProgressDialog.show(context, "", content, true, false);
                    } else {
                        mLoadingDialog = ProgressDialog.show(context, "", "đang tải dữ liệu...", true, false);
                    }
                }
                mLoadingDialog.show();
            }
        });
    }

    public void hideLoadingDialog() {
        VLog.d("");
        mMainUIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.hide();
                }
            }
        });
    }

    public void initGoogleAdmob() {
        try {
            Prefs p = new Prefs(this);
            String activeDaysString = p.getConfigByKey(Configs.k_ad_active_day);
            if (activeDaysString != null) {
                long activeDays = (long) Float.parseFloat(activeDaysString);
                VLog.d("active day: " + activeDays);
                if (activeDays <= 0) {
                    if (mInterstitialAd == null) {
                        VLog.d("Khoi tao google admob");
                        requestNewInterstitial();
                    }
                } else {
                    mInterstitialAd = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showGoogleAdmob(final Intent intent, final boolean isFinish) {
        VLog.d("");
        if (mInterstitialAd != null) {
            VLog.d("show admob ok");
            mPendingIntent = intent;
            mPendingIsFinish = isFinish;
            mInterstitialAd.show(this);
        } else {
            openNewActivity(intent, isFinish);
        }
    }

    private void requestNewInterstitial() {
        VLog.d("");
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-4411026643591265/3422102833", adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    VLog.d("AdMob Interstitial Loaded");
                    mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            VLog.d("AdMob Interstitial Dismissed");
                            mInterstitialAd = null;
                            if (mPendingIntent != null) {
                                openNewActivity(mPendingIntent, mPendingIsFinish);
                                mPendingIntent = null;
                            }
                            requestNewInterstitial();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            VLog.d("AdMob Interstitial Failed to Show: " + adError.getMessage());
                            mInterstitialAd = null;
                            if (mPendingIntent != null) {
                                openNewActivity(mPendingIntent, mPendingIsFinish);
                                mPendingIntent = null;
                            }
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    VLog.d("AdMob Interstitial Failed to Load: " + loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });
    }

    public void showPushNotification() {
        JSONObject pushNotifyObject = OtofunApplication.getPushNotifyObject();
        //Kiem tra neu la mo app tu viec bam vao push notification thi show Popup Push
        if (pushNotifyObject != null) {
            VLog.d("");
            int threadID = 0;
            String message = "";
            try {
                String newsIDString = pushNotifyObject.getString("news_id");
                message = pushNotifyObject.getString("message");
                threadID = Integer.parseInt(newsIDString);
            } catch (Exception e) {
            }

            OtofunApplication.resetPushNotifyObject();
            final int threadIDID = threadID;
            VLog.d("Open push from top activity with id:" + threadID);

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Bài viết mới");
            b.setMessage(message);
            b.setCancelable(false);
            if (threadIDID > 0) {
                b.setPositiveButton(R.string.buttonOpen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (threadIDID > 0) {
                            String url = Configs.LINK_OPEN_THREAD + threadIDID;

                            Intent intent = new Intent(self, WebActivity.class);
                            Bundle args = new Bundle();

                            args.putSerializable(Configs.k_link, url);
                            args.putSerializable(Configs.k_thread_id, threadIDID);
                            intent.putExtras(args);

                            Intent homeIntent = new Intent(self, HomeActivity.class);

                            TaskStackBuilder.create(self)
                                    .addNextIntent(homeIntent)
                                    .addNextIntent(intent)
                                    .startActivities();
                        }
                    }
                });
                b.setNegativeButton(R.string.buttonCancel, null);
            } else {
                b.setNegativeButton(R.string.buttonClose, null);
            }
            b.create().show();

        }
    }

}