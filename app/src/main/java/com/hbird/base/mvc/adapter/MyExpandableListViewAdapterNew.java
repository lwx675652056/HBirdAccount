package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturnNew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liul on 2018/7/4.
 */

public class MyExpandableListViewAdapterNew extends BaseExpandableListAdapter {
    private Context mContext;
    private callBackInstance callback;
    private int ii = 0;//组
    private int jj = 0;//child

    /**
     * 每个分组的名字的集合
     */
    private List<ZhiChuTagReturnNew.ResultBean.AllListBean> groupList;

    /**
     * 所有分组的所有子项的 GridView 数据集合
     */
    private List<List<ZhiChuTagReturnNew.ResultBean.AllListBean.SpendTypeSonsBean>> itemList;

    private GridView gridView;
    private ArrayList<String> list;

    public MyExpandableListViewAdapterNew(Context context, List<ZhiChuTagReturnNew.ResultBean.AllListBean> groupList, List<List<ZhiChuTagReturnNew.ResultBean.AllListBean.SpendTypeSonsBean>> itemList, ArrayList<String> list) {
        mContext = context;
        this.groupList = groupList;
        this.itemList = itemList;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.expandablelist_group, null);
        }
        TextView tvGroup = convertView.findViewById(R.id.tv_group);
        // 设置分组组名
        tvGroup.setText(groupList.get(groupPosition).getSpendName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.expandablelist_item, null);
        }
        // 因为 convertView 的布局就是一个 GridView，  所以可以向下转型为 GridView
        gridView = (GridView) convertView;
        // 创建 GridView 适配器
        final MyGridViewAdapterNew gridViewAdapter = new MyGridViewAdapterNew(mContext, itemList.get(groupPosition),groupPosition,ii,jj,list);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //引起外部回调
                callback.setOnCallBack(groupPosition,position);
                //点选后 刷新整个界面的数据 记住当前的选中目标 高亮显示
                setUpDate(groupPosition,position);
                //Toast.makeText(mContext, "点击了第" + (groupPosition + 1) + "组，第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
                gridViewAdapter.setCheck(groupPosition,position);
            }
        });
        return convertView;
    }

    private void setUpDate(int groupPosition, int position) {
        this.ii = groupPosition;
        this.jj = position;
        notifyDataSetChanged();
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    public void  setMyOnclickListener(callBackInstance callback){
        this.callback = callback;
    }
    public interface callBackInstance{
        void setOnCallBack(int groupPosition, int position);
    }
}
