package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.evangelsoft.econnect.util.Encrypter;
import com.example.wuhongjie.myapplication.type.SampleSuiteGrp;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.Suite;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.DBhelper;
import com.example.wuhongjie.myapplication.util.WebUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SampleSuiteService {


    public static ArrayList<SampleSuiteGrp> getSampleSuite(){
        ArrayList<SampleSuiteGrp> listItem = new ArrayList<SampleSuiteGrp>();
        SampleSuiteGrp  suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("CC");
        suiteGrp.setSampleSuiteName("衬衫");
        listItem.add(suiteGrp);
        suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("SY");
        suiteGrp.setSampleSuiteName("西服");
        listItem.add(suiteGrp);
        suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("KZ");
        suiteGrp.setSampleSuiteName("裤子");
        listItem.add(suiteGrp);
        suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("QZ");
        suiteGrp.setSampleSuiteName("裙子");
        listItem.add(suiteGrp);
        suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("MJ");
        suiteGrp.setSampleSuiteName("马夹");
        listItem.add(suiteGrp);
        suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("TX");
        suiteGrp.setSampleSuiteName("T恤");
        listItem.add(suiteGrp);
        suiteGrp=new SampleSuiteGrp();
        suiteGrp.setSampleSuiteId("DY");
        suiteGrp.setSampleSuiteName("大衣");
        listItem.add(suiteGrp);
        return listItem;
    }

}
