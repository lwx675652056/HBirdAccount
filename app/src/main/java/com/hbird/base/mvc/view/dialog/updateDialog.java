package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;


/**
 * Created by Liul at 2018/07/14
 */
public class updateDialog {


    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView content;
    private RelativeLayout bUpdate;
    private ImageView del;
    private Display display;

    public updateDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public updateDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);

        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        txt_title = view.findViewById(R.id.tv_title);
        content = view.findViewById(R.id.tv_content);
        bUpdate = view.findViewById(R.id.tv_update);
        del = view.findViewById(R.id.iv_del);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        return this;
    }

    public updateDialog setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setText("提示");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public updateDialog setMsg(String s) {
        if ("".equals(s)) {
            content.setText("");
        } else {
            content.setText(s);
        }

        return this;
    }

    /**
     * 设置更新
     * @param listener
     * @return
     */
    public updateDialog setUpdateButton(final View.OnClickListener listener) {
        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }
    /**
     * 点击取消
     * @param listener
     * @return
     */
    public updateDialog setCancleButton(final View.OnClickListener listener) {
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show() {
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public boolean isShow(){
        return dialog.isShowing();
    }
}
