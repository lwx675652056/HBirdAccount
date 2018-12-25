package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.IndexFirends;
import com.hbird.base.mvc.fragement.IndexFragementOld;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;

public class IndexGridViewAdapterOld extends BaseAdapter {
    private Context mContext;
    private IndexFragementOld context;
    private ArrayList<IndexFirends> list;

    public IndexGridViewAdapterOld(Context context, IndexFragementOld cc, ArrayList<IndexFirends> l) {
        this.mContext = context;
        this.context = cc;
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
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.gridview_view, null);
            viewHolder.img = convertView.findViewById(R.id.imagess);
            viewHolder.img2 = convertView.findViewById(R.id.imagess2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (i == 0) {
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.img2.setVisibility(View.GONE);
        } else {
            viewHolder.img.setVisibility(View.GONE);
            viewHolder.img2.setVisibility(View.VISIBLE);
        }

        GlideApp.with(context)
                .load(TextUtils.isEmpty(list.get(i).getImgUrl()) ? R.mipmap.account_logo : list.get(i).getImgUrl())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(100, 100)
                .into(i == 0 ? viewHolder.img : viewHolder.img2);
        return convertView;
    }

    public static class ViewHolder {
        com.hbird.base.mvc.widget.cycleView img;
        com.hbird.base.mvc.widget.cycleView img2;
    }

}
