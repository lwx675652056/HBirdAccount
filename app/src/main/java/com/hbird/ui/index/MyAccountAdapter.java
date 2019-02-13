package com.hbird.ui.index;

import android.content.Context;

import com.hbird.base.databinding.RowMyAccountBinding;
import com.hbird.base.listener.OnItemClickListener;
import com.hbird.bean.AccountBean;

import java.util.List;

import sing.common.base.BaseRecyclerAdapter;

/**
 * @author: LiangYX
 * @ClassName: MyAccountAdapter
 * @date: 2019/2/12 18:00
 * @Description: 我的账本
 */
public class MyAccountAdapter extends BaseRecyclerAdapter<AccountBean, RowMyAccountBinding> {

//    private OnItemClickListener listener;
//    private List<AccountBean> list;
//    private int width = 0;
//    private int height = 0;
    public MyAccountAdapter(Context context, List<AccountBean> list, int layoutId, OnItemClickListener listener) {
        super(context, list, layoutId);
//        this.listener = listener;
//        this.list = list;
//        width = ScreenUtil.getScreenWidth(context) / 6;
//        height = width * 25 / 17;
    }

    @Override
    protected void onBindItem(RowMyAccountBinding binding, AccountBean data, int position) {
        binding.setData(data);

//        viewHolder.zbDesc.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
//        String daysDot = i == 0 ? "" : "更新至" + DateUtils.getDaysDot(Long.parseLong(list.get(i).getZbUTime()));
//        viewHolder.zbName.setText("[" + list.get(i).getZbName() + "]");
//        viewHolder.zbNames.setText(list.get(i).getZbType());
//        viewHolder.zbTime.setText(daysDot);
//
//        GlideApp.with(context)
//                .load(i == 0 ? R.mipmap.icon_zongzhangben_normal : list.get(i).getZbImg())
//                .skipMemoryCache(false)
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .override(width, height)
//                .into(viewHolder.img);
//
//        viewHolder.imgChoose.setVisibility(View.INVISIBLE);
//        if (position == i) {
//            viewHolder.imgChoose.setVisibility(View.VISIBLE);
//        }
    }
}