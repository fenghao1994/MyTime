package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Citys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao02 on 2017/3/27.
 */

public class CityListAdapter extends BaseAdapter {
    private Context mContext;
    private Citys mCitys;

    private List<String> mList = new ArrayList<>();

    public CityListAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get( position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder ;
        if ( view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_city_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.city_name);
            viewHolder.drawText = (TextView) view.findViewById(R.id.draw_text);
            view.setTag( viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText( mList.get( position));
        String str = mList.get( position).substring(0, 1);
        viewHolder.drawText.setText(str);
        drawBackground(viewHolder, position);
        return view;
    }

    public void drawBackground(ViewHolder viewHolder, int position){
        if (position % 7 == 0){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_red));
        }else if (position % 7 == 1){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_origin));
        }else if (position % 7 == 2){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_yellow));
        }else if (position % 7 == 3){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_green));
        }else if (position % 7 == 4){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_qing));
        }else if (position % 7 == 5){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_blue));
        }else if (position % 7 == 6){
            viewHolder.drawText.setBackground(mContext.getResources().getDrawable(R.drawable.circle_purple));
        }
    }

    static class ViewHolder{
        public TextView textView;
        public TextView drawText;
    }
}
