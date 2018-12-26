package com.hbird.common.calendar.interf;

import android.graphics.Canvas;

import com.hbird.common.calendar.view.Day;

public interface IDayRenderer {

    void refreshContent();

    void drawDay(Canvas canvas, Day day);

    IDayRenderer copy();

}
