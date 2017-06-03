package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.FriendShare;
import com.example.mytime.mvp.ui.activity.FriendInfoActivity;
import com.example.mytime.mvp.ui.activity.FriendPlanItemActivity;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fenghao on 2017/6/3.
 */

public class FriendOpenAdapter extends RecyclerView.Adapter<FriendOpenAdapter.ViewHolder> {

    private List<FriendShare> mList = new ArrayList<>();
    private Context mContext;

    public FriendOpenAdapter(List<FriendShare> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public FriendOpenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_open_plan_item, parent, false);
        FriendOpenAdapter.ViewHolder viewHolder = new FriendOpenAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mList.get(position).getUser().getHeadImg().contains("D:\\")){
            String s = mList.get(position).getUser().getHeadImg();
            s = s.substring(3, s.length());
            s = s.replace("\\", "/");
            Glide.with(mContext).load(HttpUrl.ROOT + "/" + s).diskCacheStrategy(DiskCacheStrategy.ALL).into( holder.headerImg);
        }else {
            Glide.with(mContext).load(mList.get(position).getUser().getHeadImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into( holder.headerImg);
        }
        if (mList.get(position).getPlanItem().isComplete()){
            holder.planItemOk.setVisibility(View.VISIBLE);
        }else {
            holder.planItemOk.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mList.get(position).getPlanItem().getTitle())){
            holder.planItemTitle.setText(mList.get(position).getPlanItem().getTitle());
        }else {
            holder.planItemTitle.setText("");
        }
        if (!TextUtils.isEmpty(mList.get(position).getPlanItem().getContent())){
            holder.planItemContent.setText(mList.get(position).getPlanItem().getContent());
        }else {
            holder.planItemContent.setText("");
        }
        holder.planItemTime.setText(mList.get(position).getPlanItem().getYear() + "." +
        mList.get(position).getPlanItem().getMonth() + "." + mList.get(position).getPlanItem().getDay() +
        " " + mList.get(position).getPlanItem().getHour() + ":" + mList.get(position).getPlanItem().getMinute());

        holder.headerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInfoActivity.class);
                intent.putExtra("FRIEND", mList.get(position).getUser());
                mContext.startActivity(intent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendPlanItemActivity.class);
                intent.putExtra("PLANITEM", mList.get(position).getPlanItem());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.header_img)
        CircleImageView headerImg;
        @BindView(R.id.plan_item_ok)
        ImageView planItemOk;
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
