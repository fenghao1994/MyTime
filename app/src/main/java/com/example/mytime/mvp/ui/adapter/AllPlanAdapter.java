package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.util.MyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/10.
 */

public class AllPlanAdapter  extends RecyclerView.Adapter<AllPlanAdapter.ViewHolder>{
    private List<Plan> mList;
    private Context mContext;

    public AllPlanAdapter(Context context, List<Plan> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_note, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.content.setText( mList.get( position).getTitle());
        holder.createTime.setText(MyUtil.dateYMDHM(mList.get( position).getCreateTime()));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.create_time)
        TextView createTime;

        View noteView;

        ViewHolder(View view) {
            super(view);
            noteView = view;
            ButterKnife.bind(this, view);
        }
    }
}
