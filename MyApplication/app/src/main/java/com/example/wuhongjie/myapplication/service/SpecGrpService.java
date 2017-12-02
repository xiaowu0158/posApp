package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.SuiteGrp;
import com.example.wuhongjie.myapplication.util.DBhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 * 款式甄别
 */

public class SpecGrpService {
    public static ArrayList<SpecGrp> getSpecGrp(String suiteCode,String gender,Context context){
        ArrayList<SpecGrp> listItem = new ArrayList<SpecGrp>();
        try{
            if(gender==null){
                gender="";
            }
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC_GRP WHERE SUITE_CODE=? "+(gender.length()>0?"AND GENDER='"+gender+"'":""),new String[]{
                suiteCode
            });
            if(c.getCount()>0){
                while (c.moveToNext()) {
                    SpecGrp specGrp = new SpecGrp();
                    specGrp.setSuiteCode(c.getString(c.getColumnIndex("SUITE_CODE")));
                    specGrp.setGrpCode(c.getString(c.getColumnIndex("GRP_CODE")));
                    specGrp.setGrpId(String.valueOf(c.getInt(c.getColumnIndex("GRP_ID"))));
                    specGrp.setGrpName(c.getString(c.getColumnIndex("GRP_NAME")));
                    specGrp.setSelected(false);
                    specGrp.setSpecs(SpecService.getSpec(suiteCode,gender,String.valueOf(c.getInt(c.getColumnIndex("GRP_ID"))),context));
                    listItem.add(specGrp);
                }
                c.close();
                db.close();
                return listItem;
            }
            c.close();
            db.close();
            HashMap<String, String> params=new HashMap<String, String>();
            params.put("suiteCode",suiteCode);
            params.put("gender",gender);
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getSpecGrp.do",
                    params, Constants.CHARSET_GBK);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray dataJson=res.getJSONArray("data");
            for(int i=0;i<dataJson.length();i++){
                JSONObject jsonobject = dataJson.getJSONObject(i);
                SpecGrp specGrp = new SpecGrp();
                specGrp.setSuiteCode(jsonobject.getString("SUITE_CODE"));
                specGrp.setGrpCode(jsonobject.getString("GRP_CODE"));
                specGrp.setGrpId(jsonobject.getString("GRP_ID"));
                specGrp.setGrpName(jsonobject.getString("GRP_NAME"));
                specGrp.setSuiteCode(jsonobject.getString("SUITE_CODE"));
                specGrp.setSelected(false);
                specGrp.setSpecs(SpecService.getSpec(suiteCode,gender,jsonobject.getString("GRP_ID"),context));
                listItem.add(specGrp);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
    public static SpecGrp getSpecGrpByCode(String grpCode,Context context){
        SpecGrp specGrp = new SpecGrp();
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC_GRP WHERE GRP_CODE=?",new String[]{
                    grpCode
            });
            if(c.getCount()>0){
                if (c.moveToNext()) {
                    String suiteCode=c.getString(c.getColumnIndex("SUITE_CODE"));
                    String gender=c.getString(c.getColumnIndex("GENDER"));
                    specGrp.setSuiteCode(suiteCode);
                    specGrp.setGrpCode(c.getString(c.getColumnIndex("GRP_CODE")));
                    specGrp.setGrpId(String.valueOf(c.getInt(c.getColumnIndex("GRP_ID"))));
                    specGrp.setGrpName(c.getString(c.getColumnIndex("GRP_NAME")));
                    specGrp.setSelected(false);
                    specGrp.setSpecs(SpecService.getSpec(suiteCode,gender,String.valueOf(c.getInt(c.getColumnIndex("GRP_ID"))),context));
                }

            }
            c.close();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return specGrp;
    }

}
