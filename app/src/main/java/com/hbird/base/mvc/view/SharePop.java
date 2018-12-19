package com.hbird.base.mvc.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hbird.base.R;


/**
 * Created by Liul on 2018/7/20.
 */

public class SharePop {

    private PopupWindow popupWindow;

    private Context context;

    private TextView linearone , lineartwo;

    onMyClickListener listener;

    public SharePop(Context context, onMyClickListener listeners) {

        this.context = context;

        listener = listeners;
        initPop();

    }


    private void initPop() {

        // 一个自定义的布局，作为显示的内容
        View popView = LayoutInflater.from(context).inflate(R.layout.view_pops, null);

        popupWindow = new PopupWindow(context);

        popupWindow.setContentView(popView);

        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        popupWindow.setTouchable(true); // 设置popupwindow可点击

        popupWindow.setOutsideTouchable(true); // 设置popupwindow外部不可点击

        popupWindow.setFocusable(true); // 如果不单单是pop消失，而且还要外部控件可以响应点击事件，改为false那么需要pop不获取焦点，点击事件就可以被外部控件拿到。

        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });

        //初始化控件
        initPopView(popView);


    }

    public void showPopWindow() {

        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.5f;
        ((Activity) context).getWindow().setAttributes(lp);

    }

    //初始化布局
    private void initPopView(final View popView) {

        linearone = (TextView) popView.findViewById(R.id.tv_wxs);
        lineartwo = (TextView) popView.findViewById(R.id.tv_wxf);

        linearone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setWx();
                popupWindow.dismiss();

            }
        });

        lineartwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setWxq();
                popupWindow.dismiss();


            }
        });
    }
    public interface onMyClickListener {
        void setWx();
        void setWxq();

    }

}
