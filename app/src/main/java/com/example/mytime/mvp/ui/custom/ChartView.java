package com.example.mytime.mvp.ui.custom;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.mytime.util.MyUtil;

/**
 * Created by fenghao02 on 2017/4/1.
 * 图表view
 */

public class ChartView extends View {

    private Context mContext;

    private ChartModel mChartModel;

    //屏幕宽度和高度  标题栏高度
    private int windowHeight, windowWidth, titleHeigt;

    //默认高度留白，单位为dp 要转成px
    private static final int DEFAULT_HEIGHT_DISTANCE = 40;
    //默认宽度留白，单位为dp 要转成px
    private static final int DEFAULT_WIDTH_DISTANCE = 20;

    //转成px的高度留白
    private int heightDistance;
    //转成px的宽度留白
    private int widthDistance;
    //转成px的标题高度留白
    private int titleDistance;

    private Path mPath;
    private Paint mPaint;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPath = new Path();
    }

    public int getTitleHeigt() {
        return titleHeigt;
    }

    public void setTitleHeigt(int titleHeigt) {
        this.titleHeigt = titleHeigt;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public ChartModel getChartModel() {
        return mChartModel;
    }

    public void setChartModel(ChartModel chartModel) {
        mChartModel = chartModel;
    }

    public void setData(int windowWidth, int windowHeight, int titleHeigt){
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.titleHeigt = titleHeigt;

        heightDistance = MyUtil.dip2px( mContext, DEFAULT_HEIGHT_DISTANCE);
        widthDistance = MyUtil.dip2px( mContext, DEFAULT_WIDTH_DISTANCE);
        titleDistance = MyUtil.dip2px( mContext, titleHeigt);

        mPaint.setStyle(Paint.Style.STROKE);//设置非填充
        mPaint.setStrokeWidth(5);//笔宽5像素
        mPaint.setColor(Color.RED);//设置为红笔
        mPaint.setAntiAlias(true);//锯齿不显示


        mPath.moveTo(widthDistance, titleDistance + heightDistance);
        mPath.lineTo(widthDistance, windowHeight - heightDistance);
        mPath.lineTo(windowWidth - widthDistance, windowHeight - heightDistance);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath( mPath, mPaint);

    }
}
