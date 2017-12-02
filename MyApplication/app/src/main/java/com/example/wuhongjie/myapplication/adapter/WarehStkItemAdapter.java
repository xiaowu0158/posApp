package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.R;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class WarehStkItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private RecordSet wareStkRs;
    private Context context;
    public final class ViewHolder {
        public TextView prodNameTextView;

        public TextView intlBcTextView;
        public TextView prodCodeTextView;
        public TextView brandNameTextView;
        public TextView prodCatNameTextView;
        public TextView parnCatNameTextView;
        public TextView specNameTextView;
        public TextView specCodeTextView;
        public TextView lstPriceTextView;
        public TextView qtyExpdTextView;
        public TextView qtyCmtdTextView;

    }
    public WarehStkItemAdapter(RecordSet wareStkRs, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.wareStkRs=wareStkRs;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return wareStkRs.recordCount();
    }

    @Override
    public Object getItem(int i) {
        return wareStkRs.getRecord(i);
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
            convertView = mInflater.inflate(R.layout.item_prodstk, null);
            holder.prodNameTextView= (TextView) convertView
                    .findViewById(R.id.prodNameTextView);
            holder.prodCodeTextView=(TextView) convertView
                    .findViewById(R.id.prodCodeTextView);
            holder.intlBcTextView=(TextView) convertView
                    .findViewById(R.id.intlBcTextView);
            holder.brandNameTextView=(TextView) convertView
                    .findViewById(R.id.brandNameTextView);
            holder.prodCatNameTextView=(TextView) convertView
                    .findViewById(R.id.prodCatNameTextView);
            holder.parnCatNameTextView=(TextView) convertView
                    .findViewById(R.id.parnCatNameTextView);
            holder.specNameTextView=(TextView) convertView
                    .findViewById(R.id.specNameTextView);
            holder.specCodeTextView=(TextView) convertView
                    .findViewById(R.id.specCodeTextView);
            holder.lstPriceTextView=(TextView) convertView
                    .findViewById(R.id.lstPriceTextView);
            holder.qtyExpdTextView=(TextView) convertView
                    .findViewById(R.id.qtyExpdTextView);
            holder.qtyCmtdTextView=(TextView) convertView
                    .findViewById(R.id.qtyCmtdTextView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String prodName="";
        String prodCode="";
        String brandName="";
        String prodCatName="";
        String parnCatName="";
        String specCode="";
        String specName="";
        try{
            prodName=new String(wareStkRs.getRecord(i).getField("PROD_NAME").getBytes(),"GBK");
            prodCode=new String(wareStkRs.getRecord(i).getField("PROD_CODE").getBytes(),"GBK");
            brandName=new String(wareStkRs.getRecord(i).getField("BRAND_NAME").getBytes(),"GBK");
            prodCatName=new String(wareStkRs.getRecord(i).getField("PROD_CAT_NAME").getBytes(),"GBK");
            parnCatName=new String(wareStkRs.getRecord(i).getField("PARN_CAT_NAME").getBytes(),"GBK");
            specName=new String(wareStkRs.getRecord(i).getField("SPEC_NAME").getBytes(),"GBK");
            specCode=new String(wareStkRs.getRecord(i).getField("SPEC_CODE").getBytes(),"GBK");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.prodNameTextView.setText(prodName);
        holder.prodCodeTextView.setText("代码："+prodCode);
        holder.brandNameTextView.setText(brandName);
        holder.prodCatNameTextView.setText(prodCatName);
        holder.parnCatNameTextView.setText("大类："+parnCatName);
        holder.specNameTextView.setText(specName);
        holder.specCodeTextView.setText("规格代码："+specCode);
        holder.intlBcTextView.setText("条码："+wareStkRs.getRecord(i).getField("INTL_BC").getString());
        holder.lstPriceTextView.setText(wareStkRs.getRecord(i).getField("LST_PRICE").getNumber().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        holder.qtyExpdTextView.setText(wareStkRs.getRecord(i).getField("QTY_EXPD").getNumber().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        holder.qtyCmtdTextView.setText("已配："+wareStkRs.getRecord(i).getField("QTY_CMTD").getNumber().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        return convertView;
    }

}
