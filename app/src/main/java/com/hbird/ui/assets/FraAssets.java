package com.hbird.ui.assets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.FraAssetsBinding;
import com.hbird.base.mvc.activity.ZiChanJieShaoActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZiChanInfoReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.APDUserDateDialog;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;
import com.hbird.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sing.common.base.BaseFragment;
import sing.common.base.BaseViewModel;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: FraAssets
 * @date: 2018/12/29 14:28
 * @Description: 资产
 */
public class FraAssets extends BaseFragment<FraAssetsBinding, BaseViewModel> {

    private String token;
    private AssetsData data;
    private AssetsAdapter adapter;
    private List<AssetsBean> list = new ArrayList<>();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fra_assets;
    }

    @Override
    public void initData() {
        data = new AssetsData();
        binding.setData(data);
        binding.setListener(new OnClick());

        String dateNowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String times = (String) SharedPreferencesUtil.get(Constants.ASSETS_TIME, dateNowStr);

        data.setTime(times);
        data.setValue("0.00");

        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");

        adapter = new AssetsAdapter(getContext(), list, R.layout.row_assets, (position, data, type) -> onItemClick((AssetsBean) data));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setNestedScrollingEnabled(false);//禁止rcyc嵌套滑动
        binding.recyclerView.setItemAnimator(null);//设置动画为null来解决闪烁问题
    }

    // item点击
    private void onItemClick(AssetsBean data) {
        Utils.playVoice(getActivity(), R.raw.changgui02);
        if (data.money<0||data.money>0){ // 资产详情页
            Intent intent = new Intent(getActivity(), ActAssetsDetail.class);
            intent.putExtra(Constants.START_INTENT_A, data);
            startActivity(intent);
        }else{ // 没有设置过值
            Intent intent = new Intent(getActivity(), ActEditAccountValue.class);
            Bundle b = new Bundle();
            b.putSerializable(Constants.START_INTENT_A, data);
            intent.putExtras(b);
            startActivityForResult(intent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            AssetsBean bean = (AssetsBean) data.getExtras().getSerializable(Constants.START_INTENT_A);
            for (int i = 0, size = list.size(); i < size; i++) {
                if (list.get(i).assetsType == bean.assetsType) {
                    list.get(i).assetsName = bean.assetsName;
                    list.get(i).money = bean.money;
                }
            }
            adapter.notifyItemRangeChanged(0, list.size());
        }
    }

    public class OnClick {
        // 资产介绍
        public void explain(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivity(ZiChanJieShaoActivity.class);
        }

        // 设置同步时间
        public void synchronizedTime(View view) {
            new APDUserDateDialog(getActivity()) {
                @Override
                protected void onBtnOkClick(String year, String month, String day) {
                    String days = DateUtils.getDays(System.currentTimeMillis());
                    if (!TextUtils.isEmpty(days)) {
                        String time = year + "年" + month + "月" + day + "日";
                        long l = 0;
                        try {
                            l = DateUtils.dateToStamp(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //调用服务端接口
                        setNetWork2Time(l, time);
                    }
                }
            }.show();
        }
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getNetWorkInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()){
            getNetWorkInfo();
        }
    }

    private void getNetWorkInfo() {
        NetWorkManager.getInstance().setContext(getActivity()).getZiChanInfo(token, null, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                ZiChanInfoReturn b1 = (ZiChanInfoReturn) b;
                // 保存的
                SharedPreferencesUtil.put(Constants.CHOOSE_ASSETS, b1.toString());
                setValue();
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }

    // 设置值
    private void setValue() {
        // 保存的
        String str = (String) SharedPreferencesUtil.get(Constants.CHOOSE_ASSETS, "");
        ZiChanInfoReturn temp = JSON.parseObject(str, ZiChanInfoReturn.class);

        if (temp != null && temp.getResult() != null) {
            double netAssets = temp.getResult().getNetAssets();
            data.setValue(String.valueOf(netAssets));

            long initDate = temp.getResult().getInitDate();
            data.setTime(DateUtils.getYearMonthDay(initDate));

            list.clear();
            list.addAll(temp.getResult().getAssets());
            adapter.notifyItemRangeChanged(0, list.size());
        }
    }

    private void setNetWork2Time(long date, String time) {
        NetWorkManager.getInstance()
                .setContext(getActivity())
                .putInitDates(date, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        data.setTime(time);
                        SharedPreferencesUtil.put(Constants.ASSETS_TIME, time);
                    }

                    @Override
                    public void onError(String s) {
                    }
                });
    }
}
