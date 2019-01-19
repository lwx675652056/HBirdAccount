package com.hbird.ui.statistics;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.StatisticsTopBean;

import java.util.List;

import sing.BaseAdapter;
import sing.BaseViewHolder;

public class TopAdapter1 extends BaseAdapter<StatisticsTopBean> {

    private OnItemClickListener listener;

    public TopAdapter1(Context context, List<StatisticsTopBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.listener = listener;
    }

    private int yyyy, mm;

    public void setYyyy(int yyyy, int mm) {
        this.yyyy = yyyy;
        this.mm = mm;
        notifyDataSetChanged();
    }

    @Override
    protected void bindData(BaseViewHolder holder, StatisticsTopBean data, int position) {
        TextView tv = holder.getView(R.id.tv);
        if (position == 0) {
            tv.setText(data.yyyy + "年" + data.getMM() + "月");
        } else {
            tv.setText(data.getMM() + "月");
        }
        if (data.yyyy == yyyy && data.mm == mm) {
            tv.setTextColor(Color.parseColor("#D80200"));
        } else {
            tv.setTextColor(Color.parseColor("#808080"));
        }

        holder.setOnItemClickListener(v -> listener.onClick(position, data, 0));
    }
}
