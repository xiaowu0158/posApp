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

import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.evangelsoft.econnect.util.Encrypter;
import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.SuiteGrp;
import com.example.wuhongjie.myapplication.util.DBhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.name;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class ProdClsService {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static ArrayList<ProdCls> getProdCls(String custCode,String pckNo,Context context){
        ArrayList<ProdCls> listItem = new ArrayList<ProdCls>();
        try{
            if(pckNo==null){
                pckNo="";
            }
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c=db.rawQuery("SELECT GENDER,CNAME FROM EXD_TG_CUSTJOB WHERE CUST_CODE='"+custCode+"' "+(pckNo.length()>0?" AND PCKNO="+pckNo:""),null);
            String gender="";
            if(c.getCount()>0){
                c.moveToNext();
                gender=c.getString(c.getColumnIndex("GENDER"));
                if(gender==null){
                    gender="";
                }
            }
            c.close();
            if(pckNo.length()<=0){
                gender="";
            }
           //db.execSQL("DELETE FROM  EXD_TG_PROD_CLS WHERE CUST_CODE='"+custCode+"'");
            c =db.rawQuery("SELECT * FROM EXD_TG_PROD_CLS WHERE CUST_CODE='"+custCode+"' "+(gender.length()>0?" AND GENDER='"+gender+"'":"")+" ORDER BY ORD_NO,PRD_CODE",null);
            if(c.getCount()>0){
                while (c.moveToNext()) {


                    ProdCls prodCls = new ProdCls();
                    prodCls.setOrdNo(c.getString(c.getColumnIndex("ORD_NO")));
                    prodCls.setSuiteGrpId(c.getString(c.getColumnIndex("SUITE_GRP_ID")));
                    prodCls.setSuiteGrpName(c.getString(c.getColumnIndex("SUITE_GRP_NAME")));
                    prodCls.setPrdCode(c.getString(c.getColumnIndex("PRD_CODE")));
                    prodCls.setPrdName(c.getString(c.getColumnIndex("PRD_NAME")));
                    prodCls.setPrdMemo(c.getString(c.getColumnIndex("PRD_MEMO")));
                    prodCls.setUom(c.getString(c.getColumnIndex("UOM")));
                    prodCls.setProdCatId(c.getString(c.getColumnIndex("PROD_CAT_ID")));
                    prodCls.setProdCatName(c.getString(c.getColumnIndex("PROD_CAT_NAME")));
                    prodCls.setGender(c.getString(c.getColumnIndex("GENDER")));
                    prodCls.setImageFileName(c.getString(c.getColumnIndex("IMAGE_FILE_NAME")));
                    prodCls.setImageUrl(c.getString(c.getColumnIndex("IMAGE_URL")));
                    prodCls.setSelected(false);
                    Cursor d=db.rawQuery("SELECT A.SPEC_CODE,A.TERM_MEMO,B.PATTERN,B.SPEC,B.SHAPE,C.SUITE_CODE,C.SUITE_NAME,A.QTY " +
                            "FROM EXD_TG_ORDITMJOBPRD A " +
                            "INNER JOIN EXD_TG_SUITE_GRP_DTL C ON A.DTL_ID=C.DTL_ID " +
                            "LEFT JOIN (SELECT SPEC_CODE,SUITE_CODE,GENDER,MAX(PATTERN)PATTERN,MAX(SPEC)SPEC,MAX(SHAPE)SHAPE FROM " +
                            "EXD_TG_SPEC " +
                            "GROUP BY SPEC_CODE,SUITE_CODE,GENDER )B ON A.SPEC_CODE=B.SPEC_CODE AND B.SUITE_CODE=C.SUITE_CODE AND B.GENDER=? " +
                            "WHERE A.ORD_NO=? AND A.PRD_CODE=? AND A.SUITE_GRP_ID=? " +
                            "AND A.PCKNO=? " +
                            "ORDER BY C.SORT ",new String[]{
                            c.getString(c.getColumnIndex("GENDER")),
                            c.getString(c.getColumnIndex("ORD_NO")),
                            c.getString(c.getColumnIndex("PRD_CODE")),
                            c.getString(c.getColumnIndex("SUITE_GRP_ID")),
                            pckNo
                    });
                    if(d.getCount()>0){
                        String teamMemo="";
                        while (d.moveToNext()) {

                            teamMemo=teamMemo+d.getString(d.getColumnIndex("SUITE_NAME"))+"：[";
                            teamMemo=teamMemo+"规格编号："+d.getString(d.getColumnIndex("SPEC_CODE"))+";";
                            if(d.getString(d.getColumnIndex("SPEC"))!=null &&
                                    d.getString(d.getColumnIndex("SPEC")).length()>0){
                                teamMemo=teamMemo+"规格："+d.getString(d.getColumnIndex("SPEC"))+";";
                            }
                            if(d.getString(d.getColumnIndex("SHAPE"))!=null &&
                                    d.getString(d.getColumnIndex("SHAPE")).length()>0){
                                teamMemo=teamMemo+"号型："+d.getString(d.getColumnIndex("SHAPE"))+";";
                            }
                            if(d.getString(d.getColumnIndex("PATTERN"))!=null &&
                                    d.getString(d.getColumnIndex("PATTERN")).length()>0){
                                teamMemo=teamMemo+d.getString(d.getColumnIndex("PATTERN"))+";";
                            }
                            if(d.getString(d.getColumnIndex("TERM_MEMO"))!=null &&
                                    d.getString(d.getColumnIndex("TERM_MEMO")).length()>0){
                                teamMemo=teamMemo+d.getString(d.getColumnIndex("TERM_MEMO"))+";";
                            }
                            teamMemo=teamMemo+"]\n";
                            prodCls.setQty(d.getString(d.getColumnIndex("QTY")));
                        }

                        prodCls.setTeamMemo(teamMemo);
                    }
                    d.close();
                    listItem.add(prodCls);
                }
                c.close();
                db.close();
               return listItem;
            }else {
                c.close();
                HashMap<String, String> params=new HashMap<String, String>();
                params.put("custCode",custCode);
                String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getOrderItem.do",
                        params, Constants.CHARSET_GBK);
                JSONObject res=new JSONObject(msg);
                if(!res.getBoolean("success")){
                    throw new Exception(res.getString("msg"));
                }
                JSONArray dataJson=res.getJSONArray("data");
                for(int i=0;i<dataJson.length();i++){

                    JSONObject jsonobject = dataJson.getJSONObject(i);
                    ProdCls prodCls = new ProdCls();
                    prodCls.setOrdNo(jsonobject.getString("ORDNO"));
                    prodCls.setSuiteGrpId(jsonobject.getString("SUITE_GRP_ID"));
                    prodCls.setSuiteGrpName(jsonobject.getString("SUITE_GRP_NAME"));
                    prodCls.setPrdCode(jsonobject.getString("PRD_CODE"));
                    prodCls.setPrdName(jsonobject.getString("PRD_NAME"));
                    prodCls.setPrdMemo(jsonobject.getString("PRD_MEMO"));
                    prodCls.setProdCatId(jsonobject.getString("CAT_ID"));
                    prodCls.setProdCatName(jsonobject.getString("PROD_CAT_NAME"));
                    prodCls.setGender(jsonobject.getString("GENDER"));
                    prodCls.setUom(jsonobject.getString("UOM"));
                    prodCls.setImageFileName(jsonobject.getString("PICTURE_URL"));

                    String fileName=jsonobject.getString("PICTURE_URL");
                    String imageUrl="";
                    if(fileName.length()<=0){
                        prodCls.setImageUrl("");
                    }else{
                        //prodCls.setImageUrl(jsonobject.getString("IMAGE_URL"));
                        imageUrl="http://URL/YoungorTgOrder/getThumPicture.do?prodName="+ Encrypter.encrypt(fileName)+"&w=400&h=300";
                        prodCls.setImageUrl(imageUrl);
                    }
                    prodCls.setSelected(false);
                    listItem.add(prodCls);
                    c=db.rawQuery("SELECT * FROM EXD_TG_PROD_CLS WHERE ORD_NO=? AND CUST_CODE=? AND PRD_CODE=? AND SUITE_GRP_ID=?",
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
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
        /*ProdCls prodCls = new ProdCls();
        prodCls.setOrdNo("Y02170200045-1");
        prodCls.setSuiteGrpId("1");
        prodCls.setSuiteGrpName("一衣一裤");
        prodCls.setPrdCode("T16100288");
        prodCls.setPrdName("T10NDG910095KZ-1");
        prodCls.setPrdMemo("标准:A档辅料  合格证不打印零售价  钮扣:有”YOUNGOR”英文字母  里衬:深色  \n" +
                "上衣:单排二粒扣N83Y-2号板  拱止口  \n" +
                "裤子:XK5-22、XK6-22  脚口:撬边");

        prodCls.setSelected(false);
        listItem.add(prodCls);
        prodCls = new ProdCls();
        prodCls.setOrdNo("Y02170200045-1");
        prodCls.setSuiteGrpId("1");
        prodCls.setSuiteGrpName("一衣二裤");
        prodCls.setPrdCode("T16100129");
        prodCls.setPrdName("T10NDG21107-1");
        prodCls.setPrdMemo("标准:A档辅料  合格证不打印零售价  里衬:深色  钮扣:有”YOUNGOR”英文字母  \n" +
                "上衣:单排二粒扣N83Y-2号板  拱止口  \n" +
                "裤子:XK5-22、XK6-22  脚口:撬边");

        prodCls.setSelected(false);*/


        return listItem;
    }
    public static ProdCls updateProdClsTeamMemo(ProdCls prodCls,String pckNo,String exceptDtlId,Context context ){
        DBhelper dBhelper= new DBhelper(context);
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor d=db.rawQuery("SELECT A.DTL_ID,A.SPEC_CODE,A.TERM_MEMO,A.QTY,B.PATTERN,B.SPEC,B.SHAPE,C.SUITE_CODE,C.SUITE_NAME " +
                "FROM EXD_TG_ORDITMJOBPRD A " +
                "INNER JOIN EXD_TG_SUITE_GRP_DTL C ON A.DTL_ID=C.DTL_ID " +
                "LEFT JOIN (SELECT SPEC_CODE,SUITE_CODE,GENDER,MAX(PATTERN)PATTERN,MAX(SPEC)SPEC,MAX(SHAPE)SHAPE FROM " +
                "EXD_TG_SPEC " +
                "GROUP BY SPEC_CODE,SUITE_CODE,GENDER )B ON A.SPEC_CODE=B.SPEC_CODE AND B.SUITE_CODE=C.SUITE_CODE AND B.GENDER=? " +
                "WHERE A.ORD_NO=? AND A.PRD_CODE=? AND A.SUITE_GRP_ID=? " +
                "AND A.PCKNO=? " +
                "ORDER BY C.SORT ",new String[]{
                prodCls.getGender(),
                prodCls.getOrdNo(),
                prodCls.getPrdCode(),
                prodCls.getSuiteGrpId(),
                pckNo
        });
        if(d.getCount()>0){
            String teamMemo="";
            while (d.moveToNext()) {
                prodCls.setQty(d.getString(d.getColumnIndex("QTY")));
                if(exceptDtlId.length()>0){
                    if(d.getString(d.getColumnIndex("DTL_ID")).equals(exceptDtlId)){
                        continue;
                    }
                }
                teamMemo=teamMemo+d.getString(d.getColumnIndex("SUITE_NAME"))+"：[";
                teamMemo=teamMemo+"规格编号："+d.getString(d.getColumnIndex("SPEC_CODE"))+";";
                if(d.getString(d.getColumnIndex("SPEC")).length()>0){
                    teamMemo=teamMemo+"规格："+d.getString(d.getColumnIndex("SPEC"))+";";
                }
                if(d.getString(d.getColumnIndex("SHAPE")).length()>0){
                    teamMemo=teamMemo+"号型："+d.getString(d.getColumnIndex("SHAPE"))+";";
                }
                if(d.getString(d.getColumnIndex("PATTERN")).length()>0){
                    teamMemo=teamMemo+d.getString(d.getColumnIndex("PATTERN"))+";";
                }
                if(d.getString(d.getColumnIndex("TERM_MEMO")).length()>0){
                    teamMemo=teamMemo+d.getString(d.getColumnIndex("TERM_MEMO"))+";";
                }
                teamMemo=teamMemo+"]\n";
            }

            prodCls.setTeamMemo(teamMemo);
        }
        d.close();
        db.close();
        return prodCls;

    }
    public static Uri getImageURI(Activity context, String path, String fileName) throws Exception {
        path=WebUtils.getRealUrl(path);
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        File cache = new File(Environment.getExternalStorageDirectory(), "YoungorTgCache");

        if(!cache.exists()){
            cache.mkdirs();
        }
        //String name = MD5.getMD5(path) + path.substring(path.lastIndexOf("."));
        //String fileName="";
        File file = new File(cache, fileName);
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
            return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
        } else {
            // 从网络上获取图片
            URL url = new URL(path);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return Uri.fromFile(file);
            }
        }
        return null;
    }
}
