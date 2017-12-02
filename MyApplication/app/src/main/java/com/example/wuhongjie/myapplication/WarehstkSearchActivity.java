package com.example.wuhongjie.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;
import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.evangelsoft.econnect.dataformat.TransientRecordSet;
import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.example.wuhongjie.myapplication.adapter.VipDtlItemAdapter;
import com.example.wuhongjie.myapplication.adapter.WarehStkItemAdapter;
import com.youngor.modules.drp.counter.intf.RlbWeb;

public class WarehstkSearchActivity extends AppCompatActivity {
    private Handler mSearchHandler;
    private ClientSession session = null;
    private static int WAREH_STK_POST = 2902;
    private EditText prod_search_editer;
    private Keyboard k1;
    private KeyboardView keyboardView;
    private ListView warehStkListView;
    private RecordSet warehStkRs=new RecordSet();
    private RecordSet shopRs;
    private WarehStkItemAdapter warehStkItemAdapter;
    private FrameLayout waiterBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehstk_seach);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        shopRs=(RecordSet)intent.getExtras().get("shop");
        initHandle();
        initView();
    }
    private void initHandle(){
        mSearchHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.arg1 == DxSessionHelper.SESSION_ERROR) {
                    waiterBar.setVisibility(View.GONE);
                    keyboardView.setEnabled(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(WarehstkSearchActivity.this);
                    builder.setMessage(msg.obj.toString());
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            WarehstkSearchActivity.this.finish();
                        }
                    });
                    builder.create().show();

                }
                if (msg.arg1 == DxSessionHelper.SESSION_ERROR_TOASE) {
                    waiterBar.setVisibility(View.GONE);
                    keyboardView.setEnabled(true);
                    Toast.makeText(WarehstkSearchActivity.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                if (msg.arg1 == WAREH_STK_POST) {
                    waiterBar.setVisibility(View.GONE);
                    keyboardView.setEnabled(true);
                    warehStkItemAdapter = new WarehStkItemAdapter(warehStkRs, WarehstkSearchActivity.this);
                    warehStkListView.setAdapter(warehStkItemAdapter);
                    warehStkItemAdapter.notifyDataSetChanged();
                }


            }
        };
    }
    private void initView(){
        waiterBar=(FrameLayout)findViewById(R.id.waiterBar);
        prod_search_editer=(EditText)findViewById(R.id.prod_search_editer);
        prod_search_editer.setInputType(InputType.TYPE_NULL);
        warehStkListView=(ListView)findViewById(R.id.warehStkListView);
        warehStkItemAdapter = new WarehStkItemAdapter(warehStkRs, WarehstkSearchActivity.this);
        warehStkListView.setAdapter(warehStkItemAdapter);
        warehStkListView.setOnItemClickListener(new WarehStkListViewItemClick());
        k1 = new Keyboard(WarehstkSearchActivity.this, R.xml.custsymbols);
        keyboardView = (KeyboardView)findViewById(R.id.stkInputKeyboardView);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(keyListener);
        keyboardView.setVisibility(View.VISIBLE);
    }
    private class WarehStkListViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Record warehStkRc=(Record) adapterView.getItemAtPosition(i);
            if(warehStkRc==null) {
                return;
            }
            Intent mIntent = WarehstkSearchActivity.this.getIntent();
            mIntent.putExtra("WarehStk", warehStkRc);
            WarehstkSearchActivity.this.setResult(RESULT_OK, mIntent);
            WarehstkSearchActivity.this.finish();
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
            Editable editable = prod_search_editer.getText();
            int start = prod_search_editer.getSelectionStart();
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
                postWarehStkProd(mSearchHandler,editable.toString());
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
    private void postWarehStkProd(final Handler handler, final String prodCode){
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
                    Object[] key=new Object[]{
                            shopRs.getRecord(0).getField("SHOP_ID").getNumber(),
                            prodCode
                    };
                    data.value = new TransientRecordSet();
                    if (!rlbWeb.getWarehStk(key, data, errMsg)) {
                        throw new Exception(errMsg.value);
                    }
                    warehStkRs=(RecordSet) data.value;
                    if(warehStkRs.recordCount()<=0){
                        throw new Exception("货号【"+prodCode+"】没有库存！");
                    }

                    msg = new Message();
                    msg.arg1 = WAREH_STK_POST;
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
