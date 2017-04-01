package com.example.mytime.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.activity.AllNoteActivity;
import com.example.mytime.mvp.ui.activity.AllPlanActivity;
import com.example.mytime.mvp.ui.activity.CountActivity;

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
                showAllPlan();
                break;
            case R.id.complete_layout:
                showCompletePlan();
                break;
            case R.id.note_layout:
                showAllNote();
                break;
            case R.id.count_layout:
                showCountPlan();
                break;
            case R.id.sign_out_layout:
                break;
        }
    }

    private void showCountPlan() {
        Intent intent = new Intent(getActivity(), CountActivity.class);
        startActivity( intent);
    }

    public void showAllNote(){
        Intent intent = new Intent(getActivity(), AllNoteActivity.class);
        startActivity( intent);
    }
    public void showAllPlan(){
        Intent intent = new Intent(getActivity(), AllPlanActivity.class);
        intent.putExtra("COMPLETEPLAN", false);
        startActivity( intent);
    }

    public void showCompletePlan(){
        Intent intent = new Intent(getActivity(), AllPlanActivity.class);
        intent.putExtra("COMPLETEPLAN", true);
        startActivity( intent);
    }
}
