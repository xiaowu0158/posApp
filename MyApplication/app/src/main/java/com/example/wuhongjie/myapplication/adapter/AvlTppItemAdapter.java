package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.evangelsoft.econnect.dataformat.RecordSet;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.util.RetailPromotionPolicyUtil;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class AvlTppItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private RecordSet avlTppRs;
    private RecordSet policyRs;
    private Context context;
    public final class ViewHolder {
        public CheckedTextView tppActiveTextView;
        public TextView tppNameTextView;
        public TextView tppDescTextView;
        public TextView custScpTextView;
        public TextView fromDatetextView;
        public TextView toDateTextView;
    }
    public AvlTppItemAdapter(RecordSet avlTppRs,RecordSet policyRs, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.avlTppRs=avlTppRs;
        this.policyRs=policyRs;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return avlTppRs.recordCount();
    }

    @Override
    public Object getItem(int i) {
        return avlTppRs.getRecord(i);
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
            convertView = mInflater.inflate(R.layout.item_avltpp, null);
            holder.tppActiveTextView= (CheckedTextView) convertView
                    .findViewById(R.id.tppActiveTextView);
            holder.tppNameTextView=(TextView) convertView
                    .findViewById(R.id.tppNameTextView);
            holder.tppDescTextView=(TextView) convertView
                    .findViewById(R.id.tppDescTextView);
            holder.custScpTextView=(TextView) convertView
                    .findViewById(R.id.custScpTextView);
            holder.fromDatetextView=(TextView) convertView
                    .findViewById(R.id.fromDatetextView);
            holder.toDateTextView=(TextView) convertView
                    .findViewById(R.id.toDateTextView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String tppName="";
        String tppType="";
        String tppParam="";
        String tppDesc="";
        String custScpDesc="";
        String fromDate="";
        String toDate="";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            tppName=new String(avlTppRs.getRecord(i).getField("TPP_NAME").getBytes(),"GBK");
            tppType=new String(avlTppRs.getRecord(i).getField("TPP_TYPE").getBytes(),"GBK");
            tppParam=new String(avlTppRs.getRecord(i).getField("TPP_PARM").getBytes(),"GBK");
            tppDesc= (new RetailPromotionPolicyUtil(policyRs)).describePolicy(tppType,tppParam,20);
            String custScp=new String(avlTppRs.getRecord(i).getField("CUST_SCP").getBytes(),"GBK");
            if(custScp.equals("V")){
                custScpDesc="VIP";
            }
            if(custScp.equals("B")){
                custScpDesc="全部";
            }
            if(custScp.equals("C")){
                custScpDesc="普通";
            }
            fromDate=sdf.format(avlTppRs.getRecord(i).getField("FROM_DATE").getDate());
            toDate=sdf.format(avlTppRs.getRecord(i).getField("TO_DATE").getDate());
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.tppActiveTextView.setChecked(true);
        holder.tppNameTextView.setText(tppName);
        holder.tppDescTextView.setText(tppDesc);
        holder.custScpTextView.setText(custScpDesc);
        holder.fromDatetextView.setText(fromDate);
        holder.toDateTextView.setText(toDate);
        return convertView;
    }

}
