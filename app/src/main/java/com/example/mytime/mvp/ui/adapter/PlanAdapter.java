package com.example.mytime.mvp.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.ui.activity.CreatePlanActivity;
import com.example.mytime.util.MyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/1.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List<Plan> mList;
    private Context mContext;
    private List<String> mCount;


    public PlanAdapter(Context context, List<Plan> list, List<String> count){
        this.mContext = context;
        this.mList = list;
        this.mCount = count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        ViewHolder viewHolder = new ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //TODO 数据绑定
        if ( "".equals(mList.get( position).getTitle())){
            holder.planName.setText("计划表");
        }else {
            holder.planName.setText(mList.get( position).getTitle());
        }
        holder.planTime.setText(MyUtil.dateYMDHM( mList.get( position).getEditTime()));
        holder.countPlanItem.setText(mCount.get( position));
        holder.planView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreatePlanActivity.class);
                intent.putExtra("PLAN", mList.get( position));
                mContext.startActivity( intent);
            }
        });

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
        @BindView(R.id.count_plan_item)
        TextView countPlanItem;

        View planView;

        ViewHolder(View view) {
            super(view);
            planView = view;
            ButterKnife.bind(this, view);

        }
    }
}
