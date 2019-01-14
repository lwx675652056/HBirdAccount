package com.hbird.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hbird.base.R;
import com.hbird.base.databinding.DialogChooseAddressBinding;
import com.hbird.bean.AddressBean;
import com.hbird.common.ViewPagerViewAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sing.BaseAdapter;
import sing.BaseViewHolder;
import sing.common.listener.OnMyPageChangeListener;
import sing.util.ScreenUtil;

/**
 * Created by Liul on 2018/10/23.
 * 首页普通 弹窗dialog
 */

public class ChooseAddressDialog {

    private Dialog dialog;
    private int currentPos = 0;// 当前的页数
    private ViewPager viewPager;
    private List<AddressBean> list1 = new ArrayList<>();
    private List<AddressBean> list2 = new ArrayList<>();
    private List<AddressBean> list3 = new ArrayList<>();
    private ViewPagerViewAdapter viewAdapter;// ViewPager的适配器

    private MyAdapter1 adapter1;
    private MyAdapter2 adapter2;
    private MyAdapter3 adapter3;

    private DialogChooseAddressBinding binding;
    private ChooseAddressDialogData data;
    private Context context;

    public ChooseAddressDialog(Context context, String province, String city, String county) {
        this.context = context;
        this.dialog = new Dialog(context, R.style.picker_dialog);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_choose_address, null, false);
        dialog.setContentView(binding.getRoot());
        binding.cancel.setOnClickListener(v -> dismiss());

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp.x = 0;
        lp.y = manager.getDefaultDisplay().getHeight();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ScreenUtil.getScreenHeight(context) / 3 * 2;
        dialog.onWindowAttributesChanged(lp);
        dialog.setCanceledOnTouchOutside(true);

        viewPager = binding.viewPager;

        RecyclerView recyclerView1 = new RecyclerView(context);
        recyclerView1.setOverScrollMode(View.OVER_SCROLL_NEVER);
        RecyclerView recyclerView2 = new RecyclerView(context);
        recyclerView1.setOverScrollMode(View.OVER_SCROLL_NEVER);
        RecyclerView recyclerView3 = new RecyclerView(context);
        recyclerView1.setOverScrollMode(View.OVER_SCROLL_NEVER);

        List<View> viewList = new ArrayList<>();
        viewList.add(recyclerView1);
        viewList.add(recyclerView2);
        viewList.add(recyclerView3);
        viewAdapter = new ViewPagerViewAdapter(viewList, currentPos);
        viewPager.setAdapter(viewAdapter);

        dialog.findViewById(R.id.ll_province).setOnClickListener(v -> checkText(false, true, 0));
        dialog.findViewById(R.id.ll_city).setOnClickListener(v -> checkText(false, true, 1));
        dialog.findViewById(R.id.ll_county).setOnClickListener(v -> checkText(false, true, 2));

        list1 = JSON.parseArray(getString(context), AddressBean.class);

        data = new ChooseAddressDialogData();
        data.setProvince(TextUtils.isEmpty(province) ? "未选择" : province);
        data.setCity(TextUtils.isEmpty(city) ? "未选择" : city);
        data.setCounty(TextUtils.isEmpty(county) ? "未选择" : county);
        binding.setData(data);
        data.setChoose(1);
        if (!TextUtils.isEmpty(province)) {
            for (int i = 0, size = list1.size(); i < size; i++) {
                if (list1.get(i).name.equals(province)) {
                    list2.clear();
                    list2.addAll(list1.get(i).cities);
                    checkText(false, false, 1);
                    for (int j = 0; j < list2.size(); j++) {
                        if (list2.get(j).name.equals(city)) {
                            if (list2.get(j).cities != null && list2.get(j).cities.size() > 0) {
                                list3.clear();
                                list3.addAll(list2.get(j).cities);
                                checkText(false, false, 2);
                            }
                        }
                    }
                }
            }
        }

        adapter1 = new MyAdapter1(context, list1, R.layout.row_choose_address);
        adapter2 = new MyAdapter2(context, list2, R.layout.row_choose_address);
        adapter3 = new MyAdapter3(context, list3, R.layout.row_choose_address);
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        recyclerView3.setLayoutManager(new LinearLayoutManager(context));

