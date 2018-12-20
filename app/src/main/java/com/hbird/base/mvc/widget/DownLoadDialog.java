package com.hbird.base.mvc.widget;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbird.base.R;

import java.text.DecimalFormat;

import sing.util.ScreenUtil;

public class DownLoadDialog extends BaseDialog {

    public DownLoadDialog(Context context) {
        super(context);

        dialog.setContentView(R.layout.dialog_update_app);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;//表示处理了
            }
            return false;
        });

        dialog.show();

        mProgress = dialog.findViewById(R.id.download_info_progress);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mProgress.getLayoutParams();
        params.width = ScreenUtil.getScreenWidth(context) / 3 * 2;
        mProgress.setLayoutParams(params);

        mSize = dialog.findViewById(R.id.tv_size);
        mSize.setText("0MB/0MB");
    }

    private TextView mSize;
    private DownLoadProgressbar mProgress;

    //循环模拟下载过程
    public void start(long current, long total) {
        if (current <= total) {
            mSize.setText(getMB(current) + "/" + getMB(total));
            mProgress.setMaxValue(total);
            mProgress.setCurrentValue(current);
        }
    }

    private String getMB(long size) {
        int MB = 1024 * 1024;//定义MB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String result = df.format(size / (float) MB);
        return result + "MB";
    }

    public boolean isShow(){
        if (dialog != null){
            return dialog.isShowing();
        }
        return false;
    }
}
