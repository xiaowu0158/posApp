package com.example.wuhongjie.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.adapter.CustItemAdapter;
import com.example.wuhongjie.myapplication.service.CustService;
import com.example.wuhongjie.myapplication.type.Cust;

import java.util.ArrayList;

public class CustSearchActivity extends AppCompatActivity {
    private Button searchBtn;//查询按钮
    private EditText searchEditer;//搜索文本框
    private ProgressBar searchProgress;
    private ListView searchListView;
    private InputMethodManager imm;
    private String curSearchContent = "";
    private View searchFooterView;
    private TextView searchFootMoreTextView;
    private ProgressBar searchFootProgress;
    private CustItemAdapter lvSearchAdapter;
    private ArrayList<Cust> custData=new ArrayList<Cust>();
    private Handler mSearchHandler;
    private int lvSumCnt=0;
    private String yxgsDm="";
    private String quyDm="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_search);
        Intent intent = getIntent();
        yxgsDm=intent.getStringExtra("yxgsDm");
        quyDm=intent.getStringExtra("quyDm");
        System.out.println(yxgsDm+quyDm);
        this.initView();
        this.initData();
    }
    private void initView(){
        //打开输入法窗口

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        searchBtn=(Button)findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new SearchBtnClick());
        searchEditer=(EditText)findViewById(R.id.search_editer);
        searchEditer.setOnFocusChangeListener(new SearchEditorFocusChange());
        searchEditer.setOnKeyListener(new SearchEditorKeyPress());
        searchProgress=(ProgressBar)findViewById(R.id.search_progress);
        searchListView=(ListView)findViewById(R.id.search_listview);
        //加载更多
        searchFooterView = getLayoutInflater().inflate(R.layout.listview_foot, null);
        searchFootMoreTextView = (TextView)searchFooterView.findViewById(R.id.listview_foot_more);
        searchFootProgress = (ProgressBar)searchFooterView.findViewById(R.id.listview_foot_progress);
        searchFooterView.setVisibility(ListView.GONE);
        searchListView.addFooterView(searchFooterView);
        //custData= CustService.getCustData("");
        lvSearchAdapter = new CustItemAdapter(custData, CustSearchActivity.this);
        searchListView.setAdapter(lvSearchAdapter);
        loadSearchData( mSearchHandler,0);
        searchListView.setOnItemClickListener(new SearchListViewItemClick());
        searchListView.setOnScrollListener(new SearchListViewScroll());
    }
    //点查询按钮
    private class SearchBtnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            searchEditer.clearFocus();
            curSearchContent=searchEditer.getText().toString();
            loadSearchData( mSearchHandler,0);
        }
    }
    private class SearchEditorFocusChange implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus){
                imm.showSoftInput(view, 0);
            }
            else{
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
    private class SearchEditorKeyPress implements View.OnKeyListener{
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                if(view.getTag() == null) {
                    view.setTag(1);
                    searchEditer.clearFocus();
                    curSearchContent = searchEditer.getText().toString();
                    loadSearchData( mSearchHandler,0);
                }else{
                    view.setTag(null);
                }
                return true;
            }
            return false;

        }
    }
    private class SearchListViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(view == searchFooterView)
                return;
            Cust cust=(Cust) adapterView.getItemAtPosition(i);
            Intent mIntent = CustSearchActivity.this.getIntent();
            System.out.println(cust.getCustCode()+cust.getCustName());
            mIntent.putExtra("custCode", cust.getCustCode());
            mIntent.putExtra("custName", cust.getCustName());

            CustSearchActivity.this.setResult(-1, mIntent);
            CustSearchActivity.this.finish();
        }
    }
    private class SearchListViewScroll implements AbsListView.OnScrollListener{
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                searchBtn.setClickable(true);
                searchProgress.setVisibility(View.GONE);
                if(msg.what>0){
                    ArrayList<Cust> list = (ArrayList<Cust>)msg.obj;
                    if(lvSumCnt==0){
                        custData.clear();
                        custData.addAll(list);
                    }else{
                        lvSumCnt=lvSumCnt+msg.what;
                        for(int i=0;i<custData.size();i++){
                            for(int j=0;j<list.size();j++){
                                if(!list.get(j).getCustCode().equals(custData.get(i))){
                                    custData.add(list.get(j));
                                }
                            }
                        }
                        if(custData.size()==0){
                            custData.addAll(list);
                        }
                    }
                    if(msg.what < 20){

                        lvSearchAdapter.notifyDataSetChanged();
                        searchFootMoreTextView.setText("已加载全部");
                    }else if(msg.what == 20){
                        //curLvDataState = UIHelper.LISTVIEW_DATA_MORE;
                        lvSearchAdapter.notifyDataSetChanged();
                        searchFootMoreTextView.setText("更多");
                    }
                }
                searchFootProgress.setVisibility(View.GONE);
            }
        };
    }
    private void loadSearchData(final Handler handler,final int action){
        if(curSearchContent.length()<=0){
            Toast.makeText(CustSearchActivity.this,
                    "请输入搜索内容", Toast.LENGTH_SHORT).show();
            return;
        }
        searchBtn.setClickable(false);
        searchProgress.setVisibility(View.VISIBLE);
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<Cust> list= CustService.getCustData(quyDm,curSearchContent);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = action;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }

}
