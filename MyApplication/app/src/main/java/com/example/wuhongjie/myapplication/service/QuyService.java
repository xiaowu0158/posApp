package com.example.wuhongjie.myapplication.service;

import android.provider.SyncStateContract;

import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.Yxgs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class QuyService {
    public static ArrayList<Area> getQuyData(String yxgsDm,String quyDm){
        ArrayList<Area> listItem = new ArrayList<Area>();
        try{
            System.out.println("营销公司代码："+yxgsDm);
            String charset = Constants.CHARSET_GBK;
            HashMap<String, String> params=new HashMap<String, String>();
            params.put("yxgs",yxgsDm);
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getQuy.do",
                    params,charset);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray quyJson=res.getJSONArray("data");
            for(int i=0;i<quyJson.length();i++){
                JSONObject jsonobject = quyJson.getJSONObject(i);
                Area area = new Area();
                area.setQuyDm(jsonobject.getString("QUYDM"));
                area.setQuyMc(jsonobject.getString("QUYMC"));
                area.setSelected(false);
                if(jsonobject.getString("QUYDM").equals(quyDm)){
                    area.setSelected(true);
                }
                listItem.add(area);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

//        Area area = new Area();
//        area.setYxgsDm("01");
//        area.setQuyDm("QY0071");
//        area.setQuyMc("服饰直销");
//        area.setSelected(false);
//        listItem.add(area);
//        area = new Area();
//        area.setYxgsDm("01");
//        area.setQuyDm("QY0073");
//        area.setQuyMc("电商直销");
//        area.setSelected(false);
//        listItem.add(area);
//        area = new Area();
//        area.setYxgsDm("01");
//        area.setQuyDm("QY0074");
//        area.setQuyMc("上海雅士会所");
//        area.setSelected(false);
//        listItem.add(area);
        return listItem;
    }
}
