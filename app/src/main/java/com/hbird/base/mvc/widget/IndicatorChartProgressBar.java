package com.hbird.base.mvc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.hbird.base.R;
import com.hbird.base.util.L;

import static android.R.attr.width;


/**
 * 自定义 统计图表柱状图
 * 哎 心累 真的不想写
 *
 * @author liul
 * created by 2018-07-17
 */
public class IndicatorChartProgressBar extends ProgressBar {

    private final String TAG = IndicatorChartProgressBar.class.getSimpleName();

    /**
     * 指示器图片背景
     */
    private Drawable mIndicatorDrawable;
    /**
     * 指示器文字的paint
     */
    private TextPaint mTextPaint;
    /**
     * 指示器文字的大小
     */
    private int mTextSize;
    /**
     * 指示器文字的颜色
     */
    private int mTextColor;
    /**
     * 指示器文字的样式
     */
    private int mTextStyle;
    /**
     * 指示器文字的对齐方式
     */
    private int mTextAlign;
    /**
     * 指示器的X轴偏移量
     */
    private int mXOffsetIndicator;
    /**
     * 指示器的X轴偏移量
     */
    private int mYOffsetIndicator;

    private int progressWidth;

    private int progressToal;

    private String indicText;

    private OnIndicatorTextChangeListener mOnIndicatorTextChangeListener;

    private OnProgressUpdateListener mOnProgressUpdateListener;

