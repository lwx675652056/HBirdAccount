package com.hbird.base.mvc.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.hbird.base.R;


/**
 * 下载进度条
 *
 * @author ywl
 */
public class DownLoadProgressbar extends View {

    private Paint paint = new Paint(); // 绘制背景灰色线条画笔
    private float offset = 0f; // 下载偏移量
    private float maxvalue = 0f; // 下载的总大小
    private float currentValue = 0f; // 下载了多少
    private Rect mBound = new Rect(); // 获取百分比数字的长宽
    private String percentValue = "0%"; // 要显示的现在百分比
    private float offsetRight = 0f; // 灰色线条距离右边的距离
    private float offsetTop = 18f; // 距离顶部的偏移量

    public DownLoadProgressbar(Context context) {
        this(context, null);
    }

    public DownLoadProgressbar(Context context, AttributeSet attribute) {
        this(context, attribute, 0);
    }

    public DownLoadProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制底色
        paint.setColor(getResources().getColor(R.color.grey_a5a5a5));
        paint.setStrokeWidth(10);
        canvas.drawLine(0, offsetTop, getWidth(), offsetTop, paint);
        // 绘制进度条颜色
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStrokeWidth(12);
        canvas.drawLine(0, offsetTop, offset, offsetTop, paint);
        // 绘制白色区域及百分比
        paint.setColor(getResources().getColor(R.color.grey_a5a5a5));
        paint.setStrokeWidth(10);

        canvas.drawLine(offset, offsetTop, offset + mBound.width() + 4, offsetTop, paint);
    }

    /**
     * 设置当前进度值
     */
    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
        int value = (int) (this.currentValue / maxvalue * 100);
        if (value < 100) {
            percentValue = value + "%";
        } else {
            percentValue = "100%";
        }
        initCurrentProgressBar();
        invalidate();
    }

    /**
     * 设置最大值
     */
    public void setMaxValue(float maxValue) {
        this.maxvalue = maxValue;
    }

    /**
     * 获取当前进度条长度
     *
     * @return
     */
    public void initCurrentProgressBar() {
//        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
                if (currentValue < maxvalue) {
                    offset = (getWidth() - offsetRight) * currentValue / maxvalue;
                } else {
                    offset = getWidth() - offsetRight;
                }
//            }
//        });
    }
}