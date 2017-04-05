package com.example.mytime.mvp.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
    @BindView(R.id.title_dialog)
    EditText mTitleDialog;
    @BindView(R.id.cancel)
    Button mCancel;
    @BindView(R.id.ok)
    Button mOk;
    private Context mContext;

    public TitleDialog(Context context) {
        this(context, R.style.MyDialog);
    }

    public TitleDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_title_dialog);
    }

    @OnClick
    public void okClick(){
        mResultListener.onResultListener( mTitleDialog.getText().toString());
        this.dismiss();
    }

    @OnClick
    public void onCancel(){
        this.dismiss();
    }

    interface ResultListener{
        void onResultListener(String str);
    }

    public ResultListener mResultListener;

    public void setResultListener(ResultListener resultListener) {
        mResultListener = resultListener;
    }
}
