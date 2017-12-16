package com.example.wuhongjie.myapplication;
/**
 * cesh
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.adapter.AvlTppItemAdapter;


public class AvlTppWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private ListView tppListView;
    private  Context context=null;
    private Handler handler;
    private PopupWindow popupWindow=null;
    private AvlTppItemAdapter avlTppItemAdapter;
    public AvlTppWindow(){
    }
    public AvlTppWindow(Context context, Handler handler, RecordSet policyStructureRs,RecordSet avlTppRs, int width, int height) {
        super();
        this.context=context;
        this.handler=handler;
        View view= LayoutInflater.from(context).inflate(R.layout.activity_avl_tpp, null);
        tppListView=(ListView)view.findViewById(R.id.tppListView);
        avlTppItemAdapter = new AvlTppItemAdapter(avlTppRs,policyStructureRs, this.context);
        tppListView.setAdapter(avlTppItemAdapter);
        popupWindow=new PopupWindow(view, width, height);
        popupWindow.setOutsideTouchable(false);
        //设置popwindow的动画效果
      //  popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

    }
    public void showAsDropDown(View parent){
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDismiss() {

    }
}
