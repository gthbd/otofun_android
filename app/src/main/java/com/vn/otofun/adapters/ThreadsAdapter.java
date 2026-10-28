package com.vn.otofun.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

import com.vn.otofun.R;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.MapHelper;
import com.vn.otofun.utils.Commons;
import com.vn.otofun.utils.VLog;

/**
 * Created by AnhVT on 9/28/15.
 */
public class ThreadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_ITEM = 1;
    final int VIEW_PROG = 0;

    List<LinkedTreeMap> items;
    Context context;
    OnItemClickListener itemRowClickListener;
    OnLoadMoreListener onLoadMoreListener;

    int visibleThreshold = 2;
    int lastVisibleItem, totalItemCount;
    boolean loading;

    public ThreadsAdapter(Context context, List<LinkedTreeMap> objects, RecyclerView view, OnItemClickListener itemRowClickListener) {
        this.context = context;
        this.items = objects;
        this.itemRowClickListener = itemRowClickListener;

        if (view != null && view.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) view.getLayoutManager();
            view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();

                    if (totalItemCount <= 0) {
                        return;
                    }

                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                    VLog.d("totalItemCount=" + totalItemCount);
//                    VLog.d("lastVisibleItem=" + lastVisibleItem);

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_item_thread, null);
        holder = new ListNewsViewHolder(view, itemRowClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof ListNewsViewHolder) {
            LinkedTreeMap item = this.items.get(i);

            ((ListNewsViewHolder) holder).titleTextView.setText(MapHelper.getValue(item, Configs.k_title, ""));

            String price = Commons.toCurrencyString(MapHelper.getValue(item, Configs.k_price, 0));

            int star = MapHelper.getValue(item, Configs.k_star, 0);
            if (star > 0) {
                ((ListNewsViewHolder) holder).starTextView.setVisibility(View.VISIBLE);
            } else {
                ((ListNewsViewHolder) holder).starTextView.setVisibility(View.GONE);
            }

            int badge = MapHelper.getValue(item, Configs.k_badge, 0);
            if (badge > 0) {
                ((ListNewsViewHolder) holder).badgeTextView.setVisibility(View.VISIBLE);
            } else {
                ((ListNewsViewHolder) holder).badgeTextView.setVisibility(View.GONE);
            }


            ((ListNewsViewHolder) holder).detail1TextView.setText(MapHelper.getValue(item, Configs.k_address, ""));
//            ((ListNewsViewHolder) holder).right1TextView.setText(i + "-" + MapHelper.getValue(item, Configs.k_id, 0));
            ((ListNewsViewHolder) holder).right1TextView.setText(price);

//            String date = Commons.formatDate(MapHelper.getValue(item, Configs.k_forum_time, ""), "kk:mm, dd/MM/yyyy");
            String date = Commons.formatToYesterdayOrToday(MapHelper.getValue(item, Configs.k_forum_time, ""));
            ((ListNewsViewHolder) holder).detail2TextView.setText(MapHelper.getValue(item, Configs.k_user_post, ""));
            ((ListNewsViewHolder) holder).right2TextView.setText(date);

            ((ListNewsViewHolder) holder).position = i;
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }

    public class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView starTextView;
        TextView badgeTextView;
        TextView detail1TextView;
        TextView right1TextView;
        TextView detail2TextView;
        TextView right2TextView;

        int position;
        OnItemClickListener listener;

        public ListNewsViewHolder(View view, OnItemClickListener listener) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.list_item_thread_title_textView);
            starTextView = (TextView) view.findViewById(R.id.list_item_thread_star_textView);
            badgeTextView = (TextView) view.findViewById(R.id.list_item_thread_badge_textView);

            detail1TextView = (TextView) view.findViewById(R.id.list_item_thread_detail1_textView);
            right1TextView = (TextView) view.findViewById(R.id.list_item_thread_right1_textView);

            detail2TextView = (TextView) view.findViewById(R.id.list_item_thread_detail2_textView);
            right2TextView = (TextView) view.findViewById(R.id.list_item_thread_right2_textView);

            this.listener = listener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onClick(position);
            }
        }
    }

    public interface OnItemClickListener {
        public void onClick(int position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
}
