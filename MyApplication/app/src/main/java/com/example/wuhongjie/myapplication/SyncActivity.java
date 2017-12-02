package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evangelsoft.econnect.dataformat.CharacterCase;
import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordFieldFormat;
import com.evangelsoft.econnect.dataformat.RecordFormat;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.evangelsoft.econnect.util.Encrypter;
import com.example.wuhongjie.myapplication.service.ProdClsService;
import com.example.wuhongjie.myapplication.service.SynService;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.DBhelper;
import com.example.wuhongjie.myapplication.util.WebUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class SyncActivity extends Activity {

    private Button downloadButton;
    private Button clearButton;

    private Handler mSearchHandler;
    private Context context;
    private static int DOWNLOAD_ACTION=0;
    private static int UPLOAD_ACTION=1;
    private static int CLEAR_ACTION=2;
    private static int DOWNLOAD_CUST_ACTION=3;
    private static int DOWNLOAD_ORDER_PROD_ACTION=4;
    private static int DOWNLOAD_SPEC_ACTION=5;
    private static int DOWNLOAD_SPEC_LINK_ACTION=6;
    private String custCode;
    public String userCode="";
    private ProgressBar progressBar;
    private TextView progressValue;
    private FrameLayout downLoadingFrameLayout;
    private Button uploadButton;
    private ProgressDialog xh_pDialog;
    private Button downLoadCustButton;
    private Button downLoadOrderProdButton;
    private Button downLoadSpecButton;
    private SharedPreferences sharedPreferences;
    private Button downLoadSpecLinkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        context=SyncActivity.this;

        Intent intent = getIntent();
        if(!WebUtils.isNetworkAvailable(SyncActivity.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(SyncActivity.this);
            builder.setMessage("此功能需要网络连接，WIFI没有连上");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SyncActivity.this.finish();
                }
            });
            builder.create().show();
        }
        sharedPreferences=SyncActivity.this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        userCode=sharedPreferences.getString("user","");
        custCode=intent.getStringExtra("custCode");

        uploadButton=(Button)findViewById(R.id.upLoadButton);
        uploadButton.setOnClickListener(new UpLoadButtonClick());

        downloadButton=(Button)findViewById(R.id.downLoadButton);
        downloadButton.setOnClickListener(new DownLoadButtonClick());
        downLoadingFrameLayout=(FrameLayout)findViewById(R.id.downLoadingFrameLayout);
        downLoadingFrameLayout.setVisibility(View.GONE);
        clearButton=(Button)findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new ClearButtonClick());

        downLoadCustButton=(Button)findViewById(R.id.downLoadCustButton);
        downLoadCustButton.setOnClickListener(new DownLoadCustButtonClick());
        downLoadOrderProdButton=(Button)findViewById(R.id.downLoadOrderProdButton);
        downLoadOrderProdButton.setOnClickListener(new DownLoadOrderProdButtonClick());

        downLoadSpecButton=(Button)findViewById(R.id.downLoadSpecButton);
        downLoadSpecButton.setOnClickListener(new DownLoadSpecButtonClick());
        downLoadSpecLinkButton=(Button)findViewById(R.id.downLoadSpecLinkButton);
        downLoadSpecLinkButton.setOnClickListener(new DownLoadSpecLinkButtonClick());
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressValue=(TextView)findViewById(R.id.progressValue);
        xh_pDialog = new ProgressDialog(SyncActivity.this);
        xh_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //xh_pDialog.setCancelable(false);
        xh_pDialog.setCanceledOnTouchOutside(false);
        initData();
    }
    private class DownLoadButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            downLoadingFrameLayout.setVisibility(View.VISIBLE);
            xh_pDialog.setTitle("提示");
            xh_pDialog.setMessage("开始下载数据...");
            xh_pDialog.setProgress(0);
            if(!xh_pDialog.isShowing()){
                xh_pDialog.show();
            }
            downLoadData(mSearchHandler);
        }
    }
    private class DownLoadCustButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            xh_pDialog.setTitle("提示");
            xh_pDialog.setMessage("开始下载顾客资料数据...");
            xh_pDialog.setProgress(0);

            if(!xh_pDialog.isShowing()){
                xh_pDialog.show();
            }
            downLoadCustData(mSearchHandler);
        }
    }
    private class DownLoadOrderProdButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            xh_pDialog.setTitle("提示");
            xh_pDialog.setMessage("开始下载订单款式资料...");
            xh_pDialog.setProgress(0);

            if(!xh_pDialog.isShowing()){
                xh_pDialog.show();
            }
            downLoadOrderProdData(mSearchHandler);
        }
    }
    private class DownLoadSpecButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            xh_pDialog.setTitle("提示");
            xh_pDialog.setMessage("开始下载套装规格资料...");
            xh_pDialog.setProgress(0);

            if(!xh_pDialog.isShowing()){
                xh_pDialog.show();
            }
            downLoadSpecData(mSearchHandler);
        }
    }
    private class DownLoadSpecLinkButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            xh_pDialog.setTitle("提示");
            xh_pDialog.setMessage("开始下载规格关联资料...");
            xh_pDialog.setProgress(0);

            if(!xh_pDialog.isShowing()){
                xh_pDialog.show();
            }
            downLoadSpecLinkData(mSearchHandler);
        }
    }
    private class UpLoadButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //downLoadingFrameLayout.setVisibility(View.VISIBLE);
            xh_pDialog.setTitle("提示");
            xh_pDialog.setMessage("开始上传数据到DRP系统...");
            xh_pDialog.setProgress(0);
            if(!xh_pDialog.isShowing()){
                xh_pDialog.show();
            }
            upLoadData(mSearchHandler);
        }
    }

    private class ClearButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            clearButton.setEnabled(false);

            clearData(mSearchHandler);
        }
    }

    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==DOWNLOAD_ACTION){
                    if(msg.what>0){

                        progressBar.setProgress(msg.what);
                        progressValue.setText(msg.obj.toString());
                        xh_pDialog.setMessage(msg.obj.toString());
                        xh_pDialog.setProgress(msg.what);
                        if(!xh_pDialog.isShowing()){
                            xh_pDialog.show();
                        }

                        if(msg.what==100){
                            downloadButton.setEnabled(true);
                            downLoadingFrameLayout.setVisibility(View.GONE);
                            xh_pDialog.cancel();
                        }
                    }
                }
                if(msg.arg1==CLEAR_ACTION){
                     progressBar.setProgress(msg.what);
                     progressValue.setText(msg.obj.toString());
                     if(msg.what==100){
                         clearButton.setEnabled(true);
                     }
                }

                if(msg.arg1==UPLOAD_ACTION){
                    if(msg.what>0){
                        progressBar.setProgress(msg.what);
                        progressValue.setText(msg.obj.toString());

                        xh_pDialog.setMessage(msg.obj.toString());
                        xh_pDialog.setProgress(msg.what);
                        if(!xh_pDialog.isShowing()){
                            xh_pDialog.show();
                        }
                        if(msg.what==100){
                            xh_pDialog.cancel();
                        }
                    }
                }
                if(msg.arg1==DOWNLOAD_CUST_ACTION){
                    if(msg.what>=0){
                        progressBar.setProgress(msg.what);
                        progressValue.setText(msg.obj.toString());

                        xh_pDialog.setMessage(msg.obj.toString());
                        xh_pDialog.setProgress(msg.what);
                        xh_pDialog.setMax(100);
                        if(!xh_pDialog.isShowing()){
                            xh_pDialog.show();
                        }
                        if(msg.what==100){
                            xh_pDialog.cancel();
                        }
                    }
                }
                if(msg.arg1==DOWNLOAD_ORDER_PROD_ACTION){
                    if(msg.what>=0){
                        progressBar.setProgress(msg.what);
                        progressValue.setText(msg.obj.toString());

                        xh_pDialog.setMessage(msg.obj.toString());
                        xh_pDialog.setProgress(msg.what);
                        xh_pDialog.setMax(100);
                        if(!xh_pDialog.isShowing()){
                            xh_pDialog.show();
                        }
                        if(msg.what==100){
                            xh_pDialog.cancel();
                        }
                    }
                }
                if(msg.arg1==DOWNLOAD_SPEC_ACTION){
                    if(msg.what>=0){
                        progressBar.setProgress(msg.what);
                        progressValue.setText(msg.obj.toString());

                        xh_pDialog.setMessage(msg.obj.toString());
                        xh_pDialog.setProgress(msg.what);
                        xh_pDialog.setMax(100);
                        if(!xh_pDialog.isShowing()){
                            xh_pDialog.show();
                        }
                        if(msg.what==100){
                            xh_pDialog.cancel();
                        }
                    }
                }
                if(msg.arg1==DOWNLOAD_SPEC_LINK_ACTION){
                    if(msg.what>=0){
                        progressBar.setProgress(msg.what);
                        progressValue.setText(msg.obj.toString());

                        xh_pDialog.setMessage(msg.obj.toString());
                        xh_pDialog.setProgress(msg.what);
                        xh_pDialog.setMax(100);
                        if(!xh_pDialog.isShowing()){
                            xh_pDialog.show();
                        }
                        if(msg.what==100){
                            xh_pDialog.cancel();
                        }
                    }
                }
            }
        };
    }
    private void downLoadData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                String errMsg="";
                DBhelper dBhelper= new DBhelper(context);
                SQLiteDatabase db = dBhelper.getWritableDatabase();
                try{

                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=0;
                    msg.obj="正在下载顾客资料";
                    handler.sendMessage(msg);

                    HashMap<String, String> params=new HashMap<String, String>();
                    params.put("custCode",custCode);
                    params.put("ltStatus","");
                    String ret= WebUtils.doGet("http://URL/YoungorTgOrder/getEmployee.do",
                            params, Constants.CHARSET_GBK);
                    JSONObject res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray custJson=res.getJSONArray("data");
                    for(int i=0;i<custJson.length();i++){
                        JSONObject jsonobject = custJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_CUSTJOB " +
                                        "WHERE CUST_CODE=? AND PCKNO=?",
                                new String[]{custCode,jsonobject.getString("PCKNO")});
                        if(c.getCount()==0){
                            db.execSQL("DELETE FROM EXD_TG_CUSTJOB WHERE CUST_CODE=? AND CNAME=?",
                                    new String[]{custCode,jsonobject.getString("CNAME")});
                            db.execSQL("INSERT INTO EXD_TG_CUSTJOB(CUST_CODE,PCKNO,DEPT,CNAME,STATUS,GENDER)" +
                                            "VALUES(?,?,?,?,?,?)",
                                    new Object[]{custCode,
                                            jsonobject.getString("PCKNO"),
                                            jsonobject.getString("DEPT"),
                                            jsonobject.getString("CNAME"),
                                            jsonobject.getString("STATUS"),
                                            jsonobject.getString("GENDER")
                                    });


                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=10;
                    msg.obj="正在下载订单资料";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("custCode",custCode);
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getOrderItem.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray prodJson=res.getJSONArray("data");
                    for(int i=0;i<prodJson.length();i++){
                        JSONObject jsonobject = prodJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_PROD_CLS " +
                                        "WHERE CUST_CODE=? AND ORD_NO=? AND PRD_CODE=? AND SUITE_GRP_ID=?",
                                new String[]{custCode,jsonobject.getString("ORDNO"),
                                        jsonobject.getString("PRD_CODE"),jsonobject.getString("SUITE_GRP_ID")});
                        if(c.getCount()==0){
                            String fileName=jsonobject.getString("PICTURE_URL");
                            String imageUrl="";
                            if(fileName.length()>0){
                                imageUrl="http://URL/YoungorTgOrder/getThumPicture.do?prodName="+ Encrypter.encrypt(fileName)+"&w=400&h=300";
                            }
                            ProdClsService.getImageURI(SyncActivity.this,imageUrl,fileName);
                            db.execSQL("INSERT INTO EXD_TG_PROD_CLS(ORD_NO,CUST_CODE,PRD_CODE,PRD_NAME,PRD_MEMO," +
                                    "PROD_CAT_ID,PROD_CAT_NAME,UOM,SUITE_GRP_ID,SUITE_GRP_NAME,IMAGE_URL,IMAGE_FILE_NAME,GENDER)" +
                                    "VALUES('"+jsonobject.getString("ORDNO")+"',"+
                                    "'"+custCode+"',"+
                                    "'"+jsonobject.getString("PRD_CODE")+"',"+
                                    "'"+jsonobject.getString("PRD_NAME")+"',"+
                                    "'"+jsonobject.getString("PRD_MEMO")+"',"+
                                    "'"+jsonobject.getString("CAT_ID")+"',"+
                                    "'"+jsonobject.getString("PROD_CAT_NAME")+"',"+
                                    "'"+jsonobject.getString("UOM")+"',"+
                                    "'"+jsonobject.getString("SUITE_GRP_ID")+"',"+
                                    "'"+jsonobject.getString("SUITE_GRP_NAME")+"',"+
                                    "'"+imageUrl+"',"+
                                    "'"+fileName+"',"+
                                    "'"+jsonobject.getString("GENDER")+"')");
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=30;
                    msg.obj="正在下载套装组合";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSuiteGrp.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray suiteGrpJson=res.getJSONArray("data");
                    for(int i=0;i<suiteGrpJson.length();i++){
                        JSONObject jsonobject = suiteGrpJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SUITE_GRP WHERE SUITE_GRP_ID=? " ,
                                new String[]{jsonobject.getString("SUITE_GRP_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SUITE_GRP(SUITE_GRP_ID,CAT_ID,SUITE_GRP_NAME,SEQ_NUM)" +
                                    "VALUES(?,?,?,?)",new Object[]{
                                    jsonobject.getString("SUITE_GRP_ID"),
                                    jsonobject.getString("CAT_ID"),
                                    jsonobject.getString("SUITE_GRP_NAME"),
                                    jsonobject.getString("SEQ_NUM")
                            });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=50;
                    msg.obj="正在下载套装明细";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteGrpId","-1");
                    params.put("catId","");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSuiteGrpDtl.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray suiteGrpDtlJson=res.getJSONArray("data");
                    for(int i=0;i<suiteGrpDtlJson.length();i++){
                        JSONObject jsonobject = suiteGrpDtlJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SUITE_GRP_DTL WHERE DTL_ID=? " ,
                                new String[]{jsonobject.getString("DTL_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SUITE_GRP_DTL(DTL_ID,SUITE_GRP_ID,SUITE_GRP_NAME," +
                                    "SUITE_CODE,SUITE_NAME,SORT,SUITE_SORT)" +
                                    "VALUES(?,?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("DTL_ID"),
                                    jsonobject.getString("SUITE_GRP_ID"),
                                    jsonobject.getString("SUITE_GRP_NAME"),
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("SUITE_NAME"),
                                    jsonobject.getString("SORT"),
                                    jsonobject.getString("SUITE_SORT")
                            });

                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=60;
                    msg.obj="正在下载规格组";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteCode","");
                    params.put("gender","");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSpecGrp.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray specGrpJson=res.getJSONArray("data");
                    for(int i=0;i<specGrpJson.length();i++){
                        JSONObject jsonobject = specGrpJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC_GRP WHERE GRP_ID=? " ,
                                new String[]{jsonobject.getString("GRP_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SPEC_GRP(GRP_ID,SUITE_CODE,GENDER,GRP_CODE,GRP_NAME,APPROVE)" +
                                    "VALUES(?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("GRP_ID"),
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("GENDER"),
                                    jsonobject.getString("GRP_CODE"),
                                    jsonobject.getString("GRP_NAME"),
                                    jsonobject.getString("APPROVE")
                            });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=80;
                    msg.obj="正在下载规格明细";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteCode","");
                    params.put("gender","");
                    params.put("grpId","-1");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSpec.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray specJson=res.getJSONArray("data");
                    for(int i=0;i<specJson.length();i++){
                        JSONObject jsonobject = specJson.getJSONObject(i);
                        msg = new Message();
                        msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                        msg.what=80;
                        msg.obj="正在下载规格明细:"+jsonobject.getString("SPEC_CODE")+"["+i+"/"+specJson.length()+"]";
                        handler.sendMessage(msg);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC WHERE SPEC_ID=? " ,
                                new String[]{jsonobject.getString("SPEC_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SPEC(SPEC_ID,SUITE_CODE,GENDER,SPEC_CODE,PATTERN,SPEC,SHAPE,GRP_ID)" +
                                    "VALUES(?,?,?,?,?,?,?,?)",
                                    new Object[]{
                                            jsonobject.getString("SPEC_ID"),
                                            jsonobject.getString("SUITE_CODE"),
                                            jsonobject.getString("GENDER"),
                                            jsonobject.getString("SPEC_CODE"),
                                            jsonobject.getString("PATTERN"),
                                            jsonobject.getString("SPEC"),
                                            jsonobject.getString("SHAPE"),
                                            jsonobject.getString("GRP_ID")
                                    });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=90;
                    msg.obj="正在下载术语描述";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteCode","");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getTerm.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray teamJson=res.getJSONArray("data");
                    for(int i=0;i<teamJson.length();i++){
                        JSONObject jsonobject = teamJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_TERMINOLOGY WHERE TERM_ID=? " ,
                                new String[]{jsonobject.getString("TERM_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_TERMINOLOGY(TERM_ID,SUITE_CODE,TERM_NAME,DEFAULT_VALUE,MAX_VALUE,MIN_VALUE,STEP_VALUE,SORT,GROUP_VALUE)" +
                                    "VALUES(?,?,?,?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("TERM_ID"),
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("TERM_NAME"),
                                    jsonobject.getString("DEFAULT_VALUE"),
                                    jsonobject.getString("MAX_VALUE"),
                                    jsonobject.getString("MIN_VALUE"),
                                    jsonobject.getString("STEP_VALUE"),
                                    jsonobject.getString("SORT"),
                                    jsonobject.getString("GROUP_VALUE")
                            });
                        }
                    }
                    //msg.what=list.size();
                    //msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    errMsg=e.getMessage();
                    msg.what=-1;
                    msg.obj=e;
                }finally {
                    db.close();
                }
                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj="下载完毕";
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }
    private void  downLoadCustData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                String errMsg = "";
                try{
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_CUST_ACTION;//告知handler当前action
                    msg.what=0;
                    msg.obj="开始下载顾客资料";
                    handler.sendMessage(msg);
                    HashMap<String, String> params=new HashMap<String, String>();
                    params.put("custCode",custCode);
                    params.put("ltStatus","");
                    String ret= WebUtils.doGet("http://URL/YoungorTgOrder/getEmployee.do",
                            params, Constants.CHARSET_GBK);
                    JSONObject res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray custJson=res.getJSONArray("data");
                    DBhelper dBhelper= new DBhelper(context);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();

                    for(int i=0;i<custJson.length();i++){
                        JSONObject jsonobject = custJson.getJSONObject(i);
                        msg = new Message();
                        msg.arg1 = DOWNLOAD_CUST_ACTION;
                        msg.what=((i+1)/custJson.length())*100;
                        msg.obj="正在下载顾客资料["+i+"/"+custJson.length()+"]:"+jsonobject.getString("CNAME");
                        handler.sendMessage(msg);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_CUSTJOB " +
                                        "WHERE CUST_CODE=? AND PCKNO=?",
                                new String[]{custCode,jsonobject.getString("PCKNO")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_CUSTJOB(CUST_CODE,PCKNO,DEPT,CNAME,STATUS)" +
                                            "VALUES(?,?,?,?,?)",
                                    new Object[]{custCode,
                                            jsonobject.getString("PCKNO"),
                                            jsonobject.getString("DEPT"),
                                            jsonobject.getString("CNAME"),
                                            jsonobject.getString("STATUS")
                                    });


                        }
                        c.close();
                    }
                    db.close();
                }catch (Exception e){
                    e.printStackTrace();
                    errMsg=e.getMessage();
                }
                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_CUST_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_CUST_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj="顾客资料下载完成";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    private void  downLoadOrderProdData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                String errMsg = "";
                try{
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ORDER_PROD_ACTION;//告知handler当前action
                    msg.what=0;
                    msg.obj="开始下载订单款式资料";
                    handler.sendMessage(msg);
                    HashMap<String, String> params=new HashMap<String, String>();
                    params=new HashMap<String, String>();
                    params.put("custCode",custCode);
                    String ret= WebUtils.doGet("http://URL/YoungorTgOrder/getOrderItem.do",
                            params, Constants.CHARSET_GBK);

                    JSONObject res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray prodJson=res.getJSONArray("data");
                    DBhelper dBhelper= new DBhelper(context);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();
                    RecordFormat format = new RecordFormat("@");
                    format.clearFields();
                    format.appendField(new RecordFieldFormat("ORD_NO", "订单号",
                            RecordFieldFormat.TYPE_VARCHAR, 30, 0, 15,
                            CharacterCase.normal));
                    format.appendField(new RecordFieldFormat("PRD_CODE", "款式号",
                            RecordFieldFormat.TYPE_VARCHAR, 30, 0, 15,
                            CharacterCase.normal));
                    format.appendField(new RecordFieldFormat("SUITE_GRP_ID", "套装代码",
                            RecordFieldFormat.TYPE_VARCHAR, 30, 0, 15,
                            CharacterCase.normal));
                    format.appendField(new RecordFieldFormat("SUITE_GRP_NAME", "套装名称",
                            RecordFieldFormat.TYPE_VARCHAR, 60, 0, 60,
                            CharacterCase.normal));
                    RecordSet rs = new RecordSet(format);
                    RecordSet remoteRs=new RecordSet(format);
                    Cursor c =db.rawQuery("SELECT * FROM EXD_TG_PROD_CLS " +
                                    "WHERE CUST_CODE=? ",
                            new String[]{custCode});
                    if(c.getCount()>0){
                        while(c.moveToNext()){
                            Record rc=rs.append();
                            rc.getField("ORD_NO").setString(c.getString(c.getColumnIndex("ORD_NO")));
                            rc.getField("PRD_CODE").setString(c.getString(c.getColumnIndex("PRD_CODE")));
                            rc.getField("SUITE_GRP_ID").setString(c.getString(c.getColumnIndex("SUITE_GRP_ID")));
                            rc.getField("SUITE_GRP_NAME").setString(c.getString(c.getColumnIndex("SUITE_GRP_NAME")));
                            rc.post();
                        }
                    }
                    c.close();
                    for(int i=0;i<prodJson.length();i++){
                        JSONObject jsonobject = prodJson.getJSONObject(i);
                        msg = new Message();
                        msg.arg1 = DOWNLOAD_ORDER_PROD_ACTION;
                        msg.what=((i+1)/prodJson.length())*100;
                        msg.obj="正在下载款式资料["+i+"/"+prodJson.length()+"]:"+jsonobject.getString("PRD_CODE");
                        handler.sendMessage(msg);
                        Record rc=remoteRs.append();
                        rc.getField("ORD_NO").setString(jsonobject.getString("ORDNO"));
                        rc.getField("PRD_CODE").setString(jsonobject.getString("PRD_CODE"));
                        rc.getField("SUITE_GRP_ID").setString(jsonobject.getString("SUITE_GRP_ID"));
                        rc.getField("SUITE_GRP_NAME").setString(jsonobject.getString("SUITE_GRP_NAME"));
                        rc.post();
                        int loc= rs.locate(0,new String[]{"ORD_NO","PRD_CODE","SUITE_GRP_ID"},new Object[]{
                                jsonobject.getString("ORDNO"),
                                jsonobject.getString("PRD_CODE"),
                                jsonobject.getString("SUITE_GRP_ID")
                        },0);
                        if(loc<0){
                            String fileName=jsonobject.getString("PICTURE_URL");
                            String imageUrl="";
                            if(fileName.length()>0){
                                imageUrl="http://URL/YoungorTgOrder/getThumPicture.do?prodName="+ Encrypter.encrypt(fileName)+"&w=400&h=300";
                            }
                            ProdClsService.getImageURI(SyncActivity.this,imageUrl,fileName);
                            db.execSQL("INSERT INTO EXD_TG_PROD_CLS(ORD_NO,CUST_CODE,PRD_CODE,PRD_NAME,PRD_MEMO," +
                                    "PROD_CAT_ID,PROD_CAT_NAME,SUITE_GRP_ID,SUITE_GRP_NAME,IMAGE_URL,IMAGE_FILE_NAME,GENDER)" +
                                    "VALUES('"+jsonobject.getString("ORDNO")+"',"+
                                    "'"+custCode+"',"+
                                    "'"+jsonobject.getString("PRD_CODE")+"',"+
                                    "'"+jsonobject.getString("PRD_NAME")+"',"+
                                    "'"+jsonobject.getString("PRD_MEMO")+"',"+
                                    "'"+jsonobject.getString("CAT_ID")+"',"+
                                    "'"+jsonobject.getString("PROD_CAT_NAME")+"',"+
                                    "'"+jsonobject.getString("SUITE_GRP_ID")+"',"+
                                    "'"+jsonobject.getString("SUITE_GRP_NAME")+"',"+
                                    "'"+imageUrl+"',"+
                                    "'"+fileName+"',"+
                                    "'"+jsonobject.getString("GENDER")+"')");
                        }
                        /*Cursor c =db.rawQuery("SELECT * FROM EXD_TG_PROD_CLS " +
                                        "WHERE CUST_CODE=? AND ORD_NO=? AND PRD_CODE=? AND SUITE_GRP_ID=?",
                                new String[]{custCode,jsonobject.getString("ORDNO"),
                                        jsonobject.getString("PRD_CODE"),jsonobject.getString("SUITE_GRP_ID")});
                        if(c.getCount()==0){
                            String fileName=jsonobject.getString("PICTURE_URL");
                            String imageUrl="";
                            if(fileName.length()>0){
                                imageUrl="http://URL/YoungorTgOrder/getThumPicture.do?prodName="+ Encrypter.encrypt(fileName)+"&w=400&h=300";
                            }
                            ProdClsService.getImageURI(SyncActivity.this,imageUrl,fileName);
                            db.execSQL("INSERT INTO EXD_TG_PROD_CLS(ORD_NO,CUST_CODE,PRD_CODE,PRD_NAME,PRD_MEMO," +
                                    "PROD_CAT_ID,PROD_CAT_NAME,SUITE_GRP_ID,SUITE_GRP_NAME,IMAGE_URL,IMAGE_FILE_NAME,GENDER)" +
                                    "VALUES('"+jsonobject.getString("ORDNO")+"',"+
                                    "'"+custCode+"',"+
                                    "'"+jsonobject.getString("PRD_CODE")+"',"+
                                    "'"+jsonobject.getString("PRD_NAME")+"',"+
                                    "'"+jsonobject.getString("PRD_MEMO")+"',"+
                                    "'"+jsonobject.getString("CAT_ID")+"',"+
                                    "'"+jsonobject.getString("PROD_CAT_NAME")+"',"+
                                    "'"+jsonobject.getString("SUITE_GRP_ID")+"',"+
                                    "'"+jsonobject.getString("SUITE_GRP_NAME")+"',"+
                                    "'"+imageUrl+"',"+
                                    "'"+fileName+"',"+
                                    "'"+jsonobject.getString("GENDER")+"')");
                        }
                        c.close();*/
                    }
                    for(int i=0;i<rs.recordCount();i++){
                        int loc= remoteRs.locate(0,new String[]{"ORD_NO","PRD_CODE","SUITE_GRP_ID"},new Object[]{
                                rs.getRecord(i).getField("ORD_NO").getString(),
                                rs.getRecord(i).getField("PRD_CODE").getString(),
                                rs.getRecord(i).getField("SUITE_GRP_ID").getString()
                        },0);
                        if(loc<0){
                            db.execSQL("DELETE FROM EXD_TG_PROD_CLS WHERE ORD_NO=? AND CUST_CODE=? AND PRD_CODE=? " +
                                    "AND SUITE_GRP_ID=?",new Object[]{
                                    rs.getRecord(i).getField("ORD_NO").getString(),
                                    custCode,
                                    rs.getRecord(i).getField("PRD_CODE").getString(),
                                    rs.getRecord(i).getField("SUITE_GRP_ID").getString()
                            });
                        }
                    }

                    db.close();
                }catch (Exception e){
                    e.printStackTrace();
                    errMsg=e.getMessage();
                }
                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ORDER_PROD_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_ORDER_PROD_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj="订单款式资料下载完成";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    private void  downLoadSpecData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                String errMsg = "";
                try{
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;//告知handler当前action
                    msg.what=0;
                    msg.obj="开始下载套装组合";
                    handler.sendMessage(msg);
                    HashMap<String, String> params=new HashMap<String, String>();
                    String ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSuiteGrp.do",
                            params, Constants.CHARSET_GBK);
                    JSONObject res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray suiteGrpJson=res.getJSONArray("data");
                    DBhelper dBhelper= new DBhelper(context);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();
                    for(int i=0;i<suiteGrpJson.length();i++){
                        JSONObject jsonobject = suiteGrpJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SUITE_GRP WHERE SUITE_GRP_ID=? " ,
                                new String[]{jsonobject.getString("SUITE_GRP_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SUITE_GRP(SUITE_GRP_ID,CAT_ID,SUITE_GRP_NAME,SEQ_NUM)" +
                                    "VALUES(?,?,?,?)",new Object[]{
                                    jsonobject.getString("SUITE_GRP_ID"),
                                    jsonobject.getString("CAT_ID"),
                                    jsonobject.getString("SUITE_GRP_NAME"),
                                    jsonobject.getString("SEQ_NUM")
                            });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;//告知handler当前action
                    msg.what=50;
                    msg.obj="正在下载套装明细";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteGrpId","-1");
                    params.put("catId","");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSuiteGrpDtl.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray suiteGrpDtlJson=res.getJSONArray("data");
                    for(int i=0;i<suiteGrpDtlJson.length();i++){
                        JSONObject jsonobject = suiteGrpDtlJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SUITE_GRP_DTL WHERE DTL_ID=? " ,
                                new String[]{jsonobject.getString("DTL_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SUITE_GRP_DTL(DTL_ID,SUITE_GRP_ID,SUITE_GRP_NAME," +
                                    "SUITE_CODE,SUITE_NAME,SORT,SUITE_SORT)" +
                                    "VALUES(?,?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("DTL_ID"),
                                    jsonobject.getString("SUITE_GRP_ID"),
                                    jsonobject.getString("SUITE_GRP_NAME"),
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("SUITE_NAME"),
                                    jsonobject.getString("SORT"),
                                    jsonobject.getString("SUITE_SORT")
                            });

                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;//告知handler当前action
                    msg.what=60;
                    msg.obj="正在下载规格组";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteCode","");
                    params.put("gender","");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSpecGrp.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray specGrpJson=res.getJSONArray("data");
                    for(int i=0;i<specGrpJson.length();i++){
                        JSONObject jsonobject = specGrpJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC_GRP WHERE GRP_ID=? " ,
                                new String[]{jsonobject.getString("GRP_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SPEC_GRP(GRP_ID,SUITE_CODE,GENDER,GRP_CODE,GRP_NAME,APPROVE)" +
                                    "VALUES(?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("GRP_ID"),
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("GENDER"),
                                    jsonobject.getString("GRP_CODE"),
                                    jsonobject.getString("GRP_NAME"),
                                    jsonobject.getString("APPROVE")
                            });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;//告知handler当前action
                    msg.what=80;
                    msg.obj="正在下载规格明细";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteCode","");
                    params.put("gender","");
                    params.put("grpId","-1");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSpec.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray specJson=res.getJSONArray("data");
                    for(int i=0;i<specJson.length();i++){
                        JSONObject jsonobject = specJson.getJSONObject(i);
                        msg = new Message();
                        msg.arg1 = DOWNLOAD_SPEC_ACTION;//告知handler当前action
                        msg.what=80;
                        msg.obj="正在下载规格明细:"+jsonobject.getString("SPEC_CODE")+"["+i+"/"+specJson.length()+"]";
                        handler.sendMessage(msg);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC WHERE SPEC_ID=? " ,
                                new String[]{jsonobject.getString("SPEC_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_SPEC(SPEC_ID,SUITE_CODE,GENDER,SPEC_CODE,PATTERN,SPEC,SHAPE,GRP_ID)" +
                                            "VALUES(?,?,?,?,?,?,?,?)",
                                    new Object[]{
                                            jsonobject.getString("SPEC_ID"),
                                            jsonobject.getString("SUITE_CODE"),
                                            jsonobject.getString("GENDER"),
                                            jsonobject.getString("SPEC_CODE"),
                                            jsonobject.getString("PATTERN"),
                                            jsonobject.getString("SPEC"),
                                            jsonobject.getString("SHAPE"),
                                            jsonobject.getString("GRP_ID")
                                    });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;//告知handler当前action
                    msg.what=90;
                    msg.obj="正在下载术语描述";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    params.put("suiteCode","");
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getTerm.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray teamJson=res.getJSONArray("data");
                    for(int i=0;i<teamJson.length();i++){
                        JSONObject jsonobject = teamJson.getJSONObject(i);
                        Cursor c =db.rawQuery("SELECT * FROM EXD_TG_TERMINOLOGY WHERE TERM_ID=? " ,
                                new String[]{jsonobject.getString("TERM_ID")});
                        if(c.getCount()==0){
                            db.execSQL("INSERT INTO EXD_TG_TERMINOLOGY(TERM_ID,SUITE_CODE,TERM_NAME,DEFAULT_VALUE,MAX_VALUE,MIN_VALUE,STEP_VALUE,SORT,GROUP_VALUE)" +
                                    "VALUES(?,?,?,?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("TERM_ID"),
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("TERM_NAME"),
                                    jsonobject.getString("DEFAULT_VALUE"),
                                    jsonobject.getString("MAX_VALUE"),
                                    jsonobject.getString("MIN_VALUE"),
                                    jsonobject.getString("STEP_VALUE"),
                                    jsonobject.getString("SORT"),
                                    jsonobject.getString("GROUP_VALUE")
                            });
                        }
                        c.close();
                    }
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_LINK_ACTION;//告知handler当前action
                    msg.what=95;
                    msg.obj="开始下载规格关联";
                    handler.sendMessage(msg);
                    params=new HashMap<String, String>();
                    ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSpecLink.do",
                            params, Constants.CHARSET_GBK);
                    res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray specLinkJson=res.getJSONArray("data");
                    db.execSQL("DELETE FROM EXD_TG_SPECLINK_DTL");
                    for(int i=0;i<specLinkJson.length();i++){
                        JSONObject jsonobject = specLinkJson.getJSONObject(i);
                        db.execSQL("INSERT INTO EXD_TG_SPECLINK_DTL(SUITE_CODE,GENDER,SHAPE,DTL_SUITE_CODE,DTL_SHAPE,DTL_STATUS)" +
                                "VALUES(?,?,?,?,?,?)",new Object[]{
                                jsonobject.getString("SUITE_CODE"),
                                jsonobject.getString("GENDER"),
                                jsonobject.getString("SHAPE"),
                                jsonobject.getString("DTL_SUITE_CODE"),
                                jsonobject.getString("DTL_SHAPE"),
                                "A"
                        });
                    }
                    db.close();

                }catch (Exception e){
                    e.printStackTrace();
                    errMsg=e.getMessage();
                }
                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_ACTION;
                    msg.what=100;
                    msg.obj="套装规格资料下载完成";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    private void downLoadSpecLinkData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                String errMsg = "";
                try{
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_LINK_ACTION;//告知handler当前action
                    msg.what=0;
                    msg.obj="开始下载规格关联";
                    handler.sendMessage(msg);
                    HashMap<String, String> params=new HashMap<String, String>();
                    String ret= WebUtils.doGet("http://URL/YoungorTgOrder/getSpecLink.do",
                            params, Constants.CHARSET_GBK);
                    JSONObject res=new JSONObject(ret);
                    if(!res.getBoolean("success")){
                        throw new Exception(res.getString("msg"));
                    }
                    JSONArray suiteGrpJson=res.getJSONArray("data");
                    DBhelper dBhelper= new DBhelper(context);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();
                    db.execSQL("DELETE FROM EXD_TG_SPECLINK_DTL");
                    for(int i=0;i<suiteGrpJson.length();i++){
                        JSONObject jsonobject = suiteGrpJson.getJSONObject(i);
                        db.execSQL("INSERT INTO EXD_TG_SPECLINK_DTL(SUITE_CODE,GENDER,SHAPE,DTL_SUITE_CODE,DTL_SHAPE,DTL_STATUS)" +
                                    "VALUES(?,?,?,?,?,?)",new Object[]{
                                    jsonobject.getString("SUITE_CODE"),
                                    jsonobject.getString("GENDER"),
                                    jsonobject.getString("SHAPE"),
                                    jsonobject.getString("DTL_SUITE_CODE"),
                                    jsonobject.getString("DTL_SHAPE"),
                                    "A"
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errMsg=e.getMessage();
                }
                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_LINK_ACTION;
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = DOWNLOAD_SPEC_LINK_ACTION;
                    msg.what=100;
                    msg.obj="规格关联资料下载完成";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    private void clearData(final Handler handler){
        new Thread() {
            public void run() {
                Message msg = new Message();
                String errMsg = "";
                try{
                    msg = new Message();
                    msg.arg1 = CLEAR_ACTION;//告知handler当前action
                    msg.what=0;
                    msg.obj="开始清除缓存";
                    handler.sendMessage(msg);
                    DBhelper dBhelper= new DBhelper(context);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();
                    db.execSQL("DELETE FROM EXD_TG_CUSTJOB");
                    db.execSQL("DELETE FROM EXD_TG_PROD_CLS");
                    db.execSQL("DELETE FROM EXD_TG_SUITE_GRP");
                    db.execSQL("DELETE FROM EXD_TG_SUITE_GRP_DTL");
                    db.execSQL("DELETE FROM EXD_TG_SPEC_GRP");
                    db.execSQL("DELETE FROM EXD_TG_SPEC");
                    db.execSQL("DELETE FROM EXD_TG_TERMINOLOGY");
                    db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD");
                    db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD_DTL");
                    db.execSQL("DELETE FROM EXD_TG_SPECLINK_DTL");
                    db.close();

                }catch (Exception e){
                    e.printStackTrace();
                    errMsg=e.getMessage();
                }
                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = CLEAR_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = CLEAR_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj="清除完毕";
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }
    public void upLoadData(final Handler handler){
        new Thread() {
            public void run() {
                Message msg = new Message();

                msg.arg1 = UPLOAD_ACTION;//告知handler当前action
                msg.what=0;
                msg.obj="开始上传数据";
                String errMsg="";
                if(custCode==null || custCode.length()<=0){
                    errMsg = SynService.upLoadData(context,userCode);
                }else{
                    errMsg = SynService.upLoadData(context,userCode,custCode);
                }

                if(errMsg.length()>0){
                    msg = new Message();
                    msg.arg1 = UPLOAD_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj=errMsg;
                    handler.sendMessage(msg);
                }else {
                    msg = new Message();
                    msg.arg1 = UPLOAD_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj="上传完毕";
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
}
