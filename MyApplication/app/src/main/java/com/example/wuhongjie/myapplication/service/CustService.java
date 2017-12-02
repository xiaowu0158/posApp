package com.example.wuhongjie.myapplication.service;

import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class CustService {
    public static ArrayList<Cust> getCustData(String quyDm,String custName){
        ArrayList<Cust> listItem = new ArrayList<Cust>();
        try{
            HashMap<String, String> params=new HashMap<String, String>();
            custName=java.net.URLEncoder.encode(custName, "GBK");
            System.out.println(custName);
            params.put("quyDm",quyDm);
            params.put("custName",custName);
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getCust.do",
                    params, Constants.CHARSET_GBK);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray custJson=res.getJSONArray("data");
            for(int i=0;i<custJson.length();i++){
                JSONObject jsonobject = custJson.getJSONObject(i);
                Cust tgCust = new Cust();
                tgCust.setAreaCode(jsonobject.getString("AREA_CODE"));
                tgCust.setCustCode(jsonobject.getString("CUST_CODE"));
                tgCust.setCustName(jsonobject.getString("CUST_NAME"));
                //System.out.println(jsonobject.getString("CUST_NAME"));
                tgCust.setSelected(false);
                listItem.add(tgCust);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
}
