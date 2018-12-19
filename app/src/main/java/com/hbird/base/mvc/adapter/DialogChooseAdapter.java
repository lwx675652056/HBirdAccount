package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.widget.cycleView;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/9.
 */

public class DialogChooseAdapter extends BaseAdapter {
    private  List<AccountZbBean.ResultBean> list;
    private Context context;
    public DialogChooseAdapter(Context context, List<AccountZbBean.ResultBean> list){
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

        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder =new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_choose_zb, null);
            viewHolder.zbName = (TextView) convertView.findViewById(R.id.tv_choose_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.zbName.setText(list.get(i).getAbName());
        return convertView;
    }

    public static class ViewHolder {
        TextView zbName;
    }
}
