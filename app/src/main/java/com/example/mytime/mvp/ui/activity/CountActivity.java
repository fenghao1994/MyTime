package com.example.mytime.mvp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.ui.custom.ChartView;
import com.example.mytime.util.MyUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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

    private LineChartView mLineChartView;
    private LineChartData data;
    private Toolbar toolbar;
    private ArrayList<PlanItem> allCompletePlanItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        mLineChartView = (LineChartView) findViewById(R.id.chart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List<Line> lines = initLine();
        data = initData(lines);
        mLineChartView.setLineChartData( data);
        Viewport viewport = initViewPort();
        mLineChartView.setMaximumViewport(viewport);
        mLineChartView.setCurrentViewport(viewport);

//        getAllCompletePlanItem();
    }

    private Viewport initViewPort() {
        Viewport viewport = new Viewport();
        viewport.top = 11;//y轴最大值
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
        axisY.setName("数量");
        //设置轴
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);

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

    public void getAllCompletePlanItem(){
        allCompletePlanItems = (ArrayList<PlanItem>) DataSupport.where("isComplete = ?", "1").find( PlanItem.class);
        initData();
    }

    public void initData(){
        Map<String, ArrayList<PlanItem>> map = new HashMap<>();
        for (int i = 0 ;i < allCompletePlanItems.size() ;i++){
            String date = MyUtil.dateYMD(allCompletePlanItems.get(i).getEditTime());
            if ( map.containsKey(date)){
                map.get(date).add( allCompletePlanItems.get(i));
            }else {
                ArrayList<PlanItem> arrayList = new ArrayList();
                arrayList.add(allCompletePlanItems.get(i));
                map.put(date, arrayList);
            }
        }
        for (Map.Entry<String, ArrayList<PlanItem>> entry : map.entrySet()){
            String key = entry.getKey();
            int count = entry.getValue().size();

        }
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
