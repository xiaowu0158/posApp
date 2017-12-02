package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.EmployeeActivity;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.SymboPopupWindow;
import com.example.wuhongjie.myapplication.TeamPopupWindow;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamGroup;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class TeamGroupItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<TeamGroup> list;
    private Context context;
    private Handler handler;
    private boolean bigFond=false;
    public final class ViewHolder {
        public ListView teamGroupItemList;
        public Button teamButton;
        //public TextView currentValue;
       // public TableRow team_rows;

    }
    public TeamGroupItemAdapter(ArrayList<TeamGroup> list, boolean bigFond,Context context, Handler handler) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.handler=handler;
        this.list=list;
        this.bigFond=bigFond;

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
        if(convertView==null){
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.teamgroup_item, null);
            //holder.team_rows= (TableRow) convertView
            //       .findViewById(R.id.team_rows);
            //ViewGroup.LayoutParams params = holder.team_rows.getLayoutParams();
            //params.height = 160;
            //holder.team_rows.setLayoutParams(params);
            holder.teamGroupItemList= (ListView) convertView
                    .findViewById(R.id.team_groupItemList);
            holder.teamButton= (Button) convertView
                    .findViewById(R.id.teamGroupButton);
            holder.teamButton.setOnClickListener(new TeamButtonClick(i));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
            holder.teamButton.setOnClickListener(new TeamButtonClick(i));
        }

        TeamGroupDetailItemAdapter teamGroupDetailItemAdapter = new TeamGroupDetailItemAdapter(list.get(i).getGroupId(),list.get(i).getTeams(),bigFond, context,handler);

        holder.teamGroupItemList.setAdapter(teamGroupDetailItemAdapter);
        setListViewHeightBasedOnChildren(holder.teamGroupItemList);
        return convertView;
    }
    private class TeamButtonClick implements View.OnClickListener{
        public int position;
        public TeamButtonClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            TeamPopupWindow teamPopupWindow = new TeamPopupWindow(context,list.get(position).getTeams(),list.get(position).getGroupId(),handler);
            teamPopupWindow.showAsDropDown(view);

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

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0); // 可删除


        listView.setLayoutParams(params);
    }
}
