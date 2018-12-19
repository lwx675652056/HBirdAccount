package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbird.base.R;


/**
 * Created by Liul at 2018/07/12
 */
public class DialogUtils {


    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView content;
    private Button bCancle;
    private Button del;
    private Display display;

    public DialogUtils(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public DialogUtils builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_utils_view, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.title_xxx);
        content = (TextView) view.findViewById(R.id.tv_content);
        bCancle = (Button) view.findViewById(R.id.btn_cancle);
        del = (Button) view.findViewById(R.id.btn_del);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    public DialogUtils setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setText("提示");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public DialogUtils setMsg(String s) {
        if ("".equals(s)) {
            content.setText("");
        } else {
            content.setText(s);
        }

        return this;
    }


    /**
     * 设置取消
     * @param text
     * @param listener
     * @return
     */
    public DialogUtils setCancleButton(String text,
                                                 final View.OnClickListener listener) {
        if ("".equals(text)) {
            bCancle.setText("取消");
        } else {
            bCancle.setText(text);
        }
        bCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }
    /**
     * 点击删除
     * @param text
     * @param listener
     * @return
     */
    public DialogUtils setSureButton(String text,
                                                 final View.OnClickListener listener) {
        if ("".equals(text)) {
            del.setText("确定");
        } else {
            del.setText(text);
        }
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
