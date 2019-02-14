package com.hbird.common.chating.interfaces.dataprovider;

import com.hbird.common.chating.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
