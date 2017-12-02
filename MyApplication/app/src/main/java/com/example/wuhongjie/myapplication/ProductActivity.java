package com.example.wuhongjie.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.wuhongjie.myapplication.adapter.ProdClsItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SuiteGrpItemAdapter;
import com.example.wuhongjie.myapplication.service.ProdClsService;
import com.example.wuhongjie.myapplication.service.SuiteGrpService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private String custName="";
    private String cname="";
    private String dept="";
    private Employee employee=new Employee();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton returnButton = (FloatingActionButton)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        //custCode=intent.getStringExtra("custCode");
        custName=intent.getStringExtra("custName");
        //pckNo=intent.getStringExtra("pckNo");
        cname=intent.getStringExtra("cname");
        dept=intent.getStringExtra("dept");
        employee=(Employee)intent.getSerializableExtra("employee");
        if(dept!=null && dept.length()>0) {
            toolbar.setTitle(employee.getCname() + "[" + custName+"/"+dept + "]");
        }else{
            toolbar.setTitle((cname==null?"":cname)+ "[" + custName+ "]");
        }

    }

}
