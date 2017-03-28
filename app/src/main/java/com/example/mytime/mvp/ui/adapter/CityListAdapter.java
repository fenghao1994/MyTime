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
            view.setTag( viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText( mList.get( position));
        return view;
    }

    static class ViewHolder{
       public TextView textView;
    }
}
