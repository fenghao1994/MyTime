package com.example.mytime.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 侧栏fragment
 */

public class NavigationFragment extends Fragment {

    @BindView(R.id.header_img)
    CircleImageView headerImg;
    @BindView(R.id.plan_layout)
    LinearLayout planLayout;
    @BindView(R.id.complete_layout)
    LinearLayout completeLayout;
    @BindView(R.id.note_layout)
    LinearLayout noteLayout;
    @BindView(R.id.count_layout)
    LinearLayout countLayout;
    @BindView(R.id.sign_out_layout)
    LinearLayout signOutLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.header_img, R.id.plan_layout, R.id.complete_layout, R.id.note_layout, R.id.count_layout, R.id.sign_out_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_img:
                break;
            case R.id.plan_layout:
                break;
            case R.id.complete_layout:
                break;
            case R.id.note_layout:
                break;
            case R.id.count_layout:
                break;
            case R.id.sign_out_layout:
                break;
        }
    }
}
