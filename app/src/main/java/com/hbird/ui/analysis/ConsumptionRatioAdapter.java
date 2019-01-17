package com.hbird.ui.analysis;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.hbird.base.R;
import com.hbird.base.databinding.RowConsumptionRatioBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.ConsumptionRatioBean;

import java.text.DecimalFormat;
import java.util.List;

import sing.common.base.BaseRecyclerAdapter;


/**
 * @author: LiangYX
 * @ClassName: ConsumptionRatioAdapter
 * @date: 2019/1/11 15:45
 * @Description: 图标 -- 分析 -- 消费结构比
 */
public class ConsumptionRatioAdapter extends BaseRecyclerAdapter<ConsumptionRatioBean, RowConsumptionRatioBinding> {

    private Context context;
    private OnItemClickListener listener;
    public ConsumptionRatioAdapter(Context context, List<ConsumptionRatioBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onBindItem(RowConsumptionRatioBinding binding, ConsumptionRatioBean bean, int position) {
        binding.setBean(bean);

        String year = bean.time.split("-")[0];
        String month = bean.time.split("-")[1];
        if (position == 1) {
            binding.setTime(year + "年" + month + "月·环比");
        } else if (position == 2) {
            binding.setTime(year + "年" + month + "月·同比");
        } else if (position == 0) {
            binding.setTime(year + "年" + month + "月·本月");
        } else {
            binding.setTime(year + "年" + month + "月");//2018年5月
        }

        Double foodSpend = bean.foodSpend;
        Double monthSpend = bean.monthSpend;
        //计算比例为 非食物支出/总支出
        double dd = 0;
        if (monthSpend != null && monthSpend != 0) {
            if (null == foodSpend) {
                foodSpend = 0.0;
            }
            dd = (monthSpend-foodSpend) / monthSpend;
        }else{// 月没有支出
            dd = 0;
        }
        double ppsa = dd * 100;
        DecimalFormat df = new DecimalFormat("0");
        String iPp = df.format(ppsa);
        if (dd == 0) {
            binding.setNoData(true);
        } else {
            binding.setNoData(false);
            binding.setRatio(iPp + "%");
        }

        // 41%以下为贫困（吃货），41-50%（工作狂）温饱，60-51%（白领）小康，70-61%（小资）富裕，高于71%为土豪（最富）
        if (ppsa > 70) {//最富
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.mipmap.bg_fxjgb_zuifu_normal));
        } else if (ppsa > 60 && ppsa <= 70) {//小资
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.mipmap.bg_xfjgb_gongzuokuang_normal));
        } else if (ppsa > 50 && ppsa <= 60) {//白领
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.mipmap.bg_xfjgb_chihuo_normal));
        } else if (ppsa > 40 && ppsa <= 50) {//工作狂
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.mipmap.bg_xfjgb_gongzuokuang_normal));
        } else if (ppsa < 41 && ppsa > 0) {//吃货
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.mipmap.bg_xfjgb_chihuo_normal));
        } else if (ppsa <= 0) {//无数据
            binding.llParent.setBackground(ContextCompat.getDrawable(context, R.mipmap.bg_fxjgb_wushuju_normal));
        }

        // 点击事件
        binding.llParent.setOnClickListener(v -> listener.onClick(position,bean,0));
    }
}