package com.example.mytime.mvp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.mytime.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class POIActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener {


    @BindView(R.id.search_layout)
    RelativeLayout searchLayout;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.activity_poi)
    CoordinatorLayout activityPoi;
    @BindView(R.id.edit_content)
    AutoCompleteTextView editContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String keyWord = "";
    private ProgressDialog progDialog;
    private PoiResult poiResult;
    private int currentPage = 0; //当前页面
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
    }

    public void init() {
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString().trim();
                if (!"".equals(newText)) {
                    InputtipsQuery inputquery = new InputtipsQuery(newText, "");
                    Inputtips inputTips = new Inputtips(POIActivity.this, inputquery);
                    inputTips.setInputtipsListener(POIActivity.this);
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.edit_content, R.id.search_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_content:
                break;
            case R.id.search_layout:
                searchClick();
                break;
        }
    }

    public void searchClick() {
        keyWord = editContent.getText().toString();
        if ("".equals(keyWord)) {
            Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
            return;
        } else {
            doSearchQuery();
        }
    }

    private void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 点击下一页按钮
     */
    public void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                Toast.makeText(this, "没有结果", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
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

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
//                        aMap.clear();// 清理之前的图标
//                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
                        Intent intent = new Intent();
                        intent.putExtra("poiitem", poiItems.get( 0));
                        intent.putExtra("address", keyWord);
                        setResult(RESULT_OK, intent);
                        this.finish();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        //没有搜索到信息 展示相关城市信息
//                        showSuggestCity(suggestionCities);
                        Toast.makeText(POIActivity.this, "没有相关信息", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "没有结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "没有结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, rCode + " 错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @OnItemClick(R.id.listView)
    public void onItemClick(int position){
        if (listString != null && listString.size() >0){
            String str = listString.get( position);
            editContent.setText( str);
        }
    }

    private List<String> listString;
    //输入提示回调
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.layout_route_inputs, listString);
            listView.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "没有结果", Toast.LENGTH_SHORT).show();
        }
    }
}
