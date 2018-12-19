package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.hbird.base.R;
import com.hbird.base.mvc.widget.PickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Liul on 2018/07/06.
 * 自定义时间选择器
 */

public abstract class APDUserDateDialog extends Dialog implements View.OnClickListener {

    private PickerView mPvYear;
    private PickerView mPvMonth;
    private PickerView mPvDay;
    private Button mBtnOk;
    private Button mBtnCancel;
    final private List<String> mYears = new ArrayList<>();
    final private List<String> mMonths = new ArrayList<>();
    final private List<String> mDays = new ArrayList<>();
    private Calendar mCal;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mBeginYear = 1980;
    private int mEndYear = 2050;

    public APDUserDateDialog(Context context) {
        super(context, R.style.apd_dialog_style);
    }

    public APDUserDateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected APDUserDateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_select);

        mPvMonth = (PickerView) findViewById(R.id.pv_month);
        mPvYear = (PickerView) findViewById(R.id.pv_year);
        mPvDay = (PickerView) findViewById(R.id.pv_day);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        onLoadedView();
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnCancel) {
            this.dismiss();
        } else if (view == mBtnOk) {
            onBtnOkClick(checkTime(mPvYear.getSelectedStr()), checkTime(mPvMonth.getSelectedStr()), checkTime(mPvDay.getSelectedStr()));
            this.dismiss();
        }
    }

    protected void onLoadedView() {
        initData();

        mPvYear.setData(mYears);
        mPvYear.setSelected(mYears.indexOf(String.valueOf(mCurrentYear + "")));
        mPvYear.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text, int index) {
                List<String> mMonthsNew = new ArrayList<>();
                if (mCurrentYear == mBeginYear + mPvYear.getSelected()) {
                    for (int i = 1; i <= mCurrentMonth; i++) {
                        mMonthsNew.add(i + "");
                    }
                    mPvMonth.setData(mMonthsNew);
                }
                if (mCurrentMonth == mPvMonth.getSelected() + 1 || 1 == mPvMonth.getSelected()) {
                    mPvDay.setData(getDayList(mPvMonth.getSelected()));
                }

                Log.e("DateDialog--Year", text + index);
            }
        });


        mPvMonth.setData(mMonths);
        mPvMonth.setSelected(mMonths.indexOf(String.valueOf(mCurrentMonth + "")));
        mPvMonth.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text, int index) {
                mPvDay.setData(getDayList(index));

                Log.e("DateDialog-Month", text + index);
            }
        });

        mPvDay.setData(mDays);
        mPvDay.setSelected(mDays.indexOf(String.valueOf(mCurrentDay + "")));
        mPvDay.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text, int index) {
                Log.e("DateDialog--Day", text + index);
            }
        });
    }

    private List<String> getDayList(int selectMonth) {
        List<String> mDaysNew = new ArrayList<>();

        if (mCurrentMonth == 1 + selectMonth) {
            for (int i = 1; i <= mCurrentDay; i++) {
                mDaysNew.add(i + "");
            }
        } else {
            for (int i = 1; i <= 28; i++) {
                mDaysNew.add(i + "");
            }
            // 2月份需要判断是否为闰月
            if (1 == selectMonth) {
                if (isLeapYear(mBeginYear + mPvYear.getSelected())) {
                    mDaysNew.add("29");
                }
            } else if (3 == selectMonth || 5 == selectMonth || 8 == selectMonth || 10 == selectMonth) {
                mDaysNew.add("29");
                mDaysNew.add("30");
            } else {
                mDaysNew.add("29");
                mDaysNew.add("30");
                mDaysNew.add("31");
            }
        }
        return mDaysNew;
    }

    private void initData() {
        mCal = Calendar.getInstance();
        mCurrentYear = mCal.get(Calendar.YEAR);
        mCurrentMonth = mCal.get(Calendar.MONTH) + 1;
        mCurrentDay = mCal.get(Calendar.DAY_OF_MONTH);

        for (int i = mBeginYear; i <= mEndYear; i++) {
            mYears.add(i + "");
        }
        for (int i = 1; i <= 12; i++) {
            mMonths.add(i + "");
        }
        for (int i = 1; i <= 31; i++) {
            mDays.add(i + "");
        }
    }

    /**
     * 判断是否为闰年
     *
     * @return 是否为闰年
     */
    private boolean isLeapYear(int year) {
        if (0 == year % 100) {
            if (0 == year % 400) {
                return true;
            }
        } else if (0 == year % 4) {
            return true;
        }
        return false;
    }

    private String checkTime(String time){
        String mTime = "";
        if (Integer.parseInt(time)<10){
            mTime = "0"+time;
        }else
        {
            mTime = time;
        }
        return mTime;
    }
    protected abstract void onBtnOkClick(String year, String month, String day);
}