    public IndicatorChartProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public IndicatorChartProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IndicatorChartProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * 初始化控件
     *
     * @param attrs
     * @param defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorProgressBar, defStyle, 0);
        if (a != null) {
            try {
                mIndicatorDrawable = a.getDrawable(R.styleable.IndicatorProgressBar_progressIndicator);
                mXOffsetIndicator = (int) a.getDimension(R.styleable.IndicatorProgressBar_xOffsetIndicator, 0);
                mYOffsetIndicator = (int) a.getDimension(R.styleable.IndicatorProgressBar_yOffsetIndicator, 0);

                initTextPaint(a);

            } finally {
                a.recycle();
            }
        }

    }

    /**
     * 初始化TextPaint的属性
     *
     * @param a
     */
    private void initTextPaint(TypedArray a) {
        //消除锯齿
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        //适配手机密度
        mTextPaint.density = getResources().getDisplayMetrics().density;
        //指示器文字大小
        mTextSize = (int) a.getDimension(R.styleable.IndicatorProgressBar_textSize, 10);
        mTextPaint.setTextSize(mTextSize);
        //指示器文字颜色
        mTextColor = a.getColor(R.styleable.IndicatorProgressBar_textColor, Color.WHITE);
        mTextPaint.setColor(mTextColor);

        //设置指示器文字的对齐方式
        mTextAlign = (a.getInt(R.styleable.IndicatorProgressBar_textAlign, 1));
        if (mTextAlign == 0) {
            mTextPaint.setTextAlign(Paint.Align.LEFT);
        } else if (mTextAlign == 1) {
            mTextPaint.setTextAlign(Paint.Align.CENTER);
        } else if (mTextAlign == 2) {
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
        }

        //是否设置粗体
        mTextStyle = (a.getInt(R.styleable.IndicatorProgressBar_textStyle, 1));
        if (mTextStyle == 0) {
            //参数skewX为倾斜因子，正数表示向左倾斜，负数表示向右倾斜。
            mTextPaint.setTextSkewX(0.0f);
            mTextPaint.setFakeBoldText(false);
        } else if (mTextStyle == 1) {
            mTextPaint.setTextSkewX(0.0f);
            mTextPaint.setFakeBoldText(true);
        } else if (mTextStyle == 2) {
            mTextPaint.setTextSkewX(-0.25f);
            mTextPaint.setFakeBoldText(false);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //需要注意的是，在setMeasuredDimension()方法调用之后，我们才能使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量出的宽高，以此之前调用这两个方法得到的值都会是0。
        //所以我们要调用父类的方法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mIndicatorDrawable != null) {
            //获取系统进度条的宽度 这个宽度也是自定义进度条的宽度 所以在这里直接赋值
            int width = getMeasuredWidth();
            int height = getMeasuredHeight()+ getIndicatorHeight();
            setMeasuredDimension(width, height);
        }
    }

    /**
     * 获取进度条的指示器的宽度，因为指示器的背景是9pitch图  所以不能设置固定的宽度
     *
     * @return 获取指示器的宽度
     */
    private int getIndicatorWidth() {

        if (mIndicatorDrawable == null) {
            return 0;
        }

        Rect rect = mIndicatorDrawable.copyBounds();
        int indicatorWidth = rect.width();

        return indicatorWidth;
    }

    /**
     * 获取进度条的指示器的高度，因为指示器的背景是9pitch图  所以不能设置固定的高度
     *
     * @return 获取指示器的高度
     */
    private int getIndicatorHeight() {

        if (mIndicatorDrawable == null) {
            return 0;
        }

//        Rect rect = mDrawableIndicator.getBounds();
        //获取drawable的宽高
        Rect rect = mIndicatorDrawable.copyBounds();
        int indicatorHeight = rect.height();
//        Log.i(TAG,"==指示器的高度==="+indicatorHeight);
        return indicatorHeight;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //开始获取层级drawable
        Drawable indicatorProgressBar = getProgressDrawable();
        //重新计算每层级的高度
//        if (mIndicatorDrawable != null) {
            if (indicatorProgressBar != null && indicatorProgressBar instanceof LayerDrawable) {
                //layer类型的drawable 所以要看层级 有几层
                LayerDrawable d = (LayerDrawable) indicatorProgressBar;
                Rect rect = d.getDrawable(0).getBounds();
                rect.top = getIndicatorHeight();
                rect.bottom = rect.height() + getIndicatorHeight();
                d.getDrawable(0).setBounds(rect);
                Rect rect1 = d.getDrawable(1).getBounds();
                rect1.top = rect.top + 0;
                rect1.left = rect.left - 5;
                rect1.right = rect1.right + 5;
                //rect.height()也就是每层drawable的高度当然也包括了指示器  也就是两层drawable的高度, 所以这块比较迷惑
                //后来查看rect的详解知道 必须加上rect.height
                rect1.bottom = rect.bottom + 0;
//                Log.i(TAG,rect.left+"==进度条的层级起始位置==="+rect1.left);
                d.getDrawable(1).setBounds(rect1);
            } else if (indicatorProgressBar != null) {
                Rect rect = indicatorProgressBar.getBounds();
                rect.top = indicatorProgressBar.getIntrinsicHeight();
                rect.bottom = rect.height() + getIndicatorHeight();
                indicatorProgressBar.setBounds(rect);
            }
//        }
        //计算指示器的宽度
        //updateProgressBar();
        //计算指示器的高度
        updateHeightProgressBar();
        // draw进度条的指示器和头部图片
        super.onDraw(canvas);

        //开始画进度条和进度条头部的图片
        canvas.save();
        int dx = 0;
        // 获取系统进度条最上边的位置 也就是头部的位置
        if (indicatorProgressBar != null && indicatorProgressBar instanceof LayerDrawable) {
            LayerDrawable d = (LayerDrawable) indicatorProgressBar;
            Drawable indicator_progress = d.findDrawableByLayerId(R.id.indicator_progress);
            dx = indicator_progress.getBounds().top;
        } else if (indicatorProgressBar != null) {
            dx = indicatorProgressBar.getBounds().top;
        }

        //加入offset  比较迷惑算法
        dx = dx - getIndicatorHeight() / 2 - mXOffsetIndicator + getPaddingBottom();
//            Log.i(TAG,PX2DPUtil.dp2px(getContext(), 25)+"==进度条的长度==="+dx);
        // 移动画笔位置
        canvas.translate(dx, 0);
        canvas.restore();

    }

    //获取它的高度 liul
    private void updateHeightProgressBar() {
        //开始获取层级drawable
        Drawable progressDrawable = getProgressDrawable();

        if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {

            LayerDrawable d = (LayerDrawable) progressDrawable;
            //当前的比列
            final double scale = getScale(getProgress());
            //获取层级中制定的drawable
            //层级中的指示器drawable
            Drawable indicator_progress = d.findDrawableByLayerId(R.id.indicator_progress);
            Rect rect = d.getBounds();
            //指示器drawable的宽度
            final int heights = rect.top - rect.bottom;//+ mXOffsetAbove + mWidthAbove
            if (indicator_progress != null) {
                Rect indicatorProgressBounds = indicator_progress.getBounds();
                indicatorProgressBounds.top = indicatorProgressBounds.bottom + (int) (heights * scale);
                //设置开始进度时的初始宽度
                indicatorProgressBounds.right = getMeasuredWidth();
                //设置进度条的drawable的宽高
                indicator_progress.setBounds(indicatorProgressBounds);

                int progressHeight = indicatorProgressBounds.top;
                if (mOnProgressUpdateListener != null) {
                    //mOnProgressUpdateListener.getUpdateWidht(progressWidth);
                    mOnProgressUpdateListener.getUpdateHeight(progressHeight);
                }
            }
        }
    }
    /**
     * 获取当前的进度在进度条中的比列
     *
     * @param progress
     * @return
     */
    private double getScale(int progress) {
        //讨厌三目运算符
        //float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;
        double scale = 0;
        if (getMax() > 0) {
            scale = (double) progress / (double) getMax();
            //L.liul(scale+"哈哈哈哈");
        }
        return scale;
    }


    public Drawable getmIndicatorDrawable() {
        return mIndicatorDrawable;
    }

    public void setmIndicatorDrawable(Drawable mIndicatorDrawable) {
        this.mIndicatorDrawable = mIndicatorDrawable;
    }

    public TextPaint getmTextPaint() {
        return mTextPaint;
    }

    public void setmTextPaint(TextPaint mTextPaint) {
        this.mTextPaint = mTextPaint;
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getmTextStyle() {
        return mTextStyle;
    }

    public void setmTextStyle(int mTextStyle) {
        this.mTextStyle = mTextStyle;
    }

    public int getmTextAlign() {
        return mTextAlign;
    }

    public void setmTextAlign(int mTextAlign) {
        this.mTextAlign = mTextAlign;
    }

    public int getmXOffsetIndicator() {
        return mXOffsetIndicator;
    }

    public void setmXOffsetIndicator(int mXOffsetIndicator) {
        this.mXOffsetIndicator = mXOffsetIndicator;
    }

    public int getmYOffsetIndicator() {
        return mYOffsetIndicator;
    }

    public void setmYOffsetIndicator(int mYOffsetIndicator) {
        this.mYOffsetIndicator = mYOffsetIndicator;
    }

    public void setmOnIndicatorTextChangeListener(OnIndicatorTextChangeListener mOnIndicatorTextChangeListener) {
        this.mOnIndicatorTextChangeListener = mOnIndicatorTextChangeListener;
    }

    public interface OnIndicatorTextChangeListener {
        public String getText();
    }

    public interface OnProgressUpdateListener {
        public void getUpdateHeight(int heightArgs);
    }

    public int getProgressWidth() {
        return progressWidth;
    }

    public void setProgressToal(int progressToal) {
        this.progressToal = progressToal;
    }

    public void setIndicText(String indicText) {
        this.indicText = indicText;
    }

    public void setmOnProgressUpdateListener(OnProgressUpdateListener mOnProgressUpdateListener) {
        this.mOnProgressUpdateListener = mOnProgressUpdateListener;
    }
}
