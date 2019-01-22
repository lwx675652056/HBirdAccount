package com.hbird.ui.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.hbird.base.databinding.RowEditAccountBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;

import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseRecyclerAdapter;
import sing.util.SharedPreferencesUtil;

/**
 * @author: LiangYX
 * @ClassName: EditAccountAdapter
 * @date: 2018/12/28 11:39
 * @Description: 账户编辑
 */
public class EditAccountAdapter extends BaseRecyclerAdapter<AssetsBean, RowEditAccountBinding> {

    private OnItemClickListener listener;
    private boolean isDelete;// 是否为删除状态
    private List<Integer> str;// 已添加的
    private List<AssetsBean> list;// 已添加的

    public EditAccountAdapter(Context context, List<AssetsBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.listener = listener;
        this.list = list;

        String temp2 = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
        List<AssetsBean> temp = JSON.parseArray(temp2, AssetsBean.class);// 缓存的已添加的账户

        str = new ArrayList<>();
        if (temp != null && temp.size() > 0) {
            for (AssetsBean s : temp) {
                str.add(s.assetsType);
            }
        }
    }

    public void setData(boolean isDelete) {
        this.isDelete = isDelete;
        notifyItemRangeChanged(0, list.size());
    }

    @Override
    protected void onBindItem(RowEditAccountBinding binding, AssetsBean data, int position) {
        binding.setBean(data);
        binding.setPosition(position);
        binding.setIsDelete(isDelete);
        binding.clParent.setOnClickListener(v -> listener.onClick(position, data, 0));
    }
}