package com.hbird.common.chating.interfaces.dataprovider;

import com.hbird.common.chating.components.YAxis.AxisDependency;
import com.hbird.common.chating.data.BarLineScatterCandleBubbleData;
import com.hbird.common.chating.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
