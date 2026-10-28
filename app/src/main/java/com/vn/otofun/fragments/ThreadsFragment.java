package com.vn.otofun.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.LinkedTreeMap;
import com.vn.otofun.BaseActivity;
import com.vn.otofun.BaseFragment;
import com.vn.otofun.OtofunApplication;
import com.vn.otofun.R;
import com.vn.otofun.activities.WebActivity;
import com.vn.otofun.adapters.ThreadsAdapter;
import com.vn.otofun.data.Prefs;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.MapHelper;
import com.vn.otofun.utils.Utils;
import com.vn.otofun.utils.VLog;
import com.vn.otofun.utils.Connector;
import com.vn.otofun.utils.SecretHttpResponseHandler;
import com.vn.otofun.utils.Commons;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AnhVT on 11/20/15.
 */
public class ThreadsFragment extends BaseFragment {
    RecyclerView mRecyclerView;
    ArrayList<LinkedTreeMap> mItemsArray = null;
    ThreadsAdapter mAdapter = null;
    ThreadsAdapter.OnItemClickListener mItemRowClickListener;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int mTopicID = 0;
    int mOldLastID = 0;
    Boolean mIsEndList = false;
    int mRowSelected = -1;
//    Button mFilterButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        VLog.d("");
        final View rightToolView = ((BaseActivity)getActivity()).mNavigationView.findViewById(R.id.actionbar_right_tool_threads);
        rightToolView.setVisibility(View.VISIBLE);

//        mFilterButton = (Button) rightToolView.findViewById(R.id.actionbar_right_tool_threads_filter_button);

        View view = inflater.inflate(R.layout.fragment_list_news, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_list_news_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_news_listView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mItemsArray = new ArrayList<LinkedTreeMap>();

//        final Fragment self = this;

        mItemRowClickListener = new ThreadsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                LinkedTreeMap item = mItemsArray.get(position);
                VLog.d("click item " + position);
                item.put(Configs.k_badge, 0);
                mAdapter.notifyItemChanged(position);

                int threadID = MapHelper.getValue(item, Configs.k_thread_id, 0);
                if (threadID > 0) {
                    openNewsByURL(Configs.LINK_OPEN_THREAD + threadID, threadID);
                    mRowSelected = position;
                }
            }

        };

        mAdapter = new ThreadsAdapter(getActivity(), mItemsArray, mRecyclerView, mItemRowClickListener);

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        VLog.d("");
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Configs.k_topic_id)) {
            mTopicID = (int) bundle.getSerializable(Configs.k_topic_id);
        }
        if (bundle != null && bundle.containsKey(Configs.k_topic_name)) {
            String topicName = (String) bundle.getSerializable(Configs.k_topic_name);
            ((BaseActivity) getActivity()).setupActionBar(topicName, 0, 0);
        }
//        if (bundle != null && bundle.containsKey(Configs.k_old_last_id)) {
//            mOldLastID = (int) bundle.getSerializable(Configs.k_old_last_id);
//        }

        Prefs p = new Prefs(getActivity());

        mOldLastID = p.getLastIDOfTopic(String.valueOf(mTopicID));


        requestNewTopicsFromServer();

    }

    @Override
    public void onResume() {
        VLog.d("");
        super.onResume();

        mAdapter.setOnLoadMoreListener(new ThreadsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                VLog.d("");
                requestNewTopicsFromServer();
            }
        });


        //Kiem tra neu quay ve tu trang detail thread thi cap nhat lai Row
        if (mRowSelected >= 0 && mRowSelected < mItemsArray.size()) {
            Prefs p = new Prefs(getActivity());
            LinkedTreeMap aItemMap = mItemsArray.get(mRowSelected);

            int threadID = MapHelper.getValue(aItemMap, Configs.k_thread_id, 0);
            aItemMap.put(Configs.k_star, p.getStarOfThread(threadID));
            mAdapter.notifyItemChanged(mRowSelected);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        VLog.d("");

        if (mItemsArray.size() > 0) {
            LinkedTreeMap itemMap = mItemsArray.get(0);
            int lastID = MapHelper.getValue(itemMap, Configs.k_id, 0);
            if (lastID > 0) {
                Prefs p = new Prefs(getActivity());
                p.setLastIDOfTopic(String.valueOf(mTopicID), lastID);
            }
        }
    }



    void refreshItems() {
        VLog.d("");
        mIsEndList = false;
        mItemsArray.clear();
        mAdapter.notifyDataSetChanged();

        requestNewTopicsFromServer();
        mSwipeRefreshLayout.setRefreshing(false);

    }

