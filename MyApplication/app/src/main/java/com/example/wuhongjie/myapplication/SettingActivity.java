package com.example.wuhongjie.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.adapter.AreaItemAdapter;
import com.example.wuhongjie.myapplication.adapter.YxgsItemAdapter;
import com.example.wuhongjie.myapplication.service.CustService;
import com.example.wuhongjie.myapplication.service.QuyService;
import com.example.wuhongjie.myapplication.service.YxgsService;
import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.type.Yxgs;
import com.example.wuhongjie.myapplication.util.WebUtils;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private ArrayList<Yxgs> yxgsList = new ArrayList<Yxgs>();
    private YxgsItemAdapter yxgsItemAdapter;
    private ArrayList<Area> areaList = new ArrayList<Area>();
    private AreaItemAdapter areaItemAdapter;
    private ImageButton custSearchButton;
    private TextView custSearchTextView;
    private Button custPostButton;
    private Button custReturnButton;
    private Handler mSearchHandler;
    private String yxgsDm="";
    private String quyDm="";
    private String custCode="";
    private String custName="";
    private static int LOAD_YXGS_ACTION=0;
    private static int LOAD_QUY_ACTION=1;
    private SharedPreferences sharedPreferences;
    private GridView areaGridview;
    private Switch switch1;
    private boolean specNumFocus=false;
    private boolean bigFond=false;
    private Switch switch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        GridView yxgsGridview = (GridView) findViewById(R.id.yxgs_gridView);
        //yxgsList= YxgsService.getYxgsData();
        initData();
        sharedPreferences=this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        yxgsDm=sharedPreferences.getString("yxgsDm","");
        quyDm=sharedPreferences.getString("quyDm","");
        custCode=sharedPreferences.getString("custCode","");
        custName=sharedPreferences.getString("custName","");
        specNumFocus=sharedPreferences.getBoolean("specNumFocus",false);
        bigFond=sharedPreferences.getBoolean("bigFond",false);
        if(!WebUtils.isNetworkAvailable(SettingActivity.this)){

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
             builder.setMessage("此功能需要网络连接，WIFI没有连上");
             builder.setTitle("提示");
             builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                     SettingActivity.this.finish();
               }
             });
            builder.create().show();
        }
        yxgsGridview.setNumColumns(5);
        yxgsItemAdapter=new YxgsItemAdapter(yxgsList,SettingActivity.this);

        yxgsGridview.setAdapter(yxgsItemAdapter);
        loadYxgsData(mSearchHandler);
        yxgsGridview.setOnItemClickListener(new ItemClickListener());
        areaGridview = (GridView) findViewById(R.id.quyGridView);
        areaGridview.setNumColumns(5);
        areaItemAdapter=new AreaItemAdapter(areaList,SettingActivity.this);
        areaGridview.setOnItemClickListener(new AreaGridItemClickListener());
        areaGridview.setAdapter(areaItemAdapter);
        if(yxgsDm.length()>0){
            loadAreaData(mSearchHandler,yxgsDm,quyDm);
        }
        switch1=(Switch)findViewById(R.id.switch1);
        switch1.setChecked(specNumFocus);
        switch2=(Switch)findViewById(R.id.switch2);
        switch2.setChecked(bigFond);
        custSearchTextView=(TextView)findViewById(R.id.custSearchTextView);
        custSearchTextView.setText(custName);
        custSearchButton = (ImageButton)findViewById(R.id.custSearchButton);
        custSearchButton.setOnClickListener(new CustSearchButtonClick());

        custPostButton=(Button)findViewById(R.id.custPostButton);
        custPostButton.setOnClickListener(new CustPostButtonClick());
        custReturnButton=(Button)findViewById(R.id.custReturnButton);
        custReturnButton.setOnClickListener(new CustReturnButtonClick());
    }

    //营销公司选中
    class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Yxgs yxgs=(Yxgs) adapterView.getItemAtPosition(i);
            yxgs.setSelected(!yxgs.isSelected());
            for(int j=0;j<yxgsList.size();j++){
                if(j!=i){
                    yxgsList.get(j).setSelected(false);
                }
            }
            if(yxgs.isSelected()){
                yxgsDm=yxgs.getYxgsDm();
            }else{
                yxgsDm="";
            }
            yxgsItemAdapter.notifyDataSetChanged();
            if(yxgs.isSelected()){
                //营销公司被选中
                quyDm="";
                //areaList= QuyService.getQuyData("01");
                loadAreaData(mSearchHandler,yxgs.getYxgsDm(),"");
            }else{
                GridView areaGridview = (GridView) findViewById(R.id.quyGridView);
                areaGridview.setAdapter(null);
            }
        }
    }
    //区域选中
    class AreaGridItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Area area=(Area) adapterView.getItemAtPosition(i);
            area.setSelected(!area.isSelected());
            for(int j=0;j<areaList.size();j++){
                if(j!=i){
                    areaList.get(j).setSelected(false);
                }
            }
            if(area.isSelected()){
                quyDm=area.getQuyDm();
            }else{
                quyDm="";
            }
            areaItemAdapter.notifyDataSetChanged();
        }
    }
    //客户搜索
    private class CustSearchButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(yxgsDm.length()<=0){
                Toast.makeText(SettingActivity.this,
                        "请选择一个营销公司", Toast.LENGTH_SHORT).show();
                return;
            }
            if(quyDm.length()<=0){
                Toast.makeText(SettingActivity.this,
                        "请选择一个区域", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(view.getContext(), CustSearchActivity.class);
            intent.putExtra("yxgsDm",yxgsDm);
            intent.putExtra("quyDm",quyDm);
            startActivityForResult(intent,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode==RESULT_OK){
           Bundle bunde = data.getExtras();
           custCode = bunde.getString("custCode");
           custName=bunde.getString("custName");
           System.out.println("客户："+custName+data.getStringExtra("custName"));
           custSearchTextView.setText(custName);

           setTitle(custName);

       }
    }
    private class CustPostButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(yxgsDm.length()<=0){
                Toast.makeText(SettingActivity.this,
                        "请选择一个营销公司", Toast.LENGTH_SHORT).show();
                return;
            }
            if(quyDm.length()<=0){
                Toast.makeText(SettingActivity.this,
                        "请选择一个区域", Toast.LENGTH_SHORT).show();
                return;
            }
            if(custCode.length()<=0){
                Toast.makeText(SettingActivity.this,
                        "请选择一个客户", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("yxgsDm", yxgsDm);
            editor.putString("quyDm", quyDm);
            editor.putString("custCode", custCode);
            editor.putString("custName",custName);
            editor.putBoolean("specNumFocus",switch1.isChecked());
            editor.putBoolean("bigFond",switch2.isChecked());
            editor.commit();
            Intent mIntent = SettingActivity.this.getIntent();
            mIntent.putExtra("custCode", custCode);
            mIntent.putExtra("custName", custName);

            SettingActivity.this.setResult(RESULT_OK, mIntent);
            SettingActivity.this.finish();
        }
    }
    private class CustReturnButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            SettingActivity.this.finish();
        }
    }
    private void loadYxgsData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<Yxgs> list= YxgsService.getYxgsData(yxgsDm);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_YXGS_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadAreaData(final Handler handler,final String yxgsDm,final String quyDm){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<Area> list= QuyService.getQuyData(yxgsDm,quyDm);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_QUY_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==LOAD_YXGS_ACTION){
                    if(msg.what>0){
                        System.out.println("sssss");
                        ArrayList<Yxgs> list = (ArrayList<Yxgs>)msg.obj;
                        System.out.println(list.get(0).getYxgsMc());
                        yxgsList.clear();
                        yxgsList.addAll(list);
                        yxgsItemAdapter.notifyDataSetChanged();

                    }
                }
                if(msg.arg1==LOAD_QUY_ACTION){
                    if(msg.what>0){
                        System.out.println("sssss");
                        ArrayList<Area> list = (ArrayList<Area>)msg.obj;
                       // System.out.println(list.get(0).getYxgsMc());
                        areaList.clear();
                        areaList.addAll(list);
                        areaItemAdapter.notifyDataSetChanged();

                    }
                }

            }
        };
    }

}
