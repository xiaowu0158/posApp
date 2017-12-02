package com.example.wuhongjie.myapplication.service;


import com.example.wuhongjie.myapplication.type.Yxgs;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.StreamTool;
import com.example.wuhongjie.myapplication.util.WebUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class YxgsService {
    public static ArrayList<Yxgs> getYxgsData(String yxgsDm) {
        ArrayList<Yxgs> listItem = new ArrayList<Yxgs>();
        try {
           /* String url= WebUtils.getRealUrl("http://URL/YoungorTgOrder/getYxgs.do");
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setConnectTimeout(50000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream json = conn.getInputStream();
                //return parsejson(json);
                byte[] jsonData = StreamTool.read(json);
                String jsonStr = new String(jsonData ,"GBK");
                System.out.println(jsonStr);
                JSONObject feedBack = new JSONObject(jsonStr);
                JSONArray yxgsJson=feedBack.getJSONArray("data");
                for(int i=0;i<yxgsJson.length();i++){
                    JSONObject jsonobject = yxgsJson.getJSONObject(i);
                    Yxgs yxgs = new Yxgs();
                    System.out.println(jsonobject.getString("YXGSDM")+jsonobject.getString("YXGSMC"));
                    yxgs.setYxgsDm(jsonobject.getString("YXGSDM"));
                    yxgs.setYxgsMc(jsonobject.getString("YXGSMC"));
                    yxgs.setSelected(false);
                    if(jsonobject.getString("YXGSDM").equals(yxgsDm)){
                        yxgs.setSelected(true);
                    }

                    listItem.add(yxgs);
                }

            }*/
            HashMap<String, String> params=new HashMap<String, String>();
            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getYxgs.do",
                    params, Constants.CHARSET_GBK);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray yxgsJson=res.getJSONArray("data");
            for(int i=0;i<yxgsJson.length();i++){
                JSONObject jsonobject = yxgsJson.getJSONObject(i);
                Yxgs yxgs = new Yxgs();
                System.out.println(jsonobject.getString("YXGSDM")+jsonobject.getString("YXGSMC"));
                yxgs.setYxgsDm(jsonobject.getString("YXGSDM"));
                yxgs.setYxgsMc(jsonobject.getString("YXGSMC"));
                yxgs.setSelected(false);
                if(jsonobject.getString("YXGSDM").equals(yxgsDm)){
                    yxgs.setSelected(true);
                }

                listItem.add(yxgs);
            }
           /* Properties localProperties = new Properties();
            localProperties.setProperty("host", "172.16.3.19");
            localProperties.setProperty("port", "4001");
            consumer = new SocketConsumer(null, null);
            client = consumer.connect(localProperties);
            AccountBookList accountBookList = (AccountBookList) (new RMIProxy(client))
                    .newStub(AccountBookList.class);


            if (!accountBookList.getYxgs(data, errMsg)) {
                throw new Exception(errMsg.value);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return listItem;
    }

}