        viewPager.addOnPageChangeListener(new OnMyPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                checkText(true, false, position);
            }
        });
    }

    class MyAdapter1 extends BaseAdapter<AddressBean> {

        public MyAdapter1(Context context, List<AddressBean> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        protected void bindData(BaseViewHolder holder, AddressBean bean, int position) {
            TextView tv = holder.getView(R.id.tv);
            if (data.getProvince().equals(bean.name)) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.getView(R.id.tv_state).setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.text_333333));
                holder.getView(R.id.tv_state).setVisibility(View.GONE);
            }

            holder.setText(R.id.tv, bean.name)
                    .setOnItemClickListener(v -> {
                        data.setProvince(bean.name);
                        data.setCity("未选择");
                        if (bean.cities != null && bean.cities.size() > 0) {
                            list2.clear();
                            list2.addAll(bean.cities);
                            adapter2.notifyDataSetChanged();
                            adapter1.notifyDataSetChanged();// 刷新当前已选的省
                            checkText(false, false, 1);
                        }
                    });
        }
    }

    class MyAdapter2 extends BaseAdapter<AddressBean> {

        public MyAdapter2(Context context, List<AddressBean> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        protected void bindData(BaseViewHolder holder, AddressBean bean, int position) {
            TextView tv = holder.getView(R.id.tv);
            if (data.getCity().equals(bean.name)) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.getView(R.id.tv_state).setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.text_333333));
                holder.getView(R.id.tv_state).setVisibility(View.GONE);
            }

            holder.setText(R.id.tv, bean.name)
                    .setOnItemClickListener(v -> {
                        data.setCity(bean.name);
                        data.setCounty("未选择");
                        if (bean.cities != null && bean.cities.size() > 0) {
                            list3.clear();
                            list3.addAll(bean.cities);
                            adapter3.notifyDataSetChanged();
                            adapter2.notifyDataSetChanged();// 刷新当前已选的市
                            checkText(false, false, 2);
                        } else {
                            if (listener != null) {
                                dialog.dismiss();
                                listener.clicked(data.getProvince(), data.getCity(), data.getCounty());
                            }
                        }
                    });
        }
    }

    class MyAdapter3 extends BaseAdapter<AddressBean> {

        public MyAdapter3(Context context, List<AddressBean> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        protected void bindData(BaseViewHolder holder, AddressBean bean, int position) {
            TextView tv = holder.getView(R.id.tv);
            if (data.getCounty().equals(bean.name)) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.getView(R.id.tv_state).setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.text_333333));
                holder.getView(R.id.tv_state).setVisibility(View.GONE);
            }

            holder.setText(R.id.tv, bean.name)
                    .setOnItemClickListener(v -> {
                        data.setCounty(bean.name);

                        adapter3.notifyDataSetChanged();
                        checkText(false, false, 2);
                        if (listener != null) {
                            dialog.dismiss();
                            listener.clicked(data.getProvince(), data.getCity(), data.getCounty());
                        }
                    });
        }
    }

    // 是否为滑动或点击顶部标签
    private void checkText(boolean scroll, boolean isClick, int pos) {
        currentPos = pos;
        if (scroll) {// viewpager滑动的
        } else if (isClick) {
            viewPager.setCurrentItem(currentPos);
        } else {
            viewAdapter.setCurrentPos(currentPos);
            viewPager.setCurrentItem(currentPos);
            data.setChoose(pos+1);
        }

        data.setPos(currentPos);
    }

    private String getString(Context context) {
        InputStream inputStream;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            inputStream = context.getResources().getAssets().open("city.json");
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bfr = new BufferedReader(isr);
            String in;
            while ((in = bfr.readLine()) != null) {
                stringBuffer.append(in);
            }
            inputStream.close();
            isr.close();
            bfr.close();
            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private OnClick listener;

    public void setListener(OnClick listener) {
        this.listener = listener;
    }

    public OnClick getListener() {
        return listener;
    }

    public interface OnClick {
        void clicked(String province, String city, String county);
    }
}