//    void onItemsLoadComplete() {
//        // Update the adapter and notify data set changed
//        // ...
//
//        // Stop refresh animation
//        mSwipeRefreshLayout.setRefreshing(false);
//    }


    private void openNewsByURL(String url, int threadID) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        Bundle args = new Bundle();

        args.putSerializable(Configs.k_link, url);
        args.putSerializable(Configs.k_thread_id, threadID);
        intent.putExtras(args);

//        ((BaseActivity)getActivity()).showGoogleAdmob(intent, false);

        startActivity(intent);

    }

    private void requestNewTopicsFromServer() {
        VLog.d("");
        if (mIsEndList) {
            return;
        }

        ((BaseActivity)getActivity()).showLoadingDialog("");

        int lastID = 0;
        if (mItemsArray.size() > 0) {
            LinkedTreeMap itemMap = mItemsArray.get(mItemsArray.size() - 1);
            lastID = MapHelper.getValue(itemMap, Configs.k_id, 0);
            if (lastID > 0) {
                lastID--;
            }
        }

        final HashMap<String, String> params = new HashMap<>();
        params.put(Configs.k_api_func, Configs.api_GetNewThreads);
        params.put(Configs.k_last_id, String.valueOf(lastID));
        params.put(Configs.k_topic_id, String.valueOf(mTopicID));

        Connector.post(getActivity(), params, new SecretHttpResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Object result, String errString) {
                ((BaseActivity)getActivity()).hideLoadingDialog();
                ((BaseActivity)getActivity()).showServerErrorDialog(statusCode, errString);
                Commons.sendGAEvent(getActivity(), Configs.GA_EVENT_NETWORK_ERROR, (String) params.get(Configs.k_api_func), Utils.getDeviceName() + "_" + statusCode, 0);
                mIsEndList = true;
                mAdapter.setLoaded();
            }
            @Override
            public void onSuccess(int statusCode, Object result) {
                ((BaseActivity)getActivity()).hideLoadingDialog();

                final Object dataMap = ((LinkedTreeMap)result).get(Configs.k_data);
                final Object threads = ((LinkedTreeMap)dataMap).get(Configs.k_list);

                if (threads instanceof ArrayList) {
                    VLog.d("mOldLastID:" + mOldLastID);
                    Prefs p = new Prefs(getActivity());

                    ArrayList threadArray = ((ArrayList) threads);
                    if (threadArray.size() > 0) {
                        for (int i=0; i<threadArray.size(); i++) {
                            LinkedTreeMap aItemMap = (LinkedTreeMap) threadArray.get(i);
                            int id = (int) MapHelper.getValue(aItemMap, Configs.k_id, 0D);
                            if (id > mOldLastID) {
                                aItemMap.put(Configs.k_badge, 1);
                            } else {
                                aItemMap.put(Configs.k_badge, 0);
                            }

                            int threadID = (int) MapHelper.getValue(aItemMap, Configs.k_thread_id, 0D);
                            aItemMap.put(Configs.k_star, p.getStarOfThread(threadID));

                        }


                        mItemsArray.addAll(threadArray);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mIsEndList = true;
                    }
                } else {
                    mIsEndList = true;
                }

                mAdapter.setLoaded();

            }
        });
    }
}
