package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.widget.IndicatorChartDownProgressBar;
import com.hbird.base.mvc.widget.IndicatorChartProgressBar;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * Created by liul on 2018/7/17.
 */

public class XiaoLv2RecyclerViewAdapter extends RecyclerView.Adapter<XiaoLv2RecyclerViewAdapter.ViewHolder>{
    private ArrayList<YearAndMonthBean> mData;
    private XiaoLv2RecyclerViewAdapter.OnItemClickListener onItemClickListener;
    int itemPosition = 0;
    Context context;
    private double Max = 0;
    private MyTask task;
    private Double baseMoney;
    private int types;

    public XiaoLv2RecyclerViewAdapter(Context context, ArrayList<YearAndMonthBean> list, double maxMoney,int types) {
        this.mData = list;
        this.context = context;
        Max = maxMoney;
        this.types = types;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
         View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_fenxi2_items, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定数据
        holder.setIsRecyclable(false);
        //判断是否是负数 这里模拟都是负数
        YearAndMonthBean chartDateBean = mData.get(position);
        Double money = chartDateBean.getMoney();
        baseMoney = money;
        if(types==3){
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holder.contents.getLayoutParams();
            int margins = (int) context.getResources().getDimension(R.dimen.width_7_80);
            lp.setMargins(margins,0,margins,0);
            holder.contents.setLayoutParams(lp);
        }else if(types==6){
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holder.contents.getLayoutParams();
            int margins = (int) context.getResources().getDimension(R.dimen.width_1_80);
            lp.setMargins(margins,0,margins,0);
            holder.contents.setLayoutParams(lp);
        }else if(types==12) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holder.contents.getLayoutParams();
            lp.setMargins(4,0,4,0);
            int width = (int) context.getResources().getDimension(R.dimen.width_7_80);
            lp.width = width;
            holder.contents.setLayoutParams(lp);
            holder.precentTexts.setTextSize(10);
            holder.precentData.setTextSize(10);
        }
        if(types==12){
            holder.precentData.setText(mData.get(position).getmDate()+"");
        }else {
            holder.precentData.setText(mData.get(position).getmDate()+"");
        }

        money = Math.abs(money);//绝对值
        int totalRegisterNum = 0;
        if(Max != 0) {
            totalRegisterNum = (int) (money * 100 / Max);
            if(totalRegisterNum<1 && money!=0){
                totalRegisterNum =1;
                //手动修改money的值 解决：比值太小 柱状图看不出效果的问题 小于%1就等于%1
            }
        }
        if(!TextUtils.isEmpty(chartDateBean.getEmptyTag())){
            holder.emptys.setVisibility(View.VISIBLE);
            holder.progressBars.setVisibility(View.GONE);
        }else {
            holder.emptys.setVisibility(View.GONE);
            holder.progressBars.setVisibility(View.VISIBLE);
        }

        if(totalRegisterNum>99){
            if(totalRegisterNum>999){
                totalRegisterNum=999;
            }
           /* if(types==12){
                holder.precentTexts.setText(totalRegisterNum+"");
            }else {
                holder.precentTexts.setText(totalRegisterNum+"%");
            }*/
            holder.precentTexts.setText(totalRegisterNum+"%");
        }else {
            if(!TextUtils.isEmpty(chartDateBean.getEmptyTag())){
                holder.precentTexts.setText("无"+"%");
            }else {
                if(totalRegisterNum<-999){
                    totalRegisterNum=-999;
                }
                holder.precentTexts.setText(totalRegisterNum+"%");
            }

        }


        holder.progressBars.setMax(100);
        setIpbValue(totalRegisterNum , holder.progressBars , totalRegisterNum  , money );
        task = new MyTask(holder.progressBars);
        int money1 = (int) Math.round(money);
        task.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, money1);
     /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos);
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener!=null){
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView,pos);
                }
                //此事件被消费 不会触发单击事件
                return true;
            }
        });*/
    }

   private class MyTask extends AsyncTask<Integer, Integer, Object> {
       private IndicatorChartProgressBar idc;
       public MyTask(IndicatorChartProgressBar idc) {
           this.idc = idc;
       }

       //doInBackground方法内部执行后台任务,不可在此方法内修改UI
       @Override
       protected Void doInBackground(Integer... params) {
           try {
               for (int i = 0; i <= params[0]; i++) {
                   //调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                   publishProgress(i, params[0], params[1]);
                   //为了演示进度,休眠1毫秒  随便睡（时间越久越慢而已）
                   //Thread.sleep(1);
               }
           } catch (Exception e) {

           }
           return null;
       }

       //onProgressUpdate方法用于更新进度信息
       @Override
       protected void onProgressUpdate(Integer... progresses) {
           setIpbValue(progresses[0], idc, progresses[1] ,progresses[2]);
       }
   }

    private void setIpbValue(final int progress,
                             final IndicatorChartProgressBar ipb_progress,
                             final int total,
                             final double preTotal) {
        ipb_progress.setProgress(progress);
        ipb_progress.setProgressToal(total);
        ipb_progress.invalidate();
        ipb_progress.requestLayout();
    }


    public void setItemClick(int i){
        this.itemPosition = i;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //新的
        TextView precentTexts;
        TextView precentData;
        IndicatorChartProgressBar progressBars;
        View emptyViewDown;
        RelativeLayout emptys;
        LinearLayout contents;

        public ViewHolder(View itemView) {
            super(itemView);
            //新的

            progressBars = (IndicatorChartProgressBar) itemView.findViewById(R.id.progressBars);
            emptyViewDown = (View) itemView.findViewById(R.id.view_empty_down);
            precentTexts = (TextView) itemView.findViewById(R.id.tv_precent);
            precentData = (TextView) itemView.findViewById(R.id.tv_data);
            emptys = (RelativeLayout) itemView.findViewById(R.id.view_emptys);
            contents = (LinearLayout) itemView.findViewById(R.id.ll_contents);


        }
    }
    //设置监听回调
    public void setOnItemClickListener(XiaoLv2RecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }


}
