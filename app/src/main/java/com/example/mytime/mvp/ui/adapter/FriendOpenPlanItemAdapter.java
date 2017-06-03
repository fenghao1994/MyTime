package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.ui.activity.FriendPlanItemActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/6/4.
 */

public class FriendOpenPlanItemAdapter extends RecyclerView.Adapter<FriendOpenPlanItemAdapter.ViewHolder> {

    private Context context;
    private List<PlanItem> planItemList;

    public FriendOpenPlanItemAdapter(Context context, List<PlanItem> planItemList) {
        this.context = context;
        this.planItemList = planItemList;
    }

    @Override
    public FriendOpenPlanItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_item, parent, false);
        FriendOpenPlanItemAdapter.ViewHolder viewHolder = new FriendOpenPlanItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendOpenPlanItemAdapter.ViewHolder holder, final int position) {
        holder.planItemTitle.setText(planItemList.get(position).getTitle() == null? "" : planItemList.get(position).getTitle());
        holder.planItemContent.setText(planItemList.get(position).getContent() == null ? "" : planItemList.get(position).getContent());
        if (planItemList.get(position).getAlarmWay() == 0) {
            holder.planItemTime.setText(planItemList.get(position).getYear() + "." +
                    (planItemList.get(position).getMonth() + 1) + "." + planItemList.get(position).getDay() +
                    " " + planItemList.get(position).getHour() + ":" + planItemList.get(position).getMinute());
        } else {
            holder.planItemTime.setText(planItemList.get(position).getHour() + ":" + planItemList.get(position).getMinute());
        }
        holder.planItemCha.setVisibility(View.GONE);
        holder.planItemOk.setVisibility(View.GONE);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendPlanItemActivity.class);
                intent.putExtra("PLANITEM", planItemList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.plan_item_title)
        TextView planItemTitle;
        @BindView(R.id.plan_item_content)
        TextView planItemContent;
        @BindView(R.id.plan_item_time)
        TextView planItemTime;
        @BindView(R.id.alarm_layout)
        LinearLayout alarmLayout;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.plan_item_ok)
        ImageView planItemOk;
        @BindView(R.id.plan_item_cha)
        ImageView planItemCha;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
