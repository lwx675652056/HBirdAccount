package com.hbird.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.ActEditAccountBinding;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZiChanInfoReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;
import com.hbird.ui.assets.ActEditAccountValue;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.ColorBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sing.common.base.BaseActivity;
import sing.common.base.BaseViewModel;
import sing.common.util.StatusBarUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActEditAccount
 * @date: 2018/12/28 13:57
 * @Description: 编辑账户
 */
public class ActEditAccount extends BaseActivity<ActEditAccountBinding, BaseViewModel> implements IBaseActivity {

    private EditAccountData data;
    private List<AssetsBean> list = new ArrayList<>();
    private EditAccountAdapter adapter;
    private String token;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarLightMode(getWindow());
        return R.layout.act_edit_account;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        ColorBar.newColorBuilder()
                .applyNav(true)
                .navColor(Color.parseColor("#FFFFFF"))
                .navDepth(0)
                .statusColor(Color.parseColor("#FFFFFF"))
                .statusDepth(0)
                .build(this)
                .apply();

        data = new EditAccountData();
        data.setEdit(true);

        binding.setBean(data);
        binding.setListener(new OnClick());
        binding.toolbar.findViewById(R.id.iv_backs).setOnClickListener(v -> finish());
        ((TextView) binding.toolbar.findViewById(R.id.tv_center_title)).setText("编辑账户");

        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");

        adapter = new EditAccountAdapter(this, list, R.layout.row_edit_account, (position, data, type) -> onItemClick((AssetsBean) data));
        adapter.setData(false);// 不是删除状态
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setNestedScrollingEnabled(false);//禁止rcyc嵌套滑动
        binding.recyclerView.setItemAnimator(null);//设置动画为null来解决闪烁问题

        getData();
    }

    // item 点击
    private void onItemClick(AssetsBean data) {
        if (data.mark == 1) {// 已设置过且没有添加，直接添加
            ToastUtil.showShort("已添加");
        } else if (data.money != 0) {// 已经设置过值，直接添加
            add(data);
        } else {// 先去设置，后添加
            Intent intent = new Intent(this, ActEditAccountValue.class);
            intent.putExtra(Constants.START_INTENT_A, data);
            startActivityForResult(intent, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            AssetsBean bean = (AssetsBean) data.getExtras().getSerializable(Constants.START_INTENT_A);
            add(bean);
        }
    }

    // 获取数据，主要是获取已经设置过值的
    private void getData() {
        NetWorkManager.getInstance().setContext(this).getZiChanInfo(token, null, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                ZiChanInfoReturn.ResultBean result = ((ZiChanInfoReturn) b).getResult();
                list.clear();
                list.addAll(result.getAssets());
                adapter.notifyItemRangeChanged(0, list.size());
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    // 添加
    private void add(AssetsBean bean) {
        NetWorkManager.getInstance().setContext(this).addAT2Mark(token, bean.assetsType, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                String str = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
                List<AssetsBean> temp = JSON.parseArray(str, AssetsBean.class);
                if (temp == null) {
                    temp = new ArrayList<>();
                }
                temp.add(bean);
                SharedPreferencesUtil.put(Constants.MY_ACCOUNT, JSON.toJSONString(temp));
                ToastUtil.showShort("添加成功");
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    // 删除
    private void delete(Integer[] ints) {
        NetWorkManager.getInstance().setContext(this).deleteAT2Mark(token, ints, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                // 若关闭界面，则无需更改UI
//                for (int i = 0; i < list.size(); i++) {
//                    if (isExist(ints,list.get(i).assetsType)){
//                        list.get(i).mark = 0;
//                    }
//                    list.get(i).check = false;// 全部置为非选择状态
//                }
//
//                data.setEdit(true);
//                adapter.setData(false);// 是删除状态

                // 将本地缓存更新一下
                String str = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
                List<AssetsBean> temp = JSON.parseArray(str, AssetsBean.class);// 一定不为空
                Iterator<AssetsBean> it = temp.iterator();
                while(it.hasNext()){
                    AssetsBean x = it.next();
                    if (isExist(ints,x.assetsType)){
                        it.remove();
                    }
                }
                SharedPreferencesUtil.put(Constants.MY_ACCOUNT, JSON.toJSONString(temp));

                ToastUtil.showShort("删除成功");
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    // 删除掉的数组，当前的id
    private boolean isExist(Integer[] ints, int assetsType) {
        for (int i = 0,size = ints.length; i < size; i++) {
            if (ints[i] == assetsType){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public boolean isUseFragment() {
        return false;
    }

    public class OnClick {
        public void editAndDelete(View view) {
            if (data.isEdit()) {// 现在是编辑，点一下变成删除
                boolean noData = getNoData(list);
                if (!noData) {
                    ToastUtil.showShort("还没有添加账户哦~");
                } else {
                    data.setEdit(false);
                    adapter.setData(true);// 是删除状态
                }
            } else {// 现在是删除，点击要调用接口删除
                List<Integer> t = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).check){
                        t.add(list.get(i).assetsType);
                    }
                }

                if (t.size() < 1) {
                    ToastUtil.showShort("请选择要删除的账户");
                } else {
                    Integer[] ints = new Integer[t.size()];
                    t.toArray(ints);
                    delete(ints);
                }
            }
        }
    }

    // 返回是否已经添加数据
    private boolean getNoData(List<AssetsBean> temp) {
        for (int i = 0, size = temp.size(); i < size; i++) {
            if (temp.get(i).mark == 0) {// 其中有不是默认账户的
                return true;
            }
        }
        return false;
    }
}
