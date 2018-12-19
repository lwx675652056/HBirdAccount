package com.hbird.base.mvc.base;

/**
 * Created by Liul on 2018/06/28.
 */

public interface UiInterface {

    /**
     * 设置布局id
     *
     * @return
     */
    int setContentId();

    /**
     * 初始化控件
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();


    /**
     * 初始化监听器
     */
    void initListener();


    /**
     * 加载数据
     */
    void loadData();
}
