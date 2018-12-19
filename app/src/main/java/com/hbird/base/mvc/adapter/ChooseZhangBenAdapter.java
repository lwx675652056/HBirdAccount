package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hbird.base.R;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.mvc.bean.AccountBookBean;

import java.util.List;

import sing.BaseViewHolder;
import sing.util.ScreenUtil;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/8.
 */

public class ChooseZhangBenAdapter extends sing.BaseAdapter<AccountBookBean.AccountBean> {

    private int width = 0;
    private OnItemClickListener listener;

    public void setData(List<AccountBookBean.AccountBean> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public ChooseZhangBenAdapter(Context context, List<AccountBookBean.AccountBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        int temp = context.getResources().getDimensionPixelSize(R.dimen.dp_35_x);
        width = (ScreenUtil.getScreenWidth(context) - temp * 3) / 2;

        this.listener = listener;
    }

    @Override
    protected void bindData(BaseViewHolder holder, AccountBookBean.AccountBean data, int position) {
        ImageView iv = holder.getView(R.id.iv_icon);
        LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) iv.getLayoutParams();
        l.width = width;
        l.height = width;
        iv.setLayoutParams(l);

        Glide.with(iv.getContext()).load(data.iconDescribe).into(iv);

        iv.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position, data, 0);
            }
        });
    }
}
