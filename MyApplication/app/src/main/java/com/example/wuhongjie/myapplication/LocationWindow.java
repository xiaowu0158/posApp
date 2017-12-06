package com.example.wuhongjie.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.adapter.AvlTppItemAdapter;
import com.example.wuhongjie.myapplication.adapter.LocationItemAdapter;


public class LocationWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private GridView locGridView;
    private  Context context=null;
    private Handler handler;
    private PopupWindow popupWindow=null;
    private LocationItemAdapter locationItemAdapter;
    private RecordSet locationRs;
    public LocationWindow(){
    }
    public LocationWindow(Context context, Handler handler, RecordSet locationRs, int width, int height) {
        super();
        this.context=context;
        this.handler=handler;
        this.locationRs=locationRs;
        View view= LayoutInflater.from(context).inflate(R.layout.window_location, null);
        locGridView=(GridView)view.findViewById(R.id.locListView);
        locationItemAdapter = new LocationItemAdapter(this.locationRs,this.context);
        locGridView.setAdapter(locationItemAdapter);
        locGridView.setOnItemClickListener(new LocGridViewItemClick());
        popupWindow=new PopupWindow(view, width, height);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

    }
    public void showAsDropDown(View parent){
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }
    private class LocGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            locationRs.getRecord(i).getField("ACTIVE").setString("T");
            locationRs.getRecord(i).post();
            for(int j=0;j<locationRs.recordCount();j++){
                if(j!=i){
                    locationRs.getRecord(j).getField("ACTIVE").setString("F");
                    locationRs.getRecord(j).post();
                }

            }
            locationItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDismiss() {

    }
}
