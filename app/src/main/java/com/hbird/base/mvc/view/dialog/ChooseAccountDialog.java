package com.hbird.base.mvc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hbird.base.R;
import com.hbird.base.mvc.widget.BaseDialog;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;

import java.util.ArrayList;
import java.util.List;

import sing.BaseAdapter;
import sing.BaseViewHolder;
import sing.util.ScreenUtil;
import sing.util.SharedPreferencesUtil;

/**
 * @author: LiangYX
 * @ClassName: ChooseAccountDialog
 * @date: 2018/12/27 18:01
 * @Description: 选择账户
 */
public class ChooseAccountDialog extends BaseDialog {

    private RecyclerView recyclerView;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChooseAccountDialog(Context context) {
        super(context);
        this.mContext = context;

        this.dialog = new Dialog(mContext, R.style.picker_dialog);
        dialog.setContentView(R.layout.dialog_choose_account);

        String str = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
        List<AssetsBean> temp = JSON.parseArray(str, AssetsBean.class);

        recyclerView = dialog.findViewById(R.id.recycler_view);

        // 添加账户
        dialog.findViewById(R.id.ll_add_account).setOnClickListener(v -> listener.onClick(temp, 0));

        setDialogLocation(context, dialog);

        LinearLayout ll = dialog.findViewById(R.id.ll_parent);
        ViewGroup.LayoutParams params = ll.getLayoutParams();
        params.height = ScreenUtil.getScreenHeight(context) / 2;
        ll.setLayoutParams(params);

        // 判断最后一次选择的账户是否被删除，如果被删除，置为未选择
        if (!isExist(temp)) {
            SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ID, -1);
            SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_DESC, "未选择");
            SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ICON, "");
        }

        List<AssetsBean> list = new ArrayList<>();
        list.add(null);// 第一个为 不选择账户

        if (temp != null && temp.size() > 0) {
            list.addAll(temp);
        }

        recyclerView.setAdapter(new MyAdapter(context, list, R.layout.row_account_dialog));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private boolean isExist(List<AssetsBean> temp) {
        int id = (int) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_ID, -1);
        for (int i = 0; i < temp.size(); i++) {
            if (id == temp.get(i).assetsType) {
                return true;
            }
        }
        return false;
    }

    class MyAdapter extends BaseAdapter<AssetsBean> {

        private int accountId;

        public MyAdapter(Context context, List<AssetsBean> list, int layoutId) {
            super(context, list, layoutId);
            accountId = (int) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_ID, -1);
        }

        @Override
        protected void bindData(BaseViewHolder holder, AssetsBean data, int position) {
            if (position == 0) {
                holder.getView(R.id.iv_checked).setVisibility(accountId == -1 ? View.VISIBLE : View.GONE);

                holder.setText(R.id.tv_title, "不选择账户")
                        .setImageResource(R.id.iv_bg, R.mipmap.jzzhxz_icon_bxzh_normal)
                        .setOnItemClickListener(v -> listener.onClick(data, 1));
            } else {
                holder.getView(R.id.iv_checked).setVisibility(data.assetsType == accountId ? View.VISIBLE : View.GONE);

                holder.setText(R.id.tv_title, data.assetsName)
                        .setImageByUrl(R.id.iv_bg, new BaseViewHolder.HolderImageLoader(data.icon) {
                            @Override
                            public void displayImage(Context context, ImageView imageView, String imagePath) {
                                Glide.with(context).load(imagePath).into(imageView);
                            }
                        })
                        .setOnItemClickListener(v -> listener.onClick(data, 1));
            }
        }
    }

    public interface OnItemClickListener {
        public void onClick(Object data, int type);
    }
}
