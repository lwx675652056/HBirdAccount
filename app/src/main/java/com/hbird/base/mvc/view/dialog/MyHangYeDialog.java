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
 * 个人信息管理界面 选择行业的dialog
 *
 */
public class MyHangYeDialog extends BaseDialog {

    private TextView tv_cancel;
    private TextView tv_sure;
    private TextView tv_title;
    private WheelView wheelView;
    private OnClickCallback callback;
    private String data;
    private List<String> list;

    public MyHangYeDialog(Context context) {
        super(context);
        this.mContext = context;
        this.list = new ArrayList<>();
            list.add("农林牧渔业");
            list.add("采矿业");
            list.add("制造业");
            list.add("电力、热力、燃气及水生产和供应业");
            list.add("建筑业");
            list.add("批发和零售业");
            list.add("交通运输、仓储和邮政业");
            list.add("住宿餐饮业");
            list.add("信息和软件服务业");
            list.add("金融业");
            list.add("房地产业");
            list.add("租赁和商务服务业");
            list.add("科学研究和技术服务业");
            list.add("水利、环境和公共设置管理业");
            list.add("居民服务、修理和其他业务");
            list.add("教育");
            list.add("卫生和社会工作");
            list.add("文化、体育和娱乐业");
            list.add("公共管理、社会保障和社会组织");
            list.add("国际组织");


        this.dialog = new Dialog(mContext, R.style.picker_dialog);
        dialog.setContentView(R.layout.dialog_money_picker);
        tv_cancel = (TextView) dialog.findViewById(R.id.cancel);
        tv_sure = (TextView) dialog.findViewById(R.id.ok);
        tv_title = (TextView) dialog.findViewById(R.id.title);
        tv_title.setText("选择行业");
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
