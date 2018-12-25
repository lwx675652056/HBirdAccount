package com.hbird.ui.detailed;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hbird.base.R;
import com.hbird.base.databinding.RowAccountDetailedBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.AccountDetailedBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: AccountDetailedModle
 * @date: 2018/12/20 14:53
 * @Description: 首页的更多 账本明细
 */
public class AccountDetailedAdapter extends BaseRecyclerAdapter<AccountDetailedBean, RowAccountDetailedBinding> {

    private List<AccountDetailedBean> list;
    private Context context;
    private OnItemClickListener listener;

    public AccountDetailedAdapter(Context context, List<AccountDetailedBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onBindItem(RowAccountDetailedBinding binding, AccountDetailedBean accountDetailedBean, int position) {
        binding.setBean(accountDetailedBean);
        binding.llView.setVisibility(View.GONE);
        if (position != 0) {
            if (position == list.size() - 1) {
                binding.llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_bottom_white));
                binding.llView.setVisibility(View.GONE);
            } else {
                if (list.get(position + 1).getIndexBeen() != null && list.get(position + 1).getIndexBeen().size() > 0) {
                    binding.llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_bottom_white));
                    binding.llView.setVisibility(View.GONE);
                } else {
                    binding.llContent.setBackground(ContextCompat.getDrawable(context, R.drawable.select_cencer_white));
                    binding.llView.setVisibility(View.VISIBLE);
                }
            }
        }

        if (list.get(position).getIndexBeen() != null && list.get(position).getIndexBeen().size() > 0) {
            binding.llView.setVisibility(View.GONE);
        }

        if (position == list.size()-1){
            binding.bottomView.setVisibility(View.VISIBLE);
        }else{
            binding.bottomView.setVisibility(View.GONE);
        }
        binding.llContent.setOnClickListener(v -> listener.onClick(position,accountDetailedBean,0));
        binding.llContent.setOnLongClickListener(v -> {
            listener.onClick(position,accountDetailedBean,1);
            return false;
        });
    }
}