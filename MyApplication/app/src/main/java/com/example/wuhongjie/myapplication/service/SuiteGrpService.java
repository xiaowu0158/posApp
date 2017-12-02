package com.example.wuhongjie.myapplication.service;

import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.type.SuiteGrp;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SuiteGrpService {
    public static ArrayList<SuiteGrp> getSuiteGrp(){
        ArrayList<SuiteGrp> listItem = new ArrayList<SuiteGrp>();
        SuiteGrp suiteGrp = new SuiteGrp();
        suiteGrp.setCatId("02");
        suiteGrp.setSeqNum("1");
        suiteGrp.setSuiteGrpId("1");
        suiteGrp.setSuiteGrpName("一衣一裤");
        suiteGrp.setSelected(false);
        listItem.add(suiteGrp);
        suiteGrp = new SuiteGrp();
        suiteGrp.setCatId("02");
        suiteGrp.setSeqNum("2");
        suiteGrp.setSuiteGrpId("2");
        suiteGrp.setSuiteGrpName("一衣二裤");
        suiteGrp.setSelected(false);
        listItem.add(suiteGrp);
        suiteGrp = new SuiteGrp();
        suiteGrp.setCatId("02");
        suiteGrp.setSeqNum("3");
        suiteGrp.setSuiteGrpId("3");
        suiteGrp.setSuiteGrpName("一衣一裤一马夹");
        suiteGrp.setSelected(false);
        listItem.add(suiteGrp);
        suiteGrp = new SuiteGrp();
        suiteGrp.setCatId("02");
        suiteGrp.setSeqNum("4");
        suiteGrp.setSuiteGrpId("4");
        suiteGrp.setSuiteGrpName("一衣一裤一马夹一裙子");
        suiteGrp.setSelected(false);
        listItem.add(suiteGrp);
        return listItem;
    }
}
