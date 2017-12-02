package com.example.wuhongjie.myapplication.util;

import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordSet;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
public class RetailPromotionPolicyUtil{
  public static int MAX_VALUE_LENGTH = 20;
  public static RecordSet policyRs;
  public RetailPromotionPolicyUtil(RecordSet policyRs) {
   this.policyRs= policyRs;
  }
  
  public static String describePolicy(String paramString, Record paramReadRow, int paramInt) throws UnsupportedEncodingException {
    String str16 = "";
    String str1 = new String(paramReadRow.getField("PROD_CODE").getBytes(),"GBK");
    //String str2 = new String(paramReadRow.getField("PROD_CODE_DESC").getBytes(),"GBK");

    String str2 = paramReadRow.getField("PROD_CODE_DESC").getString();
    int i = paramReadRow.getField("PROD_CLS_QTY").getNumber().intValue();
    String str3 = new String(paramReadRow.getField("EX_PROD_CODE").getBytes(),"GBK");
    //String str4 = paramReadRow.getField("EX_PROD_CODE_DESC").getString()==null?"":new String(paramReadRow.getField("EX_PROD_CODE_DESC").getBytes(),"GBK");
    String str4 = paramReadRow.getField("EX_PROD_CODE_DESC").getString();
    double d1 = paramReadRow.getField("DR_LM").getNumber().doubleValue();
    double d2 = paramReadRow.getField("PU_QTY").getNumber().doubleValue();
    double d3 = paramReadRow.getField("PU_VAL").getNumber().doubleValue();
    double d4 = paramReadRow.getField("ADT_VAL").getNumber().doubleValue();
    double d5 = paramReadRow.getField("DISC_RATE").getNumber().doubleValue();
    double d6 = paramReadRow.getField("PS_QTY").getNumber().doubleValue();
    //String str5 = new String(paramReadRow.getField("PS_WAY").getBytes(),"GBK");
    String str5 = paramReadRow.getField("PS_WAY").getString();
    double d7 = paramReadRow.getField("PS_VAL").getNumber().doubleValue();
    //String str6 = new String(paramReadRow.getField("PS_CODE").getBytes(),"GBK");
    String str6 = paramReadRow.getField("PS_CODE").getString();
   // String str7 = new String(paramReadRow.getField("PS_CODE_DESC").getBytes(),"GBK");
    String str7 = paramReadRow.getField("PS_CODE_DESC").getString();
    double d8 = paramReadRow.getField("PS_PRC").getNumber().doubleValue();
   // String str8 = new String(paramReadRow.getField("PS_PRC_CTRL").getBytes(),"GBK");
    String str8 = paramReadRow.getField("PS_PRC_CTRL").getString();
    double d9 = paramReadRow.getField("BD_PRC").getNumber().doubleValue();
    String str9;
    if (str1.length() == 0) {
      str9 = MessageFormat.format("商品”{0}”", new Object[] { "全部" });
    } else {
      str1=(paramInt > 3) && (str1.length() > paramInt) ? str1.substring(0, paramInt - 3) + "...":str1;
      str9 = MessageFormat.format("商品”{0}”", new Object[] { (str2 == null) || (str2.length() == 0) ? str1 : str2 });
    }
    String str10;
    if (str3.length() == 0) {
      str10 = "";
    } else {
      str10 = MessageFormat.format("除“{0}”外", new Object[] { (str4 == null) || (str4.length() == 0) ? str3 : (paramInt > 3) && (str3.length() > paramInt) ? str3.substring(0, paramInt - 3) + "..." : str4 });
    }
    String str11;
    if (str6.length() == 0) {
      str11 = "";
    } else {
      str11 = (str7 == null) || (str7.length() == 0) ? str6 : (paramInt > 3) && (str6.length() > paramInt) ? str6.substring(0, paramInt - 3) + "..." : str7;
    }
    String str12;
    if (d1 == 0.0D) {
      str12 = "";
    } else {
      str12 = MessageFormat.format("折率不小于{0}时", new Object[] { Double.valueOf(d1) });
    }
    String str13;
    if (i == 0) {
      str13 = "不分款";
    } else if (i == 1) {
      str13 = "单款";
    } else {
      str13 = MessageFormat.format("组合{0}款", new Object[] { Integer.valueOf(i) });
    }
    String str14;
    if (d2 > 0.0D) {
      str14 = MessageFormat.format("{0}件", new Object[] { Double.valueOf(d2) });
    } else if (d3 > 0.0D) {
      str14 = MessageFormat.format("{0}元", new Object[] { Double.valueOf(d3) });
    } else {
      str14 ="“无限制”";
    }
    String str15;
    if (str8.equals("EQ")) {
      str15 = "等于";
    } else if (str8.equals("NG")) {
      str15 = "不大于";
    } else if (str8.equals("LS")) {
      str15 = "小于";
    } else {
      str15 = "";
    }
    if (paramString.equals("SR1")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}满{4}折率{5}。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d5) });
    } else if (paramString.equals("SR2")) {
      str16 = MessageFormat.format(!str5.equals("A") ? "{0}{1}{2}{3}满{4}减收{5}元。" : "{0}{1}{2}{3}满{4}加赠{5}元。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d7) });
    } else if (paramString.equals("SR3")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}满{4}加{5}元送赠品“{6}”{7}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), str11, Double.valueOf(d6) });
    } else if (paramString.equals("SR4")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}满{4}加{5}元送价格{6}{7}元赠品{8}{9}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), str15, Double.valueOf(d8), str11, Double.valueOf(d6) });
    } else if (paramString.equals("SR5")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}满{4}打{5}折送赠品“{6}”{7}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d5), str11, Double.valueOf(d6) });
    } else if (paramString.equals("SP1")) {
      str16 = MessageFormat.format(!str5.equals("A") ? "{0}{1}{2}{3}每{4}减收{5}元。" : "{0}{1}{2}{3}每{4}加赠{5}元。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d7) });
    } else if (paramString.equals("SP2")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}加{5}元送{6}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), Double.valueOf(d6) });
    } else if (paramString.equals("SP3")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}加{5}元送赠品“{6}”{7}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), str11, Double.valueOf(d6) });
    } else if (paramString.equals("SP4")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}加{5}元送价格{6}{7}元赠品{8}{9}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), str15, Double.valueOf(d8), str11, Double.valueOf(d6) });
    } else if (paramString.equals("SP5")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}共售{5}元。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d9) });
    } else if (paramString.equals("SP6")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}加{5}元送价格{6}原价格赠品{7}{8}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), str15, str11, Double.valueOf(d6) });
    } else if (paramString.equals("SP7")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}加{5}元送价格{6}原金额赠品{7}{8}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d4), str15, str11, Double.valueOf(d6) });
    } else if (paramString.equals("SP8")) {
      str16 = MessageFormat.format("{0}{1}{2}{3}每{4}打{5}折送赠品“{6}”{7}件。", new Object[] { str9, str10, str12, str13, str14, Double.valueOf(d5), str11, Double.valueOf(d6) });
    } else if (paramString.equals("WR1")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}满{3}折率{4}。", new Object[] { str9, str10, str12, str14, Double.valueOf(d5) });
    } else if (paramString.equals("WR2")) {
      str16 = MessageFormat.format(!str5.equals("A") ? "整单对{0}{1}{2}满{3}减收{4}元。" : "整单对{0}{1}{2}满{3}加赠{4}元。", new Object[] { str9, str10, str12, str14, Double.valueOf(d7) });
    } else if (paramString.equals("WR3")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}满{3}加{4}元送赠品“{5}”{6}件。", new Object[] { str9, str10, str12, str14, Double.valueOf(d4), str11, Double.valueOf(d6) });
    } else if (paramString.equals("WR4")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}满{3}加{4}元送价格{5}{6}元赠品{7}{8}件。", new Object[] { str9, str10, str12, str14, Double.valueOf(d4), str15, Double.valueOf(d8), str11, Double.valueOf(d6) });
    } else if (paramString.equals("WR5")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}满{3}打{4}折送赠品“{5}”{6}件。", new Object[] { str9, str10, str12, str14, Double.valueOf(d5), str11, Double.valueOf(d6) });
    } else if (paramString.equals("WP1")) {
      str16 = MessageFormat.format(!str5.equals("A") ? "整单对{0}{1}{2}每{3}减收{4}元。" : "整单对{0}{1}{2}每{3}加赠{4}元。", new Object[] { str9, str10, str12, str14, Double.valueOf(d7) });
    } else if (paramString.equals("WP3")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}每{3}加{4}元送商品“{5}”{6}件。", new Object[] { str9, str10, str12, str14, Double.valueOf(d4), str11, Double.valueOf(d6) });
    } else if (paramString.equals("WP4")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}每{3}加{4}元送价格{5}{6}元赠品{7}{8}件。", new Object[] { str9, str10, str12, str14, Double.valueOf(d4), str15, Double.valueOf(d8), str11, Double.valueOf(d6) });
    } else if (paramString.equals("WP5")) {
      str16 = MessageFormat.format("整单对{0}{1}{2}每{3}打{4}折送赠品“{5}”{6}件。", new Object[] { str9, str10, str12, str14, Double.valueOf(d5), str11, Double.valueOf(d6) });
    }
    return str16;
  }
  
  public static String describePolicy(String paramString1, String tppParams, int paramInt){
    /*StorageDataSet localStorageDataSet = new StorageDataSet();
    localStorageDataSet.setColumns(new Column[] { new Column("TPP_DTL.PROD_CODE"),
            new Column("TPP_DTL.PROD_CODE_DESC"),
            new Column("TPP_DTL.PROD_CLS_QTY"),
            new Column("TPP_DTL.EX_PROD_CODE"),
            new Column("TPP_DTL.EX_PROD_CODE_DESC"),
            new Column("TPP_DTL.DR_LM"),
            new Column("TPL.SPEC_OFR"),
            new Column("TPP_DTL.PU_QTY"),
            new Column("TPP_DTL.PU_VAL"),
            new Column("TPP_DTL.ADT_VAL"),
            new Column("TPP_DTL.DISC_RATE"),
            new Column("TPP_DTL.PS_QTY"),
            new Column("TPP_DTL.PS_VAL"),
            new Column("TPP_DTL.PS_WAY"),
            new Column("TPP_DTL.PS_CODE"),
            new Column("TPP_DTL.PS_CODE_DESC"),
            new Column("TPP_DTL.BD_PRC"),
            new Column("TPP_DTL.PS_PRC"),
            new Column("TPP_DTL.PS_PRC_CTRL") });
    localStorageDataSet.open();
    localStorageDataSet.insertRow(false);*/

    policyRs.clear();
    Record rc=policyRs.append();
    String[] tppParamLst = tppParams.split("\\|");
    for (int i = 0; i < tppParamLst.length; i++){
      String[] arrayOfString2 = tppParamLst[i].split("=");
      if ((arrayOfString2.length > 0) && (arrayOfString2[0].length() > 0) && (arrayOfString2[1].length() > 0)){
        String str1 = arrayOfString2[0];
        String str2 = arrayOfString2[1];
        //System.out.println(policyRs.getFormat().getField(str1).getTypeDescription());
        if (policyRs.getFormat().getField(str1).getTypeDescription().equals("VC")
                ||policyRs.getFormat().getField(str1).getTypeDescription().equals("CH")) {
          rc.getField(str1).setString(str2);
        } else{
          rc.getField(str1).setNumber(new BigDecimal(str2));

        }
      }
    }
    rc.post();
    String rst="";
    try {
      rst=describePolicy(paramString1, policyRs.getRecord(0), paramInt);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return rst;
  }
  
  public static String describeStatus(String paramString, Record paramReadRow, int paramInt){
    String str="";
    try {
      str = describePolicy(paramString, paramReadRow, paramInt);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if ((paramString.equals("SR3")) || (paramString.equals("SR4")) || (paramString.equals("SR5")) || (paramString.equals("SP2")) || (paramString.equals("SP3")) || (paramString.equals("SP4")) || (paramString.equals("SP6")) || (paramString.equals("SP7")) || (paramString.equals("SP8")) || (paramString.equals("WR3")) || (paramString.equals("WR4")) || (paramString.equals("WR5")) || (paramString.equals("WP3")) || (paramString.equals("WP4")) || (paramString.equals("WP5"))) {
      str = str + MessageFormat.format("共可赠送{0}件，已赠送{1}件。", new Object[] { Double.valueOf(paramReadRow.getField("MAX_PS_QTY").getNumber().doubleValue()),
              Double.valueOf(paramReadRow.getField("CUR_PS_QTY").getNumber().doubleValue()) });
    } else if ((paramString.equals("SR2")) || (paramString.equals("SP1")) || (paramString.equals("WR2")) || (paramString.equals("WP1"))) {
      str = str + MessageFormat.format("已赠送{0}元。", new Object[] { Double.valueOf(paramReadRow.getField("CUR_PS_VAL").getNumber().doubleValue()) });
    }
    return str;
  }
}