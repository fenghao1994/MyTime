package com.example.mytime.mvp.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/1.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List mList;


    public PlanAdapter(List list){
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        ViewHolder viewHolder = new ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TODO 数据绑定
        holder.planName.setText( (String)mList.get( position));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.plan_name)
        TextView planName;
        @BindView(R.id.plan_ok)
        ImageView planOk;
        @BindView(R.id.plan_time)
        TextView planTime;
        @BindView(R.id.plan_cha)
        ImageView planCha;
        @BindView(R.id.alarm_layout)
        LinearLayout alarmLayout;

        View planView;

        ViewHolder(View view) {
            super(view);
            planView = view;
            ButterKnife.bind(this, view);

        }
    }
}
