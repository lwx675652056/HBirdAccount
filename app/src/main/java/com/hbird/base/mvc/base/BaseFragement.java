package com.hbird.base.mvc.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.listener.OnBackListener;
import com.hbird.base.mvc.view.dialog.DialogToGig;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.ToastUtils;
import com.hbird.base.util.VoiceUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import sing.common.util.LogUtil;

/**
 * Created by Liul on 2018/6/28.
 */
public abstract class BaseFragement extends Fragment implements UiInterface, OnBackListener {

    protected boolean isVisible;
    public boolean isPrepared;
    private boolean isFirstLoad = true;

    public ProgressDialog myDialog;
    private Unbinder butterKnife;
    private DialogToGig dialogToGig;
    private VoiceUtils voiceUtils;


    public void hideProgress() {
        if (dialogToGig != null) {
            dialogToGig.hide();
        }
    }

    public void hideGifProgress() {
        if (dialogToGig != null) {
            dialogToGig.hide();
        }
    }


    public void showProgress(String title) {

        if (dialogToGig == null) {
            dialogToGig = new DialogToGig(getActivity());
        }
        dialogToGig.builder().show();
    }

    public void showGifProgress(String title) {
        if (dialogToGig == null) {
            dialogToGig = new DialogToGig(getActivity());
        }
        dialogToGig.builder().show();
    }


    public void showMessage(String arg) {
        ToastUtils.ShowMessage(getActivity(), arg);
    }

    public View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(setContentId(), null);
        butterKnife = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        isPrepared = true;
        //初始化布局
        initView();
        //初始化界面的数据
        initData();
        //给控件设置监听器
        initListener();

        lazyLoad();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (isPrepared) {
                loadDate();
            }

            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
            loadDate();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {
    }

    protected void onVisible() {
        lazyLoad();
    }

    private void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        loadDataForNet();
        LogUtil.e("loadDataForNet");

    }


    public void loadDataForNet() {

    }

    public void loadDate() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //butterKnife.unbind();
    }

    public void playVoice(int resid) {
        boolean opens = SPUtil.getPrefBoolean(getActivity(), CommonTag.VOICE_KEY, true);
        if (opens) {
            try {
                if (voiceUtils == null) {
                    voiceUtils = VoiceUtils.newInstance(getActivity());
                }
                voiceUtils.playVoice(resid);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // do some thing
    }
}
