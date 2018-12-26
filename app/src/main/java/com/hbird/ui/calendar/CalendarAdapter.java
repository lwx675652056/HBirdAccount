package com.hbird.ui.calendar;

import android.content.Context;

import com.hbird.base.databinding.RowCalendarBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.AccountDetailedBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: CalendarAdapter
 * @date: 2018/12/26 17:08
 * @Description: 日历
 */
public class CalendarAdapter extends BaseRecyclerAdapter<AccountDetailedBean, RowCalendarBinding> {

    private List<AccountDetailedBean> list;
    private OnItemClickListener listener;

    public CalendarAdapter(Context context, List<AccountDetailedBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.listener = listener;
    }

    @Override
    protected void onBindItem(RowCalendarBinding binding, AccountDetailedBean bean, int position) {
        binding.setBean(bean);
        binding.setLastPosition(position == list.size() - 1);

        binding.llContent.setOnClickListener(v -> listener.onClick(position, bean, 0));
        binding.llContent.setOnLongClickListener(v -> {
            listener.onClick(position, bean, 1);
            return false;
        });
    }
}