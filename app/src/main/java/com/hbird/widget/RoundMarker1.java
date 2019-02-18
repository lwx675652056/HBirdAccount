package com.hbird.widget;

import android.content.Context;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.common.chating.components.MarkerView;
import com.hbird.common.chating.data.Entry;
import com.hbird.common.chating.highlight.Highlight;
import com.hbird.common.chating.utils.MPPointF;

public class RoundMarker1 extends MarkerView {

    TextView tvContent;

    public RoundMarker1(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        a = e.getX();
    }

    float a = 0;

    @Override
    public MPPointF getOffset() {
        if (a < 1) {
            return new MPPointF(0, -getHeight());
        } else {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    public TextView getText(){
        return tvContent;
    }
}
