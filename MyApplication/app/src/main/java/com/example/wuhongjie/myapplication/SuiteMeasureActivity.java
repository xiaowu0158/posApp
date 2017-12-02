package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.adapter.EmployeeItemAdapter;
import com.example.wuhongjie.myapplication.adapter.ProdCatItemAdapter;
import com.example.wuhongjie.myapplication.adapter.ProdMeasureItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SuiteMeasureItemAdapter;
import com.example.wuhongjie.myapplication.service.EmployeeService;
import com.example.wuhongjie.myapplication.service.ProdClsService;
import com.example.wuhongjie.myapplication.service.SuiteService;
import com.example.wuhongjie.myapplication.service.SynService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCat;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.Suite;
import com.example.wuhongjie.myapplication.type.SuiteGrpDtl;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamGroup;
import com.example.wuhongjie.myapplication.util.FullyLinearLayoutManager;

import java.util.ArrayList;

public class SuiteMeasureActivity extends Activity {
    private Handler mSearchHandler;
    private SharedPreferences sharedPreferences;
    private String yxgsDm;
    private String quyDm;
    private String custCode;
    private String custName;
    private boolean specNumFocus;
    private boolean bigFond;
    private String userCode;
    private Activity activity;
    private Employee employee=new Employee();
    private static int LOAD_SUITE=1;
    private static int LOAD_ORDERITM_ACTION=5;
    private static int LOAD_CURR_TEAMVALUE_ACTION=9;
    private static int LOAD_CURR_TEAMVALUE_INPUT_ACTION=19;
    private static int LOAD_CURR_SPEC_ACTION=8;
    private static int UP_LOAD_DRP=13;
    private static int SAVE_BUTTON_ACTION=11;
    private ArrayList<Suite> suiteList=new ArrayList<Suite>();
    private ArrayList<ProdCls> prodClsList=new ArrayList<ProdCls>();
    private ArrayList<ProdCat> prodCatList=new ArrayList<ProdCat>();
    private ProdMeasureItemAdapter prodMeasureItemAdapter;
    private SuiteMeasureItemAdapter suiteMeasureItemAdapter;
    private RecyclerView suiteMeasure_ListView;
    private LinearLayout foot_progressLinearLayout;
    private RecyclerView prodListView;
    private Button ltSaveButton;
    private Button ltBackButton;
    private LinearLayout spreadLayout;
    private TextView spreadTextView;
    private TextView nameTextView;
    private GridView prodCatGridview;
    private ProdCatItemAdapter prodCatItemAdapter;
    private String currProdCatId="-1";
    private boolean isPad=true;
    private FullyLinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suite_measure);
        activity =SuiteMeasureActivity.this;
        suiteMeasure_ListView=(RecyclerView) findViewById(R.id.suiteMeasureListView);
        prodListView=(RecyclerView) findViewById(R.id.prodListView);
        FullyLinearLayoutManager linearLayoutManager2 = new FullyLinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        prodListView.setLayoutManager(linearLayoutManager2);
        foot_progressLinearLayout=(LinearLayout) findViewById(R.id.foot_progressLinearLayout);
        linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        suiteMeasure_ListView.setLayoutManager(linearLayoutManager);
        prodCatGridview=(GridView)findViewById(R.id.prodCatGridview);
        ltSaveButton=(Button) findViewById(R.id.ltSaveButton);
        ltSaveButton.setOnClickListener(new LtSaveButtonClick());
        ltBackButton=(Button)findViewById(R.id.ltBackButton);
        ltBackButton.setOnClickListener(new LtBackButtonClick());
        spreadLayout=(LinearLayout) findViewById(R.id.spreadLayout);
        spreadLayout.setOnClickListener(new SpreadClick());
        spreadTextView=(TextView) findViewById(R.id.spreadTextView);
        nameTextView=(TextView) findViewById(R.id.nameTextView);
        //HeaderStormItemDiratcion diraction = new HeaderStormItemDiratcion(1);
        //suiteMeasure_ListView.addItemDecoration(diraction);
        Intent intent = getIntent();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) SuiteMeasureActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        if(metrics.widthPixels<800){
            isPad=false;
        }
        sharedPreferences=SuiteMeasureActivity.this.getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        yxgsDm=sharedPreferences.getString("yxgsDm","");
        quyDm=sharedPreferences.getString("quyDm","");
        custCode=sharedPreferences.getString("custCode","");
        custName=sharedPreferences.getString("custName","");
        specNumFocus=sharedPreferences.getBoolean("specNumFocus",false);
        bigFond=sharedPreferences.getBoolean("bigFond",false);
        userCode=sharedPreferences.getString("user","");
        employee=(Employee) intent.getSerializableExtra("employee");
        String s=custName+"\n"+employee.getCname();
        if(employee.getDept()!=null&&employee.getDept().length()>0){
            s=s+"【"+employee.getDept()+"】";
        }
        nameTextView.setText(s);
        this.initData();
        loadSuiteData(mSearchHandler);
        loadOrderItemData(mSearchHandler);
        suiteMeasureItemAdapter = new SuiteMeasureItemAdapter(suiteList,bigFond, specNumFocus,SuiteMeasureActivity.this,mSearchHandler);
        suiteMeasure_ListView.setAdapter(suiteMeasureItemAdapter);
        prodMeasureItemAdapter=new ProdMeasureItemAdapter(prodClsList,SuiteMeasureActivity.this,mSearchHandler);
        prodListView.setAdapter(prodMeasureItemAdapter);
        prodCatItemAdapter=new ProdCatItemAdapter(prodCatList,SuiteMeasureActivity.this);
        prodCatGridview.setAdapter(prodCatItemAdapter);
        prodCatGridview.setOnItemClickListener(new ProdCatGridViewItemClick1());
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                if(msg.arg1==LOAD_SUITE){
                    if(msg.what>0){
                        ArrayList<Suite> list = (ArrayList<Suite>)msg.obj;
                        suiteList.clear();
                        suiteList.addAll(list);
                        suiteMeasureItemAdapter.notifyDataSetChanged();
                    }
                    foot_progressLinearLayout.setVisibility(View.GONE);
                }
                if(msg.arg1==LOAD_CURR_TEAMVALUE_ACTION){
                    if(msg.what>=0){
                        ArrayList<Team> currteams=(ArrayList<Team>)(((Object[])msg.obj)[0]);
                        String groupId=(String)(((Object[])msg.obj)[1]);
                        for(int i=0;i<suiteList.size();i++){
                            ArrayList<TeamGroup> teamGroups=suiteList.get(i).getTeamGroup();
                            for(int j=0;j<teamGroups.size();j++){
                                if(teamGroups.get(j).getGroupId().equals(groupId)){
                                    suiteList.get(i).getTeamGroup().get(j).setTeams(currteams);
                                    if(suiteList.get(i).getTeamGroup().size()==1 ||
                                            (suiteList.get(i).getTeamGroup().size()==j+1)){
                                        suiteList.get(i).setCollapsible(true);
                                    }
                                    break;
                                }

                            }
                        }

                        suiteMeasureItemAdapter.notifyDataSetChanged();
                    }
                }
                if(msg.arg1==LOAD_CURR_TEAMVALUE_INPUT_ACTION){
                    if(msg.what>=0){
                        ArrayList<Team> currteams=(ArrayList<Team>)(((Object[])msg.obj)[0]);
                        String groupId=(String)(((Object[])msg.obj)[1]);

                        for(int i=0;i<suiteList.size();i++){
                            ArrayList<TeamGroup> teamGroups=suiteList.get(i).getTeamGroup();
                            for(int j=0;j<teamGroups.size();j++){
                                if(teamGroups.get(j).getGroupId().equals(groupId)){
                                    suiteList.get(i).getTeamGroup().get(j).setTeams(currteams);
                                    break;
                                }
                            }

                        }
                        suiteMeasureItemAdapter.notifyDataSetChanged();
                    }
                }
                if(msg.arg1==LOAD_CURR_SPEC_ACTION){
                    if(msg.what>0){
                        Spec spec=(Spec)(((Object[])msg.obj)[0]);
                        int  position=(int)(((Object[])msg.obj)[1]);
                        //System.out.println("========>"+position+"======"+spec.getSpec());
                        suiteList.get(position).setSpec(spec);
                        suiteMeasureItemAdapter.notifyDataSetChanged();
                    }

                }
                if(msg.arg1==LOAD_ORDERITM_ACTION){
                    if(msg.what>0){
                        ArrayList<ProdCls> list = (ArrayList<ProdCls>)msg.obj;
                        for(int i=0;i<list.size();i++){
                            if(spreadTextView.getText().toString().equals("隐藏")){
                                list.get(i).setSimple(true);
                            }else{
                                list.get(i).setSimple(false);
                            }

                        }
                        if(prodCatList.size()==0){
                            prodCatList.clear();
                            ArrayList<String> prodCatIdList=new ArrayList<String>();
                            for(int i=0;i<list.size();i++){
                                if(!prodCatIdList.contains(list.get(i).getProdCatId())){
                                    ProdCat prodCat=new ProdCat();
                                    prodCat.setProdCatId(list.get(i).getProdCatId());
                                    prodCat.setProdCatName(list.get(i).getProdCatName());
                                    prodCat.setSelected(false);
                                    prodCatList.add(prodCat);
                                    prodCatIdList.add(list.get(i).getProdCatId());
                                }

                            }
                            ProdCat prodCat=new ProdCat();
                            prodCat.setProdCatId("-1");
                            prodCat.setProdCatName("全部");
                            prodCat.setSelected(isPad);
                            prodCatList.add(prodCat);
                            if(!isPad){
                                prodCatList.get(0).setSelected(true);
                                currProdCatId=prodCatList.get(0).getProdCatId();
                            }
                            prodCatGridview.setNumColumns(prodCatList.size());
                            prodCatItemAdapter.notifyDataSetChanged();
                        }
                        prodClsList.clear();
                        if(currProdCatId.equals("-1")){
                            prodClsList.addAll(list);
                        }else{
                            for(int i=0;i<list.size();i++){
                                if(list.get(i).getProdCatId().equals(currProdCatId)){

                                    prodClsList.add(list.get(i));
                                }
                            }
                        }
                        prodMeasureItemAdapter.notifyDataSetChanged();
                    }
                }
                if(msg.arg1==SAVE_BUTTON_ACTION){
                    Toast.makeText(activity,
                            msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    if(msg.what>0){
                        synToDrp(mSearchHandler);
                    }
                }
                if(msg.arg1==UP_LOAD_DRP){
                    activity.finish();
                }
            }
        };
    }
    private void loadSuiteData(final Handler handler){
        foot_progressLinearLayout.setVisibility(View.VISIBLE);
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<Suite> list= SuiteService.getSuite(custCode,employee.getPckNo(),SuiteMeasureActivity.this);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_SUITE;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadOrderItemData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    String pckNo=employee.getPckNo();
                    if(pckNo==null){
                        pckNo="";
                    }
                    ArrayList<ProdCls> list= ProdClsService.getProdCls(custCode,pckNo,activity);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_ORDERITM_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadSpecGrpData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    String pckNo=employee.getPckNo();
                    if(pckNo==null){
                        pckNo="";
                    }
                    ArrayList<ProdCls> list= ProdClsService.getProdCls(custCode,pckNo,activity);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_ORDERITM_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void synToDrp(final Handler handler){
        new Thread() {
            public void run() {
                String errMsg= SynService.upLoadData(activity,userCode);
                Message msg = new Message();
                msg.what=1;
                msg.obj="量体数据上传到DRP！";
                msg.arg1 = UP_LOAD_DRP;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private class LtSaveButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            new Thread() {
                public void run() {
                    String errMsg=SynService.saveData(activity,custCode,employee,prodClsList,suiteList);
                    Message msg = new Message();
                    if(errMsg.length()>0){
                        msg.what=-1;
                        msg.obj=errMsg;
                    }else{
                        msg.what=1;
                        msg.obj="量体数据保存成功！";
                    }
                    msg.arg1 = SAVE_BUTTON_ACTION;//告知handler当前action
                    mSearchHandler.sendMessage(msg);
                }
            }.start();
        }
    }
    private class LtBackButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            activity.finish();
        }
    }
    private class SpreadClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            for(int i=0;i<prodClsList.size();i++){

                if(prodClsList.get(i).isSimple()){
                    prodClsList.get(i).setSimple(false);
                }else{
                    prodClsList.get(i).setSimple(true);
                }
                if(prodClsList.get(i).isSimple()){
                    spreadTextView.setText("隐藏");
                }else{
                    spreadTextView.setText("展开");
                }
            }
            prodMeasureItemAdapter.notifyDataSetChanged();
        }
    }
    private class ProdCatGridViewItemClick1 implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           // int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
           // int lastItem = linearLayoutManager.findLastVisibleItemPosition();
           // System.out.println(firstItem+";"+lastItem);
            //linearLayoutManager.scrollToPosition(2);
            ProdCat prodCat=(ProdCat) adapterView.getItemAtPosition(i);
            prodCat.setSelected(true);
            currProdCatId=prodCat.getProdCatId();
            for(int j=0;j<prodCatList.size();j++){
                if(j!=i){
                    prodCatList.get(j).setSelected(false);
                }
            }
            prodCatItemAdapter.notifyDataSetChanged();
            loadOrderItemData(mSearchHandler);
        }
    }
}
