package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;
import com.evangelsoft.econnect.dataformat.LocateOption;
import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.evangelsoft.econnect.dataformat.RecordSetHelper;
import com.evangelsoft.econnect.dataformat.TransientRecordSet;
import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.example.wuhongjie.myapplication.adapter.RlbDtlItemAdapter;
import com.example.wuhongjie.myapplication.adapter.VipDtlItemAdapter;
import com.example.wuhongjie.myapplication.type.Employee;
import com.hp.hpl.sparta.Text;
import com.youngor.modules.drp.counter.intf.RlbWeb;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VipSearchActivity extends AppCompatActivity {
    private Handler mSearchHandler;
    private ClientSession session = null;
    private static int VIP_POST = 2901;
    private EditText vip_search_editer;
    private Keyboard k1;
    private KeyboardView keyboardView;
    private ListView vipListView;
    private RecordSet custRs=new RecordSet();
    private VipDtlItemAdapter vipDtlItemAdapter;
    private FrameLayout waiterBar;
    private TextView recCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_seach);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initHandle();
        initView();
    }
    private void initHandle(){
        mSearchHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == DxSessionHelper.SESSION_ERROR) {
                    waiterBar.setVisibility(View.GONE);
                    keyboardView.setEnabled(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(VipSearchActivity.this);
                    builder.setMessage(msg.obj.toString());
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            VipSearchActivity.this.finish();
                        }
                    });
                    builder.create().show();

                }
                if (msg.arg1 == DxSessionHelper.SESSION_ERROR_TOASE) {
                    waiterBar.setVisibility(View.GONE);
                    keyboardView.setEnabled(true);
                    Toast.makeText(VipSearchActivity.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                if (msg.arg1 == VIP_POST) {
                    waiterBar.setVisibility(View.GONE);
                    keyboardView.setEnabled(true);
                    vipDtlItemAdapter = new VipDtlItemAdapter(custRs, VipSearchActivity.this);
                    vipListView.setAdapter(vipDtlItemAdapter);
                    vipDtlItemAdapter.notifyDataSetChanged();

                    recCountTextView.setText(String.valueOf(custRs.recordCount()) );
                }


            }
        };
    }
    private void initView(){
        waiterBar=(FrameLayout)findViewById(R.id.waiterBar);
        vip_search_editer=(EditText)findViewById(R.id.vip_search_editer);
        recCountTextView=(TextView)findViewById(R.id.recCountTextView);
        vip_search_editer.setInputType(InputType.TYPE_NULL);
        vipListView=(ListView)findViewById(R.id.vipListView);
        vipDtlItemAdapter = new VipDtlItemAdapter(custRs, VipSearchActivity.this);
        vipListView.setAdapter(vipDtlItemAdapter);
        vipListView.setOnItemClickListener(new VipListViewItemClick());
        k1 = new Keyboard(VipSearchActivity.this, R.xml.custsymbols);
        keyboardView = (KeyboardView)findViewById(R.id.vipInputKeyboardView);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(keyListener);
        keyboardView.setVisibility(View.VISIBLE);
    }
    private class VipListViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Record vipRc=(Record) adapterView.getItemAtPosition(i);
            if(vipRc==null) {
                return;
            }
            Intent mIntent = VipSearchActivity.this.getIntent();
            mIntent.putExtra("Vip", vipRc);
            VipSearchActivity.this.setResult(RESULT_OK, mIntent);
            VipSearchActivity.this.finish();
        }
    }
    private KeyboardView.OnKeyboardActionListener keyListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = vip_search_editer.getText();
            int start = vip_search_editer.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }  else if (primaryCode == Keyboard.KEYCODE_CANCEL) { // 清空
                editable.clear();
            } else if (primaryCode == Keyboard.KEYCODE_DONE) { // 提交
                waiterBar.setVisibility(View.VISIBLE);
                keyboardView.setEnabled(false);
                postVipCust(mSearchHandler,editable.toString());
                //editable.clear();
            } else if (primaryCode == -999) { // 清空
                finish();
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
    private void postVipCust(final Handler handler, final String custCode){
        new Thread() {
            public void run() {
                Message msg = null;
                try {

                    session = ((YoungorApplication) getApplication()).getDxSession();
                    if (!((YoungorApplication) getApplication()).sessionLinked) {
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR;
                        msg.obj = DxSessionHelper.errMsg;
                        handler.sendMessage(msg);
                        return;
                    }
                    RlbWeb rlbWeb = (RlbWeb) new RMIProxy(session)
                            .newStub(RlbWeb.class);
                    VariantHolder data = new VariantHolder();
                    VariantHolder<String> errMsg = new VariantHolder<String>();
                    data.value = new TransientRecordSet();
                    if (!rlbWeb.getVipCust(custCode, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    custRs=(RecordSet) data.value;
                    if(custRs.recordCount()<=0){
                        throw new Exception("顾客【"+custCode+"】不存在！");
                    }

                    msg = new Message();
                    msg.arg1 = VIP_POST;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    msg = new Message();
                    msg.arg1 = DxSessionHelper.SESSION_ERROR_TOASE;
                    msg.obj = e.getMessage();
                    handler.sendMessage(msg);
                }
            }
        }.start();

    }
}
