package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hbird.base.R;


/**
 * Created by Liul at 2018/07/12
 */
public class DialogToGig {


    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private ImageView gifView;
    private Display display;

    public DialogToGig(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public DialogToGig builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_gif_view, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        gifView = (ImageView)view.findViewById(R.id.gif_view);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setDimAmount(0.5f);//dialog以外的背景透明
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;//dialog背景设置透明度
        window.setAttributes(lp); //lp.alpha = 0.5f 透明度设置 其值要合理 自己反复测试

        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int)LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Glide.with(context)
                .asGif()
                .load(R.drawable.loading)
                .into(gifView);
        return this;
    }


    /**
     * 设置取消
     */
    public void hide() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    public void show() {
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public boolean isShow(){
        return dialog.isShowing();
    }
}
