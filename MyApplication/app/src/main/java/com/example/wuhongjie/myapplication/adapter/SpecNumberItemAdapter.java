package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.ShapeNum;
import com.example.wuhongjie.myapplication.type.SpecNumber;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class SpecNumberItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<SpecNumber> list;
    private Context context;
    public final class ViewHolder {
        public TextView title;
        public LinearLayout layout;
    }
    public SpecNumberItemAdapter(ArrayList<SpecNumber> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.setting_grid_view_item2, null);
            holder.title= (TextView) convertView
                    .findViewById(R.id.item);
            holder.layout=(LinearLayout) convertView
                    .findViewById(R.id.item_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(list.get(i).getValue());
        if(list.get(i).isSelected()){
            holder.layout.setBackgroundResource(R.xml.shape_border_red);
            holder.title.setTextColor(Color.RED);
        }else {
            holder.layout.setBackgroundResource(R.xml.shape_border);
            holder.title.setTextColor(Color.GRAY);
        }
        return convertView;
    }

}
