package com.example.wuhongjie.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wuhongjie.myapplication.adapter.CustItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SuiteGrpItemAdapter;
import com.example.wuhongjie.myapplication.service.CustService;
import com.example.wuhongjie.myapplication.service.SuiteGrpService;
import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.type.SuiteGrp;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {

    private ListView searchListView;
    private SuiteGrpItemAdapter lvSearchAdapter;
    private ArrayList<SuiteGrp> suiteGrpData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        searchListView=(ListView)findViewById(R.id.searchView);
        suiteGrpData= SuiteGrpService.getSuiteGrp();
        lvSearchAdapter = new SuiteGrpItemAdapter(suiteGrpData, SearchItemActivity.this);
        searchListView.setAdapter(lvSearchAdapter);
        searchListView.setOnItemClickListener(new SearchItemActivity.SearchListViewItemClick());
    }
    private class SearchListViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            SuiteGrp suiteGrp=(SuiteGrp) adapterView.getItemAtPosition(i);
            Intent mIntent = SearchItemActivity.this.getIntent();

            mIntent.putExtra("suiteGrpId", suiteGrp.getSuiteGrpId());
            mIntent.putExtra("suiteGrpName", suiteGrp.getSuiteGrpName());

            SearchItemActivity.this.setResult(RESULT_OK, mIntent);
            SearchItemActivity.this.finish();
        }
    }
}
