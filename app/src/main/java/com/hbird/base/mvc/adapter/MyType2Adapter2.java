package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.MyTypeItem;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.widget.DragAdapterInterface;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liul on 18/7/02.
 */
public class MyType2Adapter2 extends BaseAdapter implements DragAdapterInterface {

    private List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> datas = new ArrayList<>();
    private Context context;
    private BaseFragement mContext;
    private CommondataChangeInterface mCommondataChangeInterface;
    private String showType;

    public CommondataChangeInterface getCommondataChangeInterface() {
        return mCommondataChangeInterface;
    }

    public void setCommondataChangeInterface(CommondataChangeInterface commondataChangeInterface) {
        mCommondataChangeInterface = commondataChangeInterface;
    }

    public MyType2Adapter2(Context context, BaseFragement glideContext) {
        this.context = context;
        this.mContext = glideContext;
    }

    public void setDatas(List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> datas, String showType) {
        this.datas.clear();
        this.datas.addAll(datas);
        this.showType = showType;
        notifyDataSetChanged();
    }

    public void setShowType(String s){
        this.showType = s;
        notifyDataSetChanged();
    }

    public String getShowType(){
        return showType;
    }
    public int getCount() {
        return datas.size();
    }

    public void deleteItem(int i ){
        datas.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean bean = datas.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_item_come, null);
            holder.deleteImg = convertView.findViewById(R.id.delete_img);
            holder.iconImg = convertView.findViewById(R.id.icon_img);
            holder.nameTv = convertView.findViewById(R.id.name_tv);
            holder.container = convertView.findViewById(R.id.item_container);
            holder.rlParent = convertView.findViewById(R.id.rl_parent);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if(!TextUtils.equals(bean.getIncomeName(),"更多")){
            holder.rlParent.setBackground(ContextCompat.getDrawable(context,R.drawable.gray_circle));
            if ("1".equals(showType)) {
                holder.deleteImg.setVisibility(View.GONE);
            } else {
                holder.deleteImg.setVisibility(View.VISIBLE);
            }
        }else {
            holder.rlParent.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_circle));
            if(TextUtils.equals("1",showType)){
                holder.deleteImg.setVisibility(View.GONE);
                holder.container.setVisibility(View.VISIBLE);
            }else {
                holder.deleteImg.setVisibility(View.GONE);
                if(position==datas.size()-1){
                    holder.container.setVisibility(View.GONE);
                    return convertView;
                }
            }
        }


        String icon=bean.getIcon();
        if (!TextUtils.isEmpty(icon)) {
            //显示圆形图标
            //Glide.with(mContext).load(icon).into(holder.iconImg);

            GlideApp.with(mContext)
                    .load(icon)
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(50,50)
                    .into(holder.iconImg);
        }
        holder.nameTv.setText(bean.getIncomeName());
        holder.container.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    class Holder {
        public ImageView deleteImg;
        public ImageView iconImg;
        public TextView nameTv;
        public RelativeLayout container;
        public RelativeLayout rlParent;
    }


    //数据位置的交换  将数据保存到本地供首页调用
    @Override
    public void reOrder(int startPosition, int endPosition) {
        if (endPosition < datas.size()) {
            SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean object = datas.remove(startPosition);
            datas.add(endPosition, object);//位置进行更改
            mCommondataChangeInterface.setdatas(datas);
            notifyDataSetChanged();
        }
    }

    public interface CommondataChangeInterface {
        void setdata(MyTypeItem workContent);

        void setdatas(List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> datas);
    }
}
