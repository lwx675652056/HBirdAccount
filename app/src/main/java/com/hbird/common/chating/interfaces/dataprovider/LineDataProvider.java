package com.hbird.common.chating.interfaces.dataprovider;

import com.hbird.common.chating.components.YAxis;
import com.hbird.common.chating.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
