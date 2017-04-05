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
import com.example.mytime.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 完成统计activity
 */
public class CountActivity extends AppCompatActivity {

    private ChartView mChartView;
    private ImageView back;
    private TextView title;
    private RelativeLayout relative;
    private LineChartView mLineChartView;
    private LineChartData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        mChartView = (ChartView) findViewById(R.id.chart_view);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        relative = (RelativeLayout) findViewById(R.id.relative);

        mLineChartView = (LineChartView) findViewById(R.id.chart);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountActivity.this.finish();
            }
        });
        List<Line> lines = initLine();
        data = initData(lines);
        mLineChartView.setLineChartData( data);
        Viewport viewport = initViewPort();
        mLineChartView.setMaximumViewport(viewport);
        mLineChartView.setCurrentViewport(viewport);

      /*  DisplayMetrics dm = new DisplayMetrics();
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
        });*/
    }

    private Viewport initViewPort() {
        Viewport viewport = new Viewport();
        viewport.top = 10;//y轴最大值
        viewport.bottom = 0;
        viewport.left = 0;
        viewport.right = 7;//x轴显示7列
        return viewport;

    }

    private LineChartData initData(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        //初始化轴
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("时间");
        //前加字符
//        axisX.setFormatter(new SimpleAxisValueFormatter().setPrependedText("aaaa".toCharArray()));
        //后加字符
//        axisX.setFormatter(new SimpleAxisValueFormatter().setAppendedText("aaaa".toCharArray()));
//        axisX.setFormatter(new SimpleAxisValueFormatter());
        axisY.setName("销量");
        //设置轴
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        //设置负值 设置为负无穷 默认为0
//        data.setBaseValue(Float.NEGATIVE_INFINITY);

        return data;

    }

    private List<Line> initLine() {
        List<Line> lineList = new ArrayList<>();

        List<PointValue> pointValueList = new ArrayList<>();
        PointValue pointValue1 = new PointValue(1,4);
        pointValueList.add(pointValue1);
        PointValue pointValue2 = new PointValue(2,3);
        pointValueList.add(pointValue2);
        PointValue pointValue3 = new PointValue(3,6);
        pointValueList.add(pointValue3);
        PointValue pointValue4 = new PointValue(4,1);
        pointValueList.add(pointValue4);
        PointValue pointValue5 = new PointValue(5,5);
        pointValueList.add(pointValue5);
        PointValue pointValue6 = new PointValue(6,0);
        pointValueList.add(pointValue6);
        PointValue pointValue7 = new PointValue(7, 10);
        pointValueList.add(pointValue7);

        Line line = new Line(pointValueList);
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setShape(ValueShape.CIRCLE);
        lineList.add(line);

        return lineList;

    }

    /*private int getStatusBarHeight() {
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
    }*/
}
