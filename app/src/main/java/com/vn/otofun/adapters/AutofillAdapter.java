package com.vn.otofun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.vn.otofun.R;
import com.vn.otofun.utils.MapHelper;

import static com.vn.otofun.utils.MapHelper.getValue;

/**
 * Created by AnhVT on 10/12/15.
 */
public class AutofillAdapter extends ArrayAdapter<HashMap> implements View.OnClickListener {

    public interface OnItemClickListener {
        public void onClick(HashMap data);
    }

    OnItemClickListener mListener;
    int mTextHolderResId;

    public AutofillAdapter(Context context, ArrayList<HashMap> items, int textHolderResId) {
        super(context, 0, items);
        mTextHolderResId = textHolderResId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AutofillViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_history_item, parent, false);
            holder = new AutofillViewHolder();
            holder.accountNameTextView = (TextView) convertView.findViewById(R.id.dropdown_history_item_account_name_textView);
            holder.accountNumberTextView = (TextView) convertView.findViewById(R.id.dropdown_history_item_account_number_textView);
            convertView.setTag(holder);
            convertView.setOnClickListener(this);
        } else {
            holder = (AutofillViewHolder) convertView.getTag();
        }

        HashMap item = getItem(position);
        String bankName = MapHelper.getValue(item, "trade_name", "");
        String accountName = MapHelper.getValue(item, "account_holder", "");
        holder.accountNameTextView.setText(getContext().getString(mTextHolderResId, bankName, accountName));
        holder.accountNumberTextView.setText(MapHelper.getValue(item, "account_number", ""));
        holder.position = position;

        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            HashMap data = getItem(((AutofillViewHolder) view.getTag()).position);
            mListener.onClick(data);
        }
    }

    class AutofillViewHolder {
        TextView accountNameTextView;
        TextView accountNumberTextView;
        int position;
    }
}