package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class EasyGridviewAdapter extends BaseAdapter{

    private List<Photo> mList;
    private Context mContext;
    private int mImageWidth;
    public onDeleteImage onDeleteImage;
    private boolean flag;
    /*public EasyGridviewAdapter(Context context, List<Photo> list, int imageWidth){
        this.mContext = context;
        this.mList = list;
        this.mImageWidth = imageWidth;
    }*/

    public EasyGridviewAdapter(Context context, int imageWidth){
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.mImageWidth = imageWidth;
    }

    public void setData(List<Photo> list){
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setFlag(boolean flag){
        this.flag = flag;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        EasyGridviewAdapter.ViewHolder viewHolder;
        if ( view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
            viewHolder = new EasyGridviewAdapter.ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            viewHolder.delete = (ImageView) view.findViewById(R.id.delete);
            view.setTag( viewHolder);
        }else {
            viewHolder = (EasyGridviewAdapter.ViewHolder) view.getTag();
        }
        if (flag){
            viewHolder.delete.setVisibility(View.VISIBLE);
        }else {
            viewHolder.delete.setVisibility(View.GONE);
        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteImage != null){
                    onDeleteImage.onDelete( i);
                }
            }
        });

        ViewGroup.LayoutParams param = viewHolder.imageView.getLayoutParams();
        param.height = mImageWidth;
        param.width = mImageWidth;

        viewHolder.imageView.setLayoutParams(param);
        Glide.with(mContext).load(mList.get(i).getAddress()).into( viewHolder.imageView);
        return view;
    }

    static class ViewHolder{
        public ImageView imageView;
        public ImageView delete;
    }

    public interface onDeleteImage{
        void onDelete(int position);
    }


    public void setOnDeleteImage(onDeleteImage onDeleteImage){
        this.onDeleteImage = onDeleteImage;
    }
}
