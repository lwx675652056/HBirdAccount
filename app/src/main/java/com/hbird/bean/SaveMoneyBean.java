package com.hbird.bean;

import java.util.List;

/**
 * @author: LiangYX
 * @ClassName: SaveMoneyBean
 * @date: 2019/1/16 14:54
 * @Description: 存钱效率
 */
public class SaveMoneyBean extends BaseBean {

    public FixedSpendBean fixedSpend;
    public List<ArraysBean> arrays;

    public static class FixedSpendBean {
        public double fixedLifeExpenditure; // 固定生活支出金额 无数据返回null
        public double fixedLargeExpenditure; // 固定大额支出金额 无数据返回null

        @Override
        public String toString() {
            return "{" +
                    "fixedLifeExpenditure:" + fixedLifeExpenditure +
                    ", fixedLargeExpenditure:" + fixedLargeExpenditure +
                    '}';
        }
    }

    public static class ArraysBean {
        public double monthIncome;  // 月收入总计
        public String time; // 月支出总计
        public double monthSpend; // 月支出总计

        @Override
        public String toString() {
            return "{" +
                    "monthIncome:" + monthIncome +
                    ", time:'" + time + '\'' +
                    ", monthSpend:" + monthSpend +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "fixedSpend:" + fixedSpend +
                ", arrays:" + arrays +
                '}';
    }
}
