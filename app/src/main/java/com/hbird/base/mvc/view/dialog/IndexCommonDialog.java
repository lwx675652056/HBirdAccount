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
import com.hbird.ui.index.IndexFragement;
import com.ljy.devring.image.support.GlideApp;

/**
 * Created by Liul on 2018/10/23.
 * 首页普通 弹窗dialog
 */

public class IndexCommonDialog {
    private Context context;
    private IndexFragement context2;
    private Dialog dialog;
    private Display display;
    private LinearLayout lLayout_bg;
    private ImageView ivClose;
    private ImageView chaKan;

    public IndexCommonDialog(Context context, IndexFragement context2) {
        this.context = context;
        this.context2 = context2;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IndexCommonDialog builder() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_index_common, null);

        lLayout_bg = view.findViewById(R.id.lLayout_bg);
        ivClose = view.findViewById(R.id.iv_dels);
        chaKan = view.findViewById(R.id.iv_img_bg);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    public IndexCommonDialog setMsg(String url) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .override(500, 700)
                .into(chaKan);
        return this;
    }

    //去查看
    public IndexCommonDialog setChaKan(final View.OnClickListener listener) {
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
     *
     * @param listener
     * @return
     */
    public IndexCommonDialog setCancleButton(final View.OnClickListener listener) {
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
