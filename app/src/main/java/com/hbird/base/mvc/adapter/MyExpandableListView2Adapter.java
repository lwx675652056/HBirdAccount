package com.hbird.base.mvc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.bean.AddMoreTypeBean;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuAddMoreTypeBean;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Liul on 2018/7/4.
 */

public class MyExpandableListView2Adapter extends BaseExpandableListAdapter {
    private Context mContext;
    private callBackInstance callback;
    private int ii = 0;//组
    private int jj = 0;//child
    ArrayList<String> list;

    /**
     * 每个分组的名字的集合
     */
    private List<ShouRuAddMoreTypeBean.ResultBean.AllListBean> groupList;

    /**
     * 所有分组的所有子项的 GridView 数据集合
     */
    private List<List<ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean>> itemList;

    private GridView gridView;

    public MyExpandableListView2Adapter(Context context, List<ShouRuAddMoreTypeBean.ResultBean.AllListBean> groupList,
                                        List<List<ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean>> itemList
                                        ,ArrayList<String> list) {
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.expandablelist_group, null);
        }
        TextView tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
        // 设置分组组名
        tvGroup.setText(groupList.get(groupPosition).getIncomeName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.expandablelist_item, null);
        }
        // 因为 convertView 的布局就是一个 GridView，
        // 所以可以向下转型为 GridView
        gridView = (GridView) convertView;
        // 创建 GridView 适配器
        final MyGridView2Adapter gridViewAdapter = new MyGridView2Adapter(mContext,
                itemList.get(groupPosition),groupPosition,ii,jj,list);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //引起外部回调
                callback.setOnCallBack(groupPosition,position);
                //gridViewAdapter.setChecks(groupPosition ,position);
                setUpDate(groupPosition,position);
                //Toast.makeText(mContext, "点击了第" + (groupPosition + 1) + "组，第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
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
