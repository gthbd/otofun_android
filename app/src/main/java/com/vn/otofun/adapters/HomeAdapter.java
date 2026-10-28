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
import java.util.StringTokenizer;

import com.vn.otofun.R;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.MapHelper;
import com.vn.otofun.utils.Commons;
import com.vn.otofun.utils.VLog;

/**
 * Created by AnhVT on 9/28/15.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_ITEM = 1;
    final int VIEW_PROG = 0;

    List<LinkedTreeMap> items;
    Context context;
    OnItemClickListener itemRowClickListener;

    boolean loading;

    public HomeAdapter(Context context, List<LinkedTreeMap> objects, RecyclerView view, OnItemClickListener itemRowClickListener) {
        this.context = context;
        this.items = objects;
        this.itemRowClickListener = itemRowClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(this.context).inflate(R.layout.list_item_home, null);
        holder = new ListNewsViewHolder(view, itemRowClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof ListNewsViewHolder) {
            LinkedTreeMap item = this.items.get(i);

            ((ListNewsViewHolder) holder).iconTextView.setText(MapHelper.getValue(item, Configs.k_icon, ""));
            ((ListNewsViewHolder) holder).titleTextView.setText(MapHelper.getValue(item, Configs.k_title, ""));
            ((ListNewsViewHolder) holder).detailTextView.setText(MapHelper.getValue(item, Configs.k_detail, ""));

            int badge = MapHelper.getValue(item, Configs.k_badge, 0);
            ((ListNewsViewHolder) holder).badgeTextView.setText(String.valueOf(badge));
            if (badge > 0) {
                ((ListNewsViewHolder) holder).badgeTextView.setVisibility(View.VISIBLE);
            } else {
                ((ListNewsViewHolder) holder).badgeTextView.setVisibility(View.GONE);
            }

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
        TextView iconTextView;
        TextView titleTextView;
        TextView detailTextView;
        TextView badgeTextView;

        int position;
        OnItemClickListener listener;

        public ListNewsViewHolder(View view, OnItemClickListener listener) {
            super(view);

            iconTextView = (TextView) view.findViewById(R.id.list_item_home_icon_textView);
            titleTextView = (TextView) view.findViewById(R.id.list_item_home_title_textView);
            detailTextView = (TextView) view.findViewById(R.id.list_item_home_detail_textView);
            badgeTextView = (TextView) view.findViewById(R.id.list_item_home_badge_textView);

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

}
