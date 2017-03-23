package com.example.mytime.mvp.ui.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.ui.activity.CreatePlanActivity;
import com.example.mytime.mvp.ui.activity.CreatePlanItemActivity;
import com.example.mytime.receiver.AlarmReceiver;
import com.example.mytime.util.MyUtil;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by fenghao on 2017/1/4.
 */

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemAdapter.ViewHolder> {

    private List<PlanItem> mList;
    //    private List<Time> times;
    private Context mContext;

    public PlanItemAdapter(Context context, List<PlanItem> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //// TODO: 2017/1/4  显示的时间处理

        //如果只提醒一次则显示年月日，否则不显示
        PlanItem planItem = mList.get(position);
        if (planItem.getAlarmWay() == 0) {
            holder.planItemTime.setText(planItem.getYear() + "." +
                    (planItem.getMonth() + 1) + "." + planItem.getDay() +
                    " " + planItem.getHour() + ":" + planItem.getMinute());
        } else {
            holder.planItemTime.setText(planItem.getHour() + ":" + planItem.getMinute());
        }
        holder.planItemContent.setText(mList.get(position).getContent());
        holder.planItemTitle.setText(mList.get(position).getTitle());
        holder.planItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreatePlanItemActivity.class);
                intent.putExtra("PLANITEM", mList.get(position));
                ((CreatePlanActivity) mContext).startActivityForResult(intent, CreatePlanActivity.REQUEST);
            }
        });
        holder.planItemCha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "删除任务成功!", Toast.LENGTH_SHORT).show();
                cancelAlarmClock(mList.get(position));
                mList.get(position).setDelete(true);
                mList.get(position).update(mList.get(position).getId());
            }
        });

        holder.planItemOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAlarmClock(mList.get( position));
                Toast.makeText(mContext, "wanchengle", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void completeAlarmClock(PlanItem planItem) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = planItem.getYear();
        int month = planItem.getMonth();
        int day = planItem.getDay();
        if (planItem.getAlarmWay() == 0) {
            planItem.setComplete( true);
            cancelAlarmClock(planItem);
            planItem.update( planItem.getId());
            return;
        } else if (planItem.getAlarmWay() == 1) {
            if (month == 1) {
                if (day == 28) {
                    if (MyUtil.isRun(year)) {
                        day = day + 1;
                    } else {
                        month = 2;
                        day = 1;
                    }
                }
            } else if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7
                    || month == 9 || month == 11) {
                if (day == 31) {
                    if (month == 11) {
                        year += 1;
                        month = 0;
                        day = 1;
                    } else {
                        month += 1;
                        day = 1;
                    }
                }
            } else {
                if (day == 30) {
                    month += 1;
                    day = 1;
                }
            }
        }else if ( planItem.getAlarmWay() == 2){
            if ( month == 1){
                if ( MyUtil.isRun( year)){
                    if ( day + 7 > 29){
                        day = (day + 7) % 29;
                        month += 1;
                    }else {
                        day = day + 7;
                    }
                }else {
                    if ( day + 7 > 28){
                        day = (day + 7) % 28;
                        month += 1;
                    }else {
                        day = day + 7;
                    }
                }
            }else if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7
                    || month == 9 || month == 11){
                if ( month == 11){
                    if ( day + 7 > 31){
                        day = (day + 7) % 31;
                        month = 0;
                        year += 1;
                    }else {

                    }
                }else {
                    if ( day + 7 > 31){
                        day = (day + 7) % 31;
                        month += 1;
                    }else {
                        day = day + 7;
                    }
                }
            }else {
                if ( day + 7 > 30){
                    day = (day + 7) % 30;
                    month += 1;
                }else {
                    day = day + 7;
                }
            }
        }
        planItem.setYear(year);
        planItem.setMonth(month);
        planItem.setDay(day);
        planItem.update( planItem.getId());
        calendar.set(Calendar.YEAR, planItem.getYear());
        calendar.set(Calendar.MONTH, planItem.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, planItem.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, planItem.getHour());
        calendar.set(Calendar.MINUTE, planItem.getMinute());
        calendar.set(Calendar.MILLISECOND, 0);
        setAlarm(calendar, planItem);
    }

    public void setAlarm(Calendar calendar, PlanItem planItem) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, planItem.getId(), intent, 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        //AlarmManager.RTC_WAKEUP，硬件闹钟，当闹钟发射时唤醒手机休眠；

        if (planItem.getAlarmWay() == 0){
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }else if (planItem.getAlarmWay() == 1){
            //每天
            long time = 1000 * 60 * 60 * 24;
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),time, pi);
        }else if (planItem.getAlarmWay() == 2){
            long time = 1000 * 60 * 60 * 24 * 7;
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),time, pi);
        }/*else if (planItem.getAlarmWay() == 3){

        }*/
    }

    public void cancelAlarmClock(PlanItem planItem) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, planItem.getId(), intent, 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        am.cancel(pi);

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
