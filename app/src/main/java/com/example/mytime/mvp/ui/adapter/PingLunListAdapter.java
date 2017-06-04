package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PingLun;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by fenghao on 2017/6/4.
 */

public class PingLunListAdapter extends BaseAdapter {
    private Context context;
    private List<PingLun> lunList;

    public PingLunListAdapter(Context context, List<PingLun> lunList) {
        this.context = context;
        this.lunList = lunList;
    }

    @Override
    public int getCount() {
        return lunList.size();
    }

    @Override
    public Object getItem(int position) {
        return lunList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ping_lun, null);
            viewHolder = new ViewHolder();
            viewHolder.headerImg = (CircleImageView) convertView.findViewById(R.id.header_img);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag( viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(lunList.get(position).getPhoneNumber());
        viewHolder.time.setText(MyUtil.dateYMDHM(lunList.get(position).getEditTime()));
        viewHolder.content.setText(lunList.get(position).getContent());
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_HEADIMG)
                .addParams("phoneNumber", lunList.get(position).getPhoneNumber())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "获取头像失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        response = response.substring(3, response.length());
                        response = response.replace("\\", "/");
                        Glide.with(context).load(HttpUrl.ROOT + "/" + response).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.headerImg);
                    }
                });
        return convertView;
    }

    static class ViewHolder {
        CircleImageView headerImg;
        TextView name;
        TextView time;
        TextView content;
    }
}
