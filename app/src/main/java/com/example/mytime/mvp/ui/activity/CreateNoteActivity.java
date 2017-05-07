package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.http_callback.UserCallBack;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.presenter.ICreateNotePresenter;
import com.example.mytime.mvp.presenter.impl.CreateNotePresenterImpl;
import com.example.mytime.mvp.ui.adapter.EasyGridviewAdapter;
import com.example.mytime.mvp.ui.adapter.ImageItemAdapter;
import com.example.mytime.mvp.ui.view.ICreateNoteView;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;

public class CreateNoteActivity extends AppCompatActivity implements ICreateNoteView , EasyGridviewAdapter.onDeleteImage {

    private static final int IMAGE_PICKER = 1;

    ArrayList<ImageItem> images;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.photo_gou)
    ImageView photoGou;

    String noteTitle;
    String noteContent;
    long noteCreateTime;
    long noteEditTime;
    boolean noteIsEdit;
    ArrayList<Photo> noteAddress;
    Note note;
    EasyGridviewAdapter easyGridviewAdapter;
    ICreateNotePresenter createNotePresenter;
    private AlertDialog alertDialog;

    private int mWidth;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getWidth();
        easyGridviewAdapter = new EasyGridviewAdapter(this, mWidth);
        gridview.setAdapter(easyGridviewAdapter);

        createNotePresenter = new CreateNotePresenterImpl(this);
        note = (Note) getIntent().getSerializableExtra("NOTE");
        if (note != null) {
            createNotePresenter.showData(note);
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CreateNoteActivity.this, ImageZoomActivity.class);
                intent.putExtra("image_path", noteAddress.get(position).getAddress());
                startActivity(intent);
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                easyGridviewAdapter.setFlag(true);
                easyGridviewAdapter.notifyDataSetChanged();
                return true;
            }
        });

        easyGridviewAdapter.setOnDeleteImage(this);

        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);


    }

    /**
     * 获取屏幕宽度的1/3
     */
    public void getWidth(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        mWidth = display.getWidth() / 3;
    }

    @Override
    public void showData(Note note, List<Photo> list) {
        content.setText(note.getContent());

        //光标置于文末处
        content.setSelection(note.getContent().length());
        easyGridviewAdapter.setData(list);
        toolbarTitle.setText("编辑");

        noteAddress = (ArrayList<Photo>) list;
        note.setAddress( noteAddress);

        if (note.getAddress() != null && note.getAddress().size() > 0){
            photoGou.setVisibility(View.VISIBLE);
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                /*ImageItemAdapter imageItemAdapter = new ImageItemAdapter(images, this, mWidth);
                gridview.setAdapter(imageItemAdapter);
                imageItemAdapter.notifyDataSetChanged();*/
                noteAddress = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    Photo photo = new Photo();
                    photo.setAddress(images.get(i).path);
                    noteAddress.add(photo);
                }
                easyGridviewAdapter.setData(noteAddress);
                if (images != null && images.size() > 0){
                    photoGou.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.toolbar_title, R.id.ok, R.id.toolbar, R.id.content, R.id.photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                break;
            case R.id.ok:
                submit();
                break;
            case R.id.toolbar:
                break;
            case R.id.content:
                break;
            case R.id.photo:
                takePhoto();
                break;
        }
    }

    public void submit() {
        noteContent = content.getText().toString();
        if (TextUtils.isEmpty(noteContent)) {
            Toast.makeText(this, "便签内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (note == null) {
            note = new Note();
            noteTitle = "";
            noteCreateTime = System.currentTimeMillis();
            noteEditTime = noteCreateTime;
            noteIsEdit = false;
            note.setTitle(noteTitle);
            note.setCreateTime(noteCreateTime);
            note.setEditTime(noteEditTime);
            note.setEdit(noteIsEdit);
            note.setContent(noteContent);
            note.setAddress(noteAddress);

            //需要上传到服务器
            if (Extra.NET_WORK == 2){
                saveObjectWithNetWork();
            }

            createNotePresenter.saveNote(note, noteAddress);

        } else {
            noteEditTime = System.currentTimeMillis();
            noteIsEdit = true;
            note.setEditTime(noteEditTime);
            note.setEdit(noteIsEdit);
            note.setAddress(noteAddress);
            note.setContent(noteContent);

            //需要上传到服务器
            if (Extra.NET_WORK == 2){
                updateObjectWithNetWork();
            }

            createNotePresenter.update(note, noteAddress);
        }
    }

    @Override
    public void complete() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        this.finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onDelete(final int position) {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("确定要删除吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteAddress.remove(position);
                        easyGridviewAdapter.setFlag(false);
                        easyGridviewAdapter.setData(noteAddress);

//                        su
                        alertDialog.dismiss();
                        if (noteAddress.size() == 0){
                            photoGou.setVisibility(View.GONE);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        return;
                    }
                })
                .show();
    }
    
    public void saveObjectWithNetWork(){
        postUrl(HttpUrl.POST_SAVE_NOTE);
    }
    public void updateObjectWithNetWork(){
        postUrl(HttpUrl.POST_UPDATA_NOTE);
    }
    public void postUrl(String url){
        Map<String, File> map = new HashMap<>();
        for (int i = 0 ; i < noteAddress.size(); i++){
            File file = new File(noteAddress.get(i).getAddress());
            map.put("file" + i, file);
        }

        OkHttpUtils
                .post()
                .url(url)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("id", note.getId() + "")
                .addParams("title", note.getTitle())
                .addParams("content", note.getContent())
                .addParams("createTime", note.getCreateTime() + "")
                .addParams("editTime", note.getEditTime() + "")
                .addParams("isEdit", note.isEdit() + "")
                .addParams("isDelete", note.isDelete() + "")
                .files("address", map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }
}
