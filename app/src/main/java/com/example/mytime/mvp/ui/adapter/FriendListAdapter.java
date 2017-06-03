package com.example.mytime.mvp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Friend;
import com.example.mytime.mvp.ui.activity.FriendInfoActivity;
import com.example.mytime.util.HttpUrl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fenghao on 2017/6/3.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<Friend> friends;
    private Context context;

    private OnChangeRelationShip onChangeRelationShip;

    public void setOnChangeRelationShip(OnChangeRelationShip onChangeRelationShip) {
        this.onChangeRelationShip = onChangeRelationShip;
    }

    public FriendListAdapter(List<Friend> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        FriendListAdapter.ViewHolder viewHolder = new FriendListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FriendListAdapter.ViewHolder holder, final int position) {
        if (friends.get(position).getUser().getHeadImg().contains("D:\\")) {
            String s = friends.get(position).getUser().getHeadImg();
            s = s.substring(3, s.length());
            s = s.replace("\\", "/");
            Glide.with(context).load(HttpUrl.ROOT + "/" + s).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.headerImg);
        } else {
            Glide.with(context).load(friends.get(position).getUser().getHeadImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into( holder.headerImg);
        }
        holder.userName.setText(friends.get(position).getUser().getUserName() == null ? "": friends.get(position).getUser().getUserName());
        holder.phoneNumber.setText(friends.get(position).getUser().getPhoneNumber());
        holder.signature.setText(friends.get(position).getUser().getPersonalizedSignature() == null ? "" : friends.get(position).getUser().getPersonalizedSignature());
        String active = friends.get(position).getUserActive();
        if (!TextUtils.isEmpty(active) && active.equals("WATCH")){
            holder.watchChoose.setChecked(true);
            holder.hiteChoose.setChecked(false);
            holder.defaultChoose.setChecked(false);
        }else if (!TextUtils.isEmpty(active) && active.equals("HITE")){
            holder.watchChoose.setChecked(false);
            holder.hiteChoose.setChecked(true);
            holder.defaultChoose.setChecked(false);
        }else {
            holder.watchChoose.setChecked(false);
            holder.hiteChoose.setChecked(false);
            holder.defaultChoose.setChecked(true);
        }


        holder.watchChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    friends.get(position).setUserActive("WATCH");
                    if (onChangeRelationShip != null){
                        onChangeRelationShip.onChangeRelationShip(friends.get(position));
                    }
                }
            }
        });

        holder.hiteChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    friends.get(position).setUserActive("HITE");
                    if (onChangeRelationShip != null){
                        onChangeRelationShip.onChangeRelationShip(friends.get(position));
                    }
                }

            }
        });

        holder.defaultChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    friends.get(position).setUserActive("");
                    if (onChangeRelationShip != null){
                        onChangeRelationShip.onChangeRelationShip(friends.get(position));
                    }
                }

            }
        });

        holder.friendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendInfoActivity.class);
                intent.putExtra("FRIEND", friends.get(position).getUser());
                context.startActivity(intent);
            }
        });
    }

    public interface OnChangeRelationShip{
        void onChangeRelationShip(Friend friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header_img)
        ImageView headerImg;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.phone_number)
        TextView phoneNumber;
        @BindView(R.id.signature)
        TextView signature;
        @BindView(R.id.default_choose)
        RadioButton defaultChoose;
        @BindView(R.id.watch_choose)
        RadioButton watchChoose;
        @BindView(R.id.hite_choose)
        RadioButton hiteChoose;
        @BindView(R.id.friend_layout)
        CardView friendLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
