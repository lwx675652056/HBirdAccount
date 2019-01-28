package com.hbird.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hbird.base.R;

public class BottomChooseDialog {

    private Dialog dialog;

    public BottomChooseDialog(Activity activity,OnDialogClickListener listener) {
        dialog = new Dialog(activity, R.style.bottom_dialog);

        View view = LayoutInflater.from(activity).inflate(R.layout.upload_user_pic, null, false);
        dialog.setContentView(view);
        Window win = dialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;//设置宽度
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;//设置高度
            win.setAttributes(params);
        }

        TextView one = view.findViewById(R.id.Photograph);
        TextView two = view.findViewById(R.id.selectImage_from_local);
        View cancel = view.findViewById(R.id.dimiss_dialoag);

        one.setOnClickListener(view1 -> {
            hide();
            listener.OnClick(0);
        });

        two.setOnClickListener(view2 -> {
            hide();
            listener.OnClick(1);
        });
        cancel.setOnClickListener(v -> {
            hide();
//            listener.OnClick(2);
        });
    }

    public interface OnDialogClickListener {
        void OnClick(int position);
    }

    public void show() {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }

    public void hide() {
        dialog.hide();
        dialog.dismiss();
    }
}