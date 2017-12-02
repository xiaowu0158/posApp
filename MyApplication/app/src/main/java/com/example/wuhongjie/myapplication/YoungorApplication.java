package com.example.wuhongjie.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;

import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-10-20.
 */

public class YoungorApplication extends Application {
    public ClientSession appSession;
    public  boolean sessionLinked=false;
    public  String errMsg="NONE";
    public ClientSession getDxSession() {
        HashMap<String, Object> userObject=new HashMap<String, Object>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        userObject.put("user",sharedPreferences.getString("user", ""));
        userObject.put("password",sharedPreferences.getString("password", ""));
        appSession= DxSessionHelper.getSession(userObject,appSession);
        sessionLinked=DxSessionHelper.sessionLinked;
        errMsg=DxSessionHelper.errMsg;
        return appSession;
    }
}
