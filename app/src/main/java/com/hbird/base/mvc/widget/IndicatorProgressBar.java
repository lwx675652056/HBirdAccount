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


/**
 * 自定义带指示器的进度条 且 进度条头部有图片帧动画
 *
 * @author liul
 * created by 2018-06-30  18:39
 */
public class IndicatorProgressBar extends ProgressBar {

    private final String TAG = IndicatorProgressBar.class.getSimpleName();

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

    public IndicatorProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IndicatorProgressBar(Context context, AttributeSet attrs, int defStyle) {
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
//            Log.i(TAG,"==进度条的getMeasuredWidth==="+getMeasuredWidth());
//            Log.i(TAG,"==进度条的总长度==="+width);
            //画的时候需要将系统进度条与指示器分隔开来
            int height = getMeasuredHeight() + getIndicatorHeight();
//            Log.i(TAG, PX2DPUtil.dp2px(getContext(), 15) + "==进度条的getMeasuredHeight==="+getMeasuredHeight()+ "===getwidth==="+getHeight());
//            Log.i(TAG,"==进度条的总高度==="+height);
//            Log.i(TAG,PX2DPUtil.dp2px(getContext(), 5)+"==指示器的x轴偏移量==="+ mYOffsetIndicator);
//            Log.i(TAG,"==指示器的高度==="+ getIndicatorHeight());

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
                //getNumberOfLayers() 就是有基层drawable
//                for (int i = 0; i < d.getNumberOfLayers(); i++) {
//                    Rect rect = d.getDrawable(i).getBounds();
//                    //开始设置rect的位置
//                    //个人认为是Y轴上的位置
//                    //top和bottom 是指矩形在y轴上的起始位置和终点位置
//                    rect.top = getIndicatorHeight();
//                    //rect.height()也就是每层drawable的高度当然也包括了指示器  也就是两层drawable的高度, 所以这块比较迷惑
//                    //后来查看rect的详解知道 必须加上rect.height
//                    rect.bottom = rect.height() + getIndicatorHeight();
//                    Log.i(TAG,i+"==第几层==进度条第几层对应的原始高低==="+rect.height());
//                    Log.i(TAG,i+"==第几层==进度条第几层对应的高低==="+rect.top+"===低==="+rect.bottom);
//                    d.getDrawable(i).setBounds(rect);
//                }
                Rect rect = d.getDrawable(0).getBounds();
                rect.top = getIndicatorHeight();
                rect.bottom = rect.height() + getIndicatorHeight();
                d.getDrawable(0).setBounds(rect);
                Rect rect1 = d.getDrawable(1).getBounds();
               /* //特殊效果
                rect1.top = rect.top + 5;
                rect1.left = rect.left + 5;
                rect1.right = rect1.right + 5;
                rect1.bottom = rect.bottom - 5;*/
                rect1.top = rect.top;
                rect1.left = rect.left;
                rect1.right = rect1.right;
                //rect.height()也就是每层drawable的高度当然也包括了指示器  也就是两层drawable的高度, 所以这块比较迷惑
                //后来查看rect的详解知道 必须加上rect.height
                rect1.bottom = rect.bottom;
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
        updateProgressBar();
        // draw进度条的指示器和头部图片
        super.onDraw(canvas);

        //开始画进度条和进度条头部的图片
//        if (mIndicatorDrawable != null) {
            canvas.save();
            int dx = 0;
            // 获取系统进度条最右边的位置 也就是头部的位置
            if (indicatorProgressBar != null && indicatorProgressBar instanceof LayerDrawable) {
                LayerDrawable d = (LayerDrawable) indicatorProgressBar;
                Drawable indicator_progress = d.findDrawableByLayerId(R.id.indicator_progress);
                dx = indicator_progress.getBounds().right;
            } else if (indicatorProgressBar != null) {
                dx = indicatorProgressBar.getBounds().right;
            }

            //加入offset  比较迷惑算法
//            Log.i(TAG,"==指示器的偏移量==="+mXOffsetIndicator + "====指示器的padingleft=="+getPaddingLeft());
            dx = dx - getIndicatorWidth() / 2 - mXOffsetIndicator + getPaddingLeft();
//            Log.i(TAG,PX2DPUtil.dp2px(getContext(), 25)+"==进度条的长度==="+dx);
            // 移动画笔位置
            canvas.translate(dx, 0);
            // 画出指示器
//            mIndicatorDrawable.draw(canvas);
            // 画出进度条上的文字
            //讨厌的三目运算符
//            String text = "";
//            if (mOnIndicatorTextChangeListener != null) {
//                text = mOnIndicatorTextChangeListener.getText();
//                Log.i(TAG, "onDraw: text1111=="+text);
//                if (getProgress() == progressToal) {
//                    mOnIndicatorTextChangeListener = null;
//                }
//            }
//            Log.i(TAG, "onDraw: text222=="+indicText);
//            canvas.drawText(indicText, getIndicatorWidth() / 2, getIndicatorHeight() / 2, mTextPaint);

            canvas.restore();
//        }

    }

