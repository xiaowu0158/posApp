package com.example.wuhongjie.myapplication.service;

import com.example.wuhongjie.myapplication.type.Pattern;
import com.example.wuhongjie.myapplication.type.ShapeNum;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class PatternService {
    public static ArrayList<Pattern> getPatternData(boolean hasLink) {
        ArrayList<Pattern> listItem = new ArrayList<Pattern>();
        try {
            Pattern pattern=new Pattern();
            pattern.setPattn("A");
            pattern.setSelected(false);
            listItem.add(pattern);
            pattern=new Pattern();
            pattern.setPattn("B");
            pattern.setSelected(false);
            listItem.add(pattern);
            pattern=new Pattern();
            pattern.setPattn("C");
            pattern.setSelected(false);
            listItem.add(pattern);
            pattern=new Pattern();
            pattern.setPattn("D");
            pattern.setSelected(false);
            listItem.add(pattern);
            if(hasLink){
                pattern=new Pattern();
                pattern.setPattn("联想");
                pattern.setSelected(true);
                listItem.add(pattern);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return listItem;
    }

}