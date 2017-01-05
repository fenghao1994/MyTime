package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.ui.activity.CreateNoteActivity;
import com.example.mytime.util.MyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/1/1.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> mList;
    private Context mContext;

    public NoteAdapter(Context context,List<Note> list){
        this.mContext = context;
        this.mList = list;
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

        View noteView;

        ViewHolder(View view) {
            super(view);
            noteView = view;
            ButterKnife.bind(this, view);
        }
    }
}