    private void updateProgressBar() {
        //开始获取层级drawable
        Drawable progressDrawable = getProgressDrawable();

        if (progressDrawable != null && progressDrawable instanceof LayerDrawable) {

            LayerDrawable d = (LayerDrawable) progressDrawable;
            //当前的比列
            final float scale = getScale(getProgress());
            //获取层级中制定的drawable
            //层级中的指示器drawable
            Drawable indicator_progress = d.findDrawableByLayerId(R.id.indicator_progress);
            Rect rect = d.getBounds();

            //指示器drawable的宽度
            final int width = rect.right - rect.left;//+ mXOffsetAbove + mWidthAbove
//            Log.i(TAG,"==进度条的左边的起始位置===" + rect.left);
            if (indicator_progress != null) {
                Rect indicatorProgressBounds = indicator_progress.getBounds();
                //根据当前的进度来画指示器drawable的宽度
//                if (getProgress()>0){
//                    indicatorProgressBounds.right = indicatorProgressBounds.left + (int) (width * scale) + getPaddingLeft();
//                }else{
//                    indicatorProgressBounds.right = indicatorProgressBounds.left + (int) (width * scale);
//                }
                indicatorProgressBounds.right = indicatorProgressBounds.left + (int) (width * scale);
                //设置进度条的drawable的宽高
                indicator_progress.setBounds(indicatorProgressBounds);

                progressWidth = indicatorProgressBounds.right;
                if (mOnProgressUpdateListener != null) {
                    mOnProgressUpdateListener.getUpdateWidht(progressWidth);
                }
            }

//            //在指示器drawable上面再添加一层drawable 更好的增加进度条特效; 也就是叠加的图层
//            Drawable indicatorPatternOverlay = d.findDrawableByLayerId(R.id.indicator_pattern);
//
//            if (indicatorPatternOverlay != null) {
//
//                if (indicator_progress != null) {
//                    // 使叠加图层适应进度条大小
//                    //其实还是progress
//                    Rect indicatorPatternOverlayBounds = indicator_progress.copyBounds();
//
//                    final int left = indicatorPatternOverlayBounds.left;
////                    final int right = indicatorPatternOverlayBounds.right;
//                    final int right = indicatorPatternOverlayBounds.right + getMax();
//
//                    //这里比较迷惑  不知道算法
//                    //后来才知道是 因为progress 是按照int类型 也就是最小间隔是1  所以才有下面的计算 left+1 大于 right  那left就是left  如果不是那就加上1
//                    indicatorPatternOverlayBounds.left = (left + 1 > right) ? left : left + 1;
//                    //这个还是比较迷惑
//                    indicatorPatternOverlayBounds.right = (right > 0) ? right - 1 : right;
//                    //设置叠加层drawable的宽度
//                    indicatorPatternOverlay.setBounds(indicatorPatternOverlayBounds);
//                } else {
//                    Rect indicatorPatternOverlayBounds = indicatorPatternOverlay.getBounds();
//                    indicatorPatternOverlayBounds.right = indicatorPatternOverlayBounds.left + (int) (width * scale + 0.5f);
//                    indicatorPatternOverlay.setBounds(indicatorPatternOverlayBounds);
//                }
//            }
        }
    }

    /**
     * 获取当前的进度在进度条中的比列
     *
     * @param progress
     * @return
     */
    private float getScale(int progress) {
        //讨厌三目运算符
        //float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;
        float scale = 0;
        if (getMax() > 0) {
            scale = (float) progress / (float) getMax();
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
        public void getUpdateWidht(int widthArgs);
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
