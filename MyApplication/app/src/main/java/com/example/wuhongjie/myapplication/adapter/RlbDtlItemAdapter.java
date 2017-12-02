package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evangelsoft.econnect.client.ClientSession;
import com.evangelsoft.econnect.client.DxSessionHelper;
import com.evangelsoft.econnect.dataformat.Record;
import com.evangelsoft.econnect.dataformat.RecordSet;
import com.evangelsoft.econnect.dataformat.TransientRecordSet;
import com.evangelsoft.econnect.dataformat.VariantHolder;
import com.evangelsoft.econnect.rmi.RMIProxy;
import com.example.wuhongjie.myapplication.CounterActivity;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.YoungorApplication;
import com.example.wuhongjie.myapplication.type.Yxgs;
import com.youngor.modules.drp.counter.intf.RlbWeb;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by wuhongjie on 2017-02-28.
 */
@SuppressWarnings("ResourceType")
public class RlbDtlItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    public RecordSet rlbDtlRs;
    public RecordSet avlTppRs;
    public RecordSet masterRs;
    public RecordSet tppRs;
    private Context context;
    private android.os.Handler handler;
    private YoungorApplication app;
    private static int RLB_PROD_POST = 1902;
    private static int RLB_PROD_POST_INNER = 1802;
    public final class ViewHolder {
        public TextView prodNameTextView;
        public TextView prodCodeTextView;
        public TextView brandSpecTextView;
        public TextView unitPriceTextView;
        public TextView disCountTextView;
        public TextView fnlPriceTextView;
        public TextView qtySubTextView;
        public TextView qtyAddTextView;
        public TextView qtyTextView;
        public TextView fnlValTextView;
        public TextView deleteLineTextView;
    }
    public RlbDtlItemAdapter(RecordSet rlbDtlRs, RecordSet avlTppRs, RecordSet masterRs, RecordSet tppRs, android.os.Handler handler,YoungorApplication app, Context context) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.rlbDtlRs=rlbDtlRs;
        this.avlTppRs=avlTppRs;
        this.masterRs=masterRs;
        this.tppRs=tppRs;
        this.handler=handler;
        this.app=app;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return rlbDtlRs.recordCount();
    }

    @Override
    public Object getItem(int i) {
        return rlbDtlRs.getRecord(i);
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
            convertView = mInflater.inflate(R.layout.item_rlbdtl, null);
            holder.prodNameTextView= (TextView) convertView
                    .findViewById(R.id.prodNameTextView);
            holder.prodCodeTextView=(TextView) convertView
                    .findViewById(R.id.prodCodeTextView);
            holder.brandSpecTextView=(TextView) convertView
                    .findViewById(R.id.brandSpecTextView);
            holder.unitPriceTextView=(TextView) convertView
                    .findViewById(R.id.unitPriceTextView);
            holder.disCountTextView=(TextView) convertView
                    .findViewById(R.id.disCountTextView);
            holder.fnlPriceTextView=(TextView) convertView
                    .findViewById(R.id.fnlPriceTextView);
            holder.qtySubTextView=(TextView) convertView
                    .findViewById(R.id.qtySubTextView);
            holder.qtyAddTextView=(TextView) convertView
                    .findViewById(R.id.qtyAddTextView);
            holder.qtyTextView=(TextView) convertView
                    .findViewById(R.id.qtyTextView);
            holder.fnlValTextView=(TextView) convertView
                    .findViewById(R.id.fnlValTextView);

            holder.deleteLineTextView=(TextView) convertView
                    .findViewById(R.id.deleteLineTextView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.qtyAddTextView.setOnClickListener(new QtyAddButtonClick(i));
        holder.qtySubTextView.setOnClickListener(new QtySubButtonClick(i));
        holder.deleteLineTextView.setOnClickListener(new DeleteLineButtonClick(i));
        String prodName="";
        String prodCode="";
        String specName="";
        String brandName="";
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        try{
            prodName=new String(rlbDtlRs.getRecord(i).getField("PROD_NAME").getBytes(),"GBK");
            prodCode=new String(rlbDtlRs.getRecord(i).getField("PROD_CODE").getBytes(),"GBK");
            specName=new String(rlbDtlRs.getRecord(i).getField("SPEC_NAME").getBytes(),"GBK");
            brandName=new String(rlbDtlRs.getRecord(i).getField("BRAND_NAME").getBytes(),"GBK");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.prodCodeTextView.setText(prodCode);
        holder.prodNameTextView.setText(prodName);
        holder.brandSpecTextView.setText("品牌："+brandName+"，规格："+specName);
        holder.unitPriceTextView.setText(currency.format(rlbDtlRs.getRecord(i).getField("UNIT_PRICE").getNumber().
                setScale(2, BigDecimal.ROUND_HALF_UP)));
        holder.fnlPriceTextView.setText(currency.format(rlbDtlRs.getRecord(i).getField("FNL_PRICE").getNumber().
                setScale(2, BigDecimal.ROUND_HALF_UP)));
        holder.qtyTextView.setText(rlbDtlRs.getRecord(i).getField("QTY").getNumber().
                setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        holder.disCountTextView.setText(rlbDtlRs.getRecord(i).getField("DISC_RATE").getNumber().
               setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        holder.fnlValTextView.setText(rlbDtlRs.getRecord(i).getField("QTY").getNumber().
                multiply(rlbDtlRs.getRecord(i).getField("FNL_PRICE").getNumber()).
                setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        return convertView;
    }
    private class DeleteLineButtonClick implements View.OnClickListener{
        public int position;
        public DeleteLineButtonClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            rlbDtlRs.getRecord(position).getField("QTY").setNumber(BigDecimal.ZERO);
            rlbDtlRs.getRecord(position).post();
            changeRlbDtl(handler,position);

           /// RlbDtlItemAdapter.this.notifyDataSetChanged();
           // changeRlbDtl(handler,position);

        }
    }
    private class QtyAddButtonClick implements View.OnClickListener{
        public int position;
        public QtyAddButtonClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            Record rlbDtlRc=rlbDtlRs.getRecord(position);
            rlbDtlRc.getField("QTY").setNumber(rlbDtlRc.getField("QTY").getNumber().add(BigDecimal.ONE));
            rlbDtlRc.post();
            //RlbDtlItemAdapter.this.notifyDataSetChanged();
            changeRlbDtl(handler,position);
        }
    }
    private class QtySubButtonClick implements View.OnClickListener{
        public int position;
        public QtySubButtonClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            Record rlbDtlRc=rlbDtlRs.getRecord(position);
            rlbDtlRc.getField("QTY").setNumber(rlbDtlRc.getField("QTY").getNumber().subtract(BigDecimal.ONE));
            rlbDtlRc.post();
            changeRlbDtl(handler,position);
           // RlbDtlItemAdapter.this.notifyDataSetChanged();

        }
    }
    private void changeRlbDtl(final android.os.Handler handler,final int loc){
            new Thread() {
                public void run() {
                    Message msg = null;
                    try {

                        ClientSession session = app.getDxSession();
                        if (!app.sessionLinked) {
                            msg = new Message();
                            msg.arg1 = DxSessionHelper.SESSION_ERROR;
                            msg.obj = DxSessionHelper.errMsg;
                            handler.sendMessage(msg);
                            return;
                        }
                        RlbWeb rlbWeb = (RlbWeb) new RMIProxy(session)
                                .newStub(RlbWeb.class);
                        VariantHolder data = new VariantHolder();
                        VariantHolder<String> errMsg = new VariantHolder<String>();

                        Object[] inputkey=new Object[]{rlbDtlRs,avlTppRs,masterRs,tppRs,loc};
                        data.value = new TransientRecordSet[]{new TransientRecordSet(),new TransientRecordSet(),new TransientRecordSet()};
                        if(!rlbWeb.executePolicy(inputkey,data,errMsg)){
                            throw new Exception(errMsg.value);
                        }
                        masterRs=((RecordSet[]) data.value)[0];
                        rlbDtlRs=((RecordSet[]) data.value)[1];
                        tppRs=((RecordSet[]) data.value)[2];
                        for(int i=rlbDtlRs.recordCount()-1;i>=0;i--){
                            if(rlbDtlRs.getRecord(i).getField("QTY").getNumber().compareTo(BigDecimal.ZERO)==0){
                                rlbDtlRs.delete(i);
                            }
                        }
                        //RlbDtlItemAdapter.this.notifyDataSetChanged();
                        //displayPolicy();
                        msg = new Message();
                        msg.arg1 = RLB_PROD_POST;
                        msg.arg2 = RLB_PROD_POST_INNER;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg = new Message();
                        msg.arg1 = DxSessionHelper.SESSION_ERROR_TOASE;
                        msg.obj = e.getMessage();
                        handler.sendMessage(msg);
                    }
                }
            }.start();
    }
}
