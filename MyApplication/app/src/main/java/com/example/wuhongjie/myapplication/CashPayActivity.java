package com.example.wuhongjie.myapplication;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;



public class CashPayActivity extends AppCompatActivity {

    private Keyboard k1;
    private Keyboard k2;
    private KeyboardView keyboardView;
    private TextView cashTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_pay);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();

    }

    private void initView() {
        setTitle("结算");
        // vip_search_editer=(EditText)findViewById(R.id.vip_search_editer);
        // vip_search_editer.setInputType(InputType.TYPE_NULL);
        // vipListView=(ListView)findViewById(R.id.vipListView);
        //  vipDtlItemAdapter = new VipDtlItemAdapter(custRs, VipSearchActivity.this);
        //  vipListView.setAdapter(vipDtlItemAdapter);
        //   vipListView.setOnItemClickListener(new VipSearchActivity.VipListViewItemClick());
        cashTextView = (TextView) findViewById(R.id.cashTextView);
        k1 = new Keyboard(CashPayActivity.this, R.xml.paysymbols);
        k2 = new Keyboard(CashPayActivity.this, R.xml.cardsymbols);
        keyboardView = (KeyboardView) findViewById(R.id.keybordview);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(keyListener);
        keyboardView.setVisibility(View.VISIBLE);
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
            /*
            Editable editable = cashTextView.getText();
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
            }*/
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
}
