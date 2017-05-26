package com.example.mytime.mvp.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
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
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.ui.activity.CreatePlanActivity;
import com.example.mytime.mvp.ui.activity.CreatePlanItemActivity;
import com.example.mytime.mvp.ui.activity.MainActivity;
import com.example.mytime.mvp.ui.custom.SharePopWindow;
import com.example.mytime.receiver.AlarmReceiver;
import com.example.mytime.util.EditTimeSortFromBToS;
import com.example.mytime.util.Extra;
import com.example.mytime.util.MyUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by fenghao on 2017/1/4.
 */

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemAdapter.ViewHolder> {

    private List<PlanItem> mList = new ArrayList<>();
    //    private List<Time> times;
    private Context mContext;
    private AlertDialog alertDialog;
    private ArrayList<PlanItem> completeList = new ArrayList<>();
    private ArrayList<PlanItem> notCompleteList = new ArrayList<>();

    public PlanItemAdapter(Context context, List<PlanItem> list) {
        this.mContext = context;
        initData(list);
    }

    private void initData(List<PlanItem> list){
        for (int i = 0 ; i < list.size() ; i++){
            if ( !list.get(i).isDelete()){
                if (list.get(i).isComplete()){
                    completeList.add(list.get(i));
                }else {
                    notCompleteList.add(list.get(i));
                }
            }
        }
        EditTimeSortFromBToS editTimeSortFromBToS = new EditTimeSortFromBToS();
        Collections.sort(completeList, editTimeSortFromBToS);
        Collections.sort(notCompleteList, editTimeSortFromBToS);
        this.mList.addAll(notCompleteList);
        this.mList.addAll(completeList);
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
        holder.setIsRecyclable(false);
        PlanItem planItem = mList.get(position);
        if (planItem.isDelete()){
            holder.cardView.setVisibility(View.GONE);
        }
        if (planItem.isComplete()){
            holder.planItemOk.setImageDrawable(mContext.getResources().getDrawable(R.drawable.gou_gray));
            holder.planItemOk.setEnabled(false);
            holder.planItemTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.planItemContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (planItem.isDelete()){
            holder.planItemView.setVisibility(View.GONE);
        }  if (planItem.isDelete()){
            holder.planItemView.setVisibility(View.GONE);
        }

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
                if (mContext instanceof CreatePlanActivity){
                    Intent intent = new Intent(mContext, CreatePlanItemActivity.class);
                    intent.putExtra("PLANITEM", mList.get(position));
                    ((CreatePlanActivity) mContext).startActivityForResult(intent, CreatePlanActivity.REQUEST);
                }else {
                    Toast.makeText(mContext, "此页面只能进行删除计划操作", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.planItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (shareLongClick != null){
                    shareLongClick.shareLongClick(mList.get(position));
                }
                return true;
            }
        });

        holder.planItemCha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext)
                        .setMessage("确定要删除吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                cancelAlarmClock(mList.get(position));
                                mList.get(position).setDelete(true);
//                                mList.get(position).setEditTime(System.currentTimeMillis());
                                mList.get(position).update(mList.get(position).getId());
                                ((CreatePlanActivity)mContext).deletePlanItem(mList.get(position));
                                mList.remove(position);
                                PlanItemAdapter.this.notifyDataSetChanged();
                                alertDialog.dismiss();

                                //发送刷新widget广播
                                Intent intent = new Intent(Extra.WIDGET_TIME);
                                mContext.sendBroadcast(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        holder.planItemOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext)
                        .setMessage("确定完成任务吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                completeAlarmClock(mList.get( position));
                                PlanItem planItem1 = mList.get(position);
                                planItem1.setComplete(true);
                                planItem1.setEditTime(System.currentTimeMillis());
                                planItem1.update(planItem1.getId());
                                ((CreatePlanActivity)mContext).completePlanItem(mList.get(position));
                                Toast.makeText(mContext, "恭喜完成该任务", Toast.LENGTH_SHORT).show();
                                PlanItemAdapter.this.notifyDataSetChanged();
                                alertDialog.dismiss();

                                //发送刷新widget广播
                                Intent intent = new Intent(Extra.WIDGET_TIME);
                                mContext.sendBroadcast(intent);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        })
                        .show();

            }
        });
        animater(holder.planItemView, position);
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

    private ShareLongClick shareLongClick;

    public void setShareLongClick(ShareLongClick shareLongClick) {
        this.shareLongClick = shareLongClick;
    }

    public interface ShareLongClick{
        void shareLongClick(PlanItem planItem);
    }
    public void animater(View view, int position){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);

        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大，且改变透明度，然后移动
        set.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
        //都设置3s，也可以为每个单独设置
        set.setDuration(800);
        set.start();
    }
}
