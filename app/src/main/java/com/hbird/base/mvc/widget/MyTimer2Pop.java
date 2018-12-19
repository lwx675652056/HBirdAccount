package com.hbird.base.mvc.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.hbird.base.R;
import com.hbird.base.mvc.adapter.FenXiMonthRecyclerViewAdapter;
import com.hbird.base.mvc.adapter.MonthRecyclerViewAdapter;
import com.hbird.base.mvc.adapter.MyRecyclerViewAdapter;
import com.hbird.base.mvc.base.MyDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by liul on 2018/08/16.
 * 自定义pop 图表 - 分析 -选择月份
 */

public class MyTimer2Pop implements View.OnClickListener{

    private PopupWindow popupWindow;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView monthRecyclerView;
    private PopDismissListener dismissListener;
    private View view;
    private ArrayList<String> listMonth;
    private int monthItem;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager m2LayoutManager;
    private MyRecyclerViewAdapter mAdapter;
    private FenXiMonthRecyclerViewAdapter monthAdapter;
    OnDateListener listener;

    public MyTimer2Pop(Context context, View view,  ArrayList<String> listMonth, int monthItem,
                       OnDateListener listener,PopDismissListener dismissListener) {
        this.context = context;
        this.view = view;
        this.listMonth = listMonth;
        this.dismissListener = dismissListener;
        this.monthItem = monthItem;
        this.listener = listener;
        initPop();
    }

    public void setDismissListener(PopDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void initPop() {
        // 一个自定义的布局，作为显示的内容
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_my_timer, null);

        popupWindow = new PopupWindow(context);

        popupWindow.setContentView(popView);

        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //设置点击外面让其消失
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //初始化控件
        initPopView(popView);
        // 设置pop消失时背景变亮
        bgBianLiang();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从下往上弹出
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
    }

    public void showPopWindow() {
        popupWindow.showAsDropDown(view);
        //  设置背景颜色变暗
        bgBianAn();
    }

    private void initPopView(View popView) {
        //月
        m2LayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        monthAdapter = new FenXiMonthRecyclerViewAdapter(context,listMonth,monthItem);
        monthRecyclerView = (RecyclerView) popView.findViewById(R.id.recyclerMothView);
        monthRecyclerView.setLayoutManager(m2LayoutManager);
        monthRecyclerView.setItemAnimator(new DefaultItemAnimator());
        monthRecyclerView.setAdapter(monthAdapter);
        Move2ToPosition(new LinearLayoutManager(context),monthRecyclerView,monthItem);

        monthAdapter.setOnItemClickListener(new FenXiMonthRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                monthAdapter.setItemClick(position);
                listener.getMonthList(listMonth.get(position));
                popupWindow.dismiss();
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }
    public static void Move2ToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }
    private void bgBianLiang() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);

                if(dismissListener!=null) {
                    dismissListener.PopDismiss();
                }
            }
        });
    }

    private void bgBianAn() {

        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.5f;
        ((Activity) context).getWindow().setAttributes(lp);

    }

    @Override
    public void onClick(View v) {

    }
    public interface OnDateListener{
        void getMonthList(String s);
    }

    public interface PopDismissListener{
        void PopDismiss();
    }
}
