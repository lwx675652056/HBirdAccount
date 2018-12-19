package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.ReturnBean.XiaoFeiBiLiReturn;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liul on 18/8/18.
 * 消费结构比 适配器
 */
public class XiaoFeiBiAdapter extends BaseAdapter {

    private List<XiaoFeiBiLiReturn.ResultBean> datas = new ArrayList<>();
    private Context context;

    public XiaoFeiBiAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<XiaoFeiBiLiReturn.ResultBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setShowType(String s) {
        notifyDataSetChanged();
    }

    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        XiaoFeiBiLiReturn.ResultBean bean = datas.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_item_xiaofei_bi, null);
            holder.monthTv =  convertView.findViewById(R.id.tv_up_month);
            holder.xiaofei =  convertView.findViewById(R.id.ll_xiaofei_a1);
            holder.texts =  convertView.findViewById(R.id.ll_texts);
            holder.emptyText =  convertView.findViewById(R.id.empty_text);
            holder.tvPrecent =  convertView.findViewById(R.id.tv_item_precent);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String time = datas.get(position).getTime();
        String[] split = time.split("-");
        String s = split[0];
        String s2 = split[1];
        if (position == 1) {
            holder.monthTv.setText(s + "年" + s2 + "月·环比");
        } else if (position == 2) {
            holder.monthTv.setText(s + "年" + s2 + "月·同比");
        } else if (position == 0) {
            holder.monthTv.setText(s + "年" + s2 + "月·本月");
        } else {
            holder.monthTv.setText(s + "年" + s2 + "月");//2018年5月
        }

        Double foodSpend = datas.get(position).getFoodSpend();
        Double monthSpend = datas.get(position).getMonthSpend();
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
            holder.texts.setVisibility(View.GONE);
            holder.emptyText.setVisibility(View.VISIBLE);
        } else {
            holder.texts.setVisibility(View.VISIBLE);
            holder.emptyText.setVisibility(View.GONE);

            holder.tvPrecent.setText(iPp + "%");
        }

        // 41%以下为贫困（吃货），41-50%（工作狂）温饱，60-51%（白领）小康，70-61%（小资）富裕，高于71%为土豪（最富）
        if (ppsa > 70) {//最富
            holder.xiaofei.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_bg_zuifu));
        } else if (ppsa > 60 && ppsa <= 70) {//小资
            holder.xiaofei.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_bg_xiaozi));
        } else if (ppsa > 50 && ppsa <= 60) {//白领
            holder.xiaofei.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_bg_bailing));
        } else if (ppsa > 40 && ppsa <= 50) {//工作狂
            holder.xiaofei.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_bg_work));
        } else if (ppsa < 41 && ppsa > 0) {//吃货
            holder.xiaofei.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_bg_chihuo));
        } else if (ppsa <= 0) {//无数据
            holder.xiaofei.setBackground(ContextCompat.getDrawable(context, R.mipmap.ic_bg_nodate));
        }

        return convertView;
    }

    class Holder {

        public LinearLayout xiaofei;
        public TextView monthTv;
        public LinearLayout texts;
        public TextView emptyText;
        public TextView tvPrecent;


    }


}
