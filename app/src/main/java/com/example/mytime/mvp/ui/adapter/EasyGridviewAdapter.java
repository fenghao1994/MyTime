package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Photo;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class EasyGridviewAdapter extends BaseAdapter{

    private List<Photo> mList;
    private Context mContext;

    public EasyGridviewAdapter(Context context, List<Photo> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get( i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageItemAdapter.ViewHolder viewHolder;
        if ( view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
            viewHolder = new ImageItemAdapter.ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            view.setTag( viewHolder);
        }else {
            viewHolder = (ImageItemAdapter.ViewHolder) view.getTag();
        }

        viewHolder.imageView.setImageURI(Uri.parse( mList.get(i).getAddress()));
        return view;
    }

    static class ViewHolder{
        public ImageView imageView;
    }
}
