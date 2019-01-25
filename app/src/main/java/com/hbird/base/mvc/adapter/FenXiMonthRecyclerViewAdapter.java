package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbird.base.R;

import java.util.ArrayList;

/**
 * Created by liul on 2018/8/16.
 */

public class FenXiMonthRecyclerViewAdapter extends RecyclerView.Adapter<FenXiMonthRecyclerViewAdapter.ViewHolder>{
    private ArrayList<String> mData;
    private FenXiMonthRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    int itemPosition = 0;
    Context context;

    public FenXiMonthRecyclerViewAdapter(Context context, ArrayList<String> list, int i) {
        this.mData = list;
        this.itemPosition = i;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
         View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_items, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 绑定数据
        holder.setIsRecyclable(false);
        holder.mTv.setText(mData.get(position));
        if(position==itemPosition){
            holder.mTv.setBackgroundResource(R.drawable.shape_hong_radius);
            holder.mTv.setTextColor(context.getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            if(onItemClickListener != null) {
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClick(holder.itemView,pos);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if(onItemClickListener!=null){
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemLongClick(holder.itemView,pos);
            }
            //此事件被消费 不会触发单击事件
            return true;
        });
    }
    public void setItemClick(int i){
        this.itemPosition = i;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.item_tv);
        }
    }
    //设置监听回调
    public void setOnItemClickListener(FenXiMonthRecyclerViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}