package com.example.wuhongjie.myapplication.service;

import com.example.wuhongjie.myapplication.type.ShapeNum;
import com.example.wuhongjie.myapplication.type.Yxgs;
import com.example.wuhongjie.myapplication.util.StreamTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class ShapeNumService {
    public static ArrayList<ShapeNum> getShapeNumData() {
        ArrayList<ShapeNum> listItem = new ArrayList<ShapeNum>();
        try {
            ShapeNum shapeNum=new ShapeNum();
            shapeNum.setShape("145");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("150");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("155");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("160");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("165");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("170");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("175");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("180");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("185");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);
            shapeNum=new ShapeNum();
            shapeNum.setShape("190");
            shapeNum.setSelected(false);
            listItem.add(shapeNum);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return listItem;
    }


}