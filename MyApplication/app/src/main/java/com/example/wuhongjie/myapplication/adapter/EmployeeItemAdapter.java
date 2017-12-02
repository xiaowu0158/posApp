package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.util.BadgeView;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class EmployeeItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Employee> list;
    private Context context;
    public final class ViewHolder {
        public TextView title;
        public TextView depart;
        public TextView corner;
        public LinearLayout layout;
        public BadgeView badge;
    }
    public EmployeeItemAdapter(ArrayList<Employee> list, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.list=list;
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
            convertView = mInflater.inflate(R.layout.employee_item, null);
            holder.title= (TextView) convertView
                    .findViewById(R.id.employee_item);
            holder.depart= (TextView) convertView
                    .findViewById(R.id.employee_dept);
            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.employee_item_line);
            holder.corner= (TextView) convertView
                    .findViewById(R.id.employee_item_corner);
            holder.badge = new BadgeView(context,  holder.corner);
            holder.badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String gender=list.get(i).getGender();
        if(gender!=null && gender.length()>0 && gender.equals("F")){
            gender="(女)";
        }else {
            gender="";
        }
        holder.title.setText(list.get(i).getCname()+gender);
        holder.depart.setText(list.get(i).getDept());
        if(list.get(i).isSelected()){
            holder.layout.setBackgroundResource(R.xml.shape2);
            if(list.get(i).isLted()){
                holder.layout.setBackgroundResource(R.xml.shape22);
            }
        }else {
            holder.layout.setBackgroundResource(R.xml.shape1);
            if(list.get(i).isLted()){
                holder.layout.setBackgroundResource(R.xml.shape11);
            }
        }
        if(list.get(i).getPrdCnt()>0) {
            holder.badge.setText(String.valueOf(list.get(i).getPrdCnt()));
            holder.badge.show();
        }else{
            holder.badge.setText("");
            holder.badge.hide();
        }
        return convertView;
    }

}
