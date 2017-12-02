package com.example.wuhongjie.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wuhongjie.myapplication.R;
import com.example.wuhongjie.myapplication.SpecPopupWindow;
import com.example.wuhongjie.myapplication.TeamPopupWindow;
import com.example.wuhongjie.myapplication.service.SynService;
import com.example.wuhongjie.myapplication.type.SpecGrp;
import com.example.wuhongjie.myapplication.type.Team;
import com.example.wuhongjie.myapplication.util.KeyboardUtil;

import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class TeamItemAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList<Team> list;
    private Context context;
    private Handler handler;
    public final class ViewHolder {
        public TextView teamName;
        public TextView teamDesc;
        public EditText teamCurrentValue;
        public Button teamButton;
        public TableRow team_row;

        //public TextView currentValue;

    }
    public TeamItemAdapter(ArrayList<Team> list, Context context,Handler handler) {
        super();
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.handler=handler;
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
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.team_item, null);
            holder.team_row= (TableRow) convertView
                    .findViewById(R.id.team_row);
            ViewGroup.LayoutParams params = holder.team_row.getLayoutParams();
            params.height = 80;
            holder.team_row.setLayoutParams(params);
            holder.teamName= (TextView) convertView
                    .findViewById(R.id.teamNameTextView);
            holder.teamName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            holder.teamDesc= (TextView) convertView
                    .findViewById(R.id.teamDesc);
            holder.teamButton= (Button) convertView
                    .findViewById(R.id.teamButton);
            holder.teamCurrentValue= (EditText) convertView
                    .findViewById(R.id.teamCurrentValue);
            holder.teamCurrentValue.setInputType(InputType.TYPE_NULL);
           // holder.teamCurrentValue.setOnClickListener(new ValueClick(i));

            /*holder.teamCurrentValue.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    System.out.println("弹出了软键盘");
                    new KeyboardUtil((Activity)context, v.getContext(), holder.teamCurrentValue).showKeyboard();
                    return false;
                }
            });*/
            holder.teamButton.setOnClickListener(new TeamButtonClick(i));
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
        public int position;
        public ValueClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            //TeamPopupWindow teamPopupWindow = new TeamPopupWindow(context,list.get(position),handler);
            // teamPopupWindow.showAsDropDown(view);
        }
    }
    private class TeamButtonClick implements View.OnClickListener{
        public int position;
        public TeamButtonClick(int p) {
            position = p;
        }
        @Override
        public void onClick(View view) {
            //TeamPopupWindow teamPopupWindow = new TeamPopupWindow(context,list.get(position),handler);
           // teamPopupWindow.showAsDropDown(view);
        }
    }

}
