package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mytime.R;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;

/**
 * Created by fenghao on 2017/1/3.
 */

public class ImageItemAdapter extends BaseAdapter {

    private ArrayList<ImageItem> mArrayList;
    private Context mContext;
    private int mImageWidth;

    public ImageItemAdapter(ArrayList<ImageItem> arrayList, Context context, int imageWidth){
        this.mArrayList = arrayList;
        this.mContext = context;
        this.mImageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if ( view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            view.setTag( viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ViewGroup.LayoutParams param = viewHolder.imageView.getLayoutParams();
        param.height = mImageWidth;
        param.width = mImageWidth;
        viewHolder.imageView.setLayoutParams(param);
        Glide.with( mContext).load( mArrayList.get(i).path).into( viewHolder.imageView);
        return view;
    }

    static class ViewHolder{
        public ImageView imageView;
    }
}
