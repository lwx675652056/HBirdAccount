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
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.ColorBar;

import java.util.ArrayList;
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

        getData();
    }

    // item 点击
    private void onItemClick(AssetsBean data) {
//        if (data.isSetting() ) {// 已设置过且没有添加，直接添加
//            if (!data.isExist()){
//                ToastUtil.showShort("已添加");
//                return;
//            }
//            add(data);
//        } else {// 先去设置，后添加
//            Intent intent = new Intent(this,ActEditAccountValue.class);
//            intent.putExtra(Constants.START_INTENT_A,data);
//            startActivityForResult(intent,1000);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null){
            AssetsBean bean= (AssetsBean) data.getExtras().getSerializable(Constants.START_INTENT_A);
            add(bean);
        }
    }

    // 获取数据，主要是获取已经设置过值的
    private void getData() {
        NetWorkManager.getInstance().setContext(this).getZiChanInfo(token, null, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                ZiChanInfoReturn b1 = (ZiChanInfoReturn) b;
                try {
                    ZiChanInfoReturn.ResultBean result = b1.getResult();
                    List<AssetsBean> list = result.getAssets();
                    getList(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private boolean isExist(Integer[] ints, int a) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] - a == 0) {
                return true;
            }
        }
        return false;
    }

    // 删除
    private void delete(Integer[] ints) {
        NetWorkManager.getInstance().setContext(this).deleteAT2Mark(token, ints, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
//                for (int i = 0; i < list.size(); i++) {
//                    if (isExist(ints,list.get(i).getAssetsType())){
//                        list.get(i).setExist(false);
//                    }
//                    list.get(i).setCheck(false);
//                }

//                String str = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
//                List<AssetsBean> temp = JSON.parseArray(str, AssetsBean.class);// 一定不为空
//
//                Iterator<AssetsBean> it = temp.iterator();
//                while(it.hasNext()){
//                    AssetsBean x = it.next();
//                    if (isExist(ints,x.getAssetsType())){
//                        it.remove();
//                    }
//                }

//                data.setEdit(true);
//                adapter.setData(false);// 是删除状态
//
//                SharedPreferencesUtil.put(Constants.MY_ACCOUNT, JSON.toJSONString(temp));
//                ToastUtil.showShort("删除成功");
//                setResult(Activity.RESULT_OK);
//                finish();
            }

            @Override
            public void onError(String s) {
            }
        });
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
//            if (data.isEdit()) {// 现在是编辑，点一下变成删除
//                boolean noData = getNoData(list);
//                if (!noData) {
//                    ToastUtil.showShort("还没有添加账户哦~");
//                } else {
//                    data.setEdit(false);
//                    adapter.setData(true);// 是删除状态
//                }
//            } else {// 现在是删除，点击要调用接口删除
//                List<Integer> t = new ArrayList<>();
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).isCheck()) {
//                        t.add(list.get(i).getAssetsType());
//                    }
//                }
//
//                if (t.size() < 1) {
//                    ToastUtil.showShort("请选择要删除的账户");
//                } else {
//                    Integer[] ints = new Integer[t.size()];
//                    t.toArray(ints);
//                    delete(ints);
//                }
//            }
        }
    }

    // 返回是否已经添加数据
    private boolean getNoData(List<AssetsBean> temp) {
//        for (int i = 0, size = temp.size(); i < size; i++) {
//            if (temp.get(i).isExist()) {
//                return true;
//            }
//        }
        return false;
    }

    public void getList(List<AssetsBean> temp1) {// 已设置过值的记录

//        String temp2 = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
//        List<AssetsBean> temp = JSON.parseArray(temp2, AssetsBean.class);// 缓存的已添加的账户
//
//        List<Integer> str = new ArrayList<>();
//        if (temp != null && temp.size() > 0) {
//            for (AssetsBean s : temp) {
//                str.add(s.getAssetsType());
//            }
//        }
//
//        list.add(new AssetsBean(1, 1, "现金", R.mipmap.icon_zcxianjin_normal, str.contains(1)));
//        list.add(new AssetsBean(2, 2, "支付宝", R.mipmap.icon_zczhifubao_normal, str.contains(2)));
//        list.add(new AssetsBean(3, 3, "微信", R.mipmap.icon_zcweixin_normal, str.contains(3)));
//        list.add(new AssetsBean(4, 12, "信用卡", R.mipmap.icon_zcxyk_normal, str.contains(12)));
//        list.add(new AssetsBean(5, 4, "理财", R.mipmap.icon_zclicai_normal, str.contains(4)));
//        list.add(new AssetsBean(6, 5, "社保", R.mipmap.icon_zcshebao_normal, str.contains(5)));
//        list.add(new AssetsBean(7, 11, "公积金", R.mipmap.icon_zcgjj_normal, str.contains(11)));
//        list.add(new AssetsBean(8, 6, "借记/储蓄卡", R.mipmap.icon_zcyinhangka_normal, str.contains(6)));
//        list.add(new AssetsBean(9, 7, "公交/校园/等充值卡", R.mipmap.icon_zcgongjiaoka_normal, str.contains(7)));
//        list.add(new AssetsBean(10, 8, "出借待收", R.mipmap.icon_zcjiekuan_normal, str.contains(8)));
//        list.add(new AssetsBean(11, 9, "负债待还", R.mipmap.icon_zcqiankuan_normal, str.contains(9)));
//        list.add(new AssetsBean(12, 10, "其他账户", R.mipmap.icon_zcqita_normal, str.contains(10)));
//
//        if (temp1 != null && temp1.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                for (int j = 0; j < temp1.size(); j++) {
//                    if (list.get(i).getAssetsType() == temp1.get(j).getAssetsType()) {
//                        list.get(i).setCreateDate(temp1.get(j).getCreateDate());
//                        list.get(i).setUpdateDate(temp1.get(j).getUpdateDate());
//                        list.get(i).setMoney(temp1.get(j).getMoney());
//                        list.get(i).setSetting(true);
//                    }
//                }
//            }
//        }
//
//        adapter.notifyDataSetChanged();
    }
}
