package com.hbird.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.chart_3_0_1v.charts.LineChart;

/**
 * *************************************************************
 * <p>
 * .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *
 * |                     ______                     |      *
 * |                  .-"      "-.                  |      *
 * |                 /            \                 |      *
 * |     _          |              |          _     |      *
 * |    ( \         |,  .-.  .-.  ,|         / )    |      *
 * |     > "=._     | )(__/  \__)( |     _.=" <     |      *
 * |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *
 * |           "=._"(_     ^^     _)"_.="           |      *
 * |               "=\__|IIIIII|__/="               |      *
 * |              _.="| \IIIIII/ |"=._              |      *
 * |    _     _.="_.="\          /"=._"=._     _    |      *
 * |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *
 * |    > _.="                            "=._ <    |      *
 * |   (_/                                    \_)   |      *
 * |                                                |      *
 * '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *
 /**
 * Created by Liul on 2018/09/27.
 * 曲线图表相关类
 *
 */

public class LineChartInViewPager extends LineChart {

    PointF downPoint = new PointF();

    public LineChartInViewPager(Context context) {
        super(context);
    }

    public LineChartInViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartInViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScaleX() > 1 && Math.abs(evt.getX() - downPoint.x) > 5) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.onTouchEvent(evt);
    }
}