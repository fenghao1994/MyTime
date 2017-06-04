package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mytime.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/6/4.
 */

public class MyLabelAdapter extends RecyclerView.Adapter<MyLabelAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private OnDeleteLabel onDeleteLabel;
    private boolean flag;

    public void setOnDeleteLabel(OnDeleteLabel onDeleteLabel) {
        this.onDeleteLabel = onDeleteLabel;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public MyLabelAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyLabelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyLabelAdapter.ViewHolder holder, final int position) {
        holder.labelName.setText(list.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteLabel != null){
                    onDeleteLabel.onDeleteLabel(list.get(position));
                }
            }
        });
        if (flag){
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.label_name)
        TextView labelName;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.circle_bg)
        RelativeLayout circleBg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnDeleteLabel{
        void onDeleteLabel(String str);
    }
}
