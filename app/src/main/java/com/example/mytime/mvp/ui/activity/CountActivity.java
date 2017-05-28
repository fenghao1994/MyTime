package com.example.mytime.mvp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.util.EditTimeSortFromBToS;
import com.example.mytime.util.EditTimeSortFromSToB;
import com.example.mytime.util.MyUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
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

        getAllCompletePlanItem();
    }

    private Viewport initViewPort() {
        Viewport viewport = new Viewport();
        viewport.top = 11;//y轴最大值
        viewport.bottom = 0;
        viewport.left = 0;
        viewport.right = 7;//x轴显示7列
        return viewport;

    }

    public void getAllCompletePlanItem(){
        allCompletePlanItems = (ArrayList<PlanItem>) DataSupport.where("isComplete = ?", "1").find( PlanItem.class);
        initData();
    }

    public void initData(){
        Map<String, ArrayList<PlanItem>> map = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        ArrayList<PointValue> pointValues = new ArrayList<>();

        List<Line> lineList = new ArrayList<>();
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
            keyList.add(key);
            countList.add(count);
        }

//        Collections.reverse(keyList);
//        Collections.reverse(countList);
        ArrayList<AxisValue> aX = new ArrayList<>();
        ArrayList<AxisValue> aY = new ArrayList<>();
        for (int i = 0 ;i < keyList.size(); i++){
            aX.add(new AxisValue(i + 1).setValue(i + 1).setLabel(keyList.get(i)));
            aY.add(new AxisValue(i + 1).setValue(countList.get(i)));
            pointValues.add(new PointValue(i + 1, countList.get(i)));
        }

        Axis axisX = new Axis();
        Axis axisY = new Axis();
        axisX.setValues( aX);
        axisX.setName("时间");
        axisY.setValues( aY);
        axisY.setName("数量");
        axisX.setHasTiltedLabels(true);// 设置X轴文字向左旋转45度

        Line line = new Line(pointValues);
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setShape(ValueShape.CIRCLE);
        lineList.add(line);
        LineChartData data = new LineChartData(lineList);
        //设置轴
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);

        mLineChartView.setLineChartData( data);
        Viewport viewport = initViewPort();
        mLineChartView.setMaximumViewport(viewport);
        mLineChartView.setCurrentViewport(viewport);
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
