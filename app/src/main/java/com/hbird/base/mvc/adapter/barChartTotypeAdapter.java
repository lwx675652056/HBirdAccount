package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.ReturnBean.chartToRanking2Return;
import com.hbird.base.mvc.fragement.ChartFragement;
import com.hbird.base.mvc.widget.IndicatorProgressBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import sing.common.util.LogUtil;

/**
 * Created by liul on 2018/06/30.
 */

public class barChartTotypeAdapter extends BaseAdapter {
    private Context context;
    private ChartFragement mContext;
    private List<chartToRanking2Return.ResultBean.StatisticsIncomeTopArraysBean> accountDateList = new ArrayList<>();

    private double Max = 0;
    private boolean isShow = true;
    private boolean isProvinceShow = true;
    private String formatNum;
    private int  numI;

    public barChartTotypeAdapter(Context context, ChartFragement mContext,
                                 List<chartToRanking2Return.ResultBean.StatisticsIncomeTopArraysBean> accountDateList,
                                 boolean isProvinceShow,Double money,int i){
        this.context = context;
        this.mContext = mContext;
        this.accountDateList = accountDateList;
        this.isProvinceShow = isProvinceShow;
        this.numI = i;
        //遍历集合，获取最大值
      /*  for (int i=0;i<accountDateList.size();i++){
            chartToRanking2Return.ResultBean.StatisticsIncomeTopArraysBean provincesRegisterStatBean = accountDateList.get(i);
            double total_register = provincesRegisterStatBean.getMoney();

            if(Max < total_register){
                Max = total_register;
            }
        }*/
        Max =money;
        //设置不能最大充满
        //Max *= 1.1;

    }



    public void setDate2(boolean isShow){
        this.isShow = isShow;
        for (chartToRanking2Return.ResultBean.StatisticsIncomeTopArraysBean p: accountDateList){
            p.setFrist(true);
        }
    }

    @Override
    public int getCount() {
        return accountDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_bar_chart , null);
        TextView textaddr = (TextView) convertView.findViewById(R.id.tv_name_first_progress);
        IndicatorProgressBar idc = (IndicatorProgressBar) convertView.findViewById(R.id.ipb_first_progre);
        TextView textNum = (TextView) convertView.findViewById(R.id.text_item_bar_contory);
        TextView percent = (TextView) convertView.findViewById(R.id.tv_bili);
        ImageView img = (ImageView) convertView.findViewById(R.id.iv_img);
        LinearLayout imgBg = (LinearLayout) convertView.findViewById(R.id.ll_img_bg);

        ImageView imageAnmins = (ImageView) convertView.findViewById(R.id.image_item_animes);//动画设置预留
        //动画效果暂时 隐藏
        if(isProvinceShow){
            if (isShow){
                imageAnmins.setVisibility(View.GONE);
            }
        }

        chartToRanking2Return.ResultBean.StatisticsIncomeTopArraysBean bean = accountDateList.get(position);

        final double preTotalRegisterNum = bean.getMoney();
        int totalRegisterNum = 0;
        if(Max != 0) {
            totalRegisterNum = (int) (preTotalRegisterNum * 100 / Max);
            DecimalFormat df   = new DecimalFormat("######0.00");
            formatNum = df.format(preTotalRegisterNum * 100 / Max);
        }
        if(TextUtils.equals(formatNum,"0.00")){
            percent.setText("<0.01"+"%");
        }else {
            percent.setText(formatNum+"%");
        }
        textaddr.setText(bean.getTypeName());
        textNum.setText("￥"+bean.getMoney());
        Glide.with(mContext).load(bean.getIcon()).into(img);
        if(numI==2){
            imgBg.setBackgroundResource(R.drawable.shape_cycle_yellow);
            idc.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_progress_bar_thirds));
        }else {
            imgBg.setBackgroundResource(R.drawable.shape_cycle_blue);
            idc.setProgressDrawable(context.getResources().getDrawable(R.drawable.indicator_progress_bar_other));
        }
        idc.setMax(100);
        //动画效果暂时注销
        /*if(isProvinceShow) {
            if (isShow) {
                imageAnmins.setImageResource(R.drawable.register_stat_other_frame);
                AnimationDrawable animation = (AnimationDrawable) imageAnmins.getDrawable();
                animation.start();
            }
        }*/
        if (position == 0)   LogUtil.e("数据 = "+totalRegisterNum );
        setIpbValue(totalRegisterNum , idc , imageAnmins , totalRegisterNum  , preTotalRegisterNum );

        if (bean.isFrist() && isProvinceShow){

            if (isShow){
                MyTask task = new MyTask(idc, imageAnmins);
                int ceil = (int)Math.ceil(preTotalRegisterNum);
                //task.executeOnExecutor(Executors.newCachedThreadPool() , totalRegisterNum, ceil);
                task.executeOnExecutor(Executors.newSingleThreadExecutor() , totalRegisterNum, ceil);
            }

            //bean.setFrist(false);
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
        LinearLayout imgBg;
    }

    private void setIpbValue(final int progress,
                             final IndicatorProgressBar ipb_progress,
                             final ImageView iv_ipb_progress_above,
                             final int total,
                             final double preTotal) {
        //Rect bounds = new Rect(0, 0, Utils.dpTopx(context, 40), Utils.dpTopx(context, 30));
//        Drawable indicatorDrawableFirst = context.getResources().getDrawable(R.drawable.pb_indicator_first);
//        indicatorDrawableFirst.setBounds(bounds);
//        ipb_progress.setmIndicatorDrawable(indicatorDrawableFirst);

   /*     ipb_progress.setmOnProgressUpdateListener(new IndicatorProgressBar.OnProgressUpdateListener() {
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
