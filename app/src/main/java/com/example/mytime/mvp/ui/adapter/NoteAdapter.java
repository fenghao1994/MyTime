package com.example.mytime.mvp.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.ui.activity.CreateNoteActivity;
import com.example.mytime.mvp.ui.activity.MainActivity;
import com.example.mytime.util.Extra;
import com.example.mytime.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/1.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> mList = new ArrayList<>();
    private Context mContext;
    private AlertDialog alertDialog;

    public NoteAdapter(Context context,List<Note> list){
        this.mContext = context;
        initData(list);
    }

    public void initData(List<Note> list){
        for (int i = 0 ; i < list.size(); i++){
            if ( !list.get(i).isDelete()){
                this.mList.add( list.get(i));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        ViewHolder viewHolder = new ViewHolder( view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.noteText.setText( mList.get( position).getContent());
        holder.timeText.setText(MyUtil.dateYMDHM(mList.get( position).getEditTime()));
        holder.noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CreateNoteActivity.class);
                intent.putExtra("NOTE", mList.get(position));
                mContext.startActivity( intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext)
                        .setMessage("确定要删除吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mList.get(position).setDelete(true);
                                mList.get(position).update(mList.get(position).getId());
                                ((MainActivity)mContext).deleteNote(mList.get(position));
                                mList.remove( position);
                                notifyDataSetChanged();
                                alertDialog.dismiss();
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
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
        animater(holder.noteView, position);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_text)
        TextView noteText;
        @BindView(R.id.time_text)
        TextView timeText;
        @BindView(R.id.delete)
        ImageView delete;

        View noteView;

        ViewHolder(View view) {
            super(view);
            noteView = view;
            ButterKnife.bind(this, view);
        }
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
