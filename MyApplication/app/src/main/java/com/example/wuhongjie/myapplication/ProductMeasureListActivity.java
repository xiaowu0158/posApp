package com.example.wuhongjie.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.service.SuiteGrpDtlService;
import com.example.wuhongjie.myapplication.type.Employee;
import com.example.wuhongjie.myapplication.type.ProdCls;
import com.example.wuhongjie.myapplication.type.SuiteGrpDtl;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of ProductMeasure. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProductMeasureDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProductMeasureListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private Handler mSearchHandler;
    private static int LOAD_SUITE_GRP_DTL_ACTION=6;
    private static int LOAD_SUITE_GRP_DTL_RESUME_ACTION=7;
    private SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;
    private String suiteGrpId="";
    private String suiteGrpName="";
    private String prodCatId="";
    private Activity activity;
    private String custName="";
    private String cname="";
    private String dept="";
    private String prodName="";
    private Employee employee=new Employee();
    private ProdCls prodCls=new ProdCls();
    private boolean mTwoPane;
    private boolean isCreate=true;
    private ArrayList<SuiteGrpDtl> suiteGrpDtls=new ArrayList<SuiteGrpDtl>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productmeasure_list);
        activity =ProductMeasureListActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        custName=intent.getStringExtra("custName");
        //pckNo=intent.getStringExtra("pckNo");
        cname=intent.getStringExtra("cname");
        dept=intent.getStringExtra("dept");
        prodName=intent.getStringExtra("prodName");
        suiteGrpId=intent.getStringExtra("suiteGrpId");
        suiteGrpName=intent.getStringExtra("suiteGrpName");
        employee=(Employee) intent.getSerializableExtra("employee");
        prodCls=(ProdCls) intent.getSerializableExtra("prodCls");
        prodCatId=intent.getStringExtra("prodCatId");
        FloatingActionButton returnButton = (FloatingActionButton)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductMeasureListActivity.this.finish();
            }
        });
        if (findViewById(R.id.productmeasure_detail_container) != null) {
            mTwoPane = true;
        }
        if(dept!=null && dept.length()>0) {
            setTitle(cname + "[" + custName+"/"+dept + "]--"+prodName);
        }else{
            setTitle(cname+ "[" + custName+ "]--"+prodName);
        }

        View recyclerView = findViewById(R.id.productmeasure_list);
        simpleItemRecyclerViewAdapter=new SimpleItemRecyclerViewAdapter(suiteGrpDtls);
        initData();
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        this.isCreate=true;
        loadSuiteGrpDtlData(mSearchHandler,LOAD_SUITE_GRP_DTL_ACTION);

    }
    protected void onResume() {
        super.onResume();
        loadSuiteGrpDtlData(mSearchHandler,LOAD_SUITE_GRP_DTL_RESUME_ACTION);

    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(simpleItemRecyclerViewAdapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<SuiteGrpDtl> mValues;

        public SimpleItemRecyclerViewAdapter(List<SuiteGrpDtl> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.productmeasure_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            if(mValues.get(position).getTermMemoRst()==null){
                holder.productMeasureListButton.setText(mValues.get(position).getSuiteName());
            }else{
                holder.productMeasureListButton.setText(mValues.get(position).getSuiteName()+"\n"
                        +mValues.get(position).getTermMemoRst());
            }

            /*if(mValues.get(position).getTermMemoRst()==null){
                holder.team_memo_linear.setVisibility(View.GONE);
            }else{
                if(mValues.get(position).getTermMemoRst().length()>0){
                    holder.team_memo_linear.setVisibility(View.VISIBLE);
                    holder.team_memo.setText(mValues.get(position).getTermMemoRst());
                }else{
                    holder.team_memo_linear.setVisibility(View.GONE);
                }
            }*/
            //holder.productMeasureListButton.setTextColor(Color.RED);

            //holder.badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            //holder.badge.setText("1");
            //holder.badge.show();
           // holder.productMeasureListButton.setEnabled(!mValues.get(position).isSelected());
            holder.productMeasureListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mItem.setSelected(true);
                    for(int i=0;i<mValues.size();i++){
                        if(!mValues.get(i).getDtlId().equals(holder.mItem.getDtlId())){
                            mValues.get(i).setSelected(false);
                        }else{
                            mValues.get(i).setSelected(true);
                        }
                    }
                    SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                    if (mTwoPane) {
                        System.out.println("sssssssss");
                        Bundle arguments = new Bundle();
                        arguments.putString("suiteCode", holder.mItem.getSuiteCode());
                        arguments.putString("suiteName", holder.mItem.getSuiteName());
                        arguments.putString("dtlId", holder.mItem.getDtlId());
                        arguments.putSerializable("employee",employee);
                        arguments.putSerializable("prodCls",prodCls);
                        ProductMeasureDetailFragment fragment = new ProductMeasureDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.productmeasure_detail_container, fragment)
                                .commit();
                    } else {

                        System.out.println("sssssoossss");
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProductMeasureDetailActivity.class);
                        intent.putExtra("suiteCode", holder.mItem.getSuiteCode());
                        intent.putExtra("suiteName", holder.mItem.getSuiteName());
                        intent.putExtra("dtlId", holder.mItem.getDtlId());
                        System.out.println("sfdfsdfsdfsdfsdfsdfsdfsdfsdfsdf"+holder.mItem.getDtlId());
                        intent.putExtra("employee",employee);
                        intent.putExtra("prodCls",prodCls);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final Button productMeasureListButton;
            public final LinearLayout team_memo_linear;
            public final TextView team_memo;
            public SuiteGrpDtl mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                productMeasureListButton = (Button) view.findViewById(R.id.productMeasureListButton);
                team_memo_linear=(LinearLayout) view.findViewById(R.id.team_memo_linear);
                team_memo=(TextView) view.findViewById(R.id.team_memo);
            }
        }
    }
    private void initData(){
        mSearchHandler=new Handler(){
            public void handleMessage(Message msg) {
                //msg.what 记录数，arg1
                if(msg.arg1==LOAD_SUITE_GRP_DTL_ACTION||
                        msg.arg1==LOAD_SUITE_GRP_DTL_RESUME_ACTION){
                    if(msg.what>0){
                        ArrayList<SuiteGrpDtl> list = (ArrayList<SuiteGrpDtl>)msg.obj;
                        suiteGrpDtls.clear();
                        suiteGrpDtls.addAll(list);
                        simpleItemRecyclerViewAdapter.notifyDataSetChanged();
                        if(!mTwoPane){
                            if((list.size()==1)&&(msg.arg1==LOAD_SUITE_GRP_DTL_ACTION)){
                                //System.out.println("sssssoossss");
                                //Context context = v.getContext();
                                Intent intent = new Intent(activity, ProductMeasureDetailActivity.class);
                                intent.putExtra("suiteCode", list.get(0).getSuiteCode());
                                intent.putExtra("suiteName", list.get(0).getSuiteName());
                                intent.putExtra("dtlId", list.get(0).getDtlId());

                                intent.putExtra("employee",employee);
                                intent.putExtra("prodCls",prodCls);
                                activity.finish();
                                activity.startActivity(intent);
                            }
                        }

                    }
                    if(msg.arg1==LOAD_SUITE_GRP_DTL_RESUME_ACTION){
                        if(!isCreate){
                            int j=0;
                            for(int i=0;i<suiteGrpDtls.size();i++){
                                if(suiteGrpDtls.get(i).getTermMemoRst()!=null){
                                    if(suiteGrpDtls.get(i).getTermMemoRst().length()>0){
                                        j++;
                                    }
                                }
                            }
                            if(suiteGrpDtls.size()>0){
                                if(suiteGrpDtls.size()==j){
                                    activity.finish();
                                }
                            }

                        }
                        isCreate=false;
                    }
                }


            }
        };
    }
    private void loadSuiteGrpDtlData(final Handler handler,final int arg){
        new Thread(){
            public void run(){
                Message msg = new Message();
                try{
                    ArrayList<SuiteGrpDtl> list= SuiteGrpDtlService.getSuiteGrp(suiteGrpId,suiteGrpName,prodCatId,prodCls,employee,ProductMeasureListActivity.this);
                    msg.what=list.size();
                    msg.obj=list;
                }catch (Exception e){
                    e.printStackTrace();
                    msg.what=-1;
                    msg.obj=e;
                }
                msg.arg1 = arg;//告知handler当前action
                handler.sendMessage(msg);
            }
        }.start();
    }
}
