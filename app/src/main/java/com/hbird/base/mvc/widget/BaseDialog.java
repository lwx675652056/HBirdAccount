package com.hbird.base.mvc.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hbird.base.R;

/**
 * 选择器弹窗基类
 * Created by Liul on 2018/7/6.
 */
public class BaseDialog implements View.OnClickListener {
    protected Dialog dialog;
    protected Context mContext;

    protected BaseDialog(Context context) {
        this.mContext = context;
        this.dialog = new Dialog(mContext, R.style.picker_dialog);
    }


    protected void setDialogLocation(Context context, Dialog dialog) {
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams lp      = window.getAttributes();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp.x = 0;
        lp.y = manager.getDefaultDisplay().getHeight();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        dialog.onWindowAttributesChanged(lp);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
