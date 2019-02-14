package com.hbird.common.chating.interfaces.dataprovider;

import com.hbird.common.chating.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
