package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.SuiteGrp;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SpecGrpItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<SpecGrp> list;
    private Context context;
    public final class ViewHolder {
        public TextView specGrpName;
    }
    public SpecGrpItemAdapter(ArrayList<SpecGrp> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.searchitem_item, null);
            holder.specGrpName= (TextView) convertView
                    .findViewById(R.id.searchitem_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.specGrpName.setText(list.get(i).getGrpName());

        return convertView;
    }

}
