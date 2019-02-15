package com.hbird.base.mvc.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.adapter.FenXiMonthRecyclerViewAdapter;
import com.hbird.base.mvc.adapter.MyRecyclerViewAdapter;

/**
 * Created by Liul on 2018/9/19.
 */

public class Guide4Pop implements View.OnClickListener {
    private PopupWindow popupWindow;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView monthRecyclerView;
    private PopDismissListener dismissListener;
    private View view;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager m2LayoutManager;
    private MyRecyclerViewAdapter mAdapter;
    private FenXiMonthRecyclerViewAdapter monthAdapter;
    private String title;

    public Guide4Pop(Context context, View view, String title, PopDismissListener dismissListener) {
        this.context = context;
        this.view = view;
        this.title = title;
        this.dismissListener = dismissListener;
        initPop();
    }

    public void setDismissListener(PopDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void initPop() {
        // 一个自定义的布局，作为显示的内容
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_guide_4, null);

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

        // 设置弹出窗体显示时的动画
        popupWindow.setAnimationStyle(R.style.new_guide_down_anim);
    }

    public void showPopWindow() {
        popupWindow.showAsDropDown(view);

        //  设置背景颜色变暗
        //bgBianAn();
    }
    public void showPopWindowToUp() {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //参数3 可以调整 距离底部图片的距离（上下调整）
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0],location[1]-130);
        //  设置背景颜色变暗
        //bgBianAn();
    }

    public void hidePopWindow() {
        popupWindow.dismiss();
    }

    private void initPopView(View popView) {
        TextView text = (TextView)popView.findViewById(R.id.tv_text);
        LinearLayout content01 = (LinearLayout)popView.findViewById(R.id.ll_content01);
            text.setText(title);
            content01.setVisibility(View.VISIBLE);

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

    }

    public interface PopDismissListener{
        void PopDismiss();
    }
}
