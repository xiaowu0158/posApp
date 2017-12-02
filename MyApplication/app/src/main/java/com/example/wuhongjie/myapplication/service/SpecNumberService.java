package com.example.wuhongjie.myapplication.service;

import com.example.wuhongjie.myapplication.type.Pattern;
import com.example.wuhongjie.myapplication.type.SpecNumber;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class SpecNumberService {
    public static ArrayList<SpecNumber> getSpecNumberData() {
        ArrayList<SpecNumber> listItem = new ArrayList<SpecNumber>();
        try {
            SpecNumber specNumber=new SpecNumber();
            specNumber.setValue("0");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("1");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("2");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("3");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("4");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("5");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("6");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("7");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("8");
            specNumber.setSelected(false);
            listItem.add(specNumber);
            specNumber=new SpecNumber();
            specNumber.setValue("9");
            specNumber.setSelected(false);
            listItem.add(specNumber);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return listItem;
    }

}