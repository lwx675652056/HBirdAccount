package com.hbird.ui.account;

import android.content.Context;

import com.hbird.base.databinding.RowEditAccountBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.AssetsBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: EditAccountAdapter
 * @date: 2018/12/28 11:39
 * @Description: 账户编辑
 */
public class EditAccountAdapter extends BaseRecyclerAdapter<AssetsBean, RowEditAccountBinding> {

    private OnItemClickListener listener;
    private boolean isDelete;// 是否为删除状态

    public EditAccountAdapter(Context context, List<AssetsBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.listener = listener;
    }

    public void setData(boolean isDelete) {
        this.isDelete = isDelete;
        notifyDataSetChanged();
    }

    @Override
    protected void onBindItem(RowEditAccountBinding binding, AssetsBean data, int position) {
        binding.setBean(data);
        binding.setPosition(position);
        binding.setIsDelete(isDelete);
        binding.clParent.setOnClickListener(v -> listener.onClick(position,data,0));
    }
}