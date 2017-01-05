package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.ui.adapter.EasyGridviewAdapter;
import com.example.mytime.mvp.ui.adapter.ImageItemAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class CreateNoteActivity extends AppCompatActivity {

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

    String noteTitle;
    String noteContent;
    long noteCreateTime;
    long noteEditTime;
    boolean noteIsEdit;
    ArrayList<Photo> noteAddress;

    Note note;
    //通过数据库获取有id的note
    Note currentNote;
    EasyGridviewAdapter easyGridviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        note = (Note) getIntent().getSerializableExtra("NOTE");
        if (note != null){
            content.setText( note.getContent());
            toolbarTitle.setText("编辑");
            noteAddress = (ArrayList<Photo>) DataSupport.where("objectId = ? and objectType = ?", note.getId() + "", 2 + "").find(Photo.class);
            easyGridviewAdapter = new EasyGridviewAdapter(this, noteAddress);
            gridview.setAdapter( easyGridviewAdapter);
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
                ImageItemAdapter imageItemAdapter = new ImageItemAdapter(images, this);
                gridview.setAdapter(imageItemAdapter);
                imageItemAdapter.notifyDataSetChanged();

                noteAddress = new ArrayList<>();
                for (int i = 0 ; i < images.size(); i++){
                    Photo photo = new Photo();
                    photo.setAddress( images.get(i).path);
                    noteAddress.add( photo);
                }
            } else {
                Toast.makeText(this, "meiyou fanhui shuju", Toast.LENGTH_SHORT).show();
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

    public void submit(){
        noteContent = content.getText().toString();
        if (TextUtils.isEmpty( noteContent)){
            Toast.makeText(this, "便签内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (note == null){
            note = new Note();
            noteTitle = "";
            noteCreateTime = System.currentTimeMillis();
            noteEditTime = noteCreateTime;
            noteIsEdit = false;

            note.setTitle( noteTitle);
            note.setCreateTime( noteCreateTime);
            note.setEditTime( noteEditTime);
            note.setEdit( noteIsEdit);
            note.setAddress( noteAddress);
            note.setContent( noteContent);

            if (note.save()){
                currentNote = DataSupport.where("createTime = ?", note.getCreateTime() + "").findFirst(Note.class);
                if ( noteAddress != null && noteAddress.size() > 0){
                    for (int i = 0 ; i < noteAddress.size(); i++){
                        noteAddress.get(i).setObjectType(2);
                        noteAddress.get(i).setObjectId( currentNote.getId());
                        noteAddress.get(i).save();
                    }
                }
                complete();
            }else {
                Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
            }
            return;
        }else {
            noteEditTime = System.currentTimeMillis();
            noteIsEdit = true;
            note.setEditTime( noteEditTime);
            note.setEdit( noteIsEdit);
            note.setAddress( noteAddress);
            note.setContent( noteContent);
            note.update( note.getId());
            currentNote = DataSupport.where("createTime = ?", note.getCreateTime() + "").findFirst(Note.class);
            if ( noteAddress != null){
                DataSupport.deleteAll(Photo.class, "objectType = ? and objectId = ?", 2 + "", currentNote.getId() + "");
                for (int i = 0 ; i < noteAddress.size(); i++){
                    noteAddress.get(i).setObjectType(2);
                    noteAddress.get(i).setObjectId( currentNote.getId());
                    noteAddress.get(i).save();
                }
            }
            complete();
        }
    }

    public void complete(){
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

    @OnItemClick(R.id.gridview)
    public void onItemClick(int position){
        if (noteAddress != null){
            Intent intent = new Intent(this, ImageZoomActivity.class);
            intent.putExtra("image_path", noteAddress.get( position).getAddress());
            startActivity( intent);
        }

    }
}
