package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.RiJi;
import com.example.mytime.mvp.ui.adapter.RiJiAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiJiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    List<RiJi> riJiList = new ArrayList<>();
    RiJiAdapter riJiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ri_ji);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiJiActivity.this, CreateRiJiActivity.class);
                startActivity(intent);
            }
        });
        riJiList.addAll(DataSupport.order("createTime desc").find(RiJi.class));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        riJiAdapter = new RiJiAdapter(this, riJiList);
        recyclerView.setAdapter(riJiAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        riJiList.clear();
        riJiList.addAll(DataSupport.order("createTime desc").find(RiJi.class));
        riJiAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return true;
    }
}
