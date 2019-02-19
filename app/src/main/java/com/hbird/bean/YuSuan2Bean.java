package com.hbird.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * @author: LiangYX
 * @ClassName: YuSuan2Bean
 * @date: 2019/2/19 17:27
 * @Description: 预算完成率 场景账本
 */
public class YuSuan2Bean extends BaseObservable {

    public SceneBudgetBean sceneBudget;
    public List<ArraysBean> arrays;

    public static class SceneBudgetBean {
        public int accountBookId;
        public long beginTime;
        public double budgetMoney;
        public int sceneType;
        public long endTime;
        public int id;
    }

    public static class ArraysBean {
        public double money;
        public String time;
        public String weekTime;
    }
}
