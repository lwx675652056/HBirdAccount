package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.IndexFirends;
import com.hbird.base.mvc.widget.cycleView;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/6.
 */

public class InviteGridViewAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<IndexFirends> list;

    public InviteGridViewAdapter(Context context, ArrayList<IndexFirends> l){
        this.mContext = context;
        this.list = l;
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
            viewHolder =new ViewHolder();
            convertView = View.inflate(mContext, R.layout.gridview_view, null);
            viewHolder.img = convertView.findViewById(R.id.imagess);
            viewHolder.img2 = convertView.findViewById(R.id.imagess2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.img2.setVisibility(View.GONE);

        GlideApp.with(mContext)
                .load(TextUtils.isEmpty(list.get(i).getImgUrl())?R.mipmap.account_logo:list.get(i).getImgUrl())
                .skipMemoryCache(false)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(100,100)
                .into(viewHolder.img);
        return convertView;
    }

    public static class ViewHolder {
        cycleView img;
        cycleView img2;// 没什么用   挡位置了，不知道是不是复用过，先隐藏
    }

}
