package com.hbird.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hbird.base.R;

import sing.refreshlayout.api.RefreshKernel;
import sing.refreshlayout.api.RefreshLayout;
import sing.refreshlayout.constant.RefreshState;
import sing.refreshlayout.constant.SpinnerStyle;

/**
 * 经典下拉头部
 */
public class RefreshHeader extends LinearLayout implements sing.refreshlayout.api.RefreshHeader {

    private Context context;
    private TextView mHeaderText;//标题文本
//    private PathsView mArrowView;//下拉箭头
//    private ImageView mProgressView;//刷新动画视图
//    private ProgressDrawable mProgressDrawable;//刷新动画
    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        setGravity(Gravity.CENTER_HORIZONTAL);
        mHeaderText = new TextView(context);
    }

    // 获取真实视图（必须返回，不能为null）
    @Override
    public View getView() {
        View v = LayoutInflater.from(context).inflate(R.layout.refresh_header,this);

        ImageView iv = v.findViewById(R.id.iv);

        Glide.with(context)
                .asGif()
                .load(com.hbird.base.R.drawable.loding)
                .into(iv);

        return v;
    }

    // 获取变换方式（必须指定一个：平移、拉伸、固定、全屏）
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    // 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
    @Override
    public void setPrimaryColors(int... colors) {
        // colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        /**
         * 尺寸定义初始化完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
         * @param kernel RefreshKernel 核心接口（用于完成高级Header功能）
         * @param height HeaderHeight or FooterHeight
         * @param maxDragHeight 最大拖动高度
         */
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        /**
         * 手指拖动下拉（会连续多次调用，用于实时控制动画关键帧）
         * @param percent 下拉的百分比 值 = offset/headerHeight (0 - percent - (headerHeight+maxDragHeight) / headerHeight )
         * @param offset 下拉的像素偏移量  0 - offset - (headerHeight+maxDragHeight)
         * @param headerHeight Header的高度
         * @param maxDragHeight 最大拖动高度
         */
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        /**
         * 手指释放之后的持续动画（会连续多次调用，用于实时控制动画关键帧）
         * @param percent 下拉的百分比 值 = offset/headerHeight (0 - percent - (headerHeight+maxDragHeight) / headerHeight )
         * @param offset 下拉的像素偏移量  0 - offset - (headerHeight+maxDragHeight)
         * @param headerHeight Header的高度
         * @param maxDragHeight 最大拖动高度
         */
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        /**
         * 开始动画（开始刷新或者开始加载动画）
         * @param layout RefreshLayout
         * @param height HeaderHeight or FooterHeight
         * @param maxDragHeight 最大拖动高度
         */
//        mProgressDrawable.start();//开始动画
    }


    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        /**
         * 动画结束
         * @param layout RefreshLayout
         * @param success 数据是否成功刷新或加载
         * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
         */
//        mProgressDrawable.stop();//停止动画
        if (success){
            mHeaderText.setText("刷新完成");
        } else {
            mHeaderText.setText("刷新失败");
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }


    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mHeaderText.setText("下拉开始刷新");
//                mArrowView.setVisibility(VISIBLE);//显示下拉箭头
//                mProgressView.setVisibility(GONE);//隐藏动画
//                mArrowView.animate().rotation(0);//还原箭头方向
                break;
            case Refreshing:
                mHeaderText.setText("正在刷新");
//                mProgressView.setVisibility(VISIBLE);//显示加载动画
//                mArrowView.setVisibility(GONE);//隐藏箭头
                break;
            case ReleaseToRefresh:
                mHeaderText.setText("释放立即刷新");
//                mArrowView.animate().rotation(180);//显示箭头改为朝上
                break;
        }
    }
}