package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.ZhangBenMsgBean;
import com.hbird.base.util.DateUtils;
import com.ljy.devring.image.support.GlideApp;

import java.util.List;

import sing.util.ScreenUtil;

/**
 * @author: LiangYX
 * @ClassName: MyZhangBenAdapter
 * @date: 2018/12/13 10:53
 * @Description: 我的账本
 */
public class MyZhangBenAdapter extends BaseAdapter {
    private Context context;
    private List<ZhangBenMsgBean> list;
    private int position = 0;
    private int width = 0;
    private int height = 0;

    public MyZhangBenAdapter(Context context, List<ZhangBenMsgBean> data) {
        this.context = context;
        this.list = data;
        width = ScreenUtil.getScreenWidth(context) / 6;
        height = width * 25 / 17;
    }

    public void setData(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_my_zhangben, null);
            viewHolder.imgChoose = convertView.findViewById(R.id.iv_img_choose);
            viewHolder.img = convertView.findViewById(R.id.iv_img);
            viewHolder.zbName = convertView.findViewById(R.id.tv_name);
            viewHolder.zbDesc = convertView.findViewById(R.id.tv_desc);
            viewHolder.zbNames = convertView.findViewById(R.id.tv_types);
            viewHolder.zbTime = convertView.findViewById(R.id.tv_update_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.zbDesc.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
        String daysDot = i == 0 ? "" : "更新至" + DateUtils.getDaysDot(Long.parseLong(list.get(i).getZbUTime()));
        viewHolder.zbName.setText("[" + list.get(i).getZbName() + "]");
        viewHolder.zbNames.setText(list.get(i).getZbType());
        viewHolder.zbTime.setText(daysDot);

        GlideApp.with(context)
                .load(i == 0 ? R.mipmap.icon_zongzhangben_normal : list.get(i).getZbImg())
                .skipMemoryCache(false)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(width, height)
                .into(viewHolder.img);

        viewHolder.imgChoose.setVisibility(View.INVISIBLE);
        if (position == i) {
            viewHolder.imgChoose.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public static class ViewHolder {
        ImageView img;
        ImageView imgChoose;
        TextView zbName;
        TextView zbDesc;
        TextView zbNames;
        TextView zbTime;
    }
}
