package com.example.wuhongjie.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.ProductMeasureDetailFragment;
import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.SpecPopupWindow;
import com.example.wuhongjie.myapplication.TeamPopupWindow;
import com.example.wuhongjie.myapplication.service.SpecService;
import com.example.wuhongjie.myapplication.type.Spec;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.SpecLink;
import com.example.wuhongjie.myapplication.type.Suite;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.type.TeamGroup;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SuiteMeasureItemAdapter extends RecyclerView.Adapter<SuiteMeasureItemAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<Suite> list;
    private Context context;
    private Handler handler;
    private boolean bigFond=false;
    private boolean specNumFocus=false;
    private String[] mData ;
    public final class ViewHolder extends RecyclerView.ViewHolder{
        public TextView suiteMeasure_title;
        public TextView prodCatName;
        public TextView selectTermTextView;
        public LinearLayout selectSpecGrpRelative;//选择款式甄别
        public TextView selectSpecGrpTextView;
        public LinearLayout selectSpecLinearLayout;//选择规格
        public TextView selectSpecTextView;

        public LinearLayout selectTermLinearLayout;
        public ListView suiteMeasure_teamListView;
        public ImageView team_submenu;
        public ImageView team_submenu_down;

        public SpecGrp specGrp;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public SuiteMeasureItemAdapter(ArrayList<Suite> list, boolean bigFond, boolean specNumFocus,Context context, Handler handler) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.handler=handler;
        this.list=list;
        this.bigFond=bigFond;
        this.specNumFocus=specNumFocus;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO Auto-generated method stub
        View convertView = mInflater.inflate(R.layout.suitemeasure_item, parent, false);
        ViewHolder holder = new ViewHolder(convertView);

        holder.suiteMeasure_title = (TextView) convertView
                .findViewById(R.id.suiteMeasure_title);
        holder.prodCatName = (TextView) convertView
                .findViewById(R.id.prodCatName);
        holder.selectTermTextView = (TextView) convertView
                .findViewById(R.id.selectTermTextView);
        holder.selectSpecGrpRelative = (LinearLayout) convertView
                .findViewById(R.id.selectSpecGrpRelative);
        holder.selectSpecLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.selectSpecLinearLayout);
        holder.selectSpecGrpTextView = (TextView) convertView
                .findViewById(R.id.selectSpecGrpTextView);
        holder.selectSpecTextView = (TextView) convertView
                .findViewById(R.id.selectSpecTextView);

        holder.selectTermLinearLayout = (LinearLayout) convertView
                .findViewById(R.id.selectTermLinearLayout);
        holder.suiteMeasure_teamListView= (ListView) convertView
                .findViewById(R.id.suiteMeasure_teamListView);
        holder.team_submenu= (ImageView) convertView
                .findViewById(R.id.team_submenu);
        holder.team_submenu_down= (ImageView) convertView
                .findViewById(R.id.team_submenu_down);
        holder.specGrp=new SpecGrp();
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        // TODO Auto-generated method stub
        //holder.mImageView.setImageResource(mDataset[position]);
        TeamGroupItemAdapter teamGroupItemAdapter = new TeamGroupItemAdapter(list.get(i).getTeamGroup(),bigFond, context,handler);
        holder.suiteMeasure_teamListView.setAdapter(teamGroupItemAdapter);
        holder.suiteMeasure_title.setText(list.get(i).getSuiteName());
        holder.prodCatName.setText(list.get(i).getProdCatName());
        holder.selectTermLinearLayout.setOnClickListener(new TermSwitchClick(holder,i));

        holder.selectSpecGrpRelative.setOnClickListener(new SelectSpecGrpClick(holder, list.get(i).getSpecGrps(),i));
        holder.selectSpecGrpTextView.setText("选择规格甄别");
        if(list.get(i).getSpecGrp()!=null){
            //System.out.println(list.get(i).getSpecGrp().getGrpName()+","+i);
            holder.selectSpecGrpTextView.setText(list.get(i).getSpecGrp().getGrpName());
        }
        holder.selectSpecLinearLayout.setOnClickListener(new SelectSpecClick(list.get(i),i));
        holder.selectSpecTextView.setText("选择规格");
        if(list.get(i).getSpec()!=null) {
            holder.selectSpecTextView.setText("规格" + list.get(i).getSpec().getSpec() + "； 编号:" + list.get(i).getSpec().getSpecCode() + "；号型:" + list.get(i).getSpec().getShape() + "；" +
                    list.get(i).getSpec().getPattern());
        }
        holder.selectTermTextView.setText("选择特体术语");
        ArrayList<TeamGroup> teamGroups = list.get(i).getTeamGroup();
        if(teamGroups!=null){
            String teamDescriptions="";
            for(int j=0;j<teamGroups.size();j++){
                ArrayList<Team> detailTeams=teamGroups.get(j).getTeams();
                for(int k=0;k<detailTeams.size();k++){
                    if(detailTeams.get(k).getCurrValue()!=null&&detailTeams.get(k).getCurrValue().length()>0){
                        if(teamDescriptions.length()<=0){
                            teamDescriptions=detailTeams.get(k).getDescription();
                        }else{
                            teamDescriptions=teamDescriptions+";"+detailTeams.get(k).getDescription();
                        }
                    }
                }
            }
            if(teamDescriptions.length()>0){
                holder.selectTermTextView.setText(teamDescriptions);
            }
        }

        setListViewHeightBasedOnChildren(holder.suiteMeasure_teamListView);
        holder.suiteMeasure_teamListView.setVisibility(View.GONE);
        holder.team_submenu.setVisibility(View.VISIBLE);
        holder.team_submenu_down.setVisibility(View.GONE);
        if(!list.get(i).isCollapsible()){
            holder.suiteMeasure_teamListView.setVisibility(View.VISIBLE);
            holder.team_submenu.setVisibility(View.GONE);
            holder.team_submenu_down.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    private class TermSwitchClick implements View.OnClickListener{
        private ViewHolder holder;
        private int position;
        public TermSwitchClick(ViewHolder h,int i) {
            holder = h;
            position=i;
        }
        @Override
        public void onClick(View view) {
            if(holder.suiteMeasure_teamListView.getVisibility()==View.VISIBLE){
                holder.suiteMeasure_teamListView.setVisibility(View.GONE);
                holder.team_submenu.setVisibility(View.VISIBLE);
                holder.team_submenu_down.setVisibility(View.GONE);
                list.get(position).setCollapsible(true);
            }else{
                holder.suiteMeasure_teamListView.setVisibility(View.VISIBLE);
                holder.team_submenu.setVisibility(View.GONE);
                holder.team_submenu_down.setVisibility(View.VISIBLE);
                list.get(position).setCollapsible(false);
            }
        }
    }
    private class SelectSpecGrpClick implements View.OnClickListener{
        private ViewHolder holder;
        private ArrayList<SpecGrp> specGrps;
        private int position;
        public SelectSpecGrpClick(ViewHolder h,ArrayList<SpecGrp> s,int i) {
            holder = h;
            specGrps=s;
            position=i;
        }
        @Override
        public void onClick(View view) {
            mData=new String[specGrps.size()];
            for(int i=0;i<specGrps.size();i++){
                mData[i]=specGrps.get(i).getGrpName();
            }
            ArrayAdapter<String> _adapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_dropdown_item_1line, mData);
            ListView listView = new ListView(view.getContext());//this为获取当前的上下文
            listView.setAdapter(_adapter);

            final AlertDialog mDialog = new AlertDialog.Builder(view.getContext()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create();
            mDialog.setTitle("选择规格编号甄别");

            mDialog.setView(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> pParent, View pView,
                                        int pPosition, long pId) {

                    holder.specGrp=specGrps.get(pPosition);
                    holder.selectSpecGrpTextView.setText(holder.specGrp.getGrpName());
                    list.get(position).setSpecGrp(specGrps.get(pPosition));
                    if(list.get(position).getSpec()!=null&&list.get(position).getSpec().getGrpId()!=null){
                        if(!list.get(position).getSpec().getGrpId().equals(specGrps.get(pPosition).getGrpId())){
                            list.get(position).setSpec(null);
                            holder.selectSpecTextView.setText("选择规格");
                        }

                    }

                   // loadSpecData(mSearchHandler);
                    mDialog.dismiss();
                }
            });
            mDialog.show();
            // loadSpecGrpData(mSearchHandler);
        }
    }
    private class SelectSpecClick implements View.OnClickListener{

        private Suite suite;
        private int position;
        public SelectSpecClick(Suite s,int i) {
            suite=s;
            position=i;
        }
        @Override
        public void onClick(View view) {
           // System.out.println("ssssssssssssssss:"+suite.getSpecGrp().getGrpName());]
            if(suite.getSpecGrp()==null){
                return;
            }
            if(suite.getSpecGrp().getGrpCode()==null ||suite.getSpecGrp().getGrpCode().length()<=0){
                return;
            }
            ArrayList<SpecLink> specLinks=new ArrayList<SpecLink>();
            for(int i=0;i<list.size();i++){
                if(i!=position
                        &&list.get(i).getSpec()!=null
                        &&list.get(i).getSuiteCode()!=null
                        &&list.get(i).getSpec().getShape()!=null){
                    String suiteCode=list.get(i).getSuiteCode();
                    String gender=list.get(i).getGender()==null?"":list.get(i).getGender();
                    String shape=list.get(i).getSpec().getShape();
                    if(suiteCode.equals("TT")||suiteCode.equals("CC")){
                        shape=list.get(i).getSpec().getSpec();
                    }
                    if(shape!=null&&shape.length()>0){
                        SpecLink specLink=new SpecLink();
                        specLink.setSuiteCode(suiteCode);
                        specLink.setGender(gender);
                        specLink.setShape(shape);
                        specLinks.add(specLink);
                    }
                }
            }
            SpecPopupWindow specPopupWindow = new SpecPopupWindow(context,specLinks,suite.getSuiteCode(),suite.getSpecGrp().getSpecs(),
                    suite.getProdCatId(),specNumFocus,position,handler);
            specPopupWindow.showAsDropDown(view);
        }
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0); // 可删除
        listView.setLayoutParams(params);
    }

}
