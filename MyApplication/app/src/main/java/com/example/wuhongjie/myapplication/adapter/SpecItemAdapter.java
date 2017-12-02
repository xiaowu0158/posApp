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
import com.example.wuhongjie.myapplication.type.SpecGrp;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class SpecItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Spec> list;
    private Context context;
    private boolean specNumFocus=false;
    public final class ViewHolder {
        public TextView specItem;
        public TextView specDesc;
        public LinearLayout layout;
    }
    public SpecItemAdapter(ArrayList<Spec> list,boolean specNumFocus, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.list=list;
        this.specNumFocus=specNumFocus;
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
            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.spec_item_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(specNumFocus){
            holder.specItem.setText(list.get(i).getSpecCode());
            holder.specDesc.setText("规格:"+list.get(i).getSpec()+";号型:"+list.get(i).getShape()+";"+
                    list.get(i).getPattern());
        }else{
            holder.specItem.setText(list.get(i).getSpec());
            holder.specDesc.setText("编号:"+list.get(i).getSpecCode()+";号型:"+list.get(i).getShape()+";"+
                    list.get(i).getPattern());
        }

        if(list.get(i).isSelected()){
            holder.layout.setBackgroundResource(R.xml.shape2);
        }else {
            holder.layout.setBackgroundResource(R.xml.shape1);
        }
        return convertView;
    }

}
