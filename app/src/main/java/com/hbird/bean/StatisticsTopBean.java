package com.hbird.bean;

/**
 * @author: LiangYX
 * @ClassName: StatisticsTopBean
 * @date: 2019/1/18 16:35
 * @Description: 统计顶部拼的年月
 */
public class StatisticsTopBean {

    public int yyyy;
    public int mm;

    public StatisticsTopBean(int yyyy, int mm) {
        this.yyyy = yyyy;
        this.mm = mm;
    }

    public String getMM() {
        return mm < 10 ? "0" + mm : "" + mm;
    }

    @Override
    public String toString() {
        return "{" +
                "yyyy:" + yyyy +
                ", mm:" + mm +
                '}';
    }
}
