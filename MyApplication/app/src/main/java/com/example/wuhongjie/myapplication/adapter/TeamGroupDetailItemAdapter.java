package com.example.wuhongjie.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.SymboPopupWindow;
import com.example.wuhongjie.myapplication.TeamPopupWindow;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.util.KeyboardUtil;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class TeamGroupDetailItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Team> list;
    private Context context;
    private Handler handler;
    private String  teamGroupId;
    private boolean bigFond=false;
    public final class ViewHolder {
        public TextView teamName;
        public TextView teamDesc;
        public EditText teamCurrentValue;
        //public TextView currentValue;
        //public TableRow team_row;
        public LinearLayout team_line;

    }
    public TeamGroupDetailItemAdapter(String teamGroupId,ArrayList<Team> list, boolean bigFond,Context context, Handler handler) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.handler=handler;
        this.list=list;
        this.teamGroupId=teamGroupId;
        this.bigFond=bigFond;
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
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.teamgroupdetail_item, null);
            holder.team_line= (LinearLayout) convertView
                    .findViewById(R.id.team_line);
          //  ViewGroup.LayoutParams params = holder.team_line.getLayoutParams();
          //  params.height = 70;
          //  holder.team_line.setLayoutParams(params);
            holder.teamName= (TextView) convertView
                    .findViewById(R.id.teamNameTextView);

            holder.teamDesc= (TextView) convertView
                    .findViewById(R.id.teamDesc);
            holder.teamDesc.setOnClickListener(new ValueClick(i));

            holder.teamCurrentValue= (EditText) convertView
                    .findViewById(R.id.teamCurrentValue);
            holder.teamCurrentValue.setInputType(InputType.TYPE_NULL);
            //holder.teamCurrentValue.setOnTouchListener(new OnEditTextTouched(i,holder.teamCurrentValue));
            holder.teamCurrentValue.setOnClickListener(new ValueClick(i));
            holder.teamCurrentValue.clearFocus();

            if (list.get(i).isSelected()) {
                holder.teamCurrentValue.setBackgroundColor(Color.GREEN);
            }else{
                holder.teamCurrentValue.setBackgroundResource(R.drawable.textview_border);
            }
            if(bigFond){
                holder.teamName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                holder.teamDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                ViewGroup.LayoutParams params = holder.teamDesc.getLayoutParams();
                params.height = 40;
                holder.teamDesc.setLayoutParams(params);
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.teamName.setText(list.get(i).getTermName());
        holder.teamDesc.setText("默认值："+list.get(i).getDefaultValue()+
                ";范围：["+list.get(i).getMinValue()+"~"+list.get(i).getMaxValue()+"];加减档："+list.get(i).getStepValue());
        if(list.get(i).getCurrValue()!=null){
            holder.teamCurrentValue.setText(list.get(i).getCurrValue());
        }
        return convertView;
    }
    private class ValueClick implements View.OnClickListener{
        private int position;
        public ValueClick(int i) {
            position=i;
        }

        @Override
        public void onClick(View view) {
           // list.get(position).setSelected(true);
            Log.v("teamGroupId",String.valueOf(teamGroupId));
            SymboPopupWindow symboPopupWindow = new SymboPopupWindow(context,handler,teamGroupId,list,position,
                    "请输入【"+list.get(position).getTermName()+"】特体值：");
            symboPopupWindow.showAsDropDown(view);
        }
    }

}
