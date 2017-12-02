package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.adapter.AreaItemAdapter;
import com.example.wuhongjie.myapplication.adapter.CustItemAdapter;
import com.example.wuhongjie.myapplication.adapter.EmployeeItemAdapter;
import com.example.wuhongjie.myapplication.service.CustService;
import com.example.wuhongjie.myapplication.service.EmployeeService;
import com.example.wuhongjie.myapplication.service.YxgsService;
import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.Yxgs;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    private Button unLtEmpButton;
    private Button ltedEmpButton;
    private Button allButton;
    private Handler mSearchHandler;
    private GridView employee_gridView;
    private ArrayList<Employee> employeeList = new ArrayList<Employee>();
    private String[] depts;
    private EmployeeItemAdapter employeeItemAdapter;
    private String custCode="";
    private String custName="";
    private String ordNo="";
    private String prodClsCode="";
    private String prodName="";
    private String suiteGrpId="";
    private String suiteGrpName="";
    private String prodCatId="";
    private String curSearchContent = "";
    private ProdCls prodCls=new ProdCls();
    //private Employee employee=new Employee();
    private AlertDialog mDialog;
    private String ltStatus="A";//量体状态:F 未量体，T 已量体，A 全部
    private String currentDept="";
    private String type="";
    public static final int REQUEST_PRODUCT = 5;
    private static int LOAD_EMPLOYEE_ACTION=3;
    public static final int REQUEST_PRODUCT_MEASURE = 4;
    private ImageButton employee_search;
    private TextView employee_search_dept;
    private EditText emp_search_editer;
    private Button emp_search_btn;
    private LinearLayout foot_progressLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        Intent intent = getIntent();
        custCode=intent.getStringExtra("custCode");
        custName=intent.getStringExtra("custName");
        type=intent.getStringExtra("type");
        ordNo=intent.getStringExtra("ordNo");
        prodClsCode=intent.getStringExtra("prodClsCode");
        prodName=intent.getStringExtra("prodName");
        suiteGrpId=intent.getStringExtra("suiteGrpId");
        suiteGrpName=intent.getStringExtra("suiteGrpName");
        prodCatId=intent.getStringExtra("prodCatId");
        prodCls=(ProdCls) intent.getSerializableExtra("prodCls");
        foot_progressLinearLayout=(LinearLayout)findViewById(R.id.foot_progressLinearLayout);
        if(custName.length()>0){
            setTitle(custName);
        }
        this.initData();
        this.initView();

    }
    protected void onResume() {
        super.onResume();
        loadEmployeeData(mSearchHandler);
    }
    private void initView(){
        employee_search_dept=(TextView)findViewById(R.id.employee_search_dept);
        employee_search=(ImageButton)findViewById(R.id.employee_search);
        employee_search.setOnClickListener(new SearchButtonClick());
        emp_search_editer=(EditText)findViewById(R.id.emp_search_editer);
        emp_search_editer.setOnKeyListener(new SearchEditorKeyPress());
        emp_search_btn=(Button)findViewById(R.id.emp_search_btn);
        emp_search_btn.setOnClickListener(new EmpSearchBtnClick());
        unLtEmpButton=(Button)findViewById(R.id.unLtEmpButton);
        ltedEmpButton=(Button)findViewById(R.id.ltedEmpButton);
        allButton=(Button)findViewById(R.id.allButton);
        unLtEmpButton.setOnClickListener(new UnLtEmpButtonnClick());
        ltedEmpButton.setOnClickListener(new LtedEmpButtonClick());
        allButton.setOnClickListener(new AllButtonClick());
        unLtEmpButton.setEnabled(true);
        ltedEmpButton.setEnabled(true);
        allButton.setEnabled(false);
        employee_gridView=(GridView) findViewById(R.id.employee_gridView);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) EmployeeActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        if(metrics.widthPixels<800){
            employee_gridView.setNumColumns(4);
        }else{
            employee_gridView.setNumColumns(5);
        }

        //employeeList= EmployeeService.getEmployeeData("");
        loadEmployeeData(mSearchHandler);
        employeeItemAdapter = new EmployeeItemAdapter(employeeList, EmployeeActivity.this);
        employee_gridView.setAdapter(employeeItemAdapter);
        employee_gridView.setOnItemClickListener(new EmployeeGridViewItemClick());
    }
    private class EmployeeGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Employee employee=(Employee) adapterView.getItemAtPosition(i);
            employee.setSelected(true);
            for(int j=0;j<employeeList.size();j++){
                if(j!=i){
                    employeeList.get(j).setSelected(false);
                }
            }
            employeeItemAdapter.notifyDataSetChanged();
            if(prodCls==null) {
                if(type.equals("LT")){
                    Intent intent = new Intent(view.getContext(), SuiteMeasureActivity.class);
                    intent.putExtra("employee", employee);
                    startActivityForResult(intent, REQUEST_PRODUCT);
                }else{
                    Intent intent = new Intent(view.getContext(), ProductActivity.class);
                    intent.putExtra("custCode", custCode);
                    intent.putExtra("custName", custName);
                    intent.putExtra("pckNo", employee.getPckNo());
                    intent.putExtra("cname", employee.getCname());
                    intent.putExtra("dept", employee.getDept());
                    intent.putExtra("employee", employee);
                    startActivityForResult(intent, REQUEST_PRODUCT);
                }
            }else{
                Intent intent = new Intent(view.getContext(), ProductMeasureListActivity.class);

                intent.putExtra("ordNo", ordNo);
                intent.putExtra("prodClsCode", prodClsCode);
                intent.putExtra("prodName", prodName);
                intent.putExtra("suiteGrpId", suiteGrpId);
                intent.putExtra("suiteGrpName", suiteGrpName);
                intent.putExtra("prodCatId", prodCatId);
                intent.putExtra("prodCls", prodCls);
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);

                intent.putExtra("pckNo", employee.getPckNo());
                intent.putExtra("cname", employee.getCname());
                intent.putExtra("dept", employee.getDept());
                intent.putExtra("employee", employee);

                ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_PRODUCT_MEASURE);
            }
        }
    }
    private class UnLtEmpButtonnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            unLtEmpButton.setEnabled(false);
            ltedEmpButton.setEnabled(true);
            allButton.setEnabled(true);
            ltStatus="F";
            curSearchContent="";
            emp_search_editer.setText("");
            loadEmployeeData(mSearchHandler);
        }
    }
    private class LtedEmpButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            unLtEmpButton.setEnabled(true);
            ltedEmpButton.setEnabled(false);
            allButton.setEnabled(true);
            ltStatus="T";
            curSearchContent="";
            emp_search_editer.setText("");
            loadEmployeeData(mSearchHandler);

        }
    }
    private class AllButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            unLtEmpButton.setEnabled(true);
            ltedEmpButton.setEnabled(true);
            allButton.setEnabled(false);
            ltStatus="A";
            curSearchContent="";
            emp_search_editer.setText("");
            loadEmployeeData(mSearchHandler);
        }
    }
    private class SearchButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            ArrayAdapter<String> _adapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.select_dialog_singlechoice, depts);
            ListView listView = new ListView(view.getContext());//this为获取当前的上下文
            listView.setAdapter(_adapter);

            mDialog = new AlertDialog.Builder(view.getContext()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            }).create();
            mDialog.setTitle("选择客户部门");

            mDialog.setView(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> pParent, View pView,
                                        int pPosition, long pId) {
                    employee_search_dept.setText(depts[pPosition]);
                    if(depts[pPosition].equals("所有顾客")){
                        currentDept="";
                    }else{
                        currentDept=depts[pPosition];
                    }
                    curSearchContent="";
                    emp_search_editer.setText("");
                    loadEmployeeData(mSearchHandler);
                    //specGrp=specGrps.get(pPosition);
                    //selectSpecGrpTextView.setText(specGrp.getGrpName());
                    //loadSpecData(mSearchHandler);
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        }
    }
    private class EmpSearchBtnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            emp_search_editer.clearFocus();
            curSearchContent=emp_search_editer.getText().toString();
            loadEmployeeData(mSearchHandler);
        }
    }
    private class SearchEditorKeyPress implements View.OnKeyListener{
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                if(view.getTag() == null) {
                    view.setTag(1);
                    emp_search_editer.clearFocus();
                    curSearchContent=emp_search_editer.getText().toString();
                    loadEmployeeData(mSearchHandler);
                }else{
                    view.setTag(null);
                }
                return true;
            }
            return false;

        }
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==LOAD_EMPLOYEE_ACTION){
                    if(msg.what>=0){
                        ArrayList<Employee> list = (ArrayList<Employee>)msg.obj;
                        String gender="";
                        if(prodCls!=null){
                            gender=prodCls.getGender();
                        }
                        if(gender==null){
                            gender="";
                        }
                        if(gender.length()>0){
                            employeeList.clear();
                            for(int i=0;i<list.size();i++){
                                if(list.get(i).getGender().equals(gender)){
                                    employeeList.add(list.get(i));
                                }
                            }
                        }else{
                            employeeList.clear();
                            employeeList.addAll(list);
                        }

                        employeeItemAdapter.notifyDataSetChanged();
                    }
                    foot_progressLinearLayout.setVisibility(View.GONE);
                }


            }
        };
    }
    private void loadEmployeeData(final Handler handler){
        foot_progressLinearLayout.setVisibility(View.VISIBLE);
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<Employee> list= EmployeeService.getEmployeeData(custCode,ltStatus,currentDept,curSearchContent,EmployeeActivity.this);
                    if(ltStatus=="A" && currentDept==""){
                        ArrayList<String > lst=EmployeeService.getDeptList(list);

                        depts=new String[lst.size()+1];
                        for(int i=0;i<lst.size();i++){
                            depts[i]=lst.get(i);
                        }
                        depts[lst.size()]="所有顾客";
                    }
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = LOAD_EMPLOYEE_ACTION;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
    private void loadDept(final Handler handler){

    }
}
