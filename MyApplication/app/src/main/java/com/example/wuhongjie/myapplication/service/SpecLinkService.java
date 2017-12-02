package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wuhongjie.myapplication.type.ShapeNum;
import com.example.wuhongjie.myapplication.type.SpecLink;
import com.example.wuhongjie.myapplication.util.DBhelper;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SpecLinkService {
    public static ArrayList<String> getSpecShapeList(ArrayList<SpecLink> specLinks,String suiteCode,Context context) {
        ArrayList<String> listItem = new ArrayList<String>();
        try {
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();

            for(int i=0;i<specLinks.size();i++){

                Cursor c=db.rawQuery("SELECT * FROM EXD_TG_SPECLINK_DTL WHERE SUITE_CODE=? AND GENDER=? AND SHAPE=? AND DTL_SUITE_CODE=?",new String[]{
                        specLinks.get(i).getSuiteCode(),
                        specLinks.get(i).getGender(),
                        specLinks.get(i).getShape(),
                        suiteCode
                });
                if(c.getCount()>0){
                    while (c.moveToNext()) {
                        //SpecLink specLink=new SpecLink();
                        //specLink.setSuiteCode(suiteCode);
                        //specLink.setGender(specLinks.get(i).getGender());
                        //specLink.setShape(c.getString(c.getColumnIndex("DTL_SHAPE")));
                        if(listItem.indexOf(c.getString(c.getColumnIndex("DTL_SHAPE")))<0){
                            listItem.add(c.getString(c.getColumnIndex("DTL_SHAPE")));
                        }

                    }
                }
                c.close();
                c=db.rawQuery("SELECT * FROM EXD_TG_SPECLINK_DTL WHERE DTL_SUITE_CODE=? AND GENDER=? AND DTL_SHAPE=? AND SUITE_CODE=?",new String[]{
                        specLinks.get(i).getSuiteCode(),
                        specLinks.get(i).getGender(),
                        specLinks.get(i).getShape(),
                        suiteCode
                });
                if(c.getCount()>0){
                    while (c.moveToNext()) {
                        //SpecLink specLink=new SpecLink();
                        //specLink.setSuiteCode(suiteCode);
                        //specLink.setGender(specLinks.get(i).getGender());
                        //specLink.setShape(c.getString(c.getColumnIndex("SHAPE")));
                        //listItem.add(specLink);
                        if(listItem.indexOf(c.getString(c.getColumnIndex("SHAPE")))<0){
                            listItem.add(c.getString(c.getColumnIndex("SHAPE")));
                        }

                    }
                }
                c.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return listItem;
    }


}