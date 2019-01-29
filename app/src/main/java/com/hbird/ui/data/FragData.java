package com.hbird.ui.data;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hbird.base.R;
import com.hbird.base.databinding.FragDataBinding;
import com.hbird.base.mvc.base.baseActivity.BaseFragementPagerAdapter;
import com.hbird.ui.analysis.FragAnalysis;
import com.hbird.ui.assets.FraAssets;
import com.hbird.ui.statistics.FragStatistics;
import com.hbird.util.Utils;

import java.util.ArrayList;

import sing.common.base.BaseFragment;
import sing.common.base.BaseViewModel;
import sing.common.listener.OnMyPageChangeListener;
import sing.common.util.StatusBarUtil;

/**
 * @author: LiangYX
 * @ClassName: FragData
 * @date: 2019/1/17 10:47
 * @Description: 数据
 */
public class FragData extends BaseFragment<FragDataBinding, BaseViewModel> {

    private DataData data;
    private OnClick listener;
    private ArrayList<Fragment> fragements = new ArrayList<>();
    private FragStatistics chartFragement;
    private FragAnalysis fenXiFragement;
    private FraAssets ziChanFragement;
    private BaseFragementPagerAdapter pagerAdapter;
    private int mm = 0;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.frag_data;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        LinearLayout ll = getActivity().findViewById(R.id.ll_parent2);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(getActivity());
        ll.setLayoutParams(params);
        ll.setPadding(0, StatusBarUtil.getStateBarHeight(getActivity()), 0, 0);

        data = new DataData();
        binding.setData(data);
        listener = new OnClick();
        binding.setListener(listener);
        chartFragement = new FragStatistics();
        fenXiFragement = new FragAnalysis();
        ziChanFragement = new FraAssets();

        fragements.add(chartFragement);
        fragements.add(ziChanFragement);
        fragements.add(fenXiFragement);
        pagerAdapter = new BaseFragementPagerAdapter(getChildFragmentManager(), fragements);
        binding.viewPager.setAdapter(pagerAdapter);

        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setCurrentItem(0);

        binding.viewPager.addOnPageChangeListener(new OnMyPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                Utils.playVoice(getActivity(), R.raw.changgui02);
                listener.change(i);
            }
        });
    }

    public class OnClick {
        // 统计 资产 分析 , 0 1 2
        public void change(int pos) {
            if (pos == data.getSelect()){
                return;
            }

            data.setSelect(pos);
            binding.viewPager.setCurrentItem(pos);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            StatusBarUtil.clearStatusBarDarkMode(getActivity().getWindow()); // 导航栏白色字体
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);
        }

        if (chartFragement != null) {
            chartFragement.setUserVisibleHint(chartFragement.getUserVisibleHint());
        }
        if (fenXiFragement != null) {
            fenXiFragement.setUserVisibleHint(fenXiFragement.getUserVisibleHint());
        }
        if (ziChanFragement != null) {
            ziChanFragement.setUserVisibleHint(ziChanFragement.getUserVisibleHint());
        }
    }

    public void setH5TiaoZhuan(final int m) {
        Handler handler = new Handler();
        handler.postDelayed(() -> listener.change(m), 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 321 && resultCode == 322) {
            if (chartFragement.getUserVisibleHint()) {
//                chartFragement.loadDataForNet();
            } else if (fenXiFragement.getUserVisibleHint()) {
                fenXiFragement.loadDataForNet();
            }
        }
    }
}
