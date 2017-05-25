package com.example.mytime.mvp.ui.custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mytime.R;

/**
 * Created by fenghao on 2017/5/25.
 */

public class SharePopWindow extends PopupWindow {

    private Context mContext;
    private View mView;
    private LinearLayout qqLayout;
    private LinearLayout wxLayout;
    private LinearLayout pengLayout;


    private QQShareOnClick qqShareOnClick;
    private WXShareOnClick wxShareOnClick;
    private PYShareOnClick pyShareOnClick;

    public void setQqShareOnClick(QQShareOnClick qqShareOnClick) {
        this.qqShareOnClick = qqShareOnClick;
    }

    public void setWxShareOnClick(WXShareOnClick wxShareOnClick) {
        this.wxShareOnClick = wxShareOnClick;
    }

    public void setPyShareOnClick(PYShareOnClick pyShareOnClick) {
        this.pyShareOnClick = pyShareOnClick;
    }

    public SharePopWindow(Context context) {
        super(context);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.layout_share, null);
        qqLayout = (LinearLayout) mView.findViewById(R.id.layout_share_qq);
        wxLayout = (LinearLayout) mView.findViewById(R.id.layout_share_weixin);
        pengLayout = (LinearLayout) mView.findViewById(R.id.layout_share_pengyou);

        qqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qqShareOnClick != null){
                    qqShareOnClick.qqShareClick();
                }
            }
        });

        wxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wxShareOnClick != null){
                    wxShareOnClick.wxShareClick();
                }
            }
        });

        pengLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pyShareOnClick != null){
                    pyShareOnClick.pyShareClick();
                }
            }
        });

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(null);
    }

    public interface QQShareOnClick{
        void qqShareClick();
    }
    public interface WXShareOnClick{
        void wxShareClick();
    }
    public interface PYShareOnClick{
        void pyShareClick();
    }

}
