package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.PinyinUtils;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.util.DBhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class EmployeeService {
    public static ArrayList<Employee> getEmployeeData(String custCode,String ltStatus,String dept,String name,Context context){
        ArrayList<Employee> listItem = new ArrayList<Employee>();
        try{
            name=name.replace("\n","");
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            String sql="SELECT A.*,B.CNT FROM EXD_TG_CUSTJOB A " +
                    "LEFT JOIN (SELECT A.PCKNO,COUNT(A.PRD_CODE)CNT " +
                    "FROM (SELECT A.PCKNO,A.PRD_CODE " +
                    "FROM EXD_TG_ORDITMJOBPRD A " +
                    "INNER JOIN EXD_TG_PROD_CLS B ON A.ORD_NO=B.ORD_NO AND A.PRD_CODE=B.PRD_CODE AND A.SUITE_GRP_ID=B.SUITE_GRP_ID " +
                    "WHERE B.CUST_CODE='"+custCode+"' " +
                    "GROUP BY A.PCKNO,A.PRD_CODE) A " +
                    "GROUP BY A.PCKNO)B ON A.PCKNO=B.PCKNO " +
                    "WHERE A.CUST_CODE='"+custCode+"'";
            Cursor c =db.rawQuery(sql,null);
            boolean local=true;
            if(c.getCount()<=0){
                local=false;
            }
            c.close();
            if(ltStatus!=null){
                if(ltStatus=="T"){
                    sql=sql+" AND STATUS='T'";
                }
                if(ltStatus=="F"){
                    sql=sql+" AND STATUS<>'T'";
                }
            }
            if(dept!=null){
                if(dept.length()>0){
                    sql=sql+" AND DEPT='"+dept+"'";
                }
            }
            c =db.rawQuery(sql,null);
            if(c.getCount()>0){
                while (c.moveToNext()) {
                    if(name.length()>0){
                        String xm=c.getString(c.getColumnIndex("CNAME"));
                        if(xm.indexOf(name)<0){
                            String py= PinyinUtils.getAlpha(c.getString(c.getColumnIndex("CNAME")));
                            if(py.indexOf(name.toUpperCase())<0){
                                continue;
                            }
                        }

                    }
                    Employee employee = new Employee();
                    employee.setPckNo(String.valueOf(c.getInt(c.getColumnIndex("PCKNO"))));
                    employee.setDept(c.getString(c.getColumnIndex("DEPT")));
                    employee.setCname(c.getString(c.getColumnIndex("CNAME")));
                    employee.setGender(c.getString(c.getColumnIndex("GENDER")));
                    employee.setSelected(false);
                    if(c.getString(c.getColumnIndex("STATUS")).equals("T")){
                        employee.setLted(true);
                    }else{
                        employee.setLted(false);
                    }
                    if(c.getInt(c.getColumnIndex("CNT"))>0){
                        employee.setPrdCnt(c.getInt(c.getColumnIndex("CNT")));
                    }else{
                        employee.setPrdCnt(0);
                    }

                    listItem.add(employee);
                }

                c.close();
                db.close();
                return listItem;
            }
            c.close();
            if(!local){
                HashMap<String, String> params=new HashMap<String, String>();

                params.put("custCode",custCode);
                params.put("ltStatus",ltStatus);
                String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getEmployee.do",
                        params, Constants.CHARSET_GBK);
                JSONObject res=new JSONObject(msg);
                if(!res.getBoolean("success")){
                    throw new Exception(res.getString("msg"));
                }
                JSONArray custJson=res.getJSONArray("data");
                for(int i=0;i<custJson.length();i++){
                    JSONObject jsonobject = custJson.getJSONObject(i);
                    Employee employee = new Employee();
                    employee.setPckNo(jsonobject.getString("PCKNO"));
                    employee.setDept(jsonobject.getString("DEPT"));
                    employee.setCname(jsonobject.getString("CNAME"));
                    employee.setGender(jsonobject.getString("GENDER"));
                    employee.setSelected(false);
                    if(jsonobject.getString("STATUS").equals("T")){
                        employee.setLted(true);
                    }else{
                        employee.setLted(false);
                    }

                    listItem.add(employee);
                    c =db.rawQuery("SELECT * FROM EXD_TG_CUSTJOB " +
                                    "WHERE CUST_CODE=? AND PCKNO=?",
                            new String[]{custCode,jsonobject.getString("PCKNO")});
                    if(c.getCount()==0){
                        db.execSQL("DELETE FROM EXD_TG_CUSTJOB WHERE CUST_CODE=? AND CNAME=?",
                                new String[]{custCode,jsonobject.getString("CNAME")});
                        db.execSQL("INSERT INTO EXD_TG_CUSTJOB(CUST_CODE,PCKNO,DEPT,CNAME,GENDER,STATUS)" +
                                "VALUES('"+custCode+"',"+jsonobject.getString("PCKNO")+","
                                +"'"+jsonobject.getString("DEPT")+"',"
                                +"'"+jsonobject.getString("CNAME")+"',"
                                +"'"+jsonobject.getString("GENDER")+"',"
                                +"'"+jsonobject.getString("STATUS")+"')");
                    }



                }
            }

            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listItem;
    }
    public static  ArrayList<String> getDeptList(ArrayList<Employee> employees){
        ArrayList<String> lst=new  ArrayList<String>();
        for(int i=0;i<employees.size();i++){
            Employee employee=employees.get(i);
            if(lst.indexOf(employee.getDept())<0){
                lst.add(employee.getDept());
            }
        }
        return lst;
    }
}
