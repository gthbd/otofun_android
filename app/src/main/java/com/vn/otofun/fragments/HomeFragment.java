package com.vn.otofun.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.vn.otofun.BaseActivity;
import com.vn.otofun.BaseFragment;
import com.vn.otofun.BuildConfig;
import com.vn.otofun.OtofunApplication;
import com.vn.otofun.R;
import com.vn.otofun.activities.ThreadsActivity;
import com.vn.otofun.activities.SettingActivity;
import com.vn.otofun.activities.WebActivity;
import com.vn.otofun.adapters.HomeAdapter;
import com.vn.otofun.data.Prefs;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.MapHelper;
import com.vn.otofun.utils.Utils;
import com.vn.otofun.utils.VLog;
import com.vn.otofun.utils.Connector;
import com.vn.otofun.utils.SecretHttpResponseHandler;
import com.vn.otofun.utils.Commons;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AnhVT on 11/20/15.
 */
public class HomeFragment extends BaseFragment {
    RecyclerView mRecyclerView;
    TextView mVersionTextView;
    ArrayList<LinkedTreeMap> mItemsArray = null;
    HomeAdapter mAdapter = null;
    HomeAdapter.OnItemClickListener mItemRowClickListener;

    int mCount_topic_88 = 0;
    int mCount_topic_312 = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        VLog.d("");
        final View rightToolView = ((BaseActivity)getActivity()).mNavigationView.findViewById(R.id.actionbar_right_tool_threads);
        rightToolView.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mVersionTextView = (TextView) view.findViewById(R.id.fragment_home_version_textview);
        mVersionTextView.setText("Version: " + BuildConfig.VERSION_NAME);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_listView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mItemsArray = new ArrayList<LinkedTreeMap>();

//        LinkedTreeMap itemMuaxebanxeMap = new LinkedTreeMap();
//        itemMuaxebanxeMap.put(Configs.k_id, Configs.HOME_ITEM_MUAXEBANXE);
//        itemMuaxebanxeMap.put(Configs.k_icon, getResources().getString(R.string.ion_model_s));
//        itemMuaxebanxeMap.put(Configs.k_title, "Mua xe bán xế");
//        itemMuaxebanxeMap.put(Configs.k_detail, "Các bài mới nhất trong topic Mua xe bán xế otofun");
//        mItemsArray.add(itemMuaxebanxeMap);
//
//        LinkedTreeMap itemDodientuMap = new LinkedTreeMap();
//        itemDodientuMap.put(Configs.k_id, Configs.HOME_ITEM_DODIENTU);
//        itemDodientuMap.put(Configs.k_icon, getResources().getString(R.string.ion_ipod));
//        itemDodientuMap.put(Configs.k_title, "Đồ điện tử");
//        itemDodientuMap.put(Configs.k_detail, "Các bài mới nhất trong topic Đồ điện tử otofun");
//        mItemsArray.add(itemDodientuMap);
//
//
//        LinkedTreeMap itemOtofunHomeMap = new LinkedTreeMap();
//        itemOtofunHomeMap.put(Configs.k_id, Configs.HOME_ITEM_OTOFUN_HOME);
//        itemOtofunHomeMap.put(Configs.k_icon, getResources().getString(R.string.ion_ios_world_outline));
//        itemOtofunHomeMap.put(Configs.k_title, "Web diễn đàn otofun");
//        itemOtofunHomeMap.put(Configs.k_detail, "Đang xem: " + p.getLastURL());
//        mItemsArray.add(itemOtofunHomeMap);
//
//        LinkedTreeMap itemSettingMap = new LinkedTreeMap();
//        itemSettingMap.put(Configs.k_id, Configs.HOME_ITEM_SETTING);
//        itemSettingMap.put(Configs.k_icon, getResources().getString(R.string.ion_ios_cog));
//        itemSettingMap.put(Configs.k_title, "Cài đặt nhận tin nhanh");
//        itemSettingMap.put(Configs.k_detail, "Tuỳ chỉnh nhận tin ngay khi có bài viết mới, theo cách bạn muốn!");
//        mItemsArray.add(itemSettingMap);
//



//        final Fragment self = this;

        mItemRowClickListener = new HomeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                LinkedTreeMap item = mItemsArray.get(position);
                VLog.d("click item " + position);

