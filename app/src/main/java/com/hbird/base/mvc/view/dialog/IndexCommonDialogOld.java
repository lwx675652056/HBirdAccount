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

import com.hbird.base.R;
import com.hbird.base.mvc.fragement.IndexFragementOld;
import com.ljy.devring.image.support.GlideApp;

/**
 * Created by Liul on 2018/10/23.
 * 首页普通 弹窗dialog
 */

public class IndexCommonDialogOld {
    private Context context;
    private IndexFragementOld context2;
    private Dialog dialog;
    private Display display;
    private LinearLayout lLayout_bg;
    private ImageView ivClose;
    private ImageView chaKan;

    public IndexCommonDialogOld(Context context, IndexFragementOld context2) {
        this.context = context;
        this.context2 = context2;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }
    public IndexCommonDialogOld builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_index_common, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        ivClose = (ImageView)view.findViewById(R.id.iv_dels);
        chaKan = (ImageView)view.findViewById(R.id.iv_img_bg);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }
    public IndexCommonDialogOld setMsg(String url) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .override(500,700)
                .into(chaKan);
        return this;
    }
    //去查看
    public IndexCommonDialogOld setChaKan(final View.OnClickListener listener){
        chaKan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
                dialog.dismiss();
            }
        });
        return this;
    }
    /**
     * 设置关闭
     * @param listener
     * @return
     */
    public IndexCommonDialogOld setCancleButton(final View.OnClickListener listener) {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }
    public void show() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
