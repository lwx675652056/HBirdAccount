package com.hbird.ui.index;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.hbird.base.databinding.RowIndexBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: IndexAdapter
 * @date: 2018/12/26 10:23
 * @Description: 首页
 */
public class IndexAdapter extends BaseRecyclerAdapter<AccountDetailedBean, RowIndexBinding> {

    private List<AccountDetailedBean> list;
    private Context context;
    private OnItemClickListener listener;
    private int persionId = 0;

    public IndexAdapter(Context context, List<AccountDetailedBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.context = context;
        this.listener = listener;
        String s = SPUtil.getPrefString(context, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        if (!TextUtils.isEmpty(s)) {
            persionId = Integer.parseInt(s);
        }
    }

    @Override
    protected void onBindItem(RowIndexBinding binding, AccountDetailedBean accountDetailedBean, int position) {
        binding.setPosition(position);
        binding.setBean(accountDetailedBean);

        binding.llView.setVisibility(View.GONE);
        if (position != 0) {
            if (position == list.size() - 1) {
                binding.llView.setVisibility(View.GONE);
            } else {
                if (list.get(position + 1).getIndexBeen() != null && list.get(position + 1).getIndexBeen().size() > 0) {
                    binding.llView.setVisibility(View.GONE);
                } else {
                    binding.llView.setVisibility(View.VISIBLE);
                }
            }
        }

        if (list.get(position).getIndexBeen() != null && list.get(position).getIndexBeen().size() > 0) {
            binding.llView.setVisibility(View.GONE);
        }

        if (accountDetailedBean != null) {
            if (accountDetailedBean.getUpdateBy() == null || accountDetailedBean.getUpdateBy() == persionId) {
                binding.imagess.setBorderColor(Color.parseColor("#D80200"));
            } else {
                binding.imagess.setBorderColor(Color.parseColor("#41AB14"));
            }
        }

        binding.llContent.setOnClickListener(v -> listener.onClick(position, accountDetailedBean, 0));
//        binding.llContent.setOnLongClickListener(v -> {
//            listener.onClick(position, accountDetailedBean, 1);
//            return true;
//        });
    }
}