package com.hbird.common.calendar.view;

import com.hbird.common.calendar.Const;

public class Week {
    public int row;
    public Day[] days = new Day[Const.TOTAL_COL];

    public Week(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }
}