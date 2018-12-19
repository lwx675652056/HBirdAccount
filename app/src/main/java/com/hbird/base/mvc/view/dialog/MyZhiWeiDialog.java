package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.widget.BaseDialog;
import com.hbird.base.mvc.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息管理界面 选择职位的dialog
 *
 */
public class MyZhiWeiDialog extends BaseDialog {

    private TextView tv_cancel;
    private TextView tv_sure;
    private TextView tv_title;
    private WheelView wheelView;
    private OnClickCallback callback;
    private String data;
    private List<String> list;

    public MyZhiWeiDialog(Context context) {
        super(context);
        this.mContext = context;
        this.list = new ArrayList<>();
            list.add("基层员工");
            list.add("中层管理");
            list.add("高层管理");
        this.dialog = new Dialog(mContext, R.style.picker_dialog);
        dialog.setContentView(R.layout.dialog_money_picker);
        tv_cancel = (TextView) dialog.findViewById(R.id.cancel);
        tv_sure = (TextView) dialog.findViewById(R.id.ok);
        tv_title = (TextView) dialog.findViewById(R.id.title);
        tv_title.setText("选择职位");
        wheelView = (WheelView) dialog.findViewById(R.id.wheel);
        wheelView.setData(list);
        data = list.get(0);
        wheelView.setOnSelectListener(new WheelView.SelectListener() {
            @Override
            public void onSelect(int index, String text) {
                data = text;
            }
        });
        tv_cancel.setOnClickListener(this);
        tv_sure.setOnClickListener(this);

        setDialogLocation(mContext, dialog);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ok:
                if (callback != null) {
                    if (!data.equals("") && !"".equals(data)) {
                        callback.onSure(data);
                    }
                }
                break;
            case R.id.cancel:
                if (callback != null) {
                    callback.onCancel();
                }
                break;
        }
    }

    public void setCallback(OnClickCallback callback) {
        this.callback = callback;
    }

    public interface OnClickCallback {
        void onCancel();

        void onSure(String data);
    }
}
