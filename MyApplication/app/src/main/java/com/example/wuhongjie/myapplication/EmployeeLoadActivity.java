package com.example.wuhongjie.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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
import com.example.wuhongjie.myapplication.util.FileUtil;

import java.io.File;


public class EmployeeLoadActivity extends AppCompatActivity {

    private GridView employee_gridView;
    private Button generalButton;
    private static final int REQUEST_CODE_CAMERA = 102;
    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_load);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        employee_gridView=(GridView) findViewById(R.id.employee_gridView);
        infoTextView=(TextView)findViewById(R.id.infoTextView);
        generalButton=(Button)findViewById(R.id.general_button);
        generalButton.setOnClickListener(new GeneralButtonClick());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String phoneName = android.os.Build.MODEL ;
        infoTextView.setText(phoneName);
    }
    private class GeneralButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
                @Override
                public void onResult(AccessToken result) {
                    String token = result.getAccessToken();
                }

                @Override
                public void onError(OCRError error) {
                    error.printStackTrace();
                    Toast.makeText(EmployeeLoadActivity.this,
                            "AK，SK方式获取token失败:"+error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }, getApplicationContext(), "zX1sWffyCUyb109wzKmXqtsu", "c17C09G1CLFHXyu1G6WozibAKenMBGZ6");
            Intent intent = new Intent(EmployeeLoadActivity.this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_CAMERA){
            recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
        }
    }
    private void recGeneral(String filePath) {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setRecognizeGranularity("small");
        //param.setVertexesLocation(true);
        param.setImageFile(new File(filePath));

        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();

                for (Word word : result.getWordList()) {
                    //word.getLocation()

                    sb.append("["+word.getLocation().getTop()+","+word.getLocation().getLeft()+","+
                            word.getLocation().getWidth()+"]"+word.getWords());
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
}
