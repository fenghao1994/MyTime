package com.example.mytime.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.util.Extra;

import org.litepal.crud.DataSupport;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by fenghao on 2017/5/26.
 */

public class TimeAppWidgetProvider extends AppWidgetProvider{
    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
        updateAllAppWidgets(context, appWidgetManager, idsSet);
    }
    // widget被删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        super.onDeleted(context, appWidgetIds);
    }


    private PlanItem getPlanItem() {
        List<PlanItem> list = DataSupport.where("isComplete = ?", 0 + "").find(PlanItem.class);
        if (list != null && list.size() > 0) {
            if (list.size() == 1){
                return list.get(0);
            }
            for (int i = 0; i < list.size(); i++) {
                boolean flag = false;
                String str = list.get(i).getYear() + "" + (list.get(i).getMonth() + 1) + "" + list.get(i).getDay()
                        + list.get(i).getHour() + "" + list.get(i).getMinute();
                for (int j = i + 1; j < list.size(); j++) {
                    String str1 = list.get(j).getYear() + "" + (list.get(j).getMonth() + 1) + "" + list.get(j).getDay()
                            + list.get(j).getHour() + "" + list.get(j).getMinute();
                    if (str.compareTo(str1) > 0) {
                        flag = true;
                    }
                    if (!flag) {
                        return list.get(i);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("xxxxxxxxxxxxxxxxx -----", intent.getAction());
        final String action = intent.getAction();
        if (action.equals(Extra.WIDGET_TIME)){
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        }
    }

    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set set) {
        int appid;
        Iterator it = set.iterator();
        while (it.hasNext()){
            appid = ((Integer)it.next()).intValue();
            PlanItem planItem = getPlanItem();
            if (planItem != null){
                RemoteViews remoteviews = new RemoteViews(context.getPackageName(), R.layout.widget_time);
                remoteviews.setTextViewText(R.id.title, planItem.getTitle());
                remoteviews.setTextViewText(R.id.content, planItem.getContent());
                remoteviews.setTextViewText(R.id.time, planItem.getYear() + "." + planItem.getMonth() + "." +
                        planItem.getDay() + " " + planItem.getHour() + ":" + planItem.getMinute());
                appWidgetManager.updateAppWidget(appid, remoteviews);
            }

        }
    }

}
