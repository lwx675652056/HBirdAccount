package com.hbird.ui.detailed;

import android.content.Context;
import android.view.View;

import com.hbird.base.databinding.RowAccountDetailedBinding;
import com.hbird.bean.AccountDetailedBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: AccountDetailedModle
 * @date: 2018/12/20 14:53
 * @Description: 首页的更多 账本明细
 */
public class AccountDetailedAdapter extends BaseRecyclerAdapter<AccountDetailedBean,RowAccountDetailedBinding> {

    public AccountDetailedAdapter(Context context, List<AccountDetailedBean> list, int layoutId) {
        super(context, list, layoutId);
    }

    @Override
    protected void onBindItem(RowAccountDetailedBinding binding, AccountDetailedBean accountDetailedBean, int position) {
        binding.setBean(accountDetailedBean);
//        binding.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUitl.showShort("点击了"+user.getName()+position);
//            }
//        });
    }
}