package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.util.DBhelper;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SpecService {
    public static ArrayList<Spec> getSpec(String suiteCode, String gender,String grpId,Context context){
        ArrayList<Spec> listItem = new ArrayList<Spec>();
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC WHERE SUITE_CODE=? AND GENDER=? AND GRP_ID=? ORDER BY SPEC_CODE",new String[]{
                    suiteCode,
                    gender,
                    grpId
            });
            if(c.getCount()>0){
                while (c.moveToNext()) {
                    Spec spec = new Spec();
                    spec.setGrpId(String.valueOf(c.getInt(c.getColumnIndex("GRP_ID"))));
                    spec.setSpecId(String.valueOf(c.getInt(c.getColumnIndex("SPEC_ID"))));
                    spec.setSuiteCode(c.getString(c.getColumnIndex("SUITE_CODE")));
                    spec.setGender(c.getString(c.getColumnIndex("GENDER")));
                    spec.setSpecCode(c.getString(c.getColumnIndex("SPEC_CODE")));
                    spec.setPattern(c.getString(c.getColumnIndex("PATTERN")));
                    spec.setSpec(c.getString(c.getColumnIndex("SPEC")));
                    spec.setShape(c.getString(c.getColumnIndex("SHAPE")));
                    spec.setSelected(false);
                    listItem.add(spec);
                }
                c.close();
                db.close();
                return listItem;
            }
            c.close();
            c =db.rawQuery("SELECT * FROM EXD_TG_SPEC WHERE SUITE_CODE=? ORDER BY SPEC_CODE",new String[]{
                    suiteCode
            });

            if(c.getCount()>0){
                c.close();
                db.close();
                return listItem;
            }
            c.close();
            db.close();
            HashMap<String, String> params=new HashMap<String, String>();

            params.put("suiteCode",suiteCode);
            params.put("gender",gender);
            params.put("grpId",grpId);
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getSpec.do",
                    params, Constants.CHARSET_GBK);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray dataJson=res.getJSONArray("data");
            for(int i=0;i<dataJson.length();i++){
                JSONObject jsonobject = dataJson.getJSONObject(i);
                Spec spec = new Spec();
                spec.setGrpId(jsonobject.getString("GRP_ID"));
                spec.setSpecId(jsonobject.getString("SPEC_ID"));
                spec.setSuiteCode(jsonobject.getString("SUITE_CODE"));
                spec.setGender(jsonobject.getString("GENDER"));
                spec.setSpecCode(jsonobject.getString("SPEC_CODE"));
                spec.setPattern(jsonobject.getString("PATTERN"));
                spec.setSpec(jsonobject.getString("SPEC"));
                spec.setShape(jsonobject.getString("SHAPE"));
                spec.setLength(jsonobject.getString("LENGTH"));
                spec.setWidth(jsonobject.getString("WIDTH"));
                spec.setSelected(false);
                listItem.add(spec);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
    public static Spec getSingleSpec(String grpId,String suiteCode, String gender,String specCode,Context context){
        Spec spec=new Spec();
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SPEC WHERE SUITE_CODE=? AND GENDER=? AND SPEC_CODE=?",new String[]{
                    suiteCode,
                    gender,
                    specCode
            });
            if(c.getCount()>0){
                if (c.moveToNext()) {
                    spec.setGrpId(grpId);
                    spec.setSpecId(c.getString(c.getColumnIndex("SPEC_ID")));
                    spec.setSuiteCode(suiteCode);
                    spec.setGender(gender);
                    spec.setSpecCode(specCode);
                    spec.setPattern(c.getString(c.getColumnIndex("PATTERN")));
                    spec.setSpec(c.getString(c.getColumnIndex("SPEC")));
                    spec.setShape(c.getString(c.getColumnIndex("SHAPE")));
                    spec.setSelected(false);
                }
            }
            c.close();
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return spec;
    }
}
