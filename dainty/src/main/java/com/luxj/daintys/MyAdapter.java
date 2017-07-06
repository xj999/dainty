package com.luxj.daintys;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luxj
 * @date create 2017/7/6
 * @description
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> data;

    public void setData(List<String> list) {
        if (data != null) {
            data.clear();
            data.addAll(list);
        } else {
            data = new ArrayList<>();
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);

        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MViewHolder h = (MViewHolder) holder;
        h.textView.setText(data.get(position));

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text1);
        }
    }
}