                String topic = MapHelper.getValue(item, Configs.k_id, "");
                String topicName = MapHelper.getValue(item, Configs.k_title, "");
//                int oldLastID = (int) MapHelper.getValue(item, Configs.k_badge, 0D);
                if (topic.length() > 0) {

                    int topicID = 0;
                    if (!TextUtils.isEmpty(topic)) {
                        if (topic.equals(Configs.HOME_ITEM_MUAXEBANXE)) {
                            topicID = 88;
                        }
                        if (topic.equals(Configs.HOME_ITEM_DODIENTU)) {
                            topicID = 312;
                        }
                    }

                    if (topicID > 0) {
                        Intent intent = new Intent(getActivity(), ThreadsActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable(Configs.k_topic_id, topicID);
                        args.putSerializable(Configs.k_topic_name, topicName);
//                        args.putSerializable(Configs.k_old_last_id, oldLastID);
                        intent.putExtras(args);

                        ((BaseActivity)getActivity()).showGoogleAdmob(intent, false);

//                        startActivity(intent);
                    } else {
                        if (topic.equals(Configs.HOME_ITEM_OTOFUN_HOME)) {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            Bundle args = new Bundle();

                            Prefs p = new Prefs(getActivity());

                            args.putSerializable(Configs.k_link, p.getLastURL());
                            args.putSerializable(Configs.k_type, Configs.TYPE_TOPIC_FORUM);
                            intent.putExtras(args);

                            startActivity(intent);

                        }
                        if (topic.equals(Configs.HOME_ITEM_SETTING)) {
                            Intent intent = new Intent(getActivity(), SettingActivity.class);
                            startActivity(intent);

                        }
                    }
                }
            }

        };

        mAdapter = new HomeAdapter(getActivity(), mItemsArray, mRecyclerView, mItemRowClickListener);

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        VLog.d("");
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        VLog.d("");
        super.onResume();
        requestNewCountThreadFromServer();

    }

    @Override
    public void onPause() {
        super.onPause();
        VLog.d("");

    }

    private void reloadListItems() {
        mItemsArray.clear();

        Prefs p = new Prefs(getActivity());

        LinkedTreeMap itemMuaxebanxeMap = new LinkedTreeMap();
        itemMuaxebanxeMap.put(Configs.k_id, Configs.HOME_ITEM_MUAXEBANXE);
        itemMuaxebanxeMap.put(Configs.k_icon, getResources().getString(R.string.ion_model_s));
        itemMuaxebanxeMap.put(Configs.k_title, "Mua xe bán xế");
        itemMuaxebanxeMap.put(Configs.k_detail, "Các bài mới nhất trong topic Mua xe bán xế otofun");
        itemMuaxebanxeMap.put(Configs.k_badge, mCount_topic_88);
        mItemsArray.add(itemMuaxebanxeMap);

        LinkedTreeMap itemDodientuMap = new LinkedTreeMap();
        itemDodientuMap.put(Configs.k_id, Configs.HOME_ITEM_DODIENTU);
        itemDodientuMap.put(Configs.k_icon, getResources().getString(R.string.ion_ipod));
        itemDodientuMap.put(Configs.k_title, "Đồ điện tử");
        itemDodientuMap.put(Configs.k_detail, "Các bài mới nhất trong topic Đồ điện tử otofun");
        itemDodientuMap.put(Configs.k_badge, mCount_topic_312);
        mItemsArray.add(itemDodientuMap);


        LinkedTreeMap itemOtofunHomeMap = new LinkedTreeMap();
        itemOtofunHomeMap.put(Configs.k_id, Configs.HOME_ITEM_OTOFUN_HOME);
        itemOtofunHomeMap.put(Configs.k_icon, getResources().getString(R.string.ion_ios_world_outline));
        itemOtofunHomeMap.put(Configs.k_title, "Web diễn đàn otofun");
        itemOtofunHomeMap.put(Configs.k_detail, "Đang xem: " + p.getLastURL());
        mItemsArray.add(itemOtofunHomeMap);

        LinkedTreeMap itemSettingMap = new LinkedTreeMap();
        itemSettingMap.put(Configs.k_id, Configs.HOME_ITEM_SETTING);
        itemSettingMap.put(Configs.k_icon, getResources().getString(R.string.ion_ios_cog));
        itemSettingMap.put(Configs.k_title, "Cài đặt nhận tin nhanh");
        itemSettingMap.put(Configs.k_detail, "Tuỳ chỉnh nhận tin ngay khi có bài viết mới, theo cách bạn muốn!");
        mItemsArray.add(itemSettingMap);


    }

    private void requestNewCountThreadFromServer() {
        VLog.d("");
        ((BaseActivity)getActivity()).showLoadingDialog("");

        Prefs p = new Prefs(getActivity());
        int lastID_muaxebanxe = p.getLastIDOfTopic(String.valueOf(Configs.TOPIC_ID_MUAXEBANXE));
        int lastID_dodientu = p.getLastIDOfTopic(String.valueOf(Configs.TOPIC_ID_DODIENTU));


        final HashMap<String, String> params = new HashMap<>();
        params.put(Configs.k_api_func, Configs.api_GetNewCountThread);
        params.put(Configs.k_topic_88, String.valueOf(lastID_muaxebanxe));
        params.put(Configs.k_topic_312, String.valueOf(lastID_dodientu));

        Connector.post(getActivity(), params, new SecretHttpResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Object result, String errString) {
                ((BaseActivity)getActivity()).hideLoadingDialog();
                ((BaseActivity)getActivity()).showServerErrorDialog(statusCode, errString);
                Commons.sendGAEvent(getActivity(), Configs.GA_EVENT_NETWORK_ERROR, (String) params.get(Configs.k_api_func), Utils.getDeviceName() + "_" + statusCode, 0);
            }
            @Override
            public void onSuccess(int statusCode, Object result) {
                ((BaseActivity)getActivity()).hideLoadingDialog();

                final Object dataMap = ((LinkedTreeMap)result).get(Configs.k_data);
                mCount_topic_88 = (int) MapHelper.getValue((LinkedTreeMap) dataMap, Configs.k_topic_88, 0D);
                mCount_topic_312 = (int) MapHelper.getValue((LinkedTreeMap) dataMap, Configs.k_topic_312, 0D);

                VLog.d("count_topic_88:" + mCount_topic_88);
                VLog.d("count_topic_312:" + mCount_topic_312);
//                for (int i=0; i<mItemsArray.size(); i++) {
//                    LinkedTreeMap aItemMap = mItemsArray.get(i);
//                    String topic = MapHelper.getValue(aItemMap, Configs.k_id, "");
//                    if (topic.equals(Configs.HOME_ITEM_MUAXEBANXE)) {
//                        aItemMap.put(Configs.k_badge, count_topic_88);
//                    }
//                    if (topic.equals(Configs.HOME_ITEM_DODIENTU)) {
//                        aItemMap.put(Configs.k_badge, count_topic_312);
//                    }
//                }
//                VLog.d("mItemsArray:" + mItemsArray);
                reloadListItems();
                mAdapter.notifyDataSetChanged();

            }
        });
    }

}
