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

public class TopAdapter3 extends BaseAdapter<StatisticsTopBean> {

    private OnItemClickListener listener;

    public TopAdapter3(Context context, List<StatisticsTopBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.listener = listener;
    }

    private int yyyy;

    public void setYyyy(int yyyy) {
        this.yyyy = yyyy;
        notifyDataSetChanged();
    }

    @Override
    protected void bindData(BaseViewHolder holder, StatisticsTopBean data, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(data.yyyy + "年");
        if (data.yyyy == yyyy) {
            tv.setTextColor(Color.parseColor("#D80200"));
        } else {
            tv.setTextColor(Color.parseColor("#808080"));
        }

        holder.setOnItemClickListener(v -> listener.onClick(position, data, 0));
    }
}
