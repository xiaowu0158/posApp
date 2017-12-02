package com.example.wuhongjie.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;
import com.example.wuhongjie.myapplication.service.SynService;
import com.example.wuhongjie.myapplication.util.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    private ViewSwitcher mViewSwitcher;
    private View loginLoading;
    private AutoCompleteTextView mAccount;
    private EditText mPwd;
    private CheckBox chb_rememberMe;
    private ImageButton btn_close;
    private Button btn_login;
    private InputMethodManager imm;
    private AnimationDrawable loadingAnimation;
    private SharedPreferences sharedPreferences;
    private static int LOGIN_ACTION=12;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);
        loginLoading = (View)findViewById(R.id.login_loading);
        mAccount = (AutoCompleteTextView)findViewById(R.id.login_account);
        mPwd = (EditText)findViewById(R.id.login_password);
       // chb_rememberMe = (CheckBox)findViewById(R.id.login_checkbox_rememberMe);
        btn_close = (ImageButton)findViewById(R.id.login_close_button);
        btn_close.setOnClickListener(new CloseButtonClick());
        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new LoginButtonClick());
        btn_logout = (Button)findViewById(R.id.login_btn_logout);
        btn_logout.setOnClickListener(new LogoutButtonClick());
        String account=sharedPreferences.getString("user","");
        String password=sharedPreferences.getString("password","");
        if(account.length()>0){
            mAccount.setText(account);
            mPwd.setText(password);
        }
    }
    private class CloseButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            LoginActivity.this.finish();
        }
    }
    private class LoginButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            String account = mAccount.getText().toString();
            String pwd = mPwd.getText().toString();

            if(StringUtils.isEmpty(account)){
                ToastMessage(v.getContext(), "账号不能为空！");
                return;
            }
            if(StringUtils.isEmpty(pwd)){
                ToastMessage(v.getContext(), "密码不能为空！");
                return;
            }
            btn_close.setVisibility(View.GONE);
            loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
            loadingAnimation.start();
            mViewSwitcher.showNext();
            login(account, pwd, true);
        }
    }
    private class LogoutButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            String account = mAccount.getText().toString();
            String pwd = mPwd.getText().toString();

            if(StringUtils.isEmpty(account)){
                return;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user", "");
            editor.putString("userName", "");
            editor.putString("password", "");
            editor.commit();
            finish();
           // btn_close.setVisibility(View.GONE);
           // loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
           // loadingAnimation.start();
           // mViewSwitcher.showNext();
          //  login(account, pwd, true);
        }
    }
    private void login(final String account, final String pwd, final boolean isRememberMe) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.arg1==LOGIN_ACTION){
                    if(msg.what == 1){

                        ToastMessage(LoginActivity.this, "登陆成功！");
                        finish();
                    }else{
                        mViewSwitcher.showPrevious();
                        btn_close.setVisibility(View.VISIBLE);
                        ToastMessage(LoginActivity.this, "登陆失败："+msg.obj);

                    }
                }
                if(msg.arg1== DxSessionHelper.SESSION_ERROR){
                    mViewSwitcher.showPrevious();
                    btn_close.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                }


            }
        };
        new Thread(){
            public void run() {
                Message msg =new Message();
                msg.arg1 = LOGIN_ACTION;//告知handler当前action
                try {

                    String loginMsg=SynService.logIn(LoginActivity.this,account,pwd);
                    JSONObject res=new JSONObject(loginMsg);
                    if(res.getBoolean("success")){
                        //保存登录信息
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user", account.toUpperCase());
                        editor.putString("userName", res.getString("USER_NAME"));
                        editor.putString("password", pwd);
                        editor.commit();
                        /*((YoungorApplication)getApplication()).appSession=null;
                        ClientSession session=((YoungorApplication)getApplication()).getDxSession();
                        if(!((YoungorApplication)getApplication()).sessionLinked){
                            msg = new Message();
                            msg.arg1 = DxSessionHelper.SESSION_ERROR;//告知handler当前action
                            msg.obj=DxSessionHelper.errMsg;
                            handler.sendMessage(msg);
                            return;
                        }*/
                        msg.what = 1;//成功
                    }else{
                       // ac.cleanLoginInfo();//清除登录信息
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user", "");
                        editor.putString("password", "");
                        editor.putString("userName", "");
                        editor.commit();
                        msg.what = 0;//失败
                        msg.obj = res.getString("msg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e.getMessage();
                }
                handler.sendMessage(msg);
            }
        }.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public static void ToastMessage(Context cont, String msg){
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }
}

