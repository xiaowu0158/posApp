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
import android.widget.PopupWindow;

import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.adapter.AvlTppItemAdapter;
import com.example.wuhongjie.myapplication.adapter.SellerItemAdapter;


public class SellerWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private GridView sellerGridView;
    private  Context context=null;
    private Handler handler;
    private PopupWindow popupWindow=null;
    private SellerItemAdapter sellerItemAdapter;
    private RecordSet sellerRs;
    public SellerWindow(){
    }
    public SellerWindow(Context context, Handler handler, RecordSet sellerRs, int width, int height) {
        super();
        this.context=context;
        this.handler=handler;
        this.sellerRs=sellerRs;
        View view= LayoutInflater.from(context).inflate(R.layout.window_seller, null);
        sellerGridView=(GridView)view.findViewById(R.id.sellerListView);
        sellerItemAdapter = new SellerItemAdapter(this.sellerRs, this.context);
        sellerGridView.setAdapter(sellerItemAdapter);
        sellerGridView.setOnItemClickListener(new SellerGridViewItemClick());
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
            sellerRs.getRecord(i).getField("ACTIVE").setString("T");
            sellerRs.getRecord(i).post();
            for(int j=0;j<sellerRs.recordCount();j++){
                if(j!=i){
                    sellerRs.getRecord(j).getField("ACTIVE").setString("F");
                    sellerRs.getRecord(j).post();
                }

            }
            sellerItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDismiss() {

    }
}
