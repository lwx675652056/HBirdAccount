package com.hbird.bean;

/**
 * @author: LiangYX
 * @ClassName: ConsumptionRatioBean
 * @date: 2019/1/11 15:43
 * @Description: 消费结构比
 */
public class ConsumptionRatioBean extends BaseBean {

    public Double foodSpend; // 月支出中食物消费金额 无数据默认null
    public String time; // 时间 年-月格式
    public Double monthSpend; // 月支出金额 无数据默认null

    @Override
    public String toString() {
        return "{" +
                "foodSpend:" + foodSpend +
                ", time:'" + time + '\'' +
                ", monthSpend:" + monthSpend +
                '}';
    }
}
