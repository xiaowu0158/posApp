package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.R;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by  on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class VipDtlItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private RecordSet vipDtlRs;
    private Context context;
    public final class ViewHolder {
        public TextView custCodeTextView;
        public TextView custNameTextView;
        public TextView birthdayTextView;
        public TextView cardNumTextView;
        public TextView custGrdTextView;
        public TextView mobileNumTextView;
        public TextView pntAvlTextView;

    }
    public VipDtlItemAdapter(RecordSet vipDtlRs, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.vipDtlRs=vipDtlRs;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return vipDtlRs.recordCount();
    }

    @Override
    public Object getItem(int i) {
        return vipDtlRs.getRecord(i);
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
            convertView = mInflater.inflate(R.layout.item_vipcust, null);
            holder.custCodeTextView= (TextView) convertView
                    .findViewById(R.id.custCodeTextView);
            holder.custNameTextView=(TextView) convertView
                    .findViewById(R.id.custNameTextView);
            holder.birthdayTextView=(TextView) convertView
                    .findViewById(R.id.birthdayTextView);
            holder.cardNumTextView=(TextView) convertView
                    .findViewById(R.id.cardNumTextView);
            holder.custGrdTextView=(TextView) convertView
                    .findViewById(R.id.custGrdTextView);
            holder.mobileNumTextView=(TextView) convertView
                    .findViewById(R.id.mobileNumTextView);
            holder.pntAvlTextView=(TextView) convertView
                    .findViewById(R.id.pntAvlTextView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String custCode="";
        String custName="";
        String cardNum="";
        String custGrd="";
        String birthday="";
        String mobileNum="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            custCode=new String(vipDtlRs.getRecord(i).getField("PRSNL_CODE").getBytes(),"GBK");
            custName=new String(vipDtlRs.getRecord(i).getField("FULL_NAME").getBytes(),"GBK");
            cardNum=new String(vipDtlRs.getRecord(i).getField("CARD_NUM").getBytes(),"GBK");
            custGrd=new String(vipDtlRs.getRecord(i).getField("CUST_GRD_NAME").getBytes(),"GBK");
            mobileNum=new String(vipDtlRs.getRecord(i).getField("MOBILE_NUM").getBytes(),"GBK");
            birthday=sdf.format(vipDtlRs.getRecord(i).getField("BIRTHDAY").getDate());


        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.custCodeTextView.setText(custCode);
        holder.custNameTextView.setText(custName);
        holder.cardNumTextView.setText(cardNum);
        holder.custGrdTextView.setText(custGrd);
        holder.birthdayTextView.setText(birthday);
        holder.mobileNumTextView.setText(mobileNum);
        holder.pntAvlTextView.setText(vipDtlRs.getRecord(i).getField("PNT_AVL").getNumber().toString());
        return convertView;
    }

}
