package com.ljy.devring.base.activity;

import com.ljy.devring.base.fragment.FragmentLifeCallback;

/**
 * author:  admin
 * date:     2018/06/29
 * description: Activity基类接口
 * 如果想通过本库的LifeCycleCallback实现相关的基类功能，那么你的Activity需实现此接口
 */

public interface IBaseActivity {

    /**
     * 该Activity是否订阅事件总线
     * @return true则自动进行注册/注销操作，false则不注册
     */
    boolean isUseEventBus();

    /**
     * 该Activity是否包含Fragment（是否注册FragmentLifecycleCallbacks）
     * @return
     * 返回false则不注册FragmentLifecycleCallbacks，也就是说{@link FragmentLifeCallback}中的操作不会进行
     */
    boolean isUseFragment();
}
