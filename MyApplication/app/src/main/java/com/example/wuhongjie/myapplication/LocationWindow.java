package com.example.wuhongjie.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.adapter.LocationItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SellerItemAdapter;

public class LocationWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private static int LOCATION_POST = 1905;
    private Button btn_submit;
    private ImageView btn_close;
    private Button btn_back;
    private GridView locGridView;
    private  Context context=null;
    private Handler handler;
    private PopupWindow popupWindow=null;
    private LocationItemAdapter locationItemAdapter;
    private RecordSet locationRs;
    private Record locationRc=null;
    public LocationWindow(){
    }
    public LocationWindow(Context context, Handler handler, RecordSet locationRs, int width, int height) {
        super();
        this.context=context;
        this.handler=handler;
        this.locationRs=locationRs;
        this.locationRc=null;
        View view= LayoutInflater.from(context).inflate(R.layout.window_location, null);

        locGridView=(GridView)view.findViewById(R.id.locGridView);
        locationItemAdapter = new LocationItemAdapter(this.locationRs, this.context);
        locGridView.setAdapter(locationItemAdapter);
        locGridView.setOnItemClickListener(new SellerGridViewItemClick());
        btn_close=(ImageView) view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new PopupCloseClick());
        btn_back=(Button) view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new PopupCloseClick());
        btn_submit=(Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new SubmitButtonClick());
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
    private class SellerGridViewItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            locationRs.getRecord(i).getField("ACTIVE").setString("T");
            locationRs.getRecord(i).post();
            locationRc=(Record)locationRs.getRecord(i).clone();
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
    private class PopupCloseClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    }
    private class SubmitButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(locationRc==null){
                for(int i=0;i<locationRs.recordCount();i++){
                    if(locationRs.getRecord(i).getField("ACTIVE").getString().equals("T")){
                        locationRc=(Record)locationRs.getRecord(i).clone();
                    }
                }
            }
            Message msg = new Message();
            msg.arg1 = LOCATION_POST;
            msg.what=0;
            msg.obj=locationRc;
            handler.sendMessage(msg);
            popupWindow.dismiss();
        }
    }
}
