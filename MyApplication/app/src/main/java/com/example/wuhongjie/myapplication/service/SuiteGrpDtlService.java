package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.SuiteGrp;
import com.example.wuhongjie.myapplication.type.SuiteGrpDtl;
import com.example.wuhongjie.myapplication.util.DBhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SuiteGrpDtlService {
    public static ArrayList<SuiteGrpDtl> getSuiteGrp(String suiteGrpId, String suiteGrpName, String prodCatId, ProdCls prodCls,Employee employee, Context context){
        ArrayList<SuiteGrpDtl> listItem = new ArrayList<SuiteGrpDtl>();
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM EXD_TG_SUITE_GRP_DTL WHERE SUITE_GRP_ID=?",new String[]{
                    suiteGrpId
            });
            if(c.getCount()>0){
                while (c.moveToNext()) {
                    SuiteGrpDtl suiteGrpDtl = new SuiteGrpDtl();
                    suiteGrpDtl.setSuiteGrpId(suiteGrpId);
                    suiteGrpDtl.setSuiteGrpName(suiteGrpName);
                    suiteGrpDtl.setSuiteCode(c.getString(c.getColumnIndex("SUITE_CODE")));
                    suiteGrpDtl.setSuiteName(c.getString(c.getColumnIndex("SUITE_NAME")));
                    suiteGrpDtl.setSuiteSort(c.getString(c.getColumnIndex("SUITE_SORT")));
                    suiteGrpDtl.setDtlId(String.valueOf(c.getInt(c.getColumnIndex("DTL_ID"))));
                    suiteGrpDtl.setSelected(false);
                    Cursor d=db.rawQuery("SELECT SPEC_CODE,TERM_MEMO FROM EXD_TG_ORDITMJOBPRD " +
                            "WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_GRP_ID=? AND PCKNO=? AND DTL_ID=?",new String[]{
                            prodCls.getOrdNo(),
                            prodCls.getPrdCode(),
                            suiteGrpId,
                            employee.getPckNo(),
                            String.valueOf(c.getInt(c.getColumnIndex("DTL_ID")))
                    });
                    if(d.getCount()>0){
                        d.moveToNext();
                        String ggbh=d.getString(d.getColumnIndex("SPEC_CODE"));
                        String sybz=d.getString(d.getColumnIndex("TERM_MEMO"));
                        if(ggbh.length()>0){
                            ggbh="规格编号：【"+ggbh+"】";
                        }
                        if(sybz.length()>0){
                            sybz="术语备注："+sybz;
                        }

                        suiteGrpDtl.setTermMemoRst(ggbh+sybz);
                    }
                    d.close();
                    listItem.add(suiteGrpDtl);
                }
                c.close();
                db.close();
                return listItem;
            }
            c.close();
            db.close();
            HashMap<String, String> params=new HashMap<String, String>();

            params.put("suiteGrpId",suiteGrpId);
            params.put("catId",prodCatId);
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getSuiteGrpDtl.do",
                    params, Constants.CHARSET_GBK);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray dataJson=res.getJSONArray("data");
            for(int i=0;i<dataJson.length();i++){
                JSONObject jsonobject = dataJson.getJSONObject(i);
                SuiteGrpDtl suiteGrpDtl = new SuiteGrpDtl();
                suiteGrpDtl.setSuiteGrpId(suiteGrpId);
                suiteGrpDtl.setSuiteGrpName(suiteGrpName);
                suiteGrpDtl.setSuiteCode(jsonobject.getString("SUITE_CODE"));
                suiteGrpDtl.setSuiteName(jsonobject.getString("SUITE_NAME"));
                suiteGrpDtl.setSuiteSort(jsonobject.getString("SUITE_SORT"));
                suiteGrpDtl.setDtlId(jsonobject.getString("DTL_ID"));
                suiteGrpDtl.setSelected(false);
                listItem.add(suiteGrpDtl);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        /*SuiteGrpDtl suiteGrpDtl = new SuiteGrpDtl();
        suiteGrpDtl.setSuiteGrpId("1");
        suiteGrpDtl.setSuiteGrpName("一衣一裤");
        suiteGrpDtl.setSuiteCode("SY");
        suiteGrpDtl.setSuiteName("上衣");
        suiteGrpDtl.setSuiteSort("1");
        suiteGrpDtl.setSelected(false);
        listItem.add(suiteGrpDtl);
        suiteGrpDtl = new SuiteGrpDtl();
        suiteGrpDtl.setSuiteGrpId("1");
        suiteGrpDtl.setSuiteGrpName("一衣一裤");
        suiteGrpDtl.setSuiteCode("KZ");
        suiteGrpDtl.setSuiteName("裤子");
        suiteGrpDtl.setSuiteSort("2");
        suiteGrpDtl.setSelected(false);
        listItem.add(suiteGrpDtl);*/
        return listItem;
    }
}
