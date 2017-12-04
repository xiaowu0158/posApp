package com.example.wuhongjie.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.type.Team;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CouponWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private EditText coupon_editer;
    private KeyboardView keyboardView;
    private Keyboard k1;
    private  Context context=null;
    private Handler handler;
    private  PopupWindow popupWindow=null;
    public CouponWindow(){
    }
    public CouponWindow(Context context, Handler handler,int width,int height) {
        super();
        this.context=context;
        this.handler=handler;
        View view= LayoutInflater.from(context).inflate(R.layout.activity_coupon, null);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("user.home"));

       // view.setFocusable(true); // 这个很重要
       // view.setFocusableInTouchMode(true);
        k1 = new Keyboard(context, R.xml.paysymbols);
        coupon_editer=(EditText)view.findViewById(R.id.coupon_editer);
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboardView_favorable);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(keyListener);
        keyboardView.setVisibility(View.VISIBLE);
        popupWindow=new PopupWindow(view, width, height);
       // popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.PopWindowAnimStyle);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

    }
    public void showAsDropDown(View parent){
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDismiss() {

    }
    public static void ToastMessage(Context cont, String msg){
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
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

            Editable editable = coupon_editer.getText();
            int start = coupon_editer.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }  else if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 清空
                editable.clear();
            } else {
                System.out.println(keyCodes.length);
                for(int i=0;i<keyCodes.length;i++){
                    if(keyCodes[i]<0){
                        break;
                    }
                    editable.insert(start, Character.toString((char) keyCodes[i]));
                    start++;
                }
               // editable.insert(start, Character.toString((char) primaryCode));
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


}
