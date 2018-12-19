package com.hbird.base.mvc.global.modle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hbird.base.R;
import com.hbird.base.util.DensityUtils;
import com.hbird.base.util.ScreenUtils;

import java.util.ArrayList;

import static android.R.attr.x;

/**
 * Created by Liul on 2018-07-05.
 */
public class ImageGridViewAdapter extends BaseAdapter {

    private ArrayList<ImageBean> marrayList;


    private Context context;

    private int mwidth;

    public ImageGridViewAdapter(Context context) {

        this.context=context;

        mwidth= ScreenUtils.getScreenWidth(context);

    }


    /***
     * 设置数据源
     * @param marrayList
     */
    public void setArrayList(ArrayList<ImageBean> marrayList) {

        this.marrayList = marrayList;
        notifyDataSetChanged();


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return marrayList.get(position);
    }

    @Override
    public int getCount() {

        return marrayList==null?0:marrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adaper_image_gridview_item,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.image);

            holder.imageView.getLayoutParams().height= (mwidth- DensityUtils.dp2px(context, 40))/3;
            holder.imageView.setLayoutParams(holder.imageView.getLayoutParams());
            convertView.setTag(holder);

        }else{

            holder=(ViewHolder)convertView.getTag();

        }
        Glide.with(context).load("file://" + marrayList.get(position).getImagePath()).into( holder.imageView);

        return convertView;
    }

    private class ViewHolder{

        private ImageView imageView;

    }
}
