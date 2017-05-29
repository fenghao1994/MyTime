package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.RiJi;
import com.example.mytime.mvp.ui.activity.CreateRiJiActivity;
import com.example.mytime.util.MyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/5/28.
 */

public class RiJiAdapter extends RecyclerView.Adapter<RiJiAdapter.ViewHolder> {

    private Context mContext;
    private List<RiJi> list;

    public RiJiAdapter(Context mContext, List<RiJi> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riji, parent, false);
        RiJiAdapter.ViewHolder viewHolder = new RiJiAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.calendar.setText(MyUtil.dateYMD(list.get(position).getCreateTime()));
        holder.rijiContent.setText(list.get(position).getContent());
        holder.weather.setText("天气: " + list.get(position).getWeather());

        holder.rijiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateRiJiActivity.class);
                intent.putExtra("DATE", list.get(position));
                intent.putExtra("FLAG", "EDIT");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.riji_content)
        TextView rijiContent;
        @BindView(R.id.weather)
        TextView weather;
        @BindView(R.id.calendar)
        TextView calendar;
        @BindView(R.id.riji_layout)
        LinearLayout rijiLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
