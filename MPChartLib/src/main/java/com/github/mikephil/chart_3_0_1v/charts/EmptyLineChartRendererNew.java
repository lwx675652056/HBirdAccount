package com.github.mikephil.chart_3_0_1v.charts;

import com.github.mikephil.chart_3_0_1v.animation.ChartAnimator;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.LineDataSet;
import com.github.mikephil.chart_3_0_1v.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.chart_3_0_1v.interfaces.datasets.ILineDataSet;
import com.github.mikephil.chart_3_0_1v.renderer.LineChartRenderer;
import com.github.mikephil.chart_3_0_1v.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/10/31.
 */

public class EmptyLineChartRendererNew extends LineChartRenderer {

    private static final int MIN_NUM = 0;
    public EmptyLineChartRendererNew(LineChart lineChart){
        this(lineChart,lineChart.getAnimator(),lineChart.getViewPortHandler());
    }
    public EmptyLineChartRendererNew(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected void drawCubicBezier(ILineDataSet dataSet) {
        LineDataSet ILineDataSet =null;
        List<Entry> yVals = null;
        for (int i = 0; i <= dataSet.getEntryCount() ; i++) {
            if(null == yVals){
                yVals = new ArrayList<>();
            }
            if(isEmptyEntry(i,dataSet)){
                yVals.add(dataSet.getEntryForIndex(i));
            }else if(!yVals.isEmpty()){
                ILineDataSet = (LineDataSet)((LineDataSet)dataSet).copy();
                ILineDataSet.clear();
                ILineDataSet.setValues(yVals);
                super.drawCubicBezier(ILineDataSet);
                yVals.clear();
            }
        }
    }

    /**
     * 检测是否不符合要求的Entry(越界，小于或等于MIN_NUM 为false)
     * @param index  下标
     * @param dataSet
     * @return  false不符合
     */
    private boolean isEmptyEntry(int index,ILineDataSet dataSet){
        return dataSet!=null && index>=0 && index<dataSet.getEntryCount() && dataSet.getEntryForIndex(index).getY()<=MIN_NUM;
    }
}