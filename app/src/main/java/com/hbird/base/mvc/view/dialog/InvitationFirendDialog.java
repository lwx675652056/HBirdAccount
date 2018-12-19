package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbird.base.R;

import static com.hbird.base.R.id.lLayout_bg;

/**
 * Created by Liul on 2018/10/22.
 * 邀请好友 弹窗dialog
 */

public class InvitationFirendDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private LinearLayout lLayout_bg;
    private ImageView ivClose;
    private ImageView chaKan;
    private TextView tvNum1;
    private TextView tvNum2;

    public InvitationFirendDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }
    public InvitationFirendDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_invitation_friend, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        ivClose = (ImageView)view.findViewById(R.id.iv_close);
        chaKan = (ImageView)view.findViewById(R.id.iv_chakan);
        tvNum1 = (TextView)view.findViewById(R.id.tv_num1);
        tvNum2 = (TextView)view.findViewById(R.id.tv_num2);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.65), LinearLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }
    public InvitationFirendDialog setMsg(String num1,String num2) {
        tvNum1.setText("成功邀请"+num1+"位好友");
        tvNum2.setText("您获得了"+num2+"张丰丰票哟~");
        return this;
    }
    //去查看
    public InvitationFirendDialog setChaKan(final View.OnClickListener listener){
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
    public InvitationFirendDialog setCancleButton(final View.OnClickListener listener) {
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
