package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.AddMoreTypeBean;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuAddMoreTypeBean;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liul on 2018/7/4.
 */

public class MyGridView2Adapter extends BaseAdapter {
    private Context mContext;
    ViewHolder viewHolder = null;
    private int ii;
    private int jj;
    /**
     * 每个分组下的每个子项的 GridView 数据集合
     */
    private List<ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean> itemGridList;
    private LinearLayout mLinearLayout;
    int groupPosition;
    private final ArrayList<Boolean> list;
    private ArrayList<Boolean> checking;
    ArrayList<String> listString;

    public MyGridView2Adapter(Context mContext, List<ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean> itemGridList
            , int groupPosition,int ii,int jj, ArrayList<String> listString) {
        this.mContext = mContext;
        this.itemGridList = itemGridList;
        list = new ArrayList<>();
        for (int i =0;i<itemGridList.size();i++){
            list.add(false);
        }
        this.ii = ii;
        this.jj = jj;
        this.listString = listString;
        this.groupPosition = groupPosition;
    }

    @Override
    public int getCount() {
        return itemGridList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemGridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void setChecks(int group,int item){

        if(groupPosition==group){
            checking = new ArrayList<>();
            checking.clear();
            for(int i=0;i<list.size();i++){
                if(i==item){
                    checking.add(true);
                }else {
                    checking.add(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.gridview_item, null);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mLinearLayout = (LinearLayout) inflater.inflate(R.layout.gridview_item, null);

            viewHolder.textTitle= (TextView) mLinearLayout.findViewById(R.id.tv_gridview);
            viewHolder.imageView = (ImageView) mLinearLayout.findViewById(R.id.iv_head);
            viewHolder.imgChoose = (ImageView) mLinearLayout.findViewById(R.id.iv_img_choose);
            viewHolder.mBg = (RelativeLayout) mLinearLayout.findViewById(R.id.rl_bg);
          /*  if(checking!=null && checking.size()>0){
                if(checking.get(position)){
                    viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_yellow);
                }else {
                    viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_gray);
                }
            }*/
            boolean hasDate = getHasDate(position);
            if(groupPosition==ii){
                if(position==jj){
                    viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_yellow);
                    viewHolder.imgChoose.setImageResource(R.mipmap.ic_shouruxuanzhong_normal);
                    viewHolder.imgChoose.setVisibility(View.GONE);
                }else {
                    if(hasDate){
                        //表示有该数据
                        viewHolder.mBg.setBackgroundResource(R.drawable.item_choose_bg_come);
                        viewHolder.imgChoose.setImageResource(R.mipmap.ic_shouruxuanzhong_normal);
                        viewHolder.imgChoose.setVisibility(View.VISIBLE);

                    }else {
                        viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_gray);
                        viewHolder.imgChoose.setVisibility(View.GONE);
                    }
                }
            }else {
                if(hasDate){
                    //表示有该数据
                    viewHolder.mBg.setBackgroundResource(R.drawable.item_choose_bg_come);
                    viewHolder.imgChoose.setImageResource(R.mipmap.ic_shouruxuanzhong_normal);
                    viewHolder.imgChoose.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_gray);
                    viewHolder.imgChoose.setVisibility(View.GONE);
                }
            }

            viewHolder.textTitle.setText(itemGridList.get(position).getIncomeName());
            GlideApp.with(mContext)
                    .load(itemGridList.get(position).getIcon())
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(50,50)
                    .into(viewHolder.imageView);
            //标记当前view
            mLinearLayout.setTag(viewHolder);
        }else {
            mLinearLayout = (LinearLayout) convertView;
            viewHolder = (ViewHolder) mLinearLayout.getTag();
            //获取id
            viewHolder.textTitle = mLinearLayout.findViewById(R.id.tv_gridview);
            viewHolder.imageView = mLinearLayout.findViewById(R.id.iv_head);
            //设置数据
            boolean hasDate = getHasDate(position);
            if(groupPosition==ii){
                if(position==jj){
                    viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_yellow);
                    viewHolder.imgChoose.setVisibility(View.GONE);
                }else {
                    if(hasDate){
                        //表示有该数据
                        viewHolder.mBg.setBackgroundResource(R.drawable.item_choose_bg_come);
                        viewHolder.imgChoose.setImageResource(R.mipmap.ic_shouruxuanzhong_normal);
                        viewHolder.imgChoose.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_gray);
                        viewHolder.imgChoose.setVisibility(View.GONE);
                    }
                }
            }else {
                if(hasDate){
                    //表示有该数据
                    viewHolder.mBg.setBackgroundResource(R.drawable.item_choose_bg_come);
                    viewHolder.imgChoose.setImageResource(R.mipmap.ic_shouruxuanzhong_normal);
                    viewHolder.imgChoose.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.mBg.setBackgroundResource(R.drawable.shape_cycle_gray);
                    viewHolder.imgChoose.setVisibility(View.GONE);
                }
            }

            viewHolder.textTitle.setText(itemGridList.get(position).getIncomeName());
            //使用glide加载图片
            GlideApp.with(mContext)
                    .load(itemGridList.get(position).getIcon()) //加载地址
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(50,50)
                    .into(viewHolder.imageView);//显示的位置
        }

        return mLinearLayout;
    }
    private boolean getHasDate(int position) {
        String spendName = itemGridList.get(position).getIncomeName();
        for (int i = 0; i < listString.size(); i++) {
            if(TextUtils.equals(listString.get(i),spendName)){
                return true;
            }
        }
        return false;
    }
    //使用viewHolder缓存数据
    public static class ViewHolder {
        TextView textTitle;
        ImageView imageView;
        ImageView imgChoose;
        RelativeLayout mBg;
    }
}
