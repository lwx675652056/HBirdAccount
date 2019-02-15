package com.hbird.base.mvc.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.adapter.MyZhangBenAdapter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ZhangBenMsgBean;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.RefreshSwipeMenu.RefreshSwipeMenuListView;
import com.hbird.base.mvc.view.RefreshSwipeMenu.SwipeMenu;
import com.hbird.base.mvc.view.RefreshSwipeMenu.SwipeMenuCreator;
import com.hbird.base.mvc.view.RefreshSwipeMenu.SwipeMenuItem;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/8.
 * 我的账本
 */

public class MyZhangBenActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.swipe)
    com.hbird.base.mvc.view.RefreshSwipeMenu.RefreshSwipeMenuListView swipe;

    private int po;
    private List<ZhangBenMsgBean> data;
    private MyZhangBenAdapter adapter;
    private String token;
    private String id;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_zhangben;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(MyZhangBenActivity.this, CommonTag.GLOABLE_TOKEN, "");
        id = getIntent().getStringExtra("ID");

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 创建滑动选项
                SwipeMenuItem rejectItem = new SwipeMenuItem(getApplicationContext());
                // 设置选项背景
                //rejectItem.setBackground(new ColorDrawable(getResources().getColor(R.color.top)));
                rejectItem.setBackground(R.drawable.icon_zuohuabianji_normal);
                // 设置选项宽度
                rejectItem.setWidth(dp2px(40, getApplicationContext()));
                rejectItem.setHeight(dp2px(40, getApplicationContext()));
                // 设置选项标题
                rejectItem.setTitle("");
                // 设置选项标题
                rejectItem.setTitleSize(0);
                // 设置选项标题颜色
                rejectItem.setTitleColor(Color.WHITE);
                // 添加选项
                menu.addMenuItem(rejectItem);

                // 创建删除选项
                SwipeMenuItem argeeItem = new SwipeMenuItem(getApplicationContext());
                argeeItem.setBackground(R.drawable.icon_zuohuashanchu_normal);
                argeeItem.setWidth(dp2px(40, getApplicationContext()));
                argeeItem.setHeight(dp2px(40, getApplicationContext()));
                argeeItem.setTitle("");
                argeeItem.setTitleSize(0);
                argeeItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(argeeItem);
            }
        };

        swipe.setMenuCreator(creator);

        swipe.setOnMenuItemClickListener(new RefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: //第一个选项
                        playVoice(R.raw.changgui02);
                        if (position == 0) {
                            showMessage("不可编辑");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.setClass(MyZhangBenActivity.this, EditorZhangBenActivity.class);
                        intent.putExtra("TITLE", data.get(position).getZbName());
                        intent.putExtra("ID", data.get(position).getId() + "");
                        startActivityForResult(intent, 401);
                        break;
                    case 1: //第二个选项
                        playVoice(R.raw.changgui02);
                        if (position == 0 || data.get(position).getDefaultFlag() == 1) {
                            showMessage("不可删除");
                            return;
                        }
                        alertDialog(position, 1);
                        break;
                }
            }
        });
        swipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playVoice(R.raw.changgui02);
                adapter.setData(i - 1);
                Intent intent = new Intent();
                if (i == 1) {
                    intent.putExtra("TITLE", "总明细账本");
                    intent.putExtra("ID", "");
                    SPUtil.setPrefInt(MyZhangBenActivity.this, CommonTag.ACCOUNT_AB_TYPEID, 0);//设置无效值
                    intent.putExtra("typeBudget", "1");
                } else {
                    intent.putExtra("TITLE", data.get(i - 1).getZbName());
                    intent.putExtra("ID", data.get(i - 1).getId() + "");
                    SPUtil.setPrefInt(MyZhangBenActivity.this, CommonTag.ACCOUNT_AB_TYPEID, data.get(i - 1).getAbTypeId());
                    intent.putExtra("typeBudget", data.get(i - 1).getTypeBudget() + "");
                    intent.putExtra("abTypeId", data.get(i - 1).getAbTypeId() + "");
                }

                setResult(130, intent);
                finish();
            }
        });
        swipe.setOnItemLongClickListener((adapterView, view, i, l) -> {
            playVoice(R.raw.changgui02);
            if (l == 0 || data.get((int) (l - 1)).getDefaultFlag() == 1) {
                showMessage("不可删除");
            } else {
                alertDialog(i-1, 0);
            }
            return true;
        });
    }

    private void alertDialog(final int position, final int type) {
        //如果最后一个人提示“是否删除本账单？删除后所有数据不可恢复。（删除，取消）；
        //如果不是最后一个人删除，删除账本提示“是否删除？删除直接退出账本，并且数据不可以恢复。”
        new DialogUtils(MyZhangBenActivity.this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("是否删除？删除直接退出账本，并且数据不可以恢复。")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", view -> {
                    playVoice(R.raw.changgui02);
                    int positions = position;
                    if (type == 0) {
                        positions = positions - 1;
                    }
                    del(positions, swipe.getChildAt(positions + 1 - swipe.getFirstVisiblePosition()));
                    //删除账本接口
                    delAccount(position);
                }).show();
    }

    private void delAccount(int i) {
        if (data.size() - 1 < i) {
            return;
        }
        NetWorkManager.getInstance().setContext(MyZhangBenActivity.this).delAccounts(data.get(i).getId(), token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                GloableReturn b1 = (GloableReturn) b;
                String sql = "delete from WATER_ORDER_COLLECT where  ACCOUNT_BOOK_ID = " + data.get(i).getId();
                boolean b2 = DevRing.tableManager(WaterOrderCollect.class).execSQL(sql);
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取我的账本接口
        getMyAccount();
    }

    @OnClick({R.id.iv_back, R.id.right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.right_title:// 添加账本
                playVoice(R.raw.changgui02);
                startActivity(new Intent(MyZhangBenActivity.this, ActChooseZhangBen.class));
                break;
        }
    }

    private void getMyAccount() {
        NetWorkManager.getInstance().setContext(MyZhangBenActivity.this).getMyAccounts(token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                AccountZbBean b1 = (AccountZbBean) b;
                List<AccountZbBean.ResultBean> result = b1.getResult();
                if (null != result) {
                    data = new ArrayList<>();
                    data.clear();
                    Set<String> set = new LinkedHashSet<>();
                    set.clear();
                    for (int i = 0; i < result.size() + 1; i++) {
                        if (i == 0) {
                            ZhangBenMsgBean bean = new ZhangBenMsgBean();
                            bean.setZbImg("");
                            bean.setZbName("总明细账本");
                            bean.setZbType("总账本");
                            bean.setZbUTime("");
                            data.add(bean);
                        } else {
                            ZhangBenMsgBean bean = new ZhangBenMsgBean();
                            bean.setZbImg(result.get(i - 1).getIcon());
                            bean.setZbName(result.get(i - 1).getAbName());
                            bean.setZbType(result.get(i - 1).getAbTypeName());
                            bean.setZbUTime(result.get(i - 1).getUpdateDate() + "");
                            bean.setId(result.get(i - 1).getId() + "");
                            bean.setDefaultFlag(result.get(i - 1).getDefaultFlag());
                            bean.setAbTypeId(result.get(i - 1).getAbTypeId());
                            bean.setTypeBudget(result.get(i - 1).getTypeBudget());
                            data.add(bean);
                            set.add(result.get(i - 1).getId() + "");
                        }
                    }
                    if (null != set && set.size() > 0) {
                        SPUtil.setStringSet(MyZhangBenActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
                    }

                    int k = 0;
                    for (int i = 0; i < data.size(); i++) {
                        if (TextUtils.equals(data.get(i).getId(), id)) {
                            k = i;
                        }
                    }
                    adapter = new MyZhangBenAdapter(MyZhangBenActivity.this, data);
                    swipe.setAdapter(adapter);
                    adapter.setData(k);

                    swipe.setListViewMode(RefreshSwipeMenuListView.HEADER);
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    /**
     * 删除item动画
     *
     * @param index
     * @param v
     */
    private void del(final int index, View v) {
        final Animation animation = (Animation) AnimationUtils.loadAnimation(v.getContext(), R.anim.list_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                data.remove(index);
                po = index;
                adapter.notifyDataSetChanged();
                animation.cancel();
            }
        });

        v.startAnimation(animation);
    }

    public int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 401 && resultCode == 402) {

        }
    }
}
