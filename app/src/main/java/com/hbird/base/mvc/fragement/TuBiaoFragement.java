package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Px;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.base.baseActivity.BaseFragementPagerAdapter;
import com.hbird.ui.analysis.FragAnalysis;
import com.hbird.ui.assets.FraAssets;
import com.hbird.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import sing.common.util.StatusBarUtil;

/**
 * Created by Liul on 2018/8/8.
 * 图表
 */

public class TuBiaoFragement extends BaseFragement implements View.OnClickListener{

    @BindView(R.id.view_pager)
   ViewPager viewPagers;

    @BindView(R.id.rl_tongji)
    FrameLayout mTongji;
    @BindView(R.id.rl_zichan)
    FrameLayout mZiChan;

    @BindView(R.id.rl_fenxi)
    FrameLayout mFenxi;

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_title2)
    TextView mTitle2;
    @BindView(R.id.tv_title3)
    TextView mTitle3;


    private ArrayList<Fragment> fragements = new ArrayList<>();
    private ChartFragement chartFragement;
//    private FenXiFragement fenXiFragement;
    private FragAnalysis fenXiFragement;
    private FraAssets ziChanFragement;
    private BaseFragementPagerAdapter pagerAdapter;
    private int firstCome=0;
    private int mm = 0;

    @Override
    public int setContentId() {
        return R.layout.fragement_tubiao;
    }

    @Override
    public void initView() {
        chartFragement = new ChartFragement();
        fenXiFragement = new FragAnalysis();
        ziChanFragement = new FraAssets();

        fragements.add(chartFragement);
        fragements.add(fenXiFragement);
        fragements.add(ziChanFragement);
        pagerAdapter = new BaseFragementPagerAdapter(getChildFragmentManager(), fragements);
        viewPagers.setAdapter(pagerAdapter);

        viewPagers.setOffscreenPageLimit(2);
        viewPagers.setCurrentItem(0);
        setView(0);
    }
    @Override
    public void initData() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Utils.initColor(getActivity(), Color.rgb(241, 92, 60));
            StatusBarUtil.clearStatusBarDarkMode(getActivity().getWindow()); // 导航栏白色字体
        }
    }

    public void setH5TiaoZhuan(final int m){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPagers.setCurrentItem(m);
                setView(m);

            }
        }, 500);
    }

    @Override
    public void initListener() {
        mTongji.setOnClickListener(this);
        mZiChan.setOnClickListener(this);
        mFenxi.setOnClickListener(this);

        viewPagers.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int arg0, float arg1, @Px int arg2) {

            }

            @Override
            public void onPageSelected(int i) {
                playVoice(R.raw.changgui02);
                setView(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadDate() {
        if(chartFragement.getUserVisibleHint()){
            chartFragement.loadDataForNet();
        }else if(fenXiFragement.getUserVisibleHint()){
            fenXiFragement.loadDataForNet();
        }
    }

    private void setView(int i) {
        if(i==0){
            mTitle.setTypeface(Typeface.DEFAULT_BOLD);
            mTitle2.setTypeface(Typeface.DEFAULT);
            mTitle3.setTypeface(Typeface.DEFAULT);
        }else if(i==1){
            mTitle2.setTypeface(Typeface.DEFAULT_BOLD);
            mTitle.setTypeface(Typeface.DEFAULT);
            mTitle3.setTypeface(Typeface.DEFAULT);
        }else{
            mTitle2.setTypeface(Typeface.DEFAULT);
            mTitle3.setTypeface(Typeface.DEFAULT_BOLD);
            mTitle.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_tongji:
                //playVoice(R.raw.changgui02);
                setView(0);
                viewPagers.setCurrentItem(0);
                break;
            case R.id.rl_fenxi:
                //playVoice(R.raw.changgui02);
                setView(1);
                viewPagers.setCurrentItem(1);
                break;
            case R.id.rl_zichan:
                //playVoice(R.raw.changgui02);
                setView(2);
                viewPagers.setCurrentItem(2);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==321 && resultCode==322){
            if(chartFragement.getUserVisibleHint()){
                chartFragement.loadDataForNet();
            }else if(fenXiFragement.getUserVisibleHint()){
                fenXiFragement.loadDataForNet();
            }
        }
    }
}
