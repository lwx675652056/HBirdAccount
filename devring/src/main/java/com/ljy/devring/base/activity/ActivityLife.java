package com.ljy.devring.base.activity;

import android.app.Activity;
import android.os.Bundle;

import com.ljy.devring.DevRing;
import com.ljy.devring.other.ActivityListManager;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.subjects.PublishSubject;


public class ActivityLife implements IActivityLife {

    private Activity mActivity;
    private final PublishSubject<ActivityEvent> mLifecycleSubject = PublishSubject.create();

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        mActivity = activity;

        mLifecycleSubject.onNext(ActivityEvent.CREATE);

        //如果 intent 包含了此字段,并且为 true 说明不加入到 list 进行统一管理
        boolean isNotAdd = false;
        if (activity.getIntent() != null) isNotAdd = activity.getIntent().getBooleanExtra(ActivityListManager.IS_NOT_ADD_ACTIVITY_LIST, false);

        if (!isNotAdd) DevRing.activityListManager().addActivity(activity);

        if (((IBaseActivity) mActivity).isUseEventBus()) {
            DevRing.busManager().register(mActivity);
        }
    }

    @Override
    public void onStart() {
        mLifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    public void onResume() {
        DevRing.activityListManager().setCurrentActivity(mActivity);
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    public void onPause() {
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    public void onStop() {
        if (DevRing.activityListManager().getCurrentActivity() == mActivity) {
            DevRing.activityListManager().setCurrentActivity(null);
        }
        mLifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);

        DevRing.activityListManager().removeActivity(mActivity);

        if (((IBaseActivity) mActivity).isUseEventBus()) {
            DevRing.busManager().unregister(mActivity);
        }
        mActivity = null;
    }

    public PublishSubject<ActivityEvent> getLifecycleSubject() {
        return mLifecycleSubject;
    }
}
