package com.example.wuhongjie.myapplication.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.evangelsoft.econnect.util.Encrypter;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.SampleSuiteGrp;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.Suite;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.DBhelper;
import com.example.wuhongjie.myapplication.util.WebUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SuiteService {

    public static ArrayList<Suite> getSuite(String custCode, String pckNo, Context context){
        ArrayList<Suite> listItem = new ArrayList<Suite>();

        try{
            listItem=getSuiteFromDb(custCode,pckNo,context);
            if(listItem.size()==0){
               HashMap<String, String> params=new HashMap<String, String>();
               params.put("custCode",custCode);
               String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getOrderItem.do",
                            params, Constants.CHARSET_GBK);
               JSONObject res=new JSONObject(msg);
               if(!res.getBoolean("success")){
                   throw new Exception(res.getString("msg"));
               }
               JSONArray dataJson=res.getJSONArray("data");
               DBhelper dBhelper= new DBhelper(context);
               SQLiteDatabase db = dBhelper.getWritableDatabase();
               for(int i=0;i<dataJson.length();i++){
                 JSONObject jsonobject = dataJson.getJSONObject(i);
                 String fileName=jsonobject.getString("PICTURE_URL");
                 String imageUrl="";
                 if(fileName.length()>0){
                     imageUrl="http://URL/YoungorTgOrder/getThumPicture.do?prodName="+ Encrypter.encrypt(fileName)+"&w=400&h=300";
                 }
                 Cursor c=db.rawQuery("SELECT * FROM EXD_TG_PROD_CLS WHERE ORD_NO=? AND CUST_CODE=? AND PRD_CODE=? AND SUITE_GRP_ID=?",
                             new String[]{
                                        jsonobject.getString("ORDNO"),
                                        custCode,
                                        jsonobject.getString("PRD_CODE"),
                                        jsonobject.getString("SUITE_GRP_ID")
                             });
                 if(c.getCount()==0){
                     db.execSQL("INSERT INTO EXD_TG_PROD_CLS(ORD_NO,CUST_CODE,PRD_CODE,PRD_NAME,PRD_MEMO,UOM," +
                                 "PROD_CAT_ID,PROD_CAT_NAME,SUITE_GRP_ID,SUITE_GRP_NAME,IMAGE_URL,IMAGE_FILE_NAME,GENDER)" +
                                 "VALUES('"+jsonobject.getString("ORDNO")+"',"+
                                 "'"+custCode+"',"+
                                 "'"+jsonobject.getString("PRD_CODE")+"',"+
                                 "'"+jsonobject.getString("PRD_NAME")+"',"+
                                 "'"+jsonobject.getString("PRD_MEMO")+"',"+
                                 "'"+jsonobject.getString("UOM")+"',"+
                                 "'"+jsonobject.getString("CAT_ID")+"',"+
                                 "'"+jsonobject.getString("PROD_CAT_NAME")+"',"+
                                 "'"+jsonobject.getString("SUITE_GRP_ID")+"',"+
                                 "'"+jsonobject.getString("SUITE_GRP_NAME")+"',"+
                                 "'"+imageUrl+"',"+
                                 "'"+fileName+"',"+
                                 "'"+jsonobject.getString("GENDER")+"')");
                 }
                 c.close();
               }
               db.close();
               listItem=getSuiteFromDb(custCode,pckNo,context);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
    public static ArrayList<Suite> getSuiteFromDb(String custCode, String pckNo, Context context){
            ArrayList<Suite> listItem = new ArrayList<Suite>();
            try{
                Properties pps = new Properties();
                pps.put("SY","上衣");
                pps.put("KZ","裤子");
                pps.put("MJ","马夹");
                pps.put("QZ","裙子");
                pps.put("JK","茄克");
                pps.put("DY","大衣");
                pps.put("CC","衬衫");
                pps.put("TT","特体");

                DBhelper dBhelper= new DBhelper(context);
                SQLiteDatabase db = dBhelper.getWritableDatabase();
                String gender="";
                Cursor c=db.rawQuery("SELECT GENDER FROM EXD_TG_CUSTJOB WHERE CUST_CODE='"+custCode+"' AND PCKNO="+pckNo,null);
                if(c.getCount()>0){
                    if(c.moveToNext()){
                        if(c.getString(c.getColumnIndex("GENDER"))!=null){
                            gender=c.getString(c.getColumnIndex("GENDER"));
                        }
                    }
                }
                c.close();
                c =db.rawQuery("SELECT DISTINCT A.PROD_CAT_ID,A.PROD_CAT_NAME,B.SUITE_CODE,C.GRP_CODE " +
                        "FROM EXD_TG_PROD_CLS A " +
                        "INNER JOIN EXD_TG_SUITE_GRP_DTL B ON A.SUITE_GRP_ID=B.SUITE_GRP_ID " +
                        "LEFT JOIN EXD_TG_PRD_SPEC_GRP C ON A.ORD_NO=C.ORD_NO AND A.PRD_CODE=C.PRD_CODE AND B.SUITE_CODE=C.SUITE_CODE " +
                        "WHERE CUST_CODE='"+custCode+"'"+(gender.length()>0?" AND A.GENDER='"+gender+"'":"")+" ORDER BY A.PROD_CAT_ID,B.SORT ",null);
                if(c.getCount()>0){
                    while (c.moveToNext()) {
                        String suiteCode=c.getString(c.getColumnIndex("SUITE_CODE"));
                        Suite suite = new Suite();
                        suite.setCollapsible(true);
                        suite.setProdCatId(c.getString(c.getColumnIndex("PROD_CAT_ID")));
                        suite.setSuiteCode(c.getString(c.getColumnIndex("SUITE_CODE")));
                        suite.setSuiteName(pps.getProperty(c.getString(c.getColumnIndex("SUITE_CODE"))));
                        suite.setProdCatName(c.getString(c.getColumnIndex("PROD_CAT_NAME")));
                        suite.setGender(gender);
                        suite.setSelected(false);
                        suite.setTeamGroup(TeamService.getTeamGroupData(c.getString(c.getColumnIndex("SUITE_CODE")),pckNo,custCode,context));
                        Cursor d=db.rawQuery("SELECT A.SPEC_CODE,A.TERM_MEMO,B.PATTERN,B.SPEC,B.SHAPE,C.SUITE_CODE,C.SUITE_NAME,D.GRP_CODE " +
                                "FROM EXD_TG_ORDITMJOBPRD A " +
                                "INNER JOIN EXD_TG_SUITE_GRP_DTL C ON A.DTL_ID=C.DTL_ID " +
                                "LEFT JOIN (SELECT SPEC_CODE,SUITE_CODE,GENDER,MAX(PATTERN)PATTERN,MAX(SPEC)SPEC,MAX(SHAPE)SHAPE FROM " +
                                "EXD_TG_SPEC " +
                                "GROUP BY SPEC_CODE,SUITE_CODE,GENDER )B ON A.SPEC_CODE=B.SPEC_CODE AND B.SUITE_CODE=C.SUITE_CODE AND B.GENDER=? " +
                                "LEFT JOIN EXD_TG_PRD_SPEC_GRP D ON A.ORD_NO=D.ORD_NO AND A.PRD_CODE=D.PRD_CODE AND C.SUITE_CODE=D.SUITE_CODE " +
                                "INNER JOIN EXD_TG_PROD_CLS E ON A.ORD_NO=E.ORD_NO AND A.PRD_CODE=E.PRD_CODE " +
                                "WHERE A.PCKNO=? AND C.SUITE_CODE=? AND E.CUST_CODE=?" +
                                "ORDER BY C.SORT ",new String[]{
                                gender,
                                pckNo,
                                c.getString(c.getColumnIndex("SUITE_CODE")),
                                custCode

                        });
                        if(d.getCount()>0){

                            String teamMemo="";
                            while (d.moveToNext()) {
                                String specCode=d.getString(d.getColumnIndex("SPEC_CODE"));
                                String specGrpCode=d.getString(d.getColumnIndex("GRP_CODE"));
                                if(specGrpCode==null){
                                    specGrpCode="";
                                }
                                if(specCode==null){
                                    specCode="";
                                }
                                if(specGrpCode.length()>0){
                                    SpecGrp specGrp=SpecGrpService.getSpecGrpByCode(specGrpCode,context);
                                    suite.setSpecGrp(specGrp);
                                }
                                if(specCode.length()>0){
                                    Spec spec=SpecService.getSingleSpec("",suiteCode,gender,specCode,context);
                                    suite.setSpec(spec);
                                }


                                teamMemo=teamMemo+d.getString(d.getColumnIndex("SUITE_NAME"))+"：[";
                                teamMemo=teamMemo+"规格编号："+d.getString(d.getColumnIndex("SPEC_CODE"))+";";
                                if(d.getString(d.getColumnIndex("SPEC"))!=null && d.getString(d.getColumnIndex("SPEC")).length()>0){
                                    teamMemo=teamMemo+"规格："+d.getString(d.getColumnIndex("SPEC"))+";";
                                }
                                if(d.getString(d.getColumnIndex("SHAPE"))!=null && d.getString(d.getColumnIndex("SHAPE")).length()>0){
                                    teamMemo=teamMemo+"号型："+d.getString(d.getColumnIndex("SHAPE"))+";";
                                }
                                if(d.getString(d.getColumnIndex("PATTERN"))!=null && d.getString(d.getColumnIndex("PATTERN")).length()>0){
                                    teamMemo=teamMemo+d.getString(d.getColumnIndex("PATTERN"))+";";
                                }
                                if(d.getString(d.getColumnIndex("TERM_MEMO"))!=null && d.getString(d.getColumnIndex("TERM_MEMO")).length()>0){
                                    teamMemo=teamMemo+d.getString(d.getColumnIndex("TERM_MEMO"))+";";
                                }
                                teamMemo=teamMemo+"]\n";
                            }

                            suite.setTeamMemo(teamMemo);
                        }
                        d.close();
                        suite.setSpecGrps(SpecGrpService.getSpecGrp(suiteCode,gender,context));
                        if(suite.getSpecGrp()==null){
                            String specGrpCode=c.getString(c.getColumnIndex("GRP_CODE"));
                            if(specGrpCode!=null){
                                SpecGrp specGrp=SpecGrpService.getSpecGrpByCode(specGrpCode,context);
                                suite.setSpecGrp(specGrp);
                            }

                        }
                        listItem.add(suite);
                    }
                    c.close();
                    db.close();

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return listItem;
    }


}
