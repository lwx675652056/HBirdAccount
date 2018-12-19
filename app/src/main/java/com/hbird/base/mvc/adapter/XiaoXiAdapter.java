package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.RequestBean.XiaoXiBean;

import java.util.ArrayList;

/**
 * Created by Liul on 2018/10/11.
 */

public class XiaoXiAdapter extends BaseAdapter {
    private ArrayList<XiaoXiBean> list;
    private Context context;

    public XiaoXiAdapter(Context context,ArrayList<XiaoXiBean> lists ){
        this.context = context;
        this.list =lists;
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
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_xiaoxi_adapter, null);
            //convertView.findViewById(R.id.)
        }
        return null;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        TextView tvNum;
        ImageView imgUrl;
    }

}
