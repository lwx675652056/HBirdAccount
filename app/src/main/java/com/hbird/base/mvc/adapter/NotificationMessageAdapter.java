package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.ReturnBean.FengMessageReturn;
import com.hbird.base.util.DateUtils;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/9.
 */

public class NotificationMessageAdapter extends BaseAdapter {
    private Context context;
    private List<FengMessageReturn.ResultBean.MessageListBean> list;
    public NotificationMessageAdapter(Context context, List<FengMessageReturn.ResultBean.MessageListBean> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
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
        ViewHolder viewHolder =null;
        if(null==convertView){
            viewHolder =new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_notification_message, null);
            viewHolder.tzTime = convertView.findViewById(R.id.tv_time);
            viewHolder.tzTypes = convertView.findViewById(R.id.tv_type);
            viewHolder.tzContent = convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        long create_date = list.get(i).getCreate_date();
        String day = DateUtils.getDay(create_date);
        viewHolder.tzTime.setText(day);
        if(list.get(i).getStatus()==2){
            viewHolder.tzTypes.setText("未读");
            viewHolder.tzTypes.setTextColor(context.getResources().getColor(R.color.bg_f15c3c));
        }else {
            viewHolder.tzTypes.setText("已读");
            viewHolder.tzTypes.setTextColor(context.getResources().getColor(R.color.text_bdbdbd));
        }
        viewHolder.tzContent.setText(list.get(i).getContent());

        return convertView;
    }

    public static class ViewHolder {
        TextView tzTime;
        TextView tzTypes;
        TextView tzContent;
    }
}
