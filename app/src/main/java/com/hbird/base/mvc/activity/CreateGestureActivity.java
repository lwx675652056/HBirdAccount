package com.hbird.base.mvc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.global.ACache;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.widget.LockPatternIndicator;
import com.hbird.base.mvc.widget.LockPatternView;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.LockPatternUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Liul on 2018/07/05.
 * 绘制解锁手势密码
 */
public class CreateGestureActivity extends BaseActivity<BaseActivityPresenter> {

	@BindView(R.id.lockPatterIndicator)
	LockPatternIndicator lockPatternIndicator;
	@BindView(R.id.lockPatternView)
	LockPatternView lockPatternView;
	@BindView(R.id.resetBtn)
    Button resetBtn;
	@BindView(R.id.messageTv)
    TextView messageTv;

	private List<LockPatternView.Cell> mChosenPattern = null;
	private ACache aCache;
	private static final long DELAYTIME = 600L;
	private static final String TAG = "CreateGestureActivity";

	@Override
	protected int getContentLayout() {
		return R.layout.activity_create_gesture;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		init();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void initEvent() {

	}


	private void init() {
		aCache = ACache.get(CreateGestureActivity.this);
		lockPatternView.setOnPatternListener(patternListener);
	}

	/**
	 * 手势监听
	 */
	private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			lockPatternView.removePostClearPatternRunnable();
			//updateStatus(Status.DEFAULT, null);
			lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
		}

		@Override
		public void onPatternComplete(List<LockPatternView.Cell> pattern) {
			//Log.e(TAG, "--onPatternDetected--");
			if(mChosenPattern == null && pattern.size() >= 4) {
				mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
				updateStatus(Status.CORRECT, pattern);
			} else if (mChosenPattern == null && pattern.size() < 4) {
				updateStatus(Status.LESSERROR, pattern);
			} else if (mChosenPattern != null) {
				if (mChosenPattern.equals(pattern)) {
					updateStatus(Status.CONFIRMCORRECT, pattern);
				} else {
					updateStatus(Status.CONFIRMERROR, pattern);
				}
			}
		}
	};

	/**
	 * 更新状态
	 * @param status
	 * @param pattern
     */
	private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
		messageTv.setTextColor(getResources().getColor(status.colorId));
		messageTv.setText(status.strId);
		switch (status) {
			case DEFAULT:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CORRECT:
				updateLockPatternIndicator();
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case LESSERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CONFIRMERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
				lockPatternView.postClearPatternRunnable(DELAYTIME);
				break;
			case CONFIRMCORRECT:
				saveChosenPattern(pattern);
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				setLockPatternSuccess();
				break;
		}
	}

	/**
	 * 更新 Indicator
	 */
	private void updateLockPatternIndicator() {
		if (mChosenPattern == null)
			return;
		lockPatternIndicator.setIndicator(mChosenPattern);
	}

	/**
	 * 重新设置手势
	 */
	@OnClick(R.id.resetBtn)
	void resetLockPattern() {
		mChosenPattern = null;
		lockPatternIndicator.setDefaultIndicator();
		updateStatus(Status.DEFAULT, null);
		lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
	}

	/**
	 * 成功设置了手势密码(跳到首页)
     */
	private void setLockPatternSuccess() {
		showMessage("设置成功，跳转到首页");
	}

	/**
	 * 保存手势密码
	 */
	private void saveChosenPattern(List<LockPatternView.Cell> cells) {
		byte[] bytes = LockPatternUtil.patternToHash(cells);
		aCache.put(CommonTag.GESTURE_PASSWORD, bytes);
	}

	private enum Status {
		//默认的状态，刚开始的时候（初始化状态）
		DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
		//第一次记录成功
		CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
		//连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
		LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
		//二次确认错误
		CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
		//二次确认正确
		CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

		private Status(int strId, int colorId) {
			this.strId = strId;
			this.colorId = colorId;
		}
		private int strId;
		private int colorId;
	}
}
