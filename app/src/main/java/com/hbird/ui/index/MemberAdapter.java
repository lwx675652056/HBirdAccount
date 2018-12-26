package com.hbird.ui.index;

import android.content.Context;

import com.hbird.base.databinding.RowMemberBinding;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

public class MemberAdapter extends BaseRecyclerAdapter<String,RowMemberBinding> {

    public MemberAdapter(Context context, List<String> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void onBindItem(RowMemberBinding binding, String s, int position) {
        binding.setBean(s);
        binding.setPosition(position);
    }
}
