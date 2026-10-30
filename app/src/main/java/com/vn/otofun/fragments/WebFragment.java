package com.vn.otofun.fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.vn.otofun.BaseActivity;
import com.vn.otofun.BaseFragment;
import com.vn.otofun.R;
import com.vn.otofun.data.Prefs;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.VLog;
import com.vn.otofun.utils.Commons;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.Locale;

/**
 * Created by AnhVT on 11/20/15.
 */
public class WebFragment extends BaseFragment {
    Button mBackButton;
    Button mForwardButton;
    Button mReloadButton;
    Button mStarButton;
    Button mHomeButton;
    CircularProgressView mBusyProgressView;
    WebView mWebView;
    int mTypeTopic = Configs.TYPE_TOPIC_OTHER;
    int mStar = 0;
    int mThreadID = 0;
    String mOriginURL = "";
    private AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        VLog.d("");
        super.onCreate(savedInstanceState);

        Commons.sendTrackerScreen(getActivity(), "Web content");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rightToolView = ((BaseActivity)getActivity()).mNavigationView.findViewById(R.id.actionbar_right_tool_webview);
        rightToolView.setVisibility(View.VISIBLE);
        TextView backButton = (TextView) ((BaseActivity)getActivity()).mNavigationView.findViewById(R.id.actionbar_style1_back_button);
        backButton.setText(R.string.ion_close_round);

        mHomeButton = (Button) rightToolView.findViewById(R.id.actionbar_right_tool_webview_home_button);
        mStarButton = (Button) rightToolView.findViewById(R.id.actionbar_right_tool_webview_star_button);
        mReloadButton = (Button) rightToolView.findViewById(R.id.actionbar_right_tool_webview_reload_button);
        mBackButton = (Button) rightToolView.findViewById(R.id.actionbar_right_tool_webview_back_button);
        mForwardButton = (Button) rightToolView.findViewById(R.id.actionbar_right_tool_webview_forward_button);
        mBusyProgressView = (CircularProgressView) rightToolView.findViewById(R.id.actionbar_right_tool_webview_busy_progress);

        mStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs p = new Prefs(getActivity());
                if (mStar > 0) {
                    mStar = 0;
                    p.removeStarOfThread(mThreadID);
                    mStarButton.setTextColor(getResources().getColor(R.color.text_blur));
                } else {
                    mStar = 1;
                    p.addStarOfThread(mThreadID);
                    mStarButton.setTextColor(getResources().getColor(R.color.text_red));
                }

            }
        });

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTypeTopic == Configs.TYPE_TOPIC_FORUM) {
                    mWebView.loadUrl(Configs.LINK_HOME_OTOFUN);
                } else {
                    mWebView.loadUrl(mOriginURL);
                }
            }
        });

        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.stopLoading();
                mWebView.reload();
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });

        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
            }
        });

        View view = inflater.inflate(R.layout.fragment_web, container, false);
        mWebView = (WebView) view.findViewById(R.id.fragment_web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startBusyLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                stopBusyLoading();

                if (mTypeTopic == Configs.TYPE_TOPIC_FORUM) {
                    Prefs p = new Prefs(getActivity());
                    p.setLastURL(url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                VLog.d("WebView error: " + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? error.getDescription() : ""));
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mAdView = (AdView) view.findViewById(R.id.fragment_web_adview);
        if (mAdView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Configs.k_link)) {
            mOriginURL = (String) bundle.getSerializable(Configs.k_link);
            if (!TextUtils.isEmpty(mOriginURL)) {
                mWebView.loadUrl(mOriginURL);
            }
        }

        if (bundle != null && bundle.containsKey(Configs.k_type)) {
            mTypeTopic = (int) bundle.getSerializable(Configs.k_type);
        }

        if (mTypeTopic == Configs.TYPE_TOPIC_FORUM) {
            mStarButton.setVisibility(View.GONE);
        } else {

            if (bundle != null && bundle.containsKey(Configs.k_thread_id)) {
                mThreadID = (int) bundle.getSerializable(Configs.k_thread_id);

                Prefs p = new Prefs(getActivity());
                mStar = p.getStarOfThread(mThreadID);
                if (mStar > 0) {
                    mStarButton.setTextColor(getResources().getColor(R.color.text_red));
                } else {
                    mStarButton.setTextColor(getResources().getColor(R.color.text_blur));
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        VLog.d("");
        stopBusyLoading();
        mWebView.stopLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        VLog.d("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VLog.d("");
    }

    protected void startBusyLoading() {
        mMainUIHandler.post(new Runnable() {
            public void run() {
                try {
                    mBusyProgressView.startAnimation();
                    mBusyProgressView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                };
            }
        });
    }
    protected void stopBusyLoading() {
        mMainUIHandler.post(new Runnable() {
            public void run() {
                try {
                    mBusyProgressView.stopAnimation();
                    mBusyProgressView.setVisibility(View.INVISIBLE);
                    if (mWebView.canGoBack()) {
                        mBackButton.setTextColor(getResources().getColor(R.color.background_main));
                    } else {
                        mBackButton.setTextColor(getResources().getColor(R.color.text_blur));
                    }
                    if (mWebView.canGoForward()) {
                        mForwardButton.setTextColor(getResources().getColor(R.color.background_main));
                    } else {
                        mForwardButton.setTextColor(getResources().getColor(R.color.text_blur));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                };
            }
        });
    }

}
