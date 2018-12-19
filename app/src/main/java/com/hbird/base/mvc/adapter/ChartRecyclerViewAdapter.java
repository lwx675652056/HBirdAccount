package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.ChartDateBean;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.widget.IndicatorChartProgressBar;
import com.hbird.base.mvc.widget.IndicatorProgressBar;
import com.hbird.base.util.L;
import com.hbird.base.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import static com.umeng.commonsdk.proguard.g.t;

/**
 * Created by liul on 2018/7/17.
 */

public class ChartRecyclerViewAdapter extends RecyclerView.Adapter<ChartRecyclerViewAdapter.ViewHolder>{
    private ArrayList<YearAndMonthBean> mData;
    private ChartRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    int itemPosition = 0;
    Context context;
    private double Max = 0;
    private MyTask task;

    public ChartRecyclerViewAdapter(Context context, ArrayList<YearAndMonthBean> list, double maxMoney, int pos) {
        this.mData = list;
        this.context = context;
        Max = maxMoney;
        this.itemPosition = pos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
         View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recycle_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定数据
        holder.setIsRecyclable(false);
        holder.mTv.setText(mData.get(position).getmDate()+"");
        if(position==itemPosition){
            holder.mTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.checks.setVisibility(View.VISIBLE);
            if(mData.get(position).getMoney()==0){
                holder.progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_chart_progress3_bar));
            }else {
                holder.progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_chart_progress2_bar));
            }

        }else {
            holder.mTv.setTextColor(context.getResources().getColor(R.color.text_808080));
            holder.progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_chart_progress_bar));
            holder.checks.setVisibility(View.GONE);
        }
        YearAndMonthBean chartDateBean = mData.get(position);
        Double money = chartDateBean.getMoney();
        int totalRegisterNum = 0;
        if(Max != 0) {
            totalRegisterNum = (int) (money * 100 / Max);
            if(totalRegisterNum<1 && money!=0){
                totalRegisterNum =1;
                //手动修改money的值 解决：比值太小 柱状图看不出效果的问题 小于%1就等于%1
            }
        }
        holder.progressBar.setMax(100);
        setIpbValue(totalRegisterNum , holder.progressBar , totalRegisterNum  , money );
        task = new MyTask(holder.progressBar);
        int money1 = (int) Math.round(money);
        //task.executeOnExecutor(Executors.newCachedThreadPool() , totalRegisterNum, money1);
        //去掉动画效果 解决内存溢出的问题（以下）
        task.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, money1);

        //task.cancel(true);
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
        });
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
        TextView mTv;
        View views;
        ImageView checks;
        IndicatorChartProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.item_tv);
            progressBar = (IndicatorChartProgressBar) itemView.findViewById(R.id.progressBar);
            views = (View) itemView.findViewById(R.id.view_gray);
            checks = (ImageView) itemView.findViewById(R.id.iv_check);

        }
    }
    //设置监听回调
    public void setOnItemClickListener(ChartRecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

  /*  //添加新的条目 测试
    public void addNewItem() {
        if(mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(0, "新添加条目");
        notifyItemInserted(0);
    }*/

    //删除第一条条目信息
    public void deleteItem() {
        if(mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(0);
        notifyItemRemoved(0);
    }


}
