package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.adapter.DialogChooseAdapter;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Liul at 2018/11/09
 * 选择账本
 */
public class Dialog2ChooseZb {


    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private ListView lv;
    private Display display;
    private List<AccountZbBean.ResultBean> list;
    private onClickListener oc;

    public Dialog2ChooseZb(Context context, List<AccountZbBean.ResultBean> listZb, onClickListener oc) {
        this.context = context;
        this.list = listZb;
        this.oc = oc;

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public Dialog2ChooseZb builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_choose_zb_view, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        lv = (ListView) view.findViewById(R.id.lv);
        DialogChooseAdapter adapter = new DialogChooseAdapter(context, list);
        lv.setAdapter(adapter);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        int margins = (int) context.getResources().getDimension(R.dimen.height_40_80);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), margins));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                oc.onclicks(i);
                dialog.dismiss();
            }
        });
        return this;
    }


 /*   public Dialog2ChooseZb setMsg(String s) {
        if ("".equals(s)) {
            content.setText("");
        } else {
            content.setText(s);
        }

        return this;
    }*/
    public interface onClickListener{
        void onclicks(int i);
    }

    public void show() {
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public boolean isShow(){
        return dialog.isShowing();
    }
}
