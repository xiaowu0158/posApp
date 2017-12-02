package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.Suite;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class ProdMeasureItemAdapter extends RecyclerView.Adapter<ProdMeasureItemAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<ProdCls> list;
    private Context context;
    private Handler handler;
    private String[] mData ;
    public final class ViewHolder extends RecyclerView.ViewHolder{
        public TextView ordNo;
        public TextView prodCode;
        public TextView prodName;
        public TextView prodMemo;
        public TextView gender;
        public TextView tv_uom;
        public TextView tv_num;
        public TextView tv_add;
        public TextView tv_reduce;
        public TextView suiteGrpId;
        public TextView prodCat;
        public LinearLayout ordLinearLayout;
        public LinearLayout prodMemoLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public ProdMeasureItemAdapter(ArrayList<ProdCls> list,Context context, Handler handler) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.handler=handler;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO Auto-generated method stub
        View convertView = mInflater.inflate(R.layout.item_product, parent, false);
        ViewHolder holder = new ViewHolder(convertView);

        holder.ordNo = (TextView) convertView
                .findViewById(R.id.ordNo);
        holder.prodCode = (TextView) convertView
                .findViewById(R.id.prodCode);
        holder.prodName = (TextView) convertView
                .findViewById(R.id.prodName);
        holder.prodMemo = (TextView) convertView
                .findViewById(R.id.prodMemo);
        holder.tv_uom = (TextView) convertView
                .findViewById(R.id.tv_uom);
        holder.gender = (TextView) convertView
                .findViewById(R.id.gender);
        holder.tv_num = (TextView) convertView
                .findViewById(R.id.tv_num);
        holder.tv_add = (TextView) convertView
                .findViewById(R.id.tv_add);
        holder.tv_reduce= (TextView) convertView
                .findViewById(R.id.tv_reduce);
        holder.suiteGrpId= (TextView) convertView
                .findViewById(R.id.suiteGrpId);
        holder.prodCat= (TextView) convertView
                .findViewById(R.id.prodCat);
        holder.ordLinearLayout=(LinearLayout) convertView
                .findViewById(R.id.ordLinearLayout);
        holder.prodMemoLinearLayout=(LinearLayout) convertView
                .findViewById(R.id.prodMemoLinearLayout);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        // TODO Auto-generated method stub
        //holder.mImageView.setImageResource(mDataset[position]);

        holder.ordNo.setText("订单号："+list.get(i).getOrdNo());
        holder.prodCode.setText(list.get(i).getPrdCode());
        holder.prodName.setText(list.get(i).getPrdName());
        holder.prodMemo.setText(list.get(i).getPrdMemo());
        if(list.get(i).getGender()!=null){
            holder.gender.setText(list.get(i).getGender().equals("M")?"男":"女");
        }

        holder.tv_uom.setText(list.get(i).getUom());
        if(list.get(i).getQty()==null){
            holder.tv_num.setText("1");
        }else{
            holder.tv_num.setText(list.get(i).getQty());
        }

        holder.suiteGrpId.setText(list.get(i).getSuiteGrpName());

        holder.prodCat.setText(list.get(i).getProdCatName());
        holder.tv_add.setOnClickListener(new ProdQtyNumAddClick(holder,i));
        holder.tv_reduce.setOnClickListener(new ProdQtyNumReduceClick(holder,i));
        if(!list.get(i).isSimple()){
            holder.ordLinearLayout.setVisibility(View.GONE);
            holder.prodMemoLinearLayout.setVisibility(View.GONE);
        }else{
            holder.ordLinearLayout.setVisibility(View.VISIBLE);
            holder.prodMemoLinearLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ProdQtyNumReduceClick implements View.OnClickListener{
        private int position;
        private ViewHolder holder;
        public ProdQtyNumReduceClick(ViewHolder h,int i) {
            holder=h;
            position=i;
        }
        @Override
        public void onClick(View view) {
            String strNum=list.get(position).getQty();
            if(strNum==null){
                strNum="1";
            }
            int num=Integer.parseInt(strNum);
            if(num>1){
                list.get(position).setQty(String.valueOf(num-1));
                holder.tv_num.setText(String.valueOf(num-1));
            }
        }
    }
    private class ProdQtyNumAddClick implements View.OnClickListener{
        private int position;
        private ViewHolder holder;
        public ProdQtyNumAddClick(ViewHolder h,int i) {
            holder=h;
            position=i;
        }
        @Override
        public void onClick(View view) {
            String strNum=list.get(position).getQty();
            if(strNum==null){
                strNum="1";
            }
            int num=Integer.parseInt(strNum);
            list.get(position).setQty(String.valueOf(num+1));
            holder.tv_num.setText(String.valueOf(num+1));
        }
    }

}
