package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.ReturnBean.UserFirendsReturn;
import com.hbird.base.mvc.widget.cycleView;
import com.hbird.base.util.DateUtils;
import com.ljy.devring.image.support.GlideApp;

import java.util.List;

/**
 * Created by Liul on 2018/10/18.
 */

public class AskFriendsAdapter extends BaseAdapter {
    Context context;
    List<UserFirendsReturn.ResultBean.ContentBean> list;

    public AskFriendsAdapter(Context context, List<UserFirendsReturn.ResultBean.ContentBean> list) {
        this.context = context;
        this.list =list;
    }
    public void loadMore(List<UserFirendsReturn.ResultBean.ContentBean> lists) {
        if (list != null) {
            this.list.addAll(lists);
        }
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_ask_firend, null);
            holder.iconFriends = (cycleView) convertView.findViewById(R.id.iv_avata);
            holder.nameFriends = (TextView)convertView.findViewById(R.id.tv_name);
            holder.timeFriends = (TextView)convertView.findViewById(R.id.tv_times);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        GlideApp.with(context)
                .load(list.get(i).getAvatarUrl())
                .placeholder(R.mipmap.ic_me_normal_headr)
                .centerCrop()
                .error(R.mipmap.ic_me_normal_headr)
                .into(holder.iconFriends);
        holder.nameFriends.setText(list.get(i).getNickName());
        long registerDate = list.get(i).getRegisterDate();
        String days = DateUtils.getDay(registerDate);
        holder.timeFriends.setText(days);
        return convertView;
    }
    class ViewHolder {
        TextView nameFriends;
        TextView timeFriends;
        cycleView iconFriends;
    }

}
