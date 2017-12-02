package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.TeamDesc;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class TeamDescItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<TeamDesc> list;
    private Context context;
    public final class ViewHolder {
        public TextView specItem;
        public TextView specDesc;
        public LinearLayout layout;
    }
    public TeamDescItemAdapter(ArrayList<TeamDesc> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.spec_item, null);
            holder.specItem= (TextView) convertView
                    .findViewById(R.id.spec_item);
            holder.specDesc=(TextView) convertView
                    .findViewById(R.id.spec_desc);
            holder.specDesc.setVisibility(View.GONE);
            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.spec_item_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(list.get(i)!=null&&list.get(i).getTeamValue()!=null){
            holder.specItem.setText(list.get(i).getTeamValue());
            holder.specDesc.setText(list.get(i).getDescription());
            if(list.get(i).isSelected()){
                holder.layout.setBackgroundResource(R.xml.shape2);
                if(list.get(i).getTeamValue().indexOf("+")>=0){
                    holder.layout.setBackgroundResource(R.xml.shape22);
                }
            }else {
                holder.layout.setBackgroundResource(R.xml.shape1);
                if(list.get(i).getTeamValue().indexOf("+")>=0){
                    holder.layout.setBackgroundResource(R.xml.shape11);
                }
            }
        }else{
            holder.layout.setVisibility(View.GONE);
        }

        return convertView;
    }

}
