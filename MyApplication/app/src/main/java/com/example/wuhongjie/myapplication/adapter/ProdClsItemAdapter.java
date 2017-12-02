package com.example.wuhongjie.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongjie.myapplication.EmployeeActivity;
import com.example.wuhongjie.myapplication.ProductActivity;
import com.example.wuhongjie.myapplication.ProductMeasureListActivity;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.service.ProdClsService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.SuiteGrp;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class ProdClsItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<ProdCls> list;
    private Employee employee;
    private String custCode;
    private String custName;
    private Configuration mConfiguration;
    private int ori;
    private Context context;
    public static final int REQUEST_PRODUCT_MEASURE = 4;
    public static final int REQUEST_EMPLOYEE = 1;
    public final class ViewHolder {
        public ImageView prodImageView1;
        public TextView prodMemo;
        public TextView prodName;
        public TextView teamMemo;
        public LinearLayout teamMemoLinear;
        public TextView ordNo;
        public TextView prdCode;
        public TextView suiteGrpId;
        public Button ksLtButton;

    }
    public ProdClsItemAdapter(ArrayList<ProdCls> list, Context context,Employee employee,
                              String custCode,String custName) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.employee=employee;
        this.custCode=custCode;
        this.custName=custName;
        this.list=list;
        this.mConfiguration = context.getResources().getConfiguration();
        this.ori = mConfiguration.orientation ;
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
            convertView = mInflater.inflate(R.layout.fragment_product, null);
            holder.prodImageView1= (ImageView) convertView
                    .findViewById(R.id.prodImageView1);
            holder.prodName= (TextView) convertView
                    .findViewById(R.id.prodName);
            holder.ordNo= (TextView) convertView
                    .findViewById(R.id.ordNo);
            holder.prdCode= (TextView) convertView
                    .findViewById(R.id.prdCode);
            holder.prodMemo= (TextView) convertView
                    .findViewById(R.id.prodMemo);
            holder.teamMemo= (TextView) convertView
                    .findViewById(R.id.team_memo);
            holder.teamMemoLinear=(LinearLayout) convertView
                    .findViewById(R.id.team_memo_linear);
            holder.suiteGrpId= (TextView) convertView
                    .findViewById(R.id.suiteGrpId);
            holder.ksLtButton= (Button) convertView
                    .findViewById(R.id.ksLtButton);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ordNo.setText("订单号："+list.get(i).getOrdNo());
        holder.prodName.setText("货号："+list.get(i).getPrdName());
        holder.prdCode.setText("货号代码："+list.get(i).getPrdCode());
        holder.prodMemo.setText(list.get(i).getPrdMemo());
        holder.suiteGrpId.setText(list.get(i).getSuiteGrpName());
        holder.teamMemo.setText(list.get(i).getTeamMemo());
        if(list.get(i).getTeamMemo()!=null){
            if(list.get(i).getTeamMemo().length()>0){
                //holder.teamMemo.setVisibility(View.VISIBLE);
                holder.teamMemoLinear.setVisibility(View.VISIBLE);
            }else{
               // holder.teamMemo.setVisibility(View.GONE);
                holder.teamMemoLinear.setVisibility(View.GONE);
            }
        }else{
           // holder.teamMemo.setVisibility(View.GONE);
            holder.teamMemoLinear.setVisibility(View.GONE);
        }
        if(ori == mConfiguration.ORIENTATION_LANDSCAPE){
            holder.teamMemoLinear.setVisibility(View.GONE);
        }
        holder.ksLtButton.setOnClickListener(new KsLtButtonClick(i));
        if(list.get(i).getImageUrl().length()>0) {
            asyncloadImage(holder.prodImageView1, list.get(i).getImageUrl(),list.get(i).getImageFileName());
        }
        //holder.prodImageView1.setImageResource();
        return convertView;
    }
    private class KsLtButtonClick implements View.OnClickListener{
        public int position;
        public KsLtButtonClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    list.get(position).getPrdMemo(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(view.getContext(), ProductMeasureListActivity.class);
            //((Activity)view.getContext()).startActivityForResult(intent,REQUEST_PRODUCT_MEASURE);
            if(employee==null){
                Intent intent = new Intent(view.getContext(), EmployeeActivity.class);

                intent.putExtra("ordNo", list.get(position).getOrdNo());
                intent.putExtra("prodClsCode", list.get(position).getPrdCode());
                intent.putExtra("prodName", list.get(position).getPrdName());
                intent.putExtra("suiteGrpId", list.get(position).getSuiteGrpId());
                intent.putExtra("suiteGrpName", list.get(position).getSuiteGrpName());
                intent.putExtra("prodCatId", list.get(position).getProdCatId());
                intent.putExtra("prodCls", list.get(position));
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);
                intent.putExtra("type", "RY");
                //intent.putExtra("pckNo", employee.getPckNo());
                //intent.putExtra("cname", employee.getCname());
                //intent.putExtra("dept", employee.getDept());
                //intent.putExtra("employee", employee);

                ((Activity) context).startActivityForResult(intent, REQUEST_EMPLOYEE);
            }else {
                Intent intent = new Intent(view.getContext(), ProductMeasureListActivity.class);

                intent.putExtra("ordNo", list.get(position).getOrdNo());
                intent.putExtra("prodClsCode", list.get(position).getPrdCode());
                intent.putExtra("prodName", list.get(position).getPrdName());
                intent.putExtra("suiteGrpId", list.get(position).getSuiteGrpId());
                intent.putExtra("suiteGrpName", list.get(position).getSuiteGrpName());
                intent.putExtra("prodCatId", list.get(position).getProdCatId());
                intent.putExtra("prodCls", list.get(position));
                intent.putExtra("custCode", custCode);
                intent.putExtra("custName", custName);

                intent.putExtra("pckNo", employee.getPckNo());
                intent.putExtra("cname", employee.getCname());
                intent.putExtra("dept", employee.getDept());
                intent.putExtra("employee", employee);

                ((Activity) context).startActivityForResult(intent, REQUEST_PRODUCT_MEASURE);
            }
        }
    }
    private void asyncloadImage(ImageView iv_header, String path,String fileName) {
        ProdClsService service = new ProdClsService();
        AsyncImageTask task = new AsyncImageTask(service, iv_header);
        task.execute(path,fileName);
    }
    private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {

        private ProdClsService service;
        private ImageView iv_header;

        public AsyncImageTask(ProdClsService service, ImageView iv_header) {
            this.service = service;
            this.iv_header = iv_header;
        }

        // 后台运行的子线程子线程
        @Override
        protected Uri doInBackground(String... params) {
            try {

                return service.getImageURI((Activity)context,params[0],params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // 这个放在在ui线程中执行
        @Override
        protected void onPostExecute(Uri result) {
            super.onPostExecute(result);
            // 完成图片的绑定
            if (iv_header != null && result != null) {
                iv_header.setImageURI(result);
            }
        }
    }
}
