package com.example.wuhongjie.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.type.Team;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SymboPopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private static int LOAD_CURR_TEAMVALUE_INPUT_ACTION=19;
    private  Context context=null;
    private  Button postButton=null;
    private  ImageView team_popupdel=null;
    private  TextView titleView=null;
    private Handler handler;
    private  PopupWindow popupWindow=null;
    private static String num = "";// 显示的结果
    private  Button clearButton=null;
    private TextView print;
    private int[] btidNum = { R.id.txt0, R.id.txt1, R.id.txt2, R.id.txt3,
            R.id.txt4, R.id.txt5, R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9,
            R.id.txtspl };
    private Button[] buttons = new Button[btidNum.length];
    private int[] btidGrNum = { R.id.add1, R.id.sub1, R.id.add2,
            R.id.sub2,R.id.add3, R.id.sub3,R.id.add05, R.id.sub05 };
    private Button[] buttonGrNums = new Button[btidGrNum.length];
    private Button addButton;
    private Button subButton;
    private String teamGroupId;
    private int teamId;
    private ArrayList<Team> teams=new ArrayList<Team>();
    public SymboPopupWindow(){

    }
    public SymboPopupWindow(Context context, Handler handler, String teamGroupId, ArrayList<Team> teams,int teamId,String title) {
        super();
        this.context=context;
        this.handler=handler;
        this.teamGroupId=teamGroupId;
        this.teams.addAll(teams);
        this.teamId=teamId;
        View view= LayoutInflater.from(context).inflate(R.layout.activity_symbo_popup_window, null);
       // view.setFocusable(true); // 这个很重要
       // view.setFocusableInTouchMode(true);
        titleView= (TextView) view.findViewById(R.id.title);
        titleView.setText(title);
        print= (TextView) view.findViewById(R.id.print);
        GetNumber get = new GetNumber();
        for (int i = 0; i < btidNum.length; i++) {
            buttons[i] = (Button) view.findViewById(btidNum[i]);
            buttons[i].setOnClickListener(get);
        }
        GetGrNumber getGr = new GetGrNumber();
        for (int i = 0; i < buttonGrNums.length; i++) {
            buttonGrNums[i] = (Button) view.findViewById(btidGrNum[i]);
            buttonGrNums[i].setOnClickListener(getGr);
        }
        team_popupdel=(ImageView) view.findViewById(R.id.team_popupdel);
        team_popupdel.setOnClickListener(new PopupCloseClick());
        AddOrSubClick addOrSubClick = new AddOrSubClick();
        addButton=(Button) view.findViewById(R.id.add);
        addButton.setOnClickListener(addOrSubClick);
        subButton=(Button) view.findViewById(R.id.sub);
        subButton.setOnClickListener(addOrSubClick);
        clearButton=(Button) view.findViewById(R.id.back);
        clearButton.setOnClickListener(new OnClear());
        postButton=(Button) view.findViewById(R.id.post);
        postButton.setOnClickListener(new OnTake());
        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

       // popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();

                    return true;
                }
                return false;
            }
        });
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

    }
    public void showAsDropDown(View parent){
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDismiss() {
        num="";
    }
    private class GetNumber implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (num==""){
                ToastMessage(context, "特体值必须以+、-开始");
                return;
            }
            String txt = ((Button) v).getText().toString();
           // boolean s = Pattern.matches("-*(\\d+).?(\\d)*", num + txt);
            num =num + txt;
            //gly.setBackgroundResource(R.drawable.js);
            print.setText(num);
        }
    }
    private class GetGrNumber implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String txt = ((Button) v).getText().toString();
            txt=txt.replace(" ","");
            if(num.length()>0){
                if(num.equals("+")||num.equals("-")){
                    num="";
                }
            }
            if(num==""){
                num=txt;
            }else{
                if(txt.substring(0,1).equals("+")){
                    System.out.println(num.substring(1));
                    System.out.println(txt.substring(1));
                    BigDecimal a=new BigDecimal(num.substring(1));
                    BigDecimal b=new BigDecimal(txt.substring(1));
                    if(num.substring(0,1).equals("-")){
                        a=a.negate();
                    }
                    BigDecimal val=a.add(b);

                    if(val.compareTo(BigDecimal.ZERO)<0){
                        num=val.toString();
                    }else if(val.compareTo(BigDecimal.ZERO)>0){
                        num="+"+val.toString();
                    }else {
                        num="";
                    }

                }else{
                    BigDecimal a=new BigDecimal(num.substring(1));
                    BigDecimal b=new BigDecimal(txt.substring(1));
                    if(num.substring(0,1).equals("-")){
                        a=a.negate();
                    }
                    BigDecimal val=a.subtract(b);
                    if(val.compareTo(BigDecimal.ZERO)<0){
                        num=val.toString();
                    }else if(val.compareTo(BigDecimal.ZERO)>0){
                        num="+"+val.toString();
                    }else {
                        num="";
                    }
                }

            }

            //gly.setBackgroundResource(R.drawable.js);
            print.setText(num);
        }
    }
    private class AddOrSubClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            num=((Button) v).getText().toString();
            //gly.setBackgroundResource(R.drawable.js);
            print.setText(num);
        }
    }
    private class OnClear implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            num = "";
            print.setText(num);
        }

    }
    private class OnTake implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(num!=""){
                if(num.substring(0,1).equals("-")){
                    BigDecimal minVal=new BigDecimal(teams.get(teamId).getMinValue());
                    BigDecimal currVal=new BigDecimal(num);
                    minVal=minVal.add(minVal);
                    minVal=minVal.abs();
                    currVal=currVal.abs();
                    if(currVal.compareTo(minVal)>0){
                        ToastMessage(context, "减档比最小档的2倍还小，超出规定范围了！");
                        return;
                    }
                }else{
                    BigDecimal maxVal=new BigDecimal(teams.get(teamId).getMaxValue());
                    BigDecimal currVal=new BigDecimal(num.substring(1));
                    maxVal=maxVal.add(maxVal);
                    maxVal=maxVal.abs();
                    currVal=currVal.abs();
                    if(currVal.compareTo(maxVal)>0){
                        ToastMessage(context, "加档比最大档的2倍还大，超出规定范围了！");
                        return;
                    }
                }
            }
            new Thread(){
                public void run(){
                    Message msg = new Message();
                    try{
                        msg.what=teams.size();
                        if(num==""){
                            teams.get(teamId).setCurrValue("");
                            teams.get(teamId).setDescription("");
                            teams.get(teamId).setSelected(false);
                        }else{
                            teams.get(teamId).setCurrValue(num);
                            teams.get(teamId).setDescription(teams.get(teamId).getTermName()+num);
                            teams.get(teamId).setSelected(true);
                        }

                        Object[] data=new Object[]{teams,teamGroupId};
                        msg.obj=data;

                    }catch (Exception e){
                        e.printStackTrace();
                        msg.what=-1;
                        msg.obj=e;
                    }
                    msg.arg1 = LOAD_CURR_TEAMVALUE_INPUT_ACTION;//告知handler当前action
                    handler.sendMessage(msg);
                }
            }.start();
            popupWindow.dismiss();
        }

    }
    private class PopupCloseClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    }
    public static void ToastMessage(Context cont, String msg){
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }
}
