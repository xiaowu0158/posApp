package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.Area;
import com.example.wuhongjie.myapplication.type.ProdCat;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class ProdCatItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<ProdCat> list;
    private Context context;
    public final class ViewHolder {
        public TextView prodCatNameBtn;
        public LinearLayout layout;
    }
    public ProdCatItemAdapter(ArrayList<ProdCat> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.item_prodcat, null);
            holder.prodCatNameBtn= (TextView) convertView
                    .findViewById(R.id.prodCatNameButton);
            //holder.layout=(LinearLayout) convertView
            //        .findViewById(R.id.yxgs_item_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.prodCatNameBtn.setText(list.get(i).getProdCatName());
        holder.prodCatNameBtn.setEnabled(!list.get(i).isSelected());

        return convertView;
    }

}
