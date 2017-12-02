package com.evangelsoft.econnect.client;

import android.os.Handler;
import android.os.Message;

import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.intf.KernelPlugIn;
import com.evangelsoft.econnect.rmi.RMIProxy;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-10-20.
 */

public class DxSessionHelper {
    public static int SESSION_ERROR=-9998;
    public static int SESSION_ERROR_TOASE=-9997;
    public static boolean sessionLinked=false;
    public static String errMsg="NONE";

    public static ClientSession getSession(HashMap<String, Object> userObject) {
        ClientSession session = null;
        try {
            //HashMap<String, Object> application = new HashMap<String, Object>();
            //application.put("user", "LIJIE2");
            //application.put("password", "");
            VariantHolder<HashMap<String, Object>> receipt = new VariantHolder<HashMap<String, Object>>();
            session = Agency.getSession("WEB", userObject, receipt);
            DxSessionHelper.sessionLinked=true;
        } catch (Exception e) {
            DxSessionHelper.sessionLinked=false;
            DxSessionHelper.errMsg=e.getMessage();
        }
        return session;
    }
    public static ClientSession getSession(HashMap<String, Object> userObject,ClientSession session) {
        try {
            if(session==null){
                session = getSession(userObject);
            }else{
                KernelPlugIn kernelPlugIn = (KernelPlugIn) (new RMIProxy(session))
                        .newStub(KernelPlugIn.class);
                try{
                    Date dt=  kernelPlugIn.now();
                }catch (Exception e){
                    session = getSession(userObject);
                }
            }
        } catch (Exception e) {
            DxSessionHelper.sessionLinked=false;
            DxSessionHelper.errMsg=e.getMessage();
        }
        return session;
    }
}
