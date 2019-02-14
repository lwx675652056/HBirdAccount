package com.hbird.common.chating.interfaces.dataprovider;

import com.hbird.common.chating.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
