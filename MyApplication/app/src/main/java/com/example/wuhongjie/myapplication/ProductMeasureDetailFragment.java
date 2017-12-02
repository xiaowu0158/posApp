package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.adapter.SpecGrpItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SpecItemAdapter;
import com.example.wuhongjie.myapplication.adapter.TeamGroupItemAdapter;
import com.example.wuhongjie.myapplication.adapter.TeamItemAdapter;
import com.example.wuhongjie.myapplication.service.ProdClsService;
import com.example.wuhongjie.myapplication.service.SpecGrpService;
import com.example.wuhongjie.myapplication.service.SpecService;
import com.example.wuhongjie.myapplication.service.SynService;
import com.example.wuhongjie.myapplication.service.TeamService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamGroup;
import com.example.wuhongjie.myapplication.util.DBhelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a single ProductMeasure detail screen.
 * This fragment is either contained in a {@link ProductMeasureListActivity}
 * in two-pane mode (on tablets) or a {@link ProductMeasureDetailActivity}
 * on handsets.
 */
public class ProductMeasureDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    //public static final String ARG_ITEM_ID = "suiteCode";

    /**
     * The dummy content this fragment is presenting.
     */
    private String suiteCode;
    private String suiteName;
    private String dtlId;
    private SpecGrp specGrp=new SpecGrp();
    private Employee employee=new Employee();
    private ProdCls prodCls=new ProdCls();
    private TextView prodCatNameTextView;
    private TextView prodCodeTextView;
    private TextView prodNameTextView;
    private TextView selectSpecGrpTextView;
    private Activity activity;
    private LinearLayout selectSpecGrpRelative;
    private Handler mSearchHandler;
    private static int LOAD_SPEC_GRP_ACTION=5;
    private static int LOAD_SPEC_ACTION=6;
    private static int LOAD_TEAM_ACTION=7;
    private static int LOAD_CURR_SPEC_ACTION=8;
    private static int LOAD_CURR_TEAMVALUE_ACTION=9;
    private static int LOAD_CURR_TEAMVALUE_INPUT_ACTION=19;
    private static int LOAD_TEAMGROUPS_ACTION=10;
    private static int PROGRESS_BAR_VISIBLE_ACTION=200;
    private static int SAVE_BUTTON_ACTION=11;
    private static int LOAD_CURRENT_DATA=12;
    private static int UP_LOAD_DRP=13;
    private ArrayList<SpecGrp> specGrps=new ArrayList<SpecGrp>();
    private SpecGrpItemAdapter specGrpItemAdapter;
    private ArrayList<Spec> specs=new ArrayList<Spec>();
    private Spec spec=new Spec();
    private SpecItemAdapter specItemAdapter;
    private ArrayList<Team> teams=new ArrayList<Team>();
    private ArrayList<TeamGroup> teamGroups=new ArrayList<TeamGroup>();
    private TeamItemAdapter teamItemAdapter;
    private TeamGroupItemAdapter teamGroupItemAdapter;
    private AlertDialog mDialog;
    private String[] mData ;
    private TextView prodNameTextView1;
    private LinearLayout prodNameLineLayout;
    private LinearLayout prodNameLineLayout1;
    private ListView teamListView;
    private SpecPopupWindow specPopupWindow;
    private LinearLayout selectSpecLinearLayout;
    private TextView selectSpecTextView;
    private LinearLayout foot_progressLinearLayout;
    private Button ltSaveButton;
    private String yxgsDm="";
    private String quyDm="";
    public String custCode="";
    public String custName="";
    public String userCode="";
    private SharedPreferences sharedPreferences;
    private Configuration mConfiguration;
    private int ori;
    private Button ltBackButton;
    private boolean specNumFocus=false;
    private boolean bigFond=false;
    private TextView teamMemoTextView;
    private LinearLayout measure_detailTeamMemo;
    private TextView tvReduce;
    private TextView tvAdd;
    private TextView tvNum;
    private TextView tvUom;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductMeasureDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        suiteCode=(String)getArguments().get("suiteCode");
        suiteName=(String)getArguments().get("suiteName");
        dtlId=(String)getArguments().get("dtlId");
        employee=(Employee) getArguments().getSerializable("employee");
        prodCls=(ProdCls) getArguments().getSerializable("prodCls");
        sharedPreferences=this.getActivity().getSharedPreferences("custInofo", Context.MODE_PRIVATE);
        yxgsDm=sharedPreferences.getString("yxgsDm","");
        quyDm=sharedPreferences.getString("quyDm","");
        custCode=sharedPreferences.getString("custCode","");
        custName=sharedPreferences.getString("custName","");
        specNumFocus=sharedPreferences.getBoolean("specNumFocus",false);
        bigFond=sharedPreferences.getBoolean("bigFond",false);
        userCode=sharedPreferences.getString("user","");

        System.out.println("111111"+suiteCode);
        activity = this.getActivity();
            //CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            //if (appBarLayout != null) {
            //    appBarLayout.setTitle(mItem.getSuiteName());

            //}
            Toolbar detail_toolbar = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (detail_toolbar != null) {
                detail_toolbar.setTitle(suiteName+"["+employee.getCname()+"]");

            }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.productmeasure_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (suiteName != null) {
            ((TextView) rootView.findViewById(R.id.productmeasure_detail)).setText(suiteName);
        }

        prodCatNameTextView=(TextView) rootView.findViewById(R.id.measure_detailProdCatNameTextView);
        prodCodeTextView=(TextView) rootView.findViewById(R.id.measure_detailProdClsCodeTextView);
        prodNameTextView=(TextView) rootView.findViewById(R.id.measure_detailProdClsNameTextView);
        prodNameTextView1=(TextView) rootView.findViewById(R.id.measure_detailProdClsNameTextView1);
        teamMemoTextView=(TextView) rootView.findViewById(R.id.measure_detailTeamMemoTextView);
        tvReduce=(TextView) rootView.findViewById(R.id.tv_reduce);
        tvAdd=(TextView) rootView.findViewById(R.id.tv_add);
        tvNum=(TextView) rootView.findViewById(R.id.tv_num);
        tvUom=(TextView) rootView.findViewById(R.id.tv_uom);
        tvReduce.setOnClickListener(new ProdQtyNumReduceClick());
        tvAdd.setOnClickListener(new ProdQtyNumAddClick());

        measure_detailTeamMemo=(LinearLayout) rootView.findViewById(R.id.measure_detailTeamMemo);
        foot_progressLinearLayout=(LinearLayout) rootView.findViewById(R.id.foot_progressLinearLayout);
        //prodNameLineLayout=(LinearLayout) rootView.findViewById(R.id.measure_detailProdClsNameLine);
        prodNameLineLayout1=(LinearLayout) rootView.findViewById(R.id.measure_detailProdClsNameLine1);
        ltSaveButton=(Button)rootView.findViewById(R.id.ltSaveButton);
        ltSaveButton.setOnClickListener(new LtSaveButtonClick());
        ltBackButton=(Button)rootView.findViewById(R.id.ltBackButton);
        ltBackButton.setOnClickListener(new LtBackButtonClick());
        mConfiguration = this.getResources().getConfiguration();
        ori = mConfiguration.orientation ; //获取屏幕方向
        if(ori == mConfiguration.ORIENTATION_LANDSCAPE){
            prodNameTextView.setVisibility(View.VISIBLE);
            prodNameLineLayout1.setVisibility(View.GONE);
        }else{
            prodNameLineLayout1.setVisibility(View.VISIBLE);
            prodNameTextView.setVisibility(View.GONE);
        }
        prodCatNameTextView.setText(prodCls.getProdCatName());
        prodCodeTextView.setText(prodCls.getPrdCode());
        prodNameTextView.setText(prodCls.getPrdName());
        prodNameTextView1.setText(prodCls.getPrdName());
        tvUom.setText(prodCls.getUom());
        selectSpecGrpTextView=(TextView) rootView.findViewById(R.id.selectSpecGrpTextView);
        selectSpecGrpRelative=(LinearLayout)rootView.findViewById(R.id.selectSpecGrpRelative);
        selectSpecLinearLayout=(LinearLayout)rootView.findViewById(R.id.selectSpecLinearLayout);
        selectSpecTextView=(TextView) rootView.findViewById(R.id.selectSpecTextView);

        teamListView=(ListView) rootView.findViewById(R.id.teamListView);
        specGrpItemAdapter=new SpecGrpItemAdapter(specGrps, this.getActivity());

        selectSpecGrpRelative.setOnClickListener(new SelectSpecGrpClick());
        selectSpecLinearLayout.setOnClickListener(new SelectSpecClick());
        this.initData();
        //teamItemAdapter=new TeamItemAdapter(teams, this.getActivity(),mSearchHandler);
        //teamListView.setAdapter(teamItemAdapter);
        teamGroupItemAdapter=new TeamGroupItemAdapter(teamGroups, bigFond,this.getActivity(),mSearchHandler);

        teamListView.setAdapter(teamGroupItemAdapter);
        teamListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //System.out.println("弹出了软键盘");
                //new KeyboardUtil(activity, activity, ).showKeyboard();
                return false;
            }
        });
        loadSpecGrpData(mSearchHandler);
       // loadTeamData(mSearchHandler);
        foot_progressLinearLayout.setVisibility(View.VISIBLE);
        loadTeamGroupData(mSearchHandler);
        return rootView;
    }
    private class ProdQtyNumReduceClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int num=Integer.parseInt(tvNum.getText().toString());
            if(num>1){
                tvNum.setText(String.valueOf(num-1));
            }
        }
    }
    private class ProdQtyNumAddClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int num=Integer.parseInt(tvNum.getText().toString());

            tvNum.setText(String.valueOf(num+1));

        }
    }
    private class SelectSpecGrpClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            ArrayAdapter<String> _adapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_dropdown_item_1line, mData);
            ListView listView = new ListView(view.getContext());//this为获取当前的上下文
            listView.setAdapter(_adapter);

            mDialog = new AlertDialog.Builder(view.getContext()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            }).create();
            mDialog.setTitle("选择款式甄别");

            mDialog.setView(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> pParent, View pView,
                                        int pPosition, long pId) {

                    specGrp=specGrps.get(pPosition);
                    selectSpecGrpTextView.setText(specGrp.getGrpName());
                    loadSpecData(mSearchHandler);
                    mDialog.dismiss();
                }
            });
            mDialog.show();
           // loadSpecGrpData(mSearchHandler);
        }
    }
    private class SelectSpecClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(specGrp.getGrpCode()==null ||specGrp.getGrpCode().length()<=0){

                return;
            }
            specPopupWindow = new SpecPopupWindow(activity,null,prodCls.getSuiteCode(),specs,prodCls.getProdCatId(),specNumFocus,0,mSearchHandler);
            specPopupWindow.showAsDropDown(view);
        }
    }

    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==LOAD_SPEC_GRP_ACTION){
                    if(msg.what>0){
                        ArrayList<SpecGrp> list = (ArrayList<SpecGrp>)msg.obj;
                        specGrps.clear();
                        specGrps.addAll(list);
                        specGrpItemAdapter.notifyDataSetChanged();
                    }
                    if(prodCls.getQty()!=null) {
                        tvNum.setText(prodCls.getQty());
                    }else{
                        tvNum.setText("1");
                    }
                }
                if(msg.arg1==LOAD_SPEC_ACTION){
                    if(msg.what>0){
                        ArrayList<Spec> list = (ArrayList<Spec>)msg.obj;
                        specs.clear();
                        specs.addAll(list);

                       // specItemAdapter.notifyDataSetChanged();

                    }
                }
                if(msg.arg1==LOAD_CURR_SPEC_ACTION){
                    if(msg.what>0){

                        spec= (Spec)(((Object[])msg.obj)[0]);
                        selectSpecTextView.setText("规格"+spec.getSpec()+"； 编号:"+spec.getSpecCode()+"；号型:"+spec.getShape()+"；"+
                                spec.getPattern());
                       // specItemAdapter.notifyDataSetChanged();

                    }
                }
                if(msg.arg1==LOAD_TEAM_ACTION){
                    if(msg.what>0){
                        ArrayList<Team> list = (ArrayList<Team>)msg.obj;
                        teams.clear();
                        teams.addAll(list);
                        setListViewHeightBasedOnChildren(teamListView);

                        teamItemAdapter.notifyDataSetChanged();

                    }
                }
                if(msg.arg1==LOAD_TEAMGROUPS_ACTION){
                    if(msg.what>=0){
                        ArrayList<TeamGroup> list = (ArrayList<TeamGroup>)msg.obj;
                        teamGroups.clear();
                        teamGroups.addAll(list);
                        setListViewHeightBasedOnChildren(teamListView);

                        teamGroupItemAdapter.notifyDataSetChanged();
                        if(teamGroups.size()>0){
                            loadCurrentData(mSearchHandler);
                        }
                        if(prodCls.getTeamMemo()!=null){
                            if(prodCls.getTeamMemo().trim().length()>0){
                                measure_detailTeamMemo.setVisibility(View.VISIBLE);
                                teamMemoTextView.setText(prodCls.getTeamMemo());
                                teamMemoTextView.setVisibility(View.VISIBLE);
                            }else{
                                measure_detailTeamMemo.setVisibility(View.GONE);
                                teamMemoTextView.setVisibility(View.GONE);
                            }


                        }else{
                            measure_detailTeamMemo.setVisibility(View.GONE);
                            teamMemoTextView.setVisibility(View.GONE);
                        }


                    }
                    foot_progressLinearLayout.setVisibility(View.GONE);
                }
                if(msg.arg1==LOAD_CURR_TEAMVALUE_ACTION){

                    if(msg.what>=0){
                        System.out.println("LOAD_CURR_TEAMVALUE_ACTION==========================");
                        ArrayList<Team> currteams=(ArrayList<Team>)(((Object[])msg.obj)[0]);

                        String groupId=(String)(((Object[])msg.obj)[1]);

                        //System.out.println(teamDesc.getTeamValue()+"============="+teamDesc.getTeamId());
                        for(int i=0;i<teamGroups.size();i++){
                            if(teamGroups.get(i).getGroupId().equals(groupId)){
                                //System.out.println(currteams.get(0).getDescription());
                                //System.out.println(currteams.get(0).getCurrValue());
                                teamGroups.get(i).setTeams(currteams);
                                break;
                            }

                        }
                        teamGroupItemAdapter.notifyDataSetChanged();

                    }
                    foot_progressLinearLayout.setVisibility(View.GONE);
                }
                if(msg.arg1==LOAD_CURR_TEAMVALUE_INPUT_ACTION){

                    if(msg.what>=0){
                        System.out.println("LOAD_CURR_TEAMVALUE_ACTION==========================");
                        ArrayList<Team> currteams=(ArrayList<Team>)(((Object[])msg.obj)[0]);

                        String groupId=(String)(((Object[])msg.obj)[1]);

                        //System.out.println(teamDesc.getTeamValue()+"============="+teamDesc.getTeamId());
                        for(int i=0;i<teamGroups.size();i++){
                            if(teamGroups.get(i).getGroupId().equals(groupId)){
                                teamGroups.get(i).setTeams(currteams);
                                break;
                            }
                        }
                        teamGroupItemAdapter.notifyDataSetChanged();

                    }
                    foot_progressLinearLayout.setVisibility(View.GONE);
                }
                if(msg.arg1==PROGRESS_BAR_VISIBLE_ACTION){
                    foot_progressLinearLayout.setVisibility(View.VISIBLE);
                }
                if(msg.arg1==SAVE_BUTTON_ACTION){

                    Toast.makeText(activity,
                            msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    synToDrp(mSearchHandler);

                }
                if(msg.arg1==UP_LOAD_DRP){
                    if(ori == mConfiguration.ORIENTATION_LANDSCAPE){

                    }else{
                        activity.finish();
                    }
                }
                if(msg.arg1==LOAD_CURRENT_DATA){
                    if(specGrp.getGrpName()!=null){
                        selectSpecGrpTextView.setText(specGrp.getGrpName());
                    }
                    if(spec.getSpec()!=null){
                        selectSpecTextView.setText("规格"+spec.getSpec()+"； 编号:"+spec.getSpecCode()+"；号型:"+spec.getShape()+"；"+
                                spec.getPattern());
                        teamGroupItemAdapter.notifyDataSetChanged();
                    }


                }
            }
        };
    }
    private void loadSpecGrpData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    prodCls=ProdClsService.updateProdClsTeamMemo(prodCls,employee.getPckNo(),dtlId,activity);
                    ArrayList<SpecGrp> list= SpecGrpService.getSpecGrp(suiteCode,prodCls.getGender(),activity);
                    mData=new String[list.size()];
                    for(int i=0;i<list.size();i++){
                        mData[i]=list.get(i).getGrpName();
                    }
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_SPEC_GRP_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadSpecData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    System.out.println("=========================");
                    ArrayList<Spec> list= SpecService.getSpec(suiteCode,prodCls.getGender(),specGrp.getGrpId(),activity);
                    msg.what=list.size();
                   // System.out.println(list.get(0).getSpec());
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_SPEC_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadTeamData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<Team> list= TeamService.getTeamData(suiteCode,activity);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_TEAM_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadTeamGroupData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    prodCls=ProdClsService.updateProdClsTeamMemo(prodCls,employee.getPckNo(),dtlId,activity);

                    ArrayList<TeamGroup> list= TeamService.getTeamGroupData(suiteCode,activity);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_TEAMGROUPS_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private class LtSaveButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(specGrp.getGrpCode()==null ||specGrp.getGrpCode().length()<=0){
                Toast.makeText(view.getContext(),
                        "请选择款式甄别", Toast.LENGTH_SHORT).show();
                return;

            }
            if( spec.getSpecCode()==null ||spec.getSpecCode().length()<=0){
                Toast.makeText(view.getContext(),
                        "请选择款式规格", Toast.LENGTH_SHORT).show();
                return;
            }
            saveData(mSearchHandler);
        }
    }
    private class LtBackButtonClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            activity.finish();
        }
    }
    private void saveData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    DBhelper dBhelper= new DBhelper(activity);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();
                    String qty=tvNum.getText().toString();
                    //TODO 点保存按钮
                    //保存款式甄别
                    Cursor c =db.rawQuery("SELECT * FROM EXD_TG_PRD_SPEC_GRP WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_CODE=?",
                            new String[]{prodCls.getOrdNo(),prodCls.getPrdCode(),suiteCode} );
                    if(c.getCount()==0){
                        db.execSQL("INSERT INTO EXD_TG_PRD_SPEC_GRP(ORD_NO,PRD_CODE,SUITE_CODE,GRP_CODE)" +
                                "VALUES(?,?,?,?)",new Object[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                suiteCode,
                                specGrp.getGrpCode()
                        });
                    }else {
                        db.execSQL("UPDATE EXD_TG_PRD_SPEC_GRP " +
                                "SET GRP_CODE=? WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_CODE=?",new Object[]{
                                specGrp.getGrpCode(),
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                suiteCode
                        });
                    }
                    c.close();
                    //保存规格信息
                    c =db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                            "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                            prodCls.getOrdNo(),
                            prodCls.getPrdCode(),
                            prodCls.getSuiteGrpId(),
                            employee.getPckNo(),
                            spec.getGrpId(),
                            dtlId
                    });
                    String jobId="";
                    if(c.getCount()>0){
                        c.moveToNext();
                        jobId=c.getString(c.getColumnIndex("JOB_ID"));
                        db.execSQL("UPDATE EXD_TG_ORDITMJOBPRD SET UPD_TIME=datetime('now'),QTY=?,SPEC_CODE=? " +
                                "WHERE ORD_NO=? AND PRD_CODE=? " +
                                "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                                qty,
                                spec.getSpecCode(),
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                spec.getGrpId(),
                                dtlId});
                        db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD  WHERE ORD_NO=? AND PRD_CODE=? " +
                        "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID<>? AND DTL_ID=?",new String[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                spec.getGrpId(),
                                dtlId});
                        db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD_DTL WHERE JOB_ID=?",new Object[]{
                                jobId
                        });
                        c.close();
                    }else{
                        db.execSQL("DELETE FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                                "AND SUITE_GRP_ID=? AND PCKNO=? AND DTL_ID=?",new String[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                dtlId
                        });
                        db.execSQL("INSERT INTO EXD_TG_ORDITMJOBPRD(ORD_NO,PRD_CODE,SUITE_GRP_ID,PCKNO,GRP_ID,SPEC_CODE,DTL_ID,QTY,UPD_TIME)" +
                                "VALUES(?,?,?,?,?,?,?,?,datetime('now'))",new Object[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                spec.getGrpId(),
                                spec.getSpecCode(),
                                dtlId,
                                qty
                        });
                        c.close();
                        c =db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                                "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                spec.getGrpId(),
                                dtlId
                        });
                        c.moveToNext();
                        jobId=c.getString(c.getColumnIndex("JOB_ID"));
                        c.close();
                    }


                    String teamDescriptions="";
                    for(int i=0;i<teamGroups.size();i++){
                        ArrayList<Team> detailTeams=teamGroups.get(i).getTeams();
                        for(int j=0;j<detailTeams.size();j++){
                            if(detailTeams.get(j).getCurrValue()!=null){
                                db.execSQL("INSERT INTO EXD_TG_ORDITMJOBPRD_DTL(JOB_ID,TERM_ID,CURR_VAL,DESCRIPTION,UPD_TIME)" +
                                        "VALUES(?,?,?,?,datetime('now'))",new Object[]{
                                        jobId,
                                        detailTeams.get(j).getTeamId(),
                                        detailTeams.get(j).getCurrValue(),
                                        detailTeams.get(j).getDescription()
                                });
                                if(teamDescriptions.length()<=0){
                                    teamDescriptions=detailTeams.get(j).getDescription();
                                }else{
                                    teamDescriptions=teamDescriptions+";"+detailTeams.get(j).getDescription();
                                }
                            }
                        }
                    }
                    db.execSQL("UPDATE EXD_TG_ORDITMJOBPRD SET TERM_MEMO=? WHERE JOB_ID=?",new Object[]{
                            teamDescriptions,
                            jobId
                    });
                    db.execSQL("UPDATE EXD_TG_CUSTJOB SET STATUS=? WHERE CUST_CODE=? AND PCKNO=?",new Object[]{
                            "T",
                            custCode,
                            employee.getPckNo()
                    });
                    db.close();
                    msg.what=1;
                    msg.obj="量体数据保存成功！";
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e.getMessage();
                }
                msg.arg1 = SAVE_BUTTON_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();

    }
    private void synToDrp(final Handler handler){
        new Thread() {
            public void run() {
                String errMsg=SynService.upLoadData(activity,userCode);
                Message msg = new Message();
                msg.what=1;
                msg.obj="量体数据上传到DRP！";
                msg.arg1 = UP_LOAD_DRP;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadCurrentData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    DBhelper dBhelper= new DBhelper(activity);
                    SQLiteDatabase db = dBhelper.getWritableDatabase();
                    Cursor c =db.rawQuery("SELECT * FROM EXD_TG_PRD_SPEC_GRP " +
                            "WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_CODE=?",new String[]{
                            prodCls.getOrdNo(),
                            prodCls.getPrdCode(),
                            suiteCode
                    });
                    String grpCode="";
                    String specCode="";
                    String jobId="";
                    if(c.getCount()>0){
                        c.moveToNext();
                        grpCode=c.getString(c.getColumnIndex("GRP_CODE"));
                        ArrayList<SpecGrp> specGrplist= SpecGrpService.getSpecGrp(suiteCode,prodCls.getGender(),activity);

                        for(int i=0;i<specGrplist.size();i++){
                            if(specGrplist.get(i).getGrpCode().equals(grpCode)){
                                specGrp=specGrplist.get(i);
                                ArrayList<Spec> list= SpecService.getSpec(suiteCode,prodCls.getGender(),specGrp.getGrpId(),activity);
                                specs.clear();
                                specs.addAll(list);
                                break;
                            }
                        }
                    }
                    c.close();
                    if(grpCode.length()>0){
                        c =db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD WHERE ORD_NO=? AND PRD_CODE=? " +
                                "AND SUITE_GRP_ID=? AND PCKNO=? AND GRP_ID=? AND DTL_ID=?",new String[]{
                                prodCls.getOrdNo(),
                                prodCls.getPrdCode(),
                                prodCls.getSuiteGrpId(),
                                employee.getPckNo(),
                                specGrp.getGrpId(),
                                dtlId
                        });
                        if(c.getCount()>0){
                            c.moveToNext();
                            specCode=c.getString(c.getColumnIndex("SPEC_CODE"));
                            spec=SpecService.getSingleSpec(specGrp.getGrpId(),suiteCode,prodCls.getGender(),specCode,activity);
                        }
                        c.close();
                    }

                    c=db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD " +
                            "WHERE ORD_NO=? AND PRD_CODE=? AND SUITE_GRP_ID=? AND PCKNO=? AND DTL_ID=?",new String[]{
                            prodCls.getOrdNo(),
                            prodCls.getPrdCode(),
                            prodCls.getSuiteGrpId(),
                            employee.getPckNo(),
                            dtlId
                    });
                    if(c.getCount()>0){
                        c.moveToNext();
                        jobId=c.getString(c.getColumnIndex("JOB_ID"));
                        c.close();
                        c=db.rawQuery("SELECT * FROM EXD_TG_ORDITMJOBPRD_DTL " +
                                "WHERE JOB_ID=?",new String[]{
                                jobId
                        });
                        if(c.getCount()>0){
                            HashMap<String,String> termValus=new HashMap<String,String>();
                            HashMap<String,String> termDescs=new HashMap<String,String>();

                            while (c.moveToNext()){
                                termValus.put(c.getString(c.getColumnIndex("TERM_ID")),c.getString(c.getColumnIndex("CURR_VAL")));
                                termDescs.put(c.getString(c.getColumnIndex("TERM_ID")),c.getString(c.getColumnIndex("DESCRIPTION")));
                            }

                            for(int i=0;i<teamGroups.size();i++){
                                ArrayList<Team> detailTeams=teamGroups.get(i).getTeams();
                                for(int j=0;j<detailTeams.size();j++){
                                    if(termValus.containsKey(detailTeams.get(j).getTeamId())){
                                        detailTeams.get(j).setCurrValue(termValus.get(detailTeams.get(j).getTeamId()));
                                        detailTeams.get(j).setDescription(termDescs.get(detailTeams.get(j).getTeamId()));
                                    }

                                }
                                teamGroups.get(i).setTeams(detailTeams);
                            }
                        }

                    }
                    c.close();
                    db.close();
                    msg.what=1;
                    msg.obj="量体数据加载成功！";
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e.getMessage();
                }
                msg.arg1 = LOAD_CURRENT_DATA;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();

    }
    public static void setGridViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight+15;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除


        listView.setLayoutParams(params);
    }
}
