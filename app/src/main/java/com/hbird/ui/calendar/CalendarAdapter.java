package com.hbird.ui.calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.hbird.base.databinding.RowCalendarBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.util.SPUtil;
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
    private int persionId = 0;
    public CalendarAdapter(Context context, List<AccountDetailedBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.listener = listener;
        String s = SPUtil.getPrefString(context, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        if (!TextUtils.isEmpty(s)) {
            persionId = Integer.parseInt(s);
        }
    }

    @Override
    protected void onBindItem(RowCalendarBinding binding, AccountDetailedBean bean, int position) {
        binding.setBean(bean);
        binding.setLastPosition(position == list.size() - 1);

        if (bean != null) {
            if (bean.getUpdateBy() == null || bean.getUpdateBy() == persionId) {
                binding.imagess.setBorderColor(Color.parseColor("#D80200"));
            } else {
                binding.imagess.setBorderColor(Color.parseColor("#41AB14"));
            }
        }

        binding.llContent.setOnClickListener(v -> listener.onClick(position, bean, 0));
        binding.llContent.setOnLongClickListener(v -> {
            listener.onClick(position, bean, 1);
            return false;
        });
    }
}