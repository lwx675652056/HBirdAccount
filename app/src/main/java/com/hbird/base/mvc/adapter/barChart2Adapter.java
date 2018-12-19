package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.CeshiDateBean;
import com.hbird.base.mvc.bean.QingXuDateBean;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvc.widget.IndicatorProgressBar;
import com.hbird.base.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by liul on 2018/06/30.
 */

public class barChart2Adapter extends BaseAdapter {
    private Context context;
    private List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> moodDateList = new ArrayList<>();

    private double Max = 0;
    private boolean isShow = true;
    private boolean isProvinceShow = true;
    private String formatNum;

    public barChart2Adapter(Context context, List<chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean> moodDateList,
                            boolean isProvinceShow,int number){
        this.context = context;
        this.moodDateList = moodDateList;
        this.isProvinceShow = isProvinceShow;
        //遍历集合，获取最大值
       /* for (int i=0;i<moodDateList.size();i++){
            chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean provincesRegisterStatBean = moodDateList.get(i);
            int total_register = provincesRegisterStatBean.getCount();

            if(Max < total_register){
                Max = total_register;
            }
        }*/
        Max = number;
        //设置不能最大充满
        //Max *= 1.1;

    }



    public void setDate2(boolean isShow){
        this.isShow = isShow;
        for (chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean p: moodDateList){
            p.setFrist(true);
        }
    }

    @Override
    public int getCount() {
        return moodDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return moodDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_bar_chart2 , null);
        TextView textaddr = (TextView) convertView.findViewById(R.id.tv_name_first_progress);
        IndicatorProgressBar idc = (IndicatorProgressBar) convertView.findViewById(R.id.ipb_first_progre);
        TextView textNum = (TextView) convertView.findViewById(R.id.text_item_bar_contory);
        TextView percent = (TextView) convertView.findViewById(R.id.tv_bili);
        ImageView imageAnmins = (ImageView) convertView.findViewById(R.id.image_item_animes);
        ImageView mood = (ImageView) convertView.findViewById(R.id.iv_mood);

        chartToRankingReturn.ResultBean.StatisticsSpendHappinessArraysBean bean = moodDateList.get(position);

        int preTotalRegisterNum = bean.getCount();
        int totalRegisterNum = 0;
        if(Max != 0) {
            totalRegisterNum = (int) (preTotalRegisterNum * 100 / Max);
            DecimalFormat df   = new DecimalFormat("######0.00");
            formatNum = df.format(preTotalRegisterNum * 100 / Max);

            if(totalRegisterNum<10 && totalRegisterNum!=0){
                preTotalRegisterNum = (int) (Max/10);
            }
        }
        percent.setText(formatNum+"%");
        if(totalRegisterNum<10 && preTotalRegisterNum!=0){
            totalRegisterNum=10;
        }
        if(null!=bean.getSpendHappiness()){
            if(bean.getSpendHappiness()==0){
                textaddr.setText("高兴");
                mood.setImageResource(R.mipmap.icon_mood_happy_blue);
            }else if(bean.getSpendHappiness()==1){
                textaddr.setText("一般");
                mood.setImageResource(R.mipmap.icon_mood_normal_blue);
            }else if(bean.getSpendHappiness()==2){
                textaddr.setText("差");
                mood.setImageResource(R.mipmap.icon_mood_blue);
            }else {

            }
        }else {

        }

        textNum.setText(bean.getCount()+"笔");

        idc.setMax(100);

        setIpbValue(totalRegisterNum , idc , imageAnmins , totalRegisterNum  , preTotalRegisterNum );

        if (bean.isFrist() && isProvinceShow){

            if (isShow){
                MyTask task = new MyTask(idc, imageAnmins);
                //task.executeOnExecutor(Executors.newCachedThreadPool() , totalRegisterNum, preTotalRegisterNum);
                task.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, preTotalRegisterNum);
            }else{
                MyTask task = new MyTask(idc, imageAnmins);
                //task.executeOnExecutor(Executors.newCachedThreadPool() , totalRegisterNum, preTotalRegisterNum);
                task.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, preTotalRegisterNum);
            }
            //不想要动画效果直接设置为false
            //bean.setFrist(true);
        }
        return convertView;
    }

    public class ViewHoder{
        ImageView imagetitle;
        TextView textaddr;
        IndicatorProgressBar idc;
        ImageView imageAnmin;
        TextView textNum;
        TextView textNums;
    }

    private void setIpbValue(final int progress,
                             final IndicatorProgressBar ipb_progress,
                             final ImageView iv_ipb_progress_above,
                             final int total,
                             final int preTotal) {
       // Rect bounds = new Rect(0, 0, Utils.dpTopx(context, 40), Utils.dpTopx(context, 30));
//        Drawable indicatorDrawableFirst = context.getResources().getDrawable(R.drawable.pb_indicator_first);
//        indicatorDrawableFirst.setBounds(bounds);
//        ipb_progress.setmIndicatorDrawable(indicatorDrawableFirst);
/*
        ipb_progress.setmOnProgressUpdateListener(new IndicatorProgressBar.OnProgressUpdateListener() {
            @Override
            public void getUpdateWidht(int widthArgs) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_ipb_progress_above.getLayoutParams();
//                params.setMargins(ipb_progress.getProgressWidth() + PX2DPUtil.dp2px(mActivity, 33), 0, 0, 0);
                params.setMargins(widthArgs + Utils.dpTopx(context, 0), 0, 0, 0);
//        Log.i(TAG, progress+"==进度条的progress和它的的实时right==="+bounds.right+"===="+bounds.bottom);
//                Log.i(TAG, progress+"==进度条的progress和它的的实时right==="+ipb_progress.getProgressWidth()+"====preTotal=="+preTotal);
//                Log.i(TAG, progress+"==widthArgs==="+widthArgs);
                iv_ipb_progress_above.setLayoutParams(params);
            }
        });*/
        ipb_progress.setProgress(progress);
        ipb_progress.setProgressToal(total);
        //给指示器上面的text赋值
//        int pStr = progress * max / 100;
//        if (progress == total) {
//            ((AnimationDrawable)iv_ipb_progress_above.getDrawable()).stop();
//        }
//        tv_desc_progress.setText("(" + pStr + "/" + total + ")");
//        final int finalPStr = pStr;
//        ipb_progress.setmOnIndicatorTextChangeListener(new IndicatorProgressBar.OnIndicatorTextChangeListener() {
//            @Override
//            public String getText() {
//                return String.valueOf(finalPStr);
//            }
//        });


//        ipb_progress.setIndicText(String.valueOf(pStr));
        ipb_progress.invalidate();
        ipb_progress.requestLayout();
    }

    private class MyTask extends AsyncTask<Integer, Integer, Object> {
        private ViewHoder mViewHolder;
        private ImageView imageAnmin ;
        private IndicatorProgressBar idc;
        public MyTask(IndicatorProgressBar idc , ImageView imageAnmin) {
            this.idc = idc;
            this.imageAnmin = imageAnmin;
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
            setIpbValue(progresses[0], idc, imageAnmin, progresses[1] ,progresses[2]);
        }
    }
}
