package com.hbird.ui.assets;

import android.content.Context;

import com.hbird.base.databinding.RowAssetsBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.AssetsBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: AssetsAdapter
 * @date: 2018/12/29 16:27
 * @Description: 资产首页
 */
public class AssetsAdapter extends BaseRecyclerAdapter<AssetsBean, RowAssetsBinding> {

    private List<AssetsBean> list;
    private OnItemClickListener listener;

    public AssetsAdapter(Context context, List<AssetsBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.listener = listener;
    }

    @Override
    protected void onBindItem(RowAssetsBinding binding, AssetsBean bean, int position) {
        binding.setLast(position == list.size() - 1);
        binding.setBean(bean);

        binding.llContent.setOnClickListener(v -> listener.onClick(position, bean, 0));
    }
}