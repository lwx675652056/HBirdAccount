package com.hbird.widget;

import android.content.Context;
import android.graphics.Canvas;

import com.github.mikephil.chart_3_0_1v.charts.LineChart;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.LineDataSet;
import com.github.mikephil.chart_3_0_1v.highlight.Highlight;
import com.github.mikephil.chart_3_0_1v.interfaces.datasets.IDataSet;

import java.lang.ref.WeakReference;

public class LineChartDrawMarkers extends LineChart {

    public LineChartDrawMarkers(Context context) {
        super(context);
    }

    private WeakReference<RoundMarker> mRoundMarkerReference;

    /**
     * 所有覆盖物是否为空
     *
     * @return TRUE FALSE
     */
    public boolean isMarkerAllNull() {
        return mRoundMarkerReference.get() == null;
    }

    public void setRoundMarker(RoundMarker roundMarker) {
        mRoundMarkerReference = new WeakReference<>(roundMarker);
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (mRoundMarkerReference == null) {
            return;
        }

        RoundMarker mRoundMarker = mRoundMarkerReference.get();
        if (mRoundMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());
            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);
            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);
            LineDataSet dataSetByIndex = (LineDataSet) getLineData().getDataSetByIndex(highlight.getDataSetIndex());

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            float circleRadius = dataSetByIndex.getCircleRadius();

            mRoundMarker.refreshContent(e, highlight);
            mRoundMarker.draw(canvas, pos[0] - mRoundMarker.getWidth() / 2, pos[1] + circleRadius - mRoundMarker.getHeight()+5);
        }
    }
}
