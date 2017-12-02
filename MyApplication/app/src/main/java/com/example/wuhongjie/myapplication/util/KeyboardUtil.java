package com.example.wuhongjie.myapplication.util;
   
import java.util.List;  
   
import android.app.Activity;  
import android.content.Context;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;  
import android.inputmethodservice.Keyboard.Key;  
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;  
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.wuhongjie.myapplication.R;

public class KeyboardUtil {  
        private Context ctx;  
        private Activity act;  
        private KeyboardView keyboardView;  
        private Keyboard k1;// 字母键盘  
        public boolean isnun = false;// 是否数据键盘
        public boolean isupper = false;// 是否大写  
   
        private EditText ed;  
   
        public KeyboardUtil( Activity act,Context ctx, EditText edit) {
                this.act = act;  
                this.ctx = ctx;  
                this.ed = edit;  

                k1 = new Keyboard(ctx, R.xml.symbols);

                keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
                keyboardView.setKeyboard(k1);  
                keyboardView.setEnabled(true);  
                keyboardView.setPreviewEnabled(true);  
                keyboardView.setOnKeyboardActionListener(listener);

                edit.requestFocus();
        }  
   
        private OnKeyboardActionListener listener = new OnKeyboardActionListener() {  
                @Override  
                public void swipeUp() {  
                }  
   
                @Override  
                public void swipeRight() {  
                }  
   
                @Override  
                public void swipeLeft() {  
                }  
   
                @Override  
                public void swipeDown() {  
                }  
   
                @Override  
                public void onText(CharSequence text) {  
                }  
   
                @Override  
                public void onRelease(int primaryCode) {  
                }  
   
                @Override  
                public void onPress(int primaryCode) {  
                }  
   
                @Override  
                public void onKey(int primaryCode, int[] keyCodes) {  
                        Editable editable = ed.getText();  
                        int start = ed.getSelectionStart();  
                        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成  
                                hideKeyboard();  
                        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退  
                                if (editable != null && editable.length() > 0) {  
                                        if (start > 0) {  
                                                editable.delete(start - 1, start);  
                                        }  
                                }  
                        } else if (primaryCode == 57419) { // go left
                                if (start > 0) {  
                                        ed.setSelection(start - 1);  
                                }  
                        } else if (primaryCode == 57421) { // go right  
                                if (start < ed.length()) {  
                                        ed.setSelection(start + 1);  
                                }  
                        } else {  
                                editable.insert(start, Character.toString((char) primaryCode));  
                        }  
                }  
        };



        public void showKeyboard() {
                int visibility = keyboardView.getVisibility();
                if (visibility == View.GONE || visibility == View.INVISIBLE) {
                        keyboardView.setVisibility(View.VISIBLE);
                }
        }

        public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();  
        if (visibility == View.VISIBLE) {  
            keyboardView.setVisibility(View.INVISIBLE);  
        }  
    }  

   
}  