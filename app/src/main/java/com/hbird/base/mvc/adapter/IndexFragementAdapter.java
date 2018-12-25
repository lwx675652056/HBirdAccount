package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.ui.index.IndexFragement;

import java.util.List;

public class IndexFragementAdapter extends BaseAdapter {

    List<indexBaseListBean> listBean;
    Context context;
    IndexFragement mContext;

    public IndexFragementAdapter(Context context, IndexFragement mContext, List<indexBaseListBean> list) {
        this.context = context;
        this.mContext = mContext;
        this.listBean = list;
    }

    public void loadMore(List<indexBaseListBean> list) {
        this.listBean.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_index_fragement, null);
            holder.topLine = convertView.findViewById(R.id.ll_title);
            holder.topDate = convertView.findViewById(R.id.tv_date);
            holder.rlMybg = convertView.findViewById(R.id.rl_mybg);
            holder.topWeek = convertView.findViewById(R.id.tv_week);
            holder.topShouRu = convertView.findViewById(R.id.tv_shouru);
            holder.topZhiChu = convertView.findViewById(R.id.tv_zhichu);
            holder.imgs = convertView.findViewById(R.id.imagess);

            holder.downLine = convertView.findViewById(R.id.ll_content);
            holder.downIcon = convertView.findViewById(R.id.iv_icon);
            holder.downName = convertView.findViewById(R.id.tv_shour);
            holder.downMood = convertView.findViewById(R.id.iv_mood);
            holder.downPay = convertView.findViewById(R.id.tv_pay_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        indexBaseListBean bean = listBean.get(position);

        if (bean.getTag() == 0) {
            //显示标题条目
            holder.topLine.setVisibility(View.VISIBLE);
            holder.downLine.setVisibility(View.GONE);
            long s = bean.getIndexBeen().get(0).getDayTime();
            String monthDay = DateUtils.getMonthDay(s);
            String day = DateUtils.getDay(s);
            String weekDay = DateUtil.dateToWeek(day);
            holder.topDate.setText(monthDay);
            holder.topWeek.setText(weekDay);
            holder.topShouRu.setText("收入：" + bean.getIndexBeen().get(0).getDayIncome());
            holder.topZhiChu.setText("支出：" + bean.getIndexBeen().get(0).getDaySpend());
        } else if (listBean.get(position).getTag() == 1) {
            //数据条目
            holder.topLine.setVisibility(View.GONE);
            holder.downLine.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(bean.getRemark())) {
                holder.downName.setText(bean.getTypeName());
            } else {
                holder.downName.setText(bean.getRemark());
            }

            Integer i = bean.getSpendHappiness();
            if (null == i) {
                holder.downMood.setVisibility(View.GONE);
            } else {
                if (i == 0) {
                    //开心
                    holder.downMood.setImageResource(R.mipmap.icon_mood_happy_blue);
                    holder.downMood.setVisibility(View.VISIBLE);
                } else if (i == 1) {
                    //一般
                    holder.downMood.setImageResource(R.mipmap.icon_mood_normal_blue);
                    holder.downMood.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    holder.downMood.setImageResource(R.mipmap.icon_mood_blue);
                    holder.downMood.setVisibility(View.VISIBLE);
                } else {
                    holder.downMood.setVisibility(View.GONE);
                }
            }

            double money = bean.getMoney();
            if (listBean.get(position).getOrderType() == 1) {//支出
                if (money < 0) {
                    double moneys = Math.abs(money);
                    holder.downPay.setText("+ " + moneys);
                } else {
                    holder.downPay.setText("- " + money);
                }

                holder.downPay.setTextColor(ContextCompat.getColor(context,R.color.text_333333));
                holder.rlMybg.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cycle_blue));
            } else {    //收入
                if (money < 0) {
                    double moneys = Math.abs(money);
                    holder.downPay.setText("-" + moneys);
                } else {
                    holder.downPay.setText("+" + money);
                }
                holder.downMood.setVisibility(View.GONE);
                holder.downPay.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                holder.rlMybg.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_cycle_yellow));
            }

            String icon = listBean.get(position).getIcon();
            Glide.with(mContext).load(icon).into(holder.downIcon);
            Glide.with(mContext).load(bean.getReporterAvatar()).into(holder.imgs);
        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout topLine;
        LinearLayout downLine;
        RelativeLayout rlMybg;
        TextView topDate;
        TextView topWeek;
        TextView topShouRu;
        TextView topZhiChu;
        ImageView downIcon;
        TextView downName;
        ImageView downMood;
        TextView downPay;
        com.hbird.base.mvc.widget.cycleView imgs;
    }
}