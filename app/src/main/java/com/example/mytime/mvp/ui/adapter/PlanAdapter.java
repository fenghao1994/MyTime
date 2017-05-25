package com.example.mytime.mvp.ui.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/1.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List<Plan> mList = new ArrayList<>();
    private Context mContext;
    private List<String> mCount = new ArrayList<>();


    public PlanAdapter(Context context, List<Plan> list, List<String> count){
        this.mContext = context;
        for (int i = 0; i < count.size(); i++){
            if (!count.get(i).equals("0")){
                this.mCount.add(count.get(i));
                this.mList.add(list.get(i));
            }
        }
//        this.mList = list;
//        this.mCount = count;
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
        animater(holder.planView, position);
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

    public void animater(View view, int position){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);

        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大，且改变透明度，然后移动
        set.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
        //都设置3s，也可以为每个单独设置
        set.setDuration((position + 1) * 1000);
        set.start();
    }
}
