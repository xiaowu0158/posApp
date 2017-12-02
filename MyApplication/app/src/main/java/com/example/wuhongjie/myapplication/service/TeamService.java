package com.example.wuhongjie.myapplication.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.wuhongjie.myapplication.util.Constants;
import com.example.wuhongjie.myapplication.util.WebUtils;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamDesc;
import com.example.wuhongjie.myapplication.type.TeamGroup;
import com.example.wuhongjie.myapplication.util.DBhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static java.lang.Math.abs;

/**
 * Created by wuhongjie on 2017-03-01.
 */

public class TeamService {
    public static ArrayList<Team> getTeamData(String suiteCode,Context context){
        ArrayList<Team> listItem = new ArrayList<Team>();
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c =db.rawQuery("SELECT * FROM EXD_TG_TERMINOLOGY WHERE SUITE_CODE='"+suiteCode+"' ORDER BY CAST(SORT AS INT)",null);
            if(c.getCount()>0){
                while (c.moveToNext()) {
                    Team team = new Team();
                    team.setTeamId(String.valueOf(c.getInt(c.getColumnIndex("TERM_ID"))));
                    team.setSuiteCode(c.getString(c.getColumnIndex("SUITE_CODE")));
                    team.setTermName(c.getString(c.getColumnIndex("TERM_NAME")));
                    team.setDefaultValue(c.getString(c.getColumnIndex("DEFAULT_VALUE")));
                    team.setMaxValue(c.getString(c.getColumnIndex("MAX_VALUE")));
                    team.setMinValue(c.getString(c.getColumnIndex("MIN_VALUE")));
                    team.setSort(c.getString(c.getColumnIndex("SORT")));
                    team.setStepValue(c.getString(c.getColumnIndex("STEP_VALUE")));
                    team.setGroupValue(c.getString(c.getColumnIndex("GROUP_VALUE")));
                    listItem.add(team);

                }

                c.close();
                db.close();
                return listItem;
            }
            c.close();
            //db.close();
            HashMap<String, String> params=new HashMap<String, String>();

            params.put("suiteCode",suiteCode);

            String msg= WebUtils.doGet("http://URL/YoungorTgOrder/getTerm.do",
                    params, Constants.CHARSET_GBK);
            JSONObject res=new JSONObject(msg);
            if(!res.getBoolean("success")){
                throw new Exception(res.getString("msg"));
            }
            JSONArray dataJson=res.getJSONArray("data");
            for(int i=0;i<dataJson.length();i++){
                JSONObject jsonobject = dataJson.getJSONObject(i);
                Team team = new Team();
                team.setTeamId(jsonobject.getString("TERM_ID"));
                team.setSuiteCode(jsonobject.getString("SUITE_CODE"));
                team.setTermName(jsonobject.getString("TERM_NAME"));
                team.setDefaultValue(jsonobject.getString("DEFAULT_VALUE"));
                team.setMaxValue(jsonobject.getString("MAX_VALUE"));
                team.setMinValue(jsonobject.getString("MIN_VALUE"));
                team.setSort(jsonobject.getString("SORT"));
                team.setStepValue(jsonobject.getString("STEP_VALUE"));
                team.setGroupValue(jsonobject.getString("GROUP_VALUE"));
                listItem.add(team);
                db.execSQL("INSERT INTO EXD_TG_TERMINOLOGY(TERM_ID,SUITE_CODE,TERM_NAME,DEFAULT_VALUE," +
                        "MAX_VALUE,MIN_VALUE,STEP_VALUE,SORT,GROUP_VALUE)" +
                        "VALUES(" +
                        "'"+jsonobject.getString("TERM_ID")+"',"+
                        "'"+jsonobject.getString("SUITE_CODE")+"',"+
                        "'"+jsonobject.getString("TERM_NAME")+"',"+
                        "'"+jsonobject.getString("DEFAULT_VALUE")+"',"+
                        "'"+jsonobject.getString("MAX_VALUE")+"',"+
                        "'"+jsonobject.getString("MIN_VALUE")+"',"+
                        "'"+jsonobject.getString("STEP_VALUE")+"',"+
                        "'"+jsonobject.getString("SORT")+"',"+
                        "'"+jsonobject.getString("GROUP_VALUE")+"')");


            }
            db.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return listItem;
    }
    public static ArrayList<TeamDesc> getTeamValueData(Team team,int cnt){
        ArrayList<TeamDesc> listItem = new ArrayList<TeamDesc>();
        ArrayList<TeamDesc> listItemSort = new ArrayList<TeamDesc>();
        ArrayList<BigDecimal> negVals=new ArrayList<BigDecimal>();
        ArrayList<BigDecimal> vals=new ArrayList<BigDecimal>();
        /*TeamDesc teamDesc=new TeamDesc();
        teamDesc.setTeamId(team.getTeamId());
        teamDesc.setTeamValue(team.getDefaultValue());
        teamDesc.setDescription(team.getTermName()+"默认");

        listItem.add(teamDesc);*/
        int defaultTvalue=new BigDecimal(team.getDefaultValue()).multiply(BigDecimal.TEN).intValue();
        int minTvalue=new BigDecimal(team.getMinValue()).multiply(BigDecimal.TEN).intValue();
        int maxTvalue=new BigDecimal(team.getMaxValue()).multiply(BigDecimal.TEN).intValue();
        int stepTvalue=new BigDecimal(team.getStepValue()).multiply(BigDecimal.TEN).intValue();
        if(stepTvalue==0){
            stepTvalue=10;
        }
        for(int i=minTvalue;i<=maxTvalue;i=i+stepTvalue){
                float size = (float)i/10;
                if(size==0){
                    continue;
                }
                TeamDesc teamDesc=new TeamDesc();
                teamDesc.setTeamId(team.getTeamId());

                DecimalFormat df = new DecimalFormat("0.0");
                String strSize=df.format(size);
                String strSizeabs=df.format(abs(size));
                if(strSize.substring(strSize.length()-1).equals("0")){
                    DecimalFormat df1 = new DecimalFormat("0");
                    strSize=df1.format(size);
                    strSizeabs=df1.format(abs(size));
                }


                String desc="";
                if(size<0){
                    desc="-"+strSizeabs;
                }else{
                    desc="+"+strSizeabs;
                }
                teamDesc.setTeamValue(desc);
                teamDesc.setDescription(team.getTermName()+desc);
                if(team.getCurrValue()!=null){
                    if(team.getCurrValue().equals(desc)){
                        teamDesc.setSelected(true);
                    }else{
                        teamDesc.setSelected(false);
                    }
                }
                if(size<0){
                    negVals.add(new BigDecimal(size).negate());
                }else{
                    vals.add(new BigDecimal(size));
                }
                listItem.add(teamDesc);

        }
        Collections.sort(negVals);
        Collections.sort(vals);
        for(int i=0;i<vals.size();i++){
            BigDecimal size=vals.get(i);
            DecimalFormat df = new DecimalFormat("0.0");
            String strSize=df.format(size);
            if(strSize.substring(strSize.length()-1).equals("0")){
                DecimalFormat df1 = new DecimalFormat("0");
                strSize=df1.format(size);
            }
            String desc="+"+strSize;
            for(int j=0;j<listItem.size();j++){
                if(listItem.get(j).getTeamValue().equals(desc)){
                    listItemSort.add(listItem.get(j));
                }
            }

        }
        if((vals.size()%cnt)>0){
            for(int i=0;i<(cnt-vals.size()%cnt);i++){
                listItemSort.add(new TeamDesc());
            }
        }

        for(int i=0;i<negVals.size();i++){
            BigDecimal size=negVals.get(i);
            DecimalFormat df = new DecimalFormat("0.0");
            String strSize=df.format(size);
            if(strSize.substring(strSize.length()-1).equals("0")){
                DecimalFormat df1 = new DecimalFormat("0");
                strSize=df1.format(size);
            }
            String desc="-"+strSize;
            for(int j=0;j<listItem.size();j++){
                if(listItem.get(j).getTeamValue().equals(desc)){
                    listItemSort.add(listItem.get(j));
                }
            }

        }

        return listItemSort;
    }
    public static ArrayList<TeamGroup> getTeamGroupData(String suiteCode, Context context){
        ArrayList<TeamGroup> teamGroups=new  ArrayList<TeamGroup>();
        try{
            HashMap<String,Integer> teamHash=new HashMap<String,Integer>();
            ArrayList<Team> listItem = new ArrayList<Team>();
            listItem=getTeamData(suiteCode,context);
            for(int i=0;i<listItem.size();i++){
                if(!teamHash.containsKey(listItem.get(i).getGroupValue())){
                    TeamGroup teamGroup=new TeamGroup();
                    teamGroup.setGroupId(listItem.get(i).getGroupValue());
                    teamGroup.setSelected(false);
                    ArrayList<Team> teamList=new ArrayList<Team>();
                    // teamList.addAll(teamGroup.getTeams());
                    teamList.add(listItem.get(i));
                    teamGroup.setTeams(teamList);
                    teamGroups.add(teamGroup);
                    teamHash.put(listItem.get(i).getGroupValue(),teamGroups.size()-1);
                }else{
                    TeamGroup teamGroup= teamGroups.get(teamHash.get(listItem.get(i).getGroupValue())) ;
                    ArrayList<Team> teamList=new ArrayList<Team>();
                    teamList.addAll(teamGroup.getTeams());
                    teamList.add(listItem.get(i));
                    teamGroup.setTeams(teamList);
                    teamGroups.set(teamHash.get(listItem.get(i).getGroupValue()),teamGroup);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return teamGroups;
    }
    public static ArrayList<TeamGroup> getTeamGroupData(String suiteCode, String pckNo,String custCode,Context context){
        ArrayList<TeamGroup> teamGroups=new  ArrayList<TeamGroup>();
        try{
            DBhelper dBhelper= new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();
            Cursor c=db.rawQuery("select DISTINCT A.* from EXD_TG_ORDITMJOBPRD_DTL A " +
                    "INNER JOIN EXD_TG_ORDITMJOBPRD B ON A.JOB_ID=B.JOB_ID " +
                    "INNER JOIN EXD_TG_SUITE_GRP_DTL C ON B.DTL_ID=C.DTL_ID " +
                    "INNER JOIN EXD_TG_PROD_CLS D ON B.ORD_NO=D.ORD_NO AND B.PRD_CODE=D.PRD_CODE " +
                    "WHERE B.PCKNO=? AND C.SUITE_CODE=? AND D.CUST_CODE=?",new String[]{
                    pckNo,suiteCode,custCode
            });
            HashMap<String,String> termValus=new HashMap<String,String>();
            HashMap<String,String> termDescs=new HashMap<String,String>();
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    termValus.put(c.getString(c.getColumnIndex("TERM_ID")), c.getString(c.getColumnIndex("CURR_VAL")));
                    termDescs.put(c.getString(c.getColumnIndex("TERM_ID")), c.getString(c.getColumnIndex("DESCRIPTION")));
                }
            }
            HashMap<String,Integer> teamHash=new HashMap<String,Integer>();
            ArrayList<Team> listItem = new ArrayList<Team>();
            listItem=getTeamData(suiteCode,context);
            for(int i=0;i<listItem.size();i++){
                if(termValus.containsKey(listItem.get(i).getTeamId())){
                    listItem.get(i).setCurrValue(termValus.get(listItem.get(i).getTeamId()));
                    listItem.get(i).setDescription(termDescs.get(listItem.get(i).getTeamId()));
                }

                if(!teamHash.containsKey(listItem.get(i).getGroupValue())){
                    TeamGroup teamGroup=new TeamGroup();
                    teamGroup.setGroupId(listItem.get(i).getGroupValue());
                    teamGroup.setSelected(false);
                    ArrayList<Team> teamList=new ArrayList<Team>();
                    // teamList.addAll(teamGroup.getTeams());
                    teamList.add(listItem.get(i));
                    teamGroup.setTeams(teamList);
                    teamGroups.add(teamGroup);
                    teamHash.put(listItem.get(i).getGroupValue(),teamGroups.size()-1);
                }else{
                    TeamGroup teamGroup= teamGroups.get(teamHash.get(listItem.get(i).getGroupValue())) ;
                    ArrayList<Team> teamList=new ArrayList<Team>();
                    teamList.addAll(teamGroup.getTeams());
                    teamList.add(listItem.get(i));
                    teamGroup.setTeams(teamList);
                    teamGroups.set(teamHash.get(listItem.get(i).getGroupValue()),teamGroup);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return teamGroups;
    }
}
