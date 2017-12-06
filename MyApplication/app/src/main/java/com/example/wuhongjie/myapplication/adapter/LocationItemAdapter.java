package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.Area;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class LocationItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private RecordSet list;
    private Context context;
    public final class ViewHolder {
        public TextView title;
        public LinearLayout layout;
    }
    public LocationItemAdapter(RecordSet list, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.recordCount();
    }

    @Override
    public Object getItem(int i) {
        return list.getRecord(i);
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
            convertView = mInflater.inflate(R.layout.setting_grid_view_item, null);
            holder.title= (TextView) convertView
                    .findViewById(R.id.yxgs_item);
            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.yxgs_item_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String locName="";
        try{
            locName=new String(list.getRecord(i).getField("LOC_NAME").getBytes(),"GBK");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.title.setText(locName);
        if(list.getRecord(i).getField("ACTIVE").getString().equals("T")){
            holder.layout.setBackgroundResource(R.xml.shape2);
        }else {
            holder.layout.setBackgroundResource(R.xml.shape1);
        }
        return convertView;
    }

}
