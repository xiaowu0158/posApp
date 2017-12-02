package com.example.wuhongjie.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evangelsoft.crosslink.customer.config.intf.DpbType;
import com.evangelsoft.econnect.client.Agency;
import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.evangelsoft.econnect.dataformat.TransientRecordSet;
import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.intf.KernelPlugIn;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.youngor.modules.drp.tuangou.base.intf.CityProvinceSelect;

import java.util.Date;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    private EditText edt_log;
    private Button btn_bind;
    private static int BIND_ACTION=1;
    private Handler mSearchHandler;
    private Button btn_clear;
    private ClientSession session=null;
    private static TestActivity content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        content=TestActivity.this;
        edt_log=(EditText)findViewById(R.id.edt_log);
        btn_clear=(Button)findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new ClearButtonClick());
        btn_bind=(Button)findViewById(R.id.btn_bind);
        btn_bind.setOnClickListener(new BindButtonClick());
        initData();
    }
    private class ClearButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //downLoadingFrameLayout.setVisibility(View.VISIBLE);
            edt_log.setText("");
        }
    }
    private class BindButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            BindData(mSearchHandler);
        }
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1== DxSessionHelper.SESSION_ERROR){
                   Toast.makeText(content, msg.obj.toString(),
                                      Toast.LENGTH_SHORT).show();

                }else{
                  String s=edt_log.getText().toString()+"\n";
                        edt_log.setText(s+msg.obj.toString());
                }
            }
        };
    }
    public void BindData(final Handler handler){
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.arg1 = BIND_ACTION;//告知handler当前action
                msg.what=0;
                msg.obj="开始执行";
                try{
                    /*HashMap<String, Object> userObject = new HashMap<String, Object>();
                    userObject.put("user", "LIJIE2");
                    userObject.put("password", "");
                    sendMsg(handler,"41");
                    System.out.println(DxSessionHelper.sessionLinked);
                    session=DxSessionHelper.getSession(userObject,session);
                    System.out.println(DxSessionHelper.sessionLinked);
                    */


                    session=((YoungorApplication)getApplication()).getDxSession();

                    if(!((YoungorApplication)getApplication()).sessionLinked){
                        sendMsg(handler,"33");
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR;//告知handler当前action
                        msg.obj=DxSessionHelper.errMsg;
                        handler.sendMessage(msg);
                    }

                    CityProvinceSelect cityProvinceSelect = (CityProvinceSelect) (new RMIProxy(session))
                            .newStub(CityProvinceSelect.class);
                    VariantHolder<String> errMsg = new VariantHolder<String>();
                    VariantHolder<Object> data = new VariantHolder<Object>();
                    data.value=new TransientRecordSet();
                    if(!cityProvinceSelect.get("60",data,errMsg)){
                        throw new Exception(errMsg.value);
                    }
                    RecordSet rs=(RecordSet) data.value;
                    for(int i=0;i<rs.recordCount();i++){
                        sendMsg(handler, new String(rs.getRecord(i).getField("CITY_NAME").getBytes(),"GBK"));
                    }
                    //Agency.returnSession("WEB", session);
                    sendMsg(handler,"1");
                    DpbType dpbType = (DpbType) (new RMIProxy(session))
                            .newStub(DpbType.class);
                    sendMsg(handler,"2");
                    if(!dpbType.getStructure(data,errMsg)){
                        sendMsg(handler,"3");
                        throw new Exception(errMsg.value);
                    }
                    sendMsg(handler,"4");
                    msg = new Message();
                    msg.arg1 = BIND_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj="执行完毕";
                    handler.sendMessage(msg);
                }catch (Exception e){
                    sendMsg(handler,"a18");
                    msg = new Message();
                    msg.arg1 = BIND_ACTION;//告知handler当前action
                    msg.what=100;
                    msg.obj=e.getMessage();
                    handler.sendMessage(msg);
                }



            }
        }.start();

    }
    private void sendMsg(final Handler handler,String txt){
        Message  msg = new Message();
        msg.arg1 = BIND_ACTION;//告知handler当前action
        msg.what=100;
        msg.obj=txt;
        handler.sendMessage(msg);
    }
}
