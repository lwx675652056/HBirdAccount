package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.common.Constants;

import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: InvitationDialog
 * @date: 2019/1/3 18:41
 * @Description: 邀请好友
 */
public class InvitationDialog {

    Dialog dialog;
    public InvitationDialog(Context context) {
        this.dialog = new Dialog(context, R.style.apd_dialog_style);
        dialog.setContentView(R.layout.dialog_invitation);

        String fengNiaoId = (String) SharedPreferencesUtil.get(Constants.FENGFENG_ID, "");
        TextView tvId = dialog.findViewById(R.id.tv_id);
        tvId.setText(fengNiaoId);

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.tv_copy).setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", fengNiaoId);
            cm.setPrimaryClip(mClipData);
            ToastUtil.showShort("复制成功");
        });
    }

    public void show(){
        if (dialog!=null){
            dialog.show();
        }
    }
}
