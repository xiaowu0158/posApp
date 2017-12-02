package com.example.wuhongjie.myapplication;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.wuhongjie.myapplication.adapter.SpecItemAdapter;
import com.example.wuhongjie.myapplication.adapter.TeamDescGroupItemAdapter;
import com.example.wuhongjie.myapplication.adapter.TeamDescItemAdapter;
import com.example.wuhongjie.myapplication.service.TeamService;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamDesc;
import com.example.wuhongjie.myapplication.type.TeamGroup;

import java.util.ArrayList;

import static com.example.wuhongjie.myapplication.R.id.popUpMesure_detailGridView;


/**
 * Created by wuhongjie on 2017-03-20.
 */

public class TeamPopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    //private final GridView popUpTeam_detailGridView;
    private final PopupWindow popupWindow;
    private final ImageView team_popupdel;
    private final ListView team_popupListView;
    private final TeamDescGroupItemAdapter teamDescGroupItemAdapter;
    private final Button teamPostButton;
    private final Button teamReturnButton;
    private final DisplayMetrics metrics;
    private Context context;
    private Handler handler;
    private String teamGroupId;
    private ArrayList<Team> teams=new ArrayList<Team>();
    private static int LOAD_CURR_TEAMVALUE_ACTION=9;
    private static int PROGRESS_BAR_VISIBLE_ACTION=200;
    public TeamPopupWindow(Context context, ArrayList<Team> teams,String teamGroupId, Handler handler) {
        this.context=context;
        this.handler=handler;
        this.teamGroupId=teamGroupId;
        View view= LayoutInflater.from(context).inflate(R.layout.team_popupwindow, null);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        team_popupdel=(ImageView) view.findViewById(R.id.team_popupdel);
        team_popupdel.setOnClickListener(new PopupCloseClick());
        //popUpTeam_detailGridView=(GridView) view.findViewById(R.id.popUpTeam_detailGridView);
        //popUpTeam_detailGridView.setNumColumns(5);
       // teamDescs= TeamService.getTeamValueData(team);
       // teamDescItemAdapter=new TeamDescItemAdapter(teamDescs, context);
        //popUpTeam_detailGridView.setAdapter(teamDescItemAdapter);
        //popUpTeam_detailGridView.setOnItemClickListener(new TeamDetailGridViewItemClick());

        team_popupListView=(ListView) view.findViewById(R.id.team_popupListView);
        this.teams.addAll(teams);
        teamDescGroupItemAdapter=new TeamDescGroupItemAdapter(this.teams,context,metrics);
        team_popupListView.setAdapter(teamDescGroupItemAdapter);
        setListViewHeightBasedOnChildren(team_popupListView);
        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
        teamPostButton=(Button)view.findViewById(R.id.teamPostButton);
        teamPostButton.setOnClickListener(new TeamPostButtonClick());
        teamReturnButton=(Button)view.findViewById(R.id.teamReturnButton);
        teamReturnButton.setOnClickListener(new PopupCloseClick());
    }

    @Override
    public void onClick(View view) {
        System.out.println("dismidddddss");
    }

    @Override
    public void onDismiss() {

    }
    public void showAsDropDown(View parent){
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }
    private class PopupCloseClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    }
    private class TeamPostButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            for(int i=0;i<teams.size();i++){
                System.out.println(teams.get(i).getCurrValue()+teams.get(i).getDescription());
            }
            Message msg = new Message();
            msg.arg1 = PROGRESS_BAR_VISIBLE_ACTION;
            msg.what=0;
            msg.obj=view.VISIBLE;
            handler.sendMessage(msg);
            new Thread(){
                public void run(){
                    Message msg = new Message();
                    try{
                        msg.what=teams.size();
                        Object[] data=new Object[]{teams,teamGroupId};
                        msg.obj=data;

                    }catch (Exception e){
                        e.printStackTrace();
                        msg.what=-1;
                        msg.obj=e;
                    }
                    msg.arg1 = LOAD_CURR_TEAMVALUE_ACTION;//告知handler当前action
                    handler.sendMessage(msg);
                }
            }.start();
            popupWindow.dismiss();
        }
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
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight()- wm.getDefaultDisplay().getHeight()/5;
        if(metrics.widthPixels<800){
            height = wm.getDefaultDisplay().getHeight()- wm.getDefaultDisplay().getHeight()/4;
        }
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        System.out.println("窗口高度"+height+";"+params.height );
        if(params.height >height){
            params.height=height;
        }
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除


        listView.setLayoutParams(params);
    }
}
