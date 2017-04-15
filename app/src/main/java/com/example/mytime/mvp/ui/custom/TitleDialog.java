package com.example.mytime.mvp.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fenghao02 on 2017/4/5.
 * 创建一个plan时，生成这个dialog
 */

public class TitleDialog extends Dialog {
    EditText mTitleDialog;
    Button mCancel;
    Button mOk;
    private Context mContext;
    private String str;

    public TitleDialog(Context context, String str) {
        super(context);
        this.mContext = context;
        this.str = str;
    }


    public TitleDialog(Context context, int themeResId, String str) {
        super(context, themeResId);
        this.mContext = context;
        this.str = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.layout_title_dialog);
        mTitleDialog = (EditText) this.getWindow().findViewById(R.id.title_dialog);
        mCancel = (Button) this.getWindow().findViewById(R.id.cancel);
        mOk = (Button) this.getWindow().findViewById(R.id.ok);

        if (str != null || !"".equals(str)) {
            mTitleDialog.setText(str);
            mTitleDialog.setSelection(str.length());
        }
//        setTitle("请输入计划表名");

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClick();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }


    public void okClick() {
        mResultListener.onResultListener(mTitleDialog.getText().toString());
    }

    public void onCancel() {
        this.dismiss();
    }

    public interface ResultListener {
        void onResultListener(String str);
    }

    public ResultListener mResultListener;

    public void setResultListener(ResultListener resultListener) {
        mResultListener = resultListener;
    }
}
