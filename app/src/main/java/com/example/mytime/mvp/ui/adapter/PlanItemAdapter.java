package com.example.mytime.mvp.ui.adapter;

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
import com.example.mytime.mvp.model.entity.Time;
import com.example.mytime.util.MyUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/4.
 */

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemAdapter.ViewHolder> {

    private List<PlanItem> mList;
    private List<Time> times;

    public PlanItemAdapter(List<PlanItem> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_item, parent, false);
        ViewHolder viewHolder = new ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        times = DataSupport.where("planItemId = ?", mList.get( position).getId() + "").find(Time.class);

        //// TODO: 2017/1/4  显示的时间处理
        if ( mList.get( position).isManyDays()){
            holder.planItemTime.setText(MyUtil.dateHM( times.get(0).getTime()));
        }else {
            holder.planItemTime.setText(MyUtil.dateYMDHM( times.get( 0).getTime()));
        }
        holder.planItemContent.setText( mList.get( position).getContent());
        holder.planItemTitle.setText( mList.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.plan_item_title)
        TextView planItemTitle;
        @BindView(R.id.plan_item_ok)
        ImageView planItemOk;
        @BindView(R.id.plan_item_content)
        TextView planItemContent;
        @BindView(R.id.plan_item_cha)
        ImageView planItemCha;
        @BindView(R.id.plan_item_time)
        TextView planItemTime;
        @BindView(R.id.alarm_layout)
        LinearLayout alarmLayout;
        @BindView(R.id.card_view)
        CardView cardView;

        View planItemView;

        ViewHolder(View view) {
            super(view);
            planItemView = view;
            ButterKnife.bind(this, view);
        }
    }
}
