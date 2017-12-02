package com.example.wuhongjie.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.wuhongjie.myapplication.adapter.GridViewAdapter;
import com.example.wuhongjie.myapplication.util.FileUtil;
import com.example.wuhongjie.myapplication.util.MyImageView;
import com.example.wuhongjie.myapplication.util.UpdateManager;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private int[] images = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f};
    private String[] titles = {"按人员量体","按货号量体","量体师傅量体","客户设置","数据同步","版本更新"};
    private String[] descs = {"先选择要量体的人员，再按货号给这个人员量体",
            "先选择要量体的货号，再按客户人员依次量体",
            "按照性别品牌套件对人员量体",
            "量体操作前先要指定客户单位，所属公司区域",
            "把客户下的信息从DRP系统同步过来",
            "版本同步失效后点此手动操作获得最新版本","",""};
    private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>>();

    private String yxgsDm = "";
    private String quyDm = "";
    public String custCode = "";
    public String custName = "";
    public String userCode = "";
    public String userName = "";
    private SharedPreferences sharedPreferences;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_SETTING = 0;
    public static final int REQUEST_EMPLOYEE = 1;
    public static final int REQUEST_SYNC = 2;
    public static final int REQUEST_PRODUCT = 4;
    public static final int REQUEST_LOGIN = 10;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private static final int REQUEST_CODE_CAMERA = 102;
    private MyImageView rylt;
    private MyImageView hhlt;
    private MyImageView sjtb;
    private MyImageView khsz;
    private TextView infoTextView;
    private String curVersionName;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        /*permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }*/
        try {

            PackageInfo info = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
            curVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        //rylt = (MyImageView) findViewById(R.id.ryltView);



        initView();
        initData();
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        String fb=" 当前分辨率："+width+"*"+height;
        infoTextView.setText(" 当前版本：" + curVersionName+fb);
        mGridView.setAdapter(new GridViewAdapter(this, mList));
        mGridView.setOnItemClickListener(new ModuleButtonClickListener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        yxgsDm = sharedPreferences.getString("yxgsDm", "");
        quyDm = sharedPreferences.getString("quyDm", "");
        custCode = sharedPreferences.getString("custCode", "");
        custName = sharedPreferences.getString("custName", "");
        userCode = sharedPreferences.getString("user", "");
        userName = sharedPreferences.getString("userName", "");

        if (userName.length() > 0) {
            infoTextView.setText("用户名：" + userName + " 当前版本：" + curVersionName+fb);
        }
        toolbar.setTitle("团购订单录入");
        if (custName.length() > 0) {
            setTitle(custName);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);

            }
        });
        UpdateManager.getUpdateManager().checkAppUpdate(this, false);
        //GridView gridView = (GridView)findViewById(R.id.mainGridView);
        //gridView.setAdapter(getAdapter());
        //gridView.setOnItemClickListener(new GridViewItemClickListener());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, REQUEST_SETTING);
            return true;
        }

        if (id == R.id.action_emp_settings) {
            /*OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
                @Override
                public void onResult(AccessToken result) {
                    String token = result.getAccessToken();
                }

                @Override
                public void onError(OCRError error) {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this,
                            "AK，SK方式获取token失败:"+error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }, getApplicationContext(), "zX1sWffyCUyb109wzKmXqtsu", "c17C09G1CLFHXyu1G6WozibAKenMBGZ6");
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);*/
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, EmployeeLoadActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);

            return true;
        }
        if(id==R.id.action_testing){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, TestActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if(id==R.id.action_counter){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CounterActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if(id==R.id.action_homepage){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, HomepageActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SETTING) {
                Bundle bunde = data.getExtras();
                custCode = bunde.getString("custCode");
                custName = bunde.getString("custName");
                setTitle(custName);
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
        }
        if (requestCode == REQUEST_LOGIN) {
            sharedPreferences = this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
            userCode = sharedPreferences.getString("user", "");
            userName = sharedPreferences.getString("userName", "");
            if (userName.length() > 0) {
                infoTextView.setText("用户名：" + userName + " 当前版本：" + curVersionName);
            } else {
                infoTextView.setText(" 当前版本：" + curVersionName);
            }
        }
    }

    private void recGeneral(String filePath) {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                for (Word word : result.getWordList()) {
                    //word.getLocation()
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                infoTextView.setText(sb);
            }

            @Override
            public void onError(OCRError error) {
                infoTextView.setText(error.getMessage());
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /*public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
    }

    private class ModuleButtonClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (userCode.length() <= 0) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                return;

            }
            if (i == 0) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EmployeeActivity.class);
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);
                intent.putExtra("type", "RY");
                startActivityForResult(intent, REQUEST_EMPLOYEE);
            }
            if (i == 1) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProductActivity.class);
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);
                startActivityForResult(intent, REQUEST_PRODUCT);
            }
            if (i == 2) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EmployeeActivity.class);
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);
                intent.putExtra("type", "LT");
                startActivityForResult(intent, REQUEST_EMPLOYEE);
            }

            if (i == 3) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_SETTING);
            }
            if (i == 4) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SyncActivity.class);
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);
                startActivityForResult(intent, REQUEST_SYNC);
            }
            if (i == 5) {
                //找到文件的路径  /data/data/包名/databases/数据库名称
               /* File dbFile = new File(Environment.getDataDirectory().getAbsolutePath()+"/data/"+getPackageName()+"/databases/tgOrderDb.db");

                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    File cache = new File(Environment.getExternalStorageDirectory(), "YoungorTgCache");
                    //文件复制到sd卡中
                    fis = new FileInputStream(dbFile);
                    fos = new FileOutputStream(cache.getAbsolutePath()+"/copy.db");
                    //fos = new File(cache, "copy.db");
                    int len = 0;
                    byte[] buffer = new byte[2048];
                    while(-1!=(len=fis.read(buffer))){
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    //关闭数据流
                    try{
                        if(fos!=null)
                            fos.close();
                        if (fis!=null)
                            fis.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }*/
                UpdateManager.getUpdateManager().checkAppUpdate(MainActivity.this, false);
            }
        }
    }
    private void initData() {
        mList.clear();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("images", images[i]);
            map.put("titles", titles[i]);
            map.put("descs", descs[i]);
            mList.add(map);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.gridview);
        infoTextView = (TextView) findViewById(R.id.infoTextView2);
    }
}
