package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.wuhongjie.myapplication.adapter.ProdClsItemAdapter;
import com.example.wuhongjie.myapplication.service.EmployeeService;
import com.example.wuhongjie.myapplication.service.ProdClsService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductActivityFragment extends Fragment {
    private Activity activity;
    private GridView productListView;
    private ArrayList<ProdCls> prodClsData=new ArrayList<ProdCls>();
    private ProdClsItemAdapter lvSearchAdapter;
    private Handler mSearchHandler;
    private String custCode="";
    private String custName="";
    private String pckNo="";
    private static int LOAD_ORDERITM_ACTION=5;
    public ProductActivityFragment() {
        System.out.println("s11sss");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity=this.getActivity();
        Intent intent = this.getActivity().getIntent();

        this.initData();
        custCode=intent.getStringExtra("custCode");
        custName=intent.getStringExtra("custName");
        pckNo=intent.getStringExtra("pckNo");
        productListView=(GridView) getView().findViewById(R.id.product_listview);
        Configuration mConfiguration = this.getResources().getConfiguration();
        int ori = mConfiguration.orientation ; //获取屏幕方向
        if(ori == mConfiguration.ORIENTATION_LANDSCAPE){
            productListView.setNumColumns(2);
        }else{
            productListView.setNumColumns(1);
        }

        lvSearchAdapter = new ProdClsItemAdapter(prodClsData, this.getActivity(),
                (Employee)intent.getSerializableExtra("employee"),custCode,custName);
        productListView.setAdapter(lvSearchAdapter);
        //prodClsData= ProdClsService.getProdCls();
        loadOrderItemData(mSearchHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.product_list,container,false);

        return rootView;

    }
    public void onResume() {
        super.onResume();
        loadOrderItemData(mSearchHandler);
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==LOAD_ORDERITM_ACTION){
                    if(msg.what>0){
                        ArrayList<ProdCls> list = (ArrayList<ProdCls>)msg.obj;
                        prodClsData.clear();
                        prodClsData.addAll(list);
                        lvSearchAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
    }
    private void loadOrderItemData(final Handler handler){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
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
}
