package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.YearAndMonthBean;
import com.hbird.base.mvc.widget.IndicatorChartDownProgressBar;
import com.hbird.base.mvc.widget.IndicatorChartProgressBar;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import static android.R.attr.width;
import static com.hbird.base.R.dimen.width_7_80;

/**
 * Created by liul on 2018/7/17.
 */

public class XiaoLvRecyclerViewAdapter extends RecyclerView.Adapter<XiaoLvRecyclerViewAdapter.ViewHolder>{
    private ArrayList<YearAndMonthBean> mData;
    private XiaoLvRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    int itemPosition = 0;
    Context context;
    private double Max = 0;
    private MyTask task;
    private My2Task task2;
    private Double baseMoney;
    private int type;
    private boolean flag = true;

    public XiaoLvRecyclerViewAdapter(Context context, ArrayList<YearAndMonthBean> list, double maxMoney,int type) {
        this.mData = list;
        this.context = context;
        Max = maxMoney;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
         View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_fenxi_items, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定数据
        holder.setIsRecyclable(false);
        holder.dataText.setText(mData.get(position).getmDate()+"");
        holder.dataText.setTextSize(11);
        //判断是否是负数 这里模拟都是负数
        YearAndMonthBean chartDateBean = mData.get(position);
        Double money = chartDateBean.getMoney();
        String emptyTag = chartDateBean.getEmptyTag();
        baseMoney = money;
        for(int i=0;i<mData.size();i++){
            double money1 = mData.get(i).getMoney();
            if(money1<0){
                flag = false;
            }
        }
        if(flag){
            //说明没有负值  则底部隐藏
            holder.mBottoms.setVisibility(View.GONE);
        }else {
            holder.mBottoms.setVisibility(View.VISIBLE);
        }
        if(type==3){
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holder.mXiaolv.getLayoutParams();
            int margins = (int) context.getResources().getDimension(R.dimen.width_7_80);
            lp.setMargins(margins,0,margins,0);
            holder.mXiaolv.setLayoutParams(lp);
        }else if(type==6){
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holder.mXiaolv.getLayoutParams();
            int margins = (int) context.getResources().getDimension(R.dimen.width_1_80);
            lp.setMargins(margins,0,margins,0);
            holder.mXiaolv.setLayoutParams(lp);
        }else if(type==12) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holder.mXiaolv.getLayoutParams();
            if(position==0){
                int margins = (int) context.getResources().getDimension(R.dimen.width_2_80);
                lp.setMargins(margins,0,4,0);
            }else {
                lp.setMargins(4,0,4,0);
            }

            int width = (int) context.getResources().getDimension(R.dimen.width_7_80);
            lp.width = width;
            holder.mXiaolv.setLayoutParams(lp);
            holder.precentTextUp.setTextSize(10);
            holder.dataText.setTextSize(10);
            holder.precentTextDown.setTextSize(10);
        }
        if(!TextUtils.isEmpty(emptyTag)){
            //不为空则 表明是自己添加的空数据集合  空数据有自己的展示方式
            holder.precentTextUp.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.emptyViewUp.setVisibility(View.VISIBLE);

            holder.emptyViewDown.setVisibility(View.VISIBLE);
            holder.progressBarDown.setVisibility(View.INVISIBLE);
            holder.precentTextDown.setVisibility(View.INVISIBLE);
            holder.precentTextDown.setVisibility(View.INVISIBLE);
            holder.progressBarDown.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_chart_progress_yellow_bar));
        }else {
            if(money<0){
                //负数
                holder.precentTextUp.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.emptyViewUp.setVisibility(View.INVISIBLE);

                holder.emptyViewDown.setVisibility(View.INVISIBLE);
                holder.progressBarDown.setVisibility(View.VISIBLE);
                holder.precentTextDown.setVisibility(View.VISIBLE);
                holder.precentTextDown.setVisibility(View.VISIBLE);
                holder.progressBarDown.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_chart_progress_yellow_bar));
            }else {
                holder.precentTextUp.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.emptyViewUp.setVisibility(View.INVISIBLE);

                holder.emptyViewDown.setVisibility(View.GONE);
                holder.progressBarDown.setVisibility(View.GONE);
                holder.precentTextDown.setVisibility(View.GONE);
                holder.progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_chart_progress2_bar));
            }
        }




        int totalRegisterNum = 0;
        money = Math.abs(money);//绝对值
        if(Max != 0) {
            totalRegisterNum = (int) (money * 100 / Max);
            if(totalRegisterNum<1 && money!=0){
                totalRegisterNum =1;
                //手动修改money的值 解决：比值太小 柱状图看不出效果的问题 小于%1就等于%1
            }
        }
        if(totalRegisterNum==100){
            holder.precentTextUp.setText(totalRegisterNum+"");
            holder.precentTextDown.setText("-"+totalRegisterNum+"");
        }else {
            if(!TextUtils.isEmpty(emptyTag)){
                holder.precentTextUp.setText("无"+"%");
                holder.precentTextDown.setText("无"+"%");
            }else {
                if(totalRegisterNum>999){
                    totalRegisterNum=999;
                }
                if(totalRegisterNum>100){
                   /* if(type==12){
                        holder.precentTextUp.setText(totalRegisterNum+"");
                        holder.precentTextDown.setText("-"+totalRegisterNum);
                    }else {
                        holder.precentTextUp.setText(totalRegisterNum+"%");
                        holder.precentTextDown.setText("-"+totalRegisterNum+"%");
                    }*/
                    holder.precentTextUp.setText(totalRegisterNum+"%");
                    holder.precentTextDown.setText("-"+totalRegisterNum+"%");
                }else {
                    holder.precentTextUp.setText(totalRegisterNum+"%");
                    holder.precentTextDown.setText("-"+totalRegisterNum+"%");
                }

            }

        }

        if(baseMoney<0){
            holder.progressBarDown.setMax(100);
            setIpb2Value(totalRegisterNum , holder.progressBarDown , totalRegisterNum  , money );
            task = new MyTask(holder.progressBarDown);
            int money1 = (int) Math.round(money);
            task.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, money1);
        }else {
            holder.progressBar.setMax(100);
            setIpbValue(totalRegisterNum , holder.progressBar , totalRegisterNum  , money );
            task2 = new My2Task(holder.progressBar);
            int money1 = (int) Math.round(money);
            task2.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, money1);
        }

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

    private class My2Task extends AsyncTask<Integer, Integer, Object> {
        private IndicatorChartProgressBar idc;
        public My2Task(IndicatorChartProgressBar idc) {
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
   private class MyTask extends AsyncTask<Integer, Integer, Object> {
       private IndicatorChartDownProgressBar idc;
       public MyTask(IndicatorChartDownProgressBar idc) {
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
           setIpb2Value(progresses[0], idc, progresses[1] ,progresses[2]);
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
    private void setIpb2Value(final int progress,
                             final IndicatorChartDownProgressBar ipb_progress,
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
        TextView dataText;
        TextView precentTextUp;
        IndicatorChartProgressBar progressBar;
        RelativeLayout emptyViewUp;

        TextView precentTextDown;
        IndicatorChartDownProgressBar progressBarDown;
        RelativeLayout emptyViewDown;
        LinearLayout mXiaolv;

        LinearLayout mBottoms;

        public ViewHolder(View itemView) {
            super(itemView);
            //新的
            dataText = (TextView)itemView.findViewById(R.id.tv_item_data);
            precentTextUp = (TextView) itemView.findViewById(R.id.tv_item_precent);
            progressBar = (IndicatorChartProgressBar) itemView.findViewById(R.id.progressBar);
            emptyViewUp = (RelativeLayout) itemView.findViewById(R.id.view_empty);

            progressBarDown = (IndicatorChartDownProgressBar) itemView.findViewById(R.id.progressBar_down);
            emptyViewDown = (RelativeLayout) itemView.findViewById(R.id.view_empty_down);
            precentTextDown = (TextView) itemView.findViewById(R.id.tv_item_precent_down);
            mXiaolv = (LinearLayout) itemView.findViewById(R.id.ll_xiaolv);
            mBottoms = (LinearLayout) itemView.findViewById(R.id.ll_bottoms);

        }
    }
    //设置监听回调
    public void setOnItemClickListener(XiaoLvRecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }


}
