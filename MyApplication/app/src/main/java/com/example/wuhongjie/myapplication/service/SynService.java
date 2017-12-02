package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.Suite;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.util.DBhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SynService {
    public static String upLoadData(Context context,String userCode){
        String errMsg="";
        try{
            String phoneName = android.os.Build.MODEL ;
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT MAX(ID)ID FROM EXD_TG_SYN ",null);
            String id="";
            String lastTime="";
            if(c.getCount()>0){
                c.moveToNext();
                if(c.getString(c.getColumnIndex("ID"))!=null){
                    id=c.getString(c.getColumnIndex("ID"));
                    c.close();
                    c =db.rawQuery("SELECT UPD_TIME FROM EXD_TG_SYN WHERE ID='"+id+"'",null);
                    if(c.getCount()>0){
                        c.moveToNext();
                        if(c.getString(c.getColumnIndex("UPD_TIME"))!=null){
                            lastTime=c.getString(c.getColumnIndex("UPD_TIME"));
                        }
                    }
                }
            }




            c.close();
            c =db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD " +
                    "WHERE UPD_TIME>?",new String[]{
                    lastTime
            });
            if(c.getCount()>0){
                JSONArray data=new JSONArray();
                while (c.moveToNext()){
                    JSONObject jobProd=new JSONObject();
                    jobProd.put("JOB_ID",c.getString(c.getColumnIndex("JOB_ID")));
                    jobProd.put("ORD_NO",c.getString(c.getColumnIndex("ORD_NO")));
                    jobProd.put("PRD_CODE",c.getString(c.getColumnIndex("PRD_CODE")));
                    jobProd.put("SUITE_GRP_ID",c.getString(c.getColumnIndex("SUITE_GRP_ID")));
                    jobProd.put("PCKNO",c.getString(c.getColumnIndex("PCKNO")));
                    jobProd.put("GRP_ID",c.getString(c.getColumnIndex("GRP_ID")));
                    jobProd.put("SPEC_CODE",c.getString(c.getColumnIndex("SPEC_CODE")));
                    jobProd.put("TERM_MEMO",c.getString(c.getColumnIndex("TERM_MEMO")));
                    jobProd.put("DTL_ID",c.getString(c.getColumnIndex("DTL_ID")));
                    if(c.getString(c.getColumnIndex("QTY"))==null){
                        jobProd.put("QTY","1");
                    }else{
                        jobProd.put("QTY",c.getString(c.getColumnIndex("QTY")));
                    }

                    jobProd.put("OPR_CODE",userCode);
                    jobProd.put("DEVICE_NAME",phoneName);
                    Cursor d=db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD_DTL " +
                            "WHERE JOB_ID=?",new String[]{
                            c.getString(c.getColumnIndex("JOB_ID"))
                    });
                    if(d.getCount()>0){
                        JSONArray jobProdDtlArray=new JSONArray();
                        while (d.moveToNext()){
                            JSONObject jobProdDtl=new JSONObject();
                            jobProdDtl.put("JOB_ID",d.getString(d.getColumnIndex("JOB_ID")));
                            jobProdDtl.put("TERM_ID",d.getString(d.getColumnIndex("TERM_ID")));
                            jobProdDtl.put("CURR_VAL",d.getString(d.getColumnIndex("CURR_VAL")));
                            jobProdDtl.put("DESCRIPTION",d.getString(d.getColumnIndex("DESCRIPTION")));
                            jobProdDtlArray.put(jobProdDtl);
                        }
                        jobProd.put("DTL",jobProdDtlArray);
                    }
                    d.close();
                    data.put(jobProd);
                }
                int connectTimeout = 3000;// 3秒
                int readTimeout = 15000000;// 15秒
                HashMap<String, String> params=new HashMap<String, String>();

                params.put("postData",data.toString());
                String ret= WebUtils.doPost("http://URL/YoungorTgOrder/saveOrdItemJobProd.do",
                        params, Constants.CHARSET_GBK,connectTimeout, readTimeout);
                JSONObject res=new JSONObject(ret);
                if(!res.getBoolean("success")){
                    throw new Exception(res.getString("msg"));
                }

                System.out.println("========================");
                System.out.println(data.toString());

            }
            c.close();

            db.execSQL("INSERT INTO EXD_TG_SYN(UPD_TIME)VALUES(datetime('now')) ");

            db.close();


        }catch (Exception e){
            e.printStackTrace();
            errMsg=e.getMessage();
        }
        return errMsg;
    }
    public static String upLoadData(Context context,String userCode,String custCode){
        String errMsg="";
        try{
            String phoneName = android.os.Build.MODEL ;
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT DISTINCT A.* FROM EXD_TG_ORDITMJOBPRD A " +
                    "INNER JOIN EXD_TG_PROD_CLS B ON A.ORD_NO=B.ORD_NO AND A.PRD_CODE=B.PRD_CODE " +
                    "WHERE B.CUST_CODE=? " ,new String[]{
                    custCode
            });
            if(c.getCount()>0){
                JSONArray data=new JSONArray();
                while (c.moveToNext()){
                    JSONObject jobProd=new JSONObject();
                    jobProd.put("JOB_ID",c.getString(c.getColumnIndex("JOB_ID")));
                    jobProd.put("ORD_NO",c.getString(c.getColumnIndex("ORD_NO")));
                    jobProd.put("PRD_CODE",c.getString(c.getColumnIndex("PRD_CODE")));
                    jobProd.put("SUITE_GRP_ID",c.getString(c.getColumnIndex("SUITE_GRP_ID")));
                    jobProd.put("PCKNO",c.getString(c.getColumnIndex("PCKNO")));
                    jobProd.put("GRP_ID",c.getString(c.getColumnIndex("GRP_ID")));
                    jobProd.put("SPEC_CODE",c.getString(c.getColumnIndex("SPEC_CODE")));
                    jobProd.put("TERM_MEMO",c.getString(c.getColumnIndex("TERM_MEMO")));
                    jobProd.put("DTL_ID",c.getString(c.getColumnIndex("DTL_ID")));
                    if(c.getString(c.getColumnIndex("QTY"))==null){
                        jobProd.put("QTY","1");
                    }else{
                        jobProd.put("QTY",c.getString(c.getColumnIndex("QTY")));
                    }

                    jobProd.put("OPR_CODE",userCode);
                    jobProd.put("DEVICE_NAME",phoneName);
                    Cursor d=db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD_DTL " +
                            "WHERE JOB_ID=?",new String[]{
                            c.getString(c.getColumnIndex("JOB_ID"))
                    });
                    if(d.getCount()>0){
                        JSONArray jobProdDtlArray=new JSONArray();
                        while (d.moveToNext()){
                            JSONObject jobProdDtl=new JSONObject();
                            jobProdDtl.put("JOB_ID",d.getString(d.getColumnIndex("JOB_ID")));
                            jobProdDtl.put("TERM_ID",d.getString(d.getColumnIndex("TERM_ID")));
                            jobProdDtl.put("CURR_VAL",d.getString(d.getColumnIndex("CURR_VAL")));
                            jobProdDtl.put("DESCRIPTION",d.getString(d.getColumnIndex("DESCRIPTION")));
                            jobProdDtlArray.put(jobProdDtl);
                        }
                        jobProd.put("DTL",jobProdDtlArray);
                    }
                    d.close();
                    data.put(jobProd);
                }
                int connectTimeout = 3000;// 3秒
                int readTimeout = 15000000;// 15秒
                HashMap<String, String> params=new HashMap<String, String>();

                params.put("postData",data.toString());
                String ret= WebUtils.doPost("http://URL/YoungorTgOrder/saveOrdItemJobProd.do",
                        params, Constants.CHARSET_GBK,connectTimeout, readTimeout);
                JSONObject res=new JSONObject(ret);
                if(!res.getBoolean("success")){
                    throw new Exception(res.getString("msg"));
                }

                System.out.println("========================");
                System.out.println(data.toString());

            }
            c.close();



            db.close();


        }catch (Exception e){
            e.printStackTrace();
            errMsg=e.getMessage();
        }
        return errMsg;
    }
    public static String logIn(Context context,String user,String password){
        String errMsg="";
        try{
            HashMap<String, String> params=new HashMap<String, String>();
            params.put("user",user);
            params.put("password",password);
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/drp/tuangou/order/getSession.do",
                    params, Constants.CHARSET_GBK);
            //JSONObject res=new JSONObject(msg);
            //if(!res.getBoolean("success")){
            //    throw new Exception(res.getString("msg"));
           // }
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            errMsg=e.getMessage();
            return errMsg;
        }

    }
    public static String saveData(Context context, Employee employee, ProdCls prodCls, Suite suite){
        String errMsg="";
        try{
                DBhelper dBhelper= new DBhelper(context);
                SQLiteDatabase db = dBhelper.getWritableDatabase();
                String qty=prodCls.getQty();
                if(qty==null){
                    qty="1";
                }
                Cursor c=null;
                //保存款式甄别
                if(suite.getSpecGrp()!=null){
                    c =db.rawQuery("SELECT * FROM EXD_TG_PRD_SPEC_GRP WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_CODE=?",
                            new String[]{prodCls.getOrdNo(),prodCls.getPrdCode(),suite.getSuiteCode()} );
                    if(c.getCount()==0){
                        db.execSQL("INSERT INTO EXD_TG_PRD_SPEC_GRP(ORD_NO,PRD_CODE,SUITE_CODE,GRP_CODE)" +
                                "VALUES(?,?,?,?)",new Object[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                suite.getSuiteCode(),
                                suite.getSpecGrp().getGrpCode()
                        });
                    }else {
                        db.execSQL("UPDATE EXD_TG_PRD_SPEC_GRP " +
                                "SET GRP_CODE=? WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_CODE=?",new Object[]{
                                suite.getSpecGrp().getGrpCode(),
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                suite.getSuiteCode()
                        });
                    }
                    c.close();
                }
                if(suite.getSpec()==null){
                    db.close();
                    return "";
                }
                //保存规格信息
                c=db.rawQuery("SELECT DISTINCT DTL_ID FROM EXD_TG_SUITE_GRP_DTL WHERE SUITE_GRP_ID=? AND SUITE_CODE=?",
                        new String[]{prodCls.getSuiteGrpId(),suite.getSuiteCode()});
                if(c.getCount()>0){
                    while (c.moveToNext()){
                        String dtlId=c.getString(c.getColumnIndex("DTL_ID"));

                        Cursor d =db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                                "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                suite.getSpecGrp().getGrpId(),
                                dtlId
                        });
                        String jobId="";
                        if(d.getCount()>0){
                            d.moveToNext();
                            jobId=d.getString(d.getColumnIndex("JOB_ID"));
                            db.execSQL("UPDATE EXD_TG_ORDITMJOBPRD SET UPD_TIME=datetime('now'),QTY=?,SPEC_CODE=? " +
                                    "WHERE ORD_NO=? AND PRD_CODE=? " +
                                    "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                                    qty,
                                    suite.getSpec().getSpecCode(),
                                    prodCls.getOrdNo(),
                                    prodCls.getPrdCode(),
                                    prodCls.getSuiteGrpId(),
                                    employee.getPckNo(),
                                    suite.getSpecGrp().getGrpId(),
                                    dtlId});
                            db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD  WHERE ORD_NO=? AND PRD_CODE=? " +
                                    "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID<>? AND DTL_ID=?",new String[]{
                                    prodCls.getOrdNo(),
                                    prodCls.getPrdCode(),
                                    prodCls.getSuiteGrpId(),
                                    employee.getPckNo(),
                                    suite.getSpecGrp().getGrpId(),
                                    dtlId});
                            db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD_DTL WHERE JOB_ID=?",new Object[]{
                                    jobId
                            });
                            d.close();
                        }else{
                            db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                                    "AND SUITE_GRP_ID=? AND PCKNO=? AND DTL_ID=?",new String[]{
                                    prodCls.getOrdNo(),
                                    prodCls.getPrdCode(),
                                    prodCls.getSuiteGrpId(),
                                    employee.getPckNo(),
                                    dtlId
                            });
                            db.execSQL("INSERT INTO EXD_TG_ORDITMJOBPRD(ORD_NO,PRD_CODE,SUITE_GRP_ID,PCKNO,GRP_ID,SPEC_CODE,DTL_ID,QTY,UPD_TIME)" +
                                    "VALUES(?,?,?,?,?,?,?,?,datetime('now'))",new Object[]{
                                    prodCls.getOrdNo(),
                                    prodCls.getPrdCode(),
                                    prodCls.getSuiteGrpId(),
                                    employee.getPckNo(),
                                    suite.getSpecGrp().getGrpId(),
                                    suite.getSpec().getSpecCode(),
                                    dtlId,
                                    qty
                            });
                            d.close();
                            d =db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                                    "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                                    prodCls.getOrdNo(),
                                    prodCls.getPrdCode(),
                                    prodCls.getSuiteGrpId(),
                                    employee.getPckNo(),
                                    suite.getSpecGrp().getGrpId(),
                                    dtlId
                            });
                            d.moveToNext();
                            jobId=d.getString(d.getColumnIndex("JOB_ID"));
                            d.close();
                        }

                        String teamDescriptions="";
                        for(int i=0;i<suite.getTeamGroup().size();i++){
                            ArrayList<Team> detailTeams=suite.getTeamGroup().get(i).getTeams();
                            for(int j=0;j<detailTeams.size();j++){
                                if(detailTeams.get(j).getCurrValue()!=null && detailTeams.get(j).getCurrValue().length()>0){
                                    db.execSQL("INSERT INTO EXD_TG_ORDITMJOBPRD_DTL(JOB_ID,TERM_ID,CURR_VAL,DESCRIPTION,UPD_TIME)" +
                                            "VALUES(?,?,?,?,datetime('now'))",new Object[]{
                                            jobId,
                                            detailTeams.get(j).getTeamId(),
                                            detailTeams.get(j).getCurrValue(),
                                            detailTeams.get(j).getDescription()
                                    });
                                    if(teamDescriptions.length()<=0){
                                        teamDescriptions=detailTeams.get(j).getDescription();
                                    }else{
                                        teamDescriptions=teamDescriptions+";"+detailTeams.get(j).getDescription();
                                    }
                                }
                            }
                        }
                        db.execSQL("UPDATE EXD_TG_ORDITMJOBPRD SET TERM_MEMO=? WHERE JOB_ID=?",new Object[]{
                                teamDescriptions,
                                jobId
                        });
                    }
                }
                c.close();
                 db.close();
            return "";
        }catch (Exception e){
            e.printStackTrace();
            errMsg=e.getMessage();
            return errMsg;
        }
    }
    public static String saveData(Context context, String custCode,Employee employee, ArrayList<ProdCls> prodClsList, ArrayList<Suite> suiteList){
        String errMsg="";
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            for(int i=0;i<prodClsList.size();i++){
                ProdCls prodCls=prodClsList.get(i);
                for(int j=0;j<suiteList.size();j++){
                    Suite suite=suiteList.get(j);
                    errMsg=saveData(context,employee,prodCls,suite);
                    if(errMsg.length()>0){
                        throw new Exception(errMsg);
                    }
                }
            }
            db.execSQL("UPDATE EXD_TG_CUSTJOB SET STATUS=? WHERE CUST_CODE=? AND PCKNO=?",new Object[]{
                    "T",
                    custCode,
                    employee.getPckNo()
            });
            db.close();
            return "";
        }catch (Exception e){
            e.printStackTrace();
            errMsg=e.getMessage();
            return errMsg;
        }
    }
}
