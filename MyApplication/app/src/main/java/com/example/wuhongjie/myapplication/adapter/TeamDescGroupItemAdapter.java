package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.service.TeamService;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamDesc;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class TeamDescGroupItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Team> list;
    private Context context;
    private DisplayMetrics metrics;
    public final class ViewHolder {
        public GridView popUpTeam_detailGridView;
        public LinearLayout layout;
        public TextView teamDesc;
        public ArrayList<TeamDesc> teamDesclist;
        public TeamDescItemAdapter teamDescItemAdapter;
    }
    public TeamDescGroupItemAdapter(ArrayList<Team> list, Context context,DisplayMetrics metrics ) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.list=list;
        this.metrics=metrics;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        int cnt=6;
        if(metrics.widthPixels<800){
            cnt=6;
        }else{
            cnt=8;
        }
        if(convertView==null){
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.team_popupwindow_item, null);
            holder.popUpTeam_detailGridView= (GridView) convertView
                    .findViewById(R.id.popUpTeam_detailGridView);
            if(metrics.widthPixels<800){
                holder.popUpTeam_detailGridView.setNumColumns(6);
            }else{
                holder.popUpTeam_detailGridView.setNumColumns(8);
            }

            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.popUpTeam_LineLayout);
            holder.teamDesc=(TextView) convertView
                    .findViewById(R.id.popUpTeam_TeamDesc);
            holder.teamDesclist=new ArrayList<TeamDesc>();
            holder.teamDescItemAdapter=new TeamDescItemAdapter(holder.teamDesclist, context);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.teamDesclist.clear();
        holder.teamDesclist.addAll(TeamService.getTeamValueData(list.get(i),cnt));
        holder.teamDescItemAdapter = new TeamDescItemAdapter(holder.teamDesclist, context);
        holder.popUpTeam_detailGridView.setAdapter(holder.teamDescItemAdapter);
        holder.teamDesc.setText(list.get(i).getTermName());
        holder.popUpTeam_detailGridView.setOnItemClickListener(new TeamDetailGridViewItemClick(holder.teamDesclist,i));
        //setListViewHeightBasedOnChildren(holder.teamGroupItemList);
        setGridViewHeightBasedOnChildren(holder.popUpTeam_detailGridView,metrics);
        return convertView;
    }
    private class TeamDetailGridViewItemClick implements AdapterView.OnItemClickListener{
        private ArrayList<TeamDesc> teamDescs;
        private int indx;
        public TeamDetailGridViewItemClick(ArrayList<TeamDesc> teamDescs,int indx) {
            this.teamDescs=teamDescs;
            this.indx=indx;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TeamDesc teamDesc=(TeamDesc) adapterView.getItemAtPosition(i);
            teamDesc.setSelected(!teamDesc.isSelected());
            for(int j=0;j<teamDescs.size();j++){
                if(j!=i){
                    teamDescs.get(j).setSelected(false);
                }
            }
            if(teamDesc.isSelected()){
                list.get(indx).setCurrValue(teamDesc.getTeamValue());
                list.get(indx).setDescription(teamDesc.getDescription());
            }else{
                list.get(indx).setCurrValue("");
                list.get(indx).setDescription("");
            }

            ((TeamDescItemAdapter)adapterView.getAdapter()).notifyDataSetChanged();



        }
    }
    public static void setGridViewHeightBasedOnChildren(GridView gridView,DisplayMetrics metrics) {
        // 获取listview的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列

        int col = gridView.getNumColumns();
        int totalHeight = 0;

        /*for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }*/
        if(listAdapter.getCount()>0){
            View listItem = listAdapter.getView(0, null, gridView);
            if(metrics.widthPixels<800){
                if(listAdapter.getCount()<=6){
                    totalHeight= 100;
                }else if(listAdapter.getCount()<=12){
                    totalHeight= 100*2;
                }else if(listAdapter.getCount()<=18){
                    totalHeight= 100*3;
                }else if(listAdapter.getCount()<=24){
                    totalHeight= 100*4;
                }else if(listAdapter.getCount()<=30){
                    totalHeight= 100*5;
                }else if(listAdapter.getCount()<=36){
                    totalHeight= 100*6;
                }
            }else{
                if(listAdapter.getCount()<=8){
                    totalHeight= 80;
                }else if(listAdapter.getCount()<=16){
                    totalHeight= 80*2;
                }else if(listAdapter.getCount()<=24){
                    totalHeight= 80*3;
                }else if(listAdapter.getCount()<=32){
                    totalHeight= 80*4;
                }else if(listAdapter.getCount()<=40){
                    totalHeight= 80*5;
                }
            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            // 设置高度
            params.height = totalHeight;

            gridView.setLayoutParams(params);
        }

        // 获取listview的布局参数

    }
}
