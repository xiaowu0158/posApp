package com.example.wuhongjie.myapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.adapter.PatternItemAdapter;
import com.example.wuhongjie.myapplication.adapter.ShapeItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SpecItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SpecNumberItemAdapter;
import com.example.wuhongjie.myapplication.service.PatternService;
import com.example.wuhongjie.myapplication.service.ShapeNumService;
import com.example.wuhongjie.myapplication.service.SpecGrpService;
import com.example.wuhongjie.myapplication.service.SpecLinkService;
import com.example.wuhongjie.myapplication.service.SpecNumberService;
import com.example.wuhongjie.myapplication.service.SuiteGrpDtlService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.Pattern;
import com.example.wuhongjie.myapplication.type.ShapeNum;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.SpecLink;
import com.example.wuhongjie.myapplication.type.SpecNumber;
import com.example.wuhongjie.myapplication.type.SuiteGrpDtl;

import java.util.ArrayList;



/**
 * Created by wuhongjie on 2017-03-20.
 */

public class SpecPopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private final GridView popUpMesure_detailGridView;
    private final PopupWindow popupWindow;
    private final ImageView pop_del;
    private final ShapeItemAdapter shapItemAdapter;
    private final PatternItemAdapter patternItemAdapter;
    private final SpecNumberItemAdapter specNumberItemAdapter;
    private final GridView popUpMesureShape_detailGridView;
    private final GridView popUpMesurePattern_detailGridView;
    private final GridView popUpNumber_detailGridView;
    private final Button clearButton;
    private final TextView specNumTextView;
    private Context context;
    private Handler handler;
    private Handler mSearchHandler;
    private static int LOAD_CURR_SPEC_ACTION=8;
    private static int LOAD_SPECS_ACTION=9;
    private ArrayList<Spec> specs=new ArrayList<Spec>();
    private ArrayList<Spec> currentSpecs=new ArrayList<Spec>();
    private ArrayList<ShapeNum> shapNumlist=new ArrayList<ShapeNum>();
    private ArrayList<Pattern> patternList=new ArrayList<Pattern>();
    private ArrayList<SpecNumber> specNumberList=new ArrayList<SpecNumber>();
    private ArrayList<SpecLink> specLinks=new ArrayList<SpecLink>();
    private SpecItemAdapter specItemAdapter;
    private String prodCatId="";
    private String suiteCode="";
    private boolean specNumFocus=false;
    private boolean hasLink=false;
    private int position;
    public SpecPopupWindow(Context context, ArrayList<SpecLink> specLinks,String suiteCode, ArrayList<Spec> specs, String prodCatId, boolean specNumFocus, int position, Handler handler) {
        this.context=context;
        this.handler=handler;
        this.specLinks=specLinks;
        this.prodCatId=prodCatId;
        this.specNumFocus=specNumFocus;
        this.position=position;
        this.suiteCode=suiteCode;

        if(specLinks!=null&&specLinks.size()>0){
            hasLink=true;
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        //System.out.println("=========:"+metrics.widthPixels+","+ metrics.densityDpi);
        int columns=5;
        if(metrics.widthPixels>=800){
            columns=8;
        }
        View view= LayoutInflater.from(context).inflate(R.layout.spec_popupwindow, null);

        pop_del=(ImageView) view.findViewById(R.id.pop_del);
        pop_del.setOnClickListener(new PopupCloseClick());
        clearButton=(Button)view.findViewById(R.id.clearButton);
        specNumTextView=(TextView)view.findViewById(R.id.specNum);
        popUpMesure_detailGridView=(GridView) view.findViewById(R.id.popUpMesure_detailGridView);

        popUpMesure_detailGridView.setNumColumns(5);
       // String columns=context.getString(R.dimen.pattern_grid_column);
        popUpMesureShape_detailGridView=(GridView) view.findViewById(R.id.popUpMesureShape_detailGridView);
        popUpMesureShape_detailGridView.setNumColumns( columns);
        popUpMesurePattern_detailGridView=(GridView) view.findViewById(R.id.popUpMesurePattern_detailGridView);
        popUpMesurePattern_detailGridView.setNumColumns(columns);
        if(prodCatId.equals("01")){
            popUpMesureShape_detailGridView.setVisibility(View.GONE);
            popUpMesurePattern_detailGridView.setVisibility(View.GONE);
            hasLink=false;
        }
        popUpNumber_detailGridView=(GridView) view.findViewById(R.id.popUpNumber_detailGridView);
        popUpNumber_detailGridView.setNumColumns(10);
        this.specs=specs;
        currentSpecs.clear();
        currentSpecs.addAll(specs);
        specItemAdapter=new SpecItemAdapter(currentSpecs, specNumFocus,context);
        popUpMesure_detailGridView.setAdapter(specItemAdapter);
        popUpMesure_detailGridView.setOnItemClickListener(new MesureDetailGridViewItemClick());

        shapNumlist= ShapeNumService.getShapeNumData();
        shapItemAdapter=new ShapeItemAdapter(shapNumlist, context);
        popUpMesureShape_detailGridView.setAdapter(shapItemAdapter);
        popUpMesureShape_detailGridView.setOnItemClickListener(new MesureShapeGridViewItemClick());

        patternList= PatternService.getPatternData(hasLink);
        patternItemAdapter=new PatternItemAdapter(patternList, context);
        popUpMesurePattern_detailGridView.setAdapter(patternItemAdapter);
        popUpMesurePattern_detailGridView.setOnItemClickListener(new MesurePatternGridViewItemClick());

        specNumberList= SpecNumberService.getSpecNumberData();
        specNumberItemAdapter=new SpecNumberItemAdapter(specNumberList, context);
        popUpNumber_detailGridView.setAdapter(specNumberItemAdapter);
        popUpNumber_detailGridView.setOnItemClickListener(new NumberGridViewItemClick());
        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
        initData();
        if(hasLink){
            loadSpecData(mSearchHandler);
        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("dismidddddss");
    }

    @Override
    public void onDismiss() {

    }
    public void showAsDropDown(View parent){
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }
    private class PopupCloseClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    }

    private class MesureDetailGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Spec spec=(Spec) adapterView.getItemAtPosition(i);
            spec.setSelected(true);
            for(int j=0;j<currentSpecs.size();j++){
                if(j!=i){
                    currentSpecs.get(j).setSelected(false);
                }
            }
            specItemAdapter.notifyDataSetChanged();

            Message msg = new Message();
            Object[] data=new Object[]{spec,position};

            msg.obj=data;
            msg.what=1;
            msg.arg1 =LOAD_CURR_SPEC_ACTION;
            handler.sendMessage(msg);
            popupWindow.dismiss();


        }

    }
    private class MesureShapeGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ShapeNum shapeNum=(ShapeNum) adapterView.getItemAtPosition(i);
            shapeNum.setSelected(!shapeNum.isSelected());
            for(int j=0;j<specNumberList.size();j++){
                if(j!=i){
                    specNumberList.get(j).setSelected(false);
                }
            }
            shapItemAdapter.notifyDataSetChanged();
            loadSpecData(mSearchHandler);
        }

    }
    private class MesurePatternGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Pattern pattern=(Pattern) adapterView.getItemAtPosition(i);
            pattern.setSelected(!pattern.isSelected());
            for(int j=0;j<patternList.size();j++){
                if(j!=i){
                    patternList.get(j).setSelected(false);
                }
            }
            patternItemAdapter.notifyDataSetChanged();
            loadSpecData(mSearchHandler);
        }

    }
    private class NumberGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SpecNumber specNumber=(SpecNumber) adapterView.getItemAtPosition(i);
            boolean is=specNumber.isSelected();
            if(is){
                String v=specNumber.getValue();
                String s=specNumTextView.getText().toString();
                if(s.length()==1){
                    if(s.equals(v)){
                        specNumTextView.setText(s+s);
                        loadSpecData(mSearchHandler);
                        return;
                    }
                }

            }
            specNumber.setSelected(!specNumber.isSelected());
            for(int j=0;j<specNumberList.size();j++){
                if(j!=i){
                    specNumberList.get(j).setSelected(false);
                }
            }
            specNumberItemAdapter.notifyDataSetChanged();
            String s=specNumTextView.getText().toString();
            if(s==null){
                s="";
            }

            if(specNumber.isSelected()){
                if(s.length()==2){
                    s="";
                }
                s=s+specNumber.getValue();
                specNumTextView.setText(s);
            }else{
                specNumTextView.setText("");
            }

            loadSpecData(mSearchHandler);
        }

    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==LOAD_SPECS_ACTION){
                    if(msg.what>=0){
                        ArrayList<Spec> list = (ArrayList<Spec>)msg.obj;
                        currentSpecs.clear();
                        currentSpecs.addAll(list);
                        //System.out.println("================"+currentSpecs.size());
                        specItemAdapter.notifyDataSetChanged();
                    }
                }


            }
        };
    }
    private void loadSpecData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    String shapeNum="";
                    String pattern="";
                    String specNum=specNumTextView.getText().toString();
                    if(specNum==null){
                        specNum="";
                    }
                    for(int i=0;i<shapNumlist.size();i++){
                        if(shapNumlist.get(i).isSelected()){
                            shapeNum=shapNumlist.get(i).getShape();
                        }
                    }
                    for(int i=0;i<patternList.size();i++){
                        if(patternList.get(i).isSelected()){
                            pattern=patternList.get(i).getPattn();
                        }
                    }

                    System.out.println(shapeNum+","+pattern);
                    ArrayList<Spec> list= new ArrayList<Spec>();
                    for(int i=0;i<specs.size();i++){
                        //System.out.println(specs.get(i).getShape());
                        boolean canAdd=false;
                        if(pattern.length()<=0){
                            canAdd=true;
                        }else if(pattern.equals("联想")){
                            ArrayList<String> sl= SpecLinkService.getSpecShapeList(specLinks,suiteCode,context);
                            if(sl.indexOf(specs.get(i).getShape())>=0){
                                canAdd=true;
                            }

                        }else if(specs.get(i).getPattern().equals(pattern)){
                            //System.out.println(specs.get(i).getPattern());
                            canAdd=true;
                        }
                        System.out.println(canAdd);
                        if(canAdd){
                            canAdd=false;
                            if(shapeNum.length()<=0){
                                canAdd=true;
                            }else if(specs.get(i).getShape().indexOf(shapeNum)>=0){

                                canAdd=true;
                            }
                        }
                        if(canAdd){
                            if(specNum.length()>0){
                                canAdd=false;
                                String specCode=specs.get(i).getSpecCode();
                                if(specNum.length()==1){
                                    if(specCode.substring(specCode.length()-2,specCode.length()-1).equals(specNum)){
                                        canAdd=true;
                                    }
                                }
                                if(specNum.length()==2){
                                    if(specCode.substring(specCode.length()-2,specCode.length()).equals(specNum)){
                                        canAdd=true;
                                    }
                                }

                            }
                        }
                        if(canAdd){
                            list.add(specs.get(i));
                        }

                    }
                    if(pattern.equals("联想")&&list.size()==0){
                        list.addAll(specs);
                    }
                    //System.out.println("list.size():"+list.size());
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_SPECS_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }

}
