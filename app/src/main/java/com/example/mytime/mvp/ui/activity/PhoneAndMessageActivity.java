package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.util.MyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneAndMessageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.ok)
    ImageView mOk;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.phone_address)
    ImageView mPhoneAddress;
    @BindView(R.id.message_content)
    EditText mMessageContent;
    @BindView(R.id.message_layout)
    RelativeLayout mMessageLayout;
    @BindView(R.id.activity_phone_and_message)
    LinearLayout mActivityPhoneAndMessage;

    private boolean isOnlyPhone;
    private String phone, message;

    private Uri uri_DATA = Uri.parse("content://com.android.contacts/data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_and_message);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        Intent intent = getIntent();
        isOnlyPhone = intent.getBooleanExtra("ONLYPHONE", false);
        phone = intent.getStringExtra("PHONE");
        message = intent.getStringExtra("MESSAGE");
        if (phone != null && !phone.equals("null")){
            mPhoneNumber.setText( phone);
        }
        if ( message != null && !message.equals("null")){
            mMessageContent.setText( message);
        }
        if ( isOnlyPhone){
            mMessageLayout.setVisibility(View.GONE);
        }else {
            mMessageLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ok)
    public void clickOk(){
        String phone = mPhoneNumber.getText().toString().trim();
        String message = mMessageContent.getText().toString();
        if ( isOnlyPhone){
            if ( !"".equals( phone) && !MyUtil.isMobileNumber( phone)){
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("PHONENUMBER", phone);
            setResult(CreatePlanItemActivity.ONLY_PHONE, intent);
            finish();
        }else {
            if ( !"".equals( phone) && !MyUtil.isMobileNumber( phone)){
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if ( (phone.equals("") && !message.equals("")) || (!phone.equals("") && message.equals(""))){
                Toast.makeText(this, "请输入手机号或者短信内容，或者两者都不填", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("PHONENUMBER", phone);
            intent.putExtra("MESSAGECONTENT", message);
            setResult(CreatePlanItemActivity.MESSAGE_CONTENT, intent);
            finish();
        }
    }

    @OnClick(R.id.phone_address)
    public void openPhoneAddress(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) { //请求码为1
            String strNumber = "";
            if(data != null) {  //判断返回的intent是不是为空
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                while(cursor.moveToNext()) {
                    String strID = cursor.getString(cursor.getColumnIndex("name_raw_contact_id"));
                    Cursor cursor2 = getContentResolver().query(uri_DATA, null,
                            "raw_contact_id = " + strID + " and mimetype_id = 5", null, null);
                    if(cursor2.moveToFirst()) {
                        strNumber = cursor2.getString(cursor2.getColumnIndex("data1"));
                        strNumber = strNumber.replace(" ", "");
                        strNumber = strNumber.replace("-", "");
                    }
                    cursor2.close();
                }
                cursor.close();
                phone = strNumber;
                mPhoneNumber.setText( phone);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


}
