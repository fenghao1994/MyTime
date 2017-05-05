package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.util.WeatherUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao02 on 2017/4/26.
 */

public class FutureWeatherAdapter extends RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder> {


    private List<WeatherEntity.ResultBean.FutureBean> futureBeanList;
    private Context mContext;

    public FutureWeatherAdapter(Context context, List<WeatherEntity.ResultBean.FutureBean> list){
        this.mContext = context;
        this.futureBeanList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_week_weather, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mWeekDay.setText(futureBeanList.get(position).getWeek());
        Map<String, Integer> map = WeatherUtil.getWeahterIcon();
//        int drawablePath = map.get(futureBeanList.get(position).getDayTime());
        int drawablePath = 0;
        if (futureBeanList.get(position).getDayTime() != null && !"".equals(futureBeanList.get(position).getDayTime())){
            drawablePath = WeatherUtil.getLikeMapPath(futureBeanList.get(position).getDayTime());
            holder.mWeekWeather.setText(futureBeanList.get(position).getDayTime());
        }else {
            drawablePath = WeatherUtil.getLikeMapPath(futureBeanList.get(position).getNight());
            holder.mWeekWeather.setText(futureBeanList.get(position).getNight());
        }

        if (drawablePath == 0){
            drawablePath = R.drawable.w00;
        }
        holder.mWeekIcon.setImageDrawable(mContext.getResources().getDrawable(drawablePath));
        String weatherTemperature = futureBeanList.get(position).getTemperature();
        String[] strs = weatherTemperature.trim().split("/");
        if (strs.length == 2){
            holder.mWeekDayTemperature.setText(strs[0].trim().toString());
            holder.mWeekNightTemperature.setText(strs[1].trim().toString());
        }else {
            holder.mWeekDayTemperature.setText(strs[0].trim().toString());
        }

    }

    @Override
    public int getItemCount() {
        return futureBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.week_day)
        TextView mWeekDay;
        @BindView(R.id.week_icon)
        ImageView mWeekIcon;
        @BindView(R.id.week_day_temperature)
        TextView mWeekDayTemperature;
        @BindView(R.id.week_night_temperature)
        TextView mWeekNightTemperature;
        @BindView(R.id.week_weather)
        TextView mWeekWeather;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
