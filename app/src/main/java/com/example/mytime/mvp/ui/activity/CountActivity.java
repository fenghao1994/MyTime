package com.example.mytime.mvp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.custom.ChartView;

import java.lang.reflect.Field;

/**
 * 完成统计activity
 */
public class CountActivity extends AppCompatActivity {

    private ChartView mChartView;
    private ImageView back;
    private TextView title;
    private RelativeLayout relative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        mChartView = (ChartView) findViewById(R.id.chart_view);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        relative = (RelativeLayout) findViewById(R.id.relative);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountActivity.this.finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int windowWidth = dm.widthPixels;
        final int windowHeight =dm.heightPixels;

        relative.post(new Runnable() {
            @Override
            public void run() {
                int titleHeight = relative.getHeight();
                mChartView.setData(windowWidth, windowHeight - getStatusBarHeight() - titleHeight, 0);
                mChartView.invalidate();
            }
        });
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = this.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
