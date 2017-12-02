package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.Cust;
import com.example.wuhongjie.myapplication.type.Yxgs;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class CustItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Cust> list;
    private Context context;
    public final class ViewHolder {
        public TextView custCode;
        public TextView custName;
        public TextView areaCode;
    }
    public CustItemAdapter(ArrayList<Cust> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.cust_searchitem, null);
            holder.custCode= (TextView) convertView
                    .findViewById(R.id.searchitem_cust_code);
            holder.custName=(TextView) convertView
                    .findViewById(R.id.searchitem_cust_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.custCode.setText(list.get(i).getCustCode());
        holder.custName.setText(list.get(i).getCustName());

        return convertView;
    }

}
