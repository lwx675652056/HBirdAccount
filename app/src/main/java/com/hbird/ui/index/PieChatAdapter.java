package com.hbird.ui.index;

import android.content.Context;
import android.view.View;

import com.hbird.base.databinding.RowPiechatBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DateUtils;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: PieChatAdapter
 * @date: 2019/1/10 16:47
 * @Description: 单笔消费排行榜
 */
public class PieChatAdapter extends BaseRecyclerAdapter<WaterOrderCollect,RowPiechatBinding> {

    private List<WaterOrderCollect> list;
    private OnItemClickListener listener;
    public PieChatAdapter(Context context, List<WaterOrderCollect> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.list = list;
        this.listener = listener;
    }

    @Override
    protected void onBindItem(RowPiechatBinding binding, WaterOrderCollect s, int position) {
        String time = DateUtils.stampToString(s.getChargeDate(),"yyyy.MM.dd");
        binding.setTime(time);
        binding.setBean(s);
        binding.setPos(position);
        binding.setLastPos(position==list.size()-1);
        binding.parent.setOnClickListener((View.OnClickListener) v -> listener.onClick(position,s,0));
    }
}
