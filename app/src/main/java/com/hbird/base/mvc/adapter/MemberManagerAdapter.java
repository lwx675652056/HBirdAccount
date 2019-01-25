package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.MembersBean;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/7.
 */

public class MemberManagerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MembersBean> list;
    private boolean dots;
    private List<Boolean> checkedStatus = new ArrayList<>();
    private int bottom;

    public MemberManagerAdapter(Context context, boolean type, List<Boolean> checkedStatus) {
        this.context = context;
        this.dots = type;
        bottom = context.getResources().getDimensionPixelSize(R.dimen.dp_15_x);
        list = new ArrayList<>();
        if (checkedStatus != null) {
            this.checkedStatus.clear();
            this.checkedStatus.addAll(checkedStatus);
        }
    }

    public void setData(ArrayList<MembersBean> ls, boolean ss, List<Boolean> checkedStatus) {
        list.clear();
        list.addAll(ls);
        dots = ss;
        this.checkedStatus.clear();
        this.checkedStatus.addAll(checkedStatus);
        notifyDataSetChanged();

    }

    public List<Boolean> getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(List<Boolean> checkedStatus) {
        this.checkedStatus = checkedStatus;
        notifyDataSetChanged();
    }

    public void setClearStatus() {
        //将状态清空 设置为false
        if (checkedStatus != null && checkedStatus.size() > 0) {
            for (int i = 0; i < checkedStatus.size(); i++) {
                checkedStatus.set(i, false);
            }
        }
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_member_manager, null);
            viewHolder.MemberImg = convertView.findViewById(R.id.img);
            viewHolder.memberName = convertView.findViewById(R.id.tv_name);
            viewHolder.memberTime = convertView.findViewById(R.id.tv_time);
            viewHolder.checkBox = convertView.findViewById(R.id.cb_quanxuan);
            viewHolder.linear = convertView.findViewById(R.id.rl_linear);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GlideApp.with(context)
                .load(list.get(i).getMemberImgUrl())
                .skipMemoryCache(false)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(viewHolder.MemberImg);
        viewHolder.memberName.setText(list.get(i).getMemberName());
        viewHolder.memberTime.setText(list.get(i).getMemberTime());
        if (dots) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            if (checkedStatus.get(i)) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkedStatus.get(i)) {
                        checkedStatus.set(i, false);
                    } else {
                        checkedStatus.set(i, true);
                    }
                    notifyDataSetChanged();
                }
            });
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.linear.getLayoutParams();
            int margins = (int) context.getResources().getDimension(R.dimen.width_2_80);
            lp.setMargins(margins, 0, margins, bottom);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.linear.getLayoutParams();
            int margins = (int) context.getResources().getDimension(R.dimen.width_4_80);
            int margins2 = (int) context.getResources().getDimension(R.dimen.width_2_80);

            lp.setMargins(margins, 0, margins, bottom);
        }


        return convertView;
    }

    public static class ViewHolder {
        com.hbird.base.mvc.widget.cycleView MemberImg;
        TextView memberName;
        TextView memberTime;
        CheckBox checkBox;
        LinearLayout linear;
    }
}
