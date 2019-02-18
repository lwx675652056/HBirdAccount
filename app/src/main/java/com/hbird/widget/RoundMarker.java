package com.hbird.widget;

import android.content.Context;

import com.hbird.common.chating.components.MarkerView;
import com.hbird.common.chating.data.Entry;
import com.hbird.common.chating.highlight.Highlight;
import com.hbird.common.chating.utils.MPPointF;

public class RoundMarker extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public RoundMarker(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight()/2);
    }
}
