package com.hbird.ui.index;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.ActMyAccountBinding;
import com.hbird.base.mvc.activity.ActChooseZhangBen;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountBean;
import com.hbird.util.Utils;
import com.hbird.widget.swipe_recyclerView.OnItemClickListener;
import com.hbird.widget.swipe_recyclerView.OnItemMenuClickListener;
import com.hbird.widget.swipe_recyclerView.SwipeMenu;
import com.hbird.widget.swipe_recyclerView.SwipeMenuBridge;
import com.hbird.widget.swipe_recyclerView.SwipeMenuCreator;
import com.hbird.widget.swipe_recyclerView.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import sing.common.base.BaseActivity;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActMyAccount
 * @date: 2019/2/12 16:46
 * @Description: 我的账本
 */
public class ActMyAccount extends BaseActivity<ActMyAccountBinding,MyAccountModle> {

//    protected BaseAdapter mAdapter;
//    protected List<String> mDataList;


    private int po;
    private List<AccountBean> list = new ArrayList<>();
//    private MyZhangBenAdapter adapter;
    private String token;
//    private List<AccountZbBean.ResultBean> result;
    private String id;

    private MyAccountAdapter adapter;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_my_account;
    }

    @Override
    public void initData() {
        Utils.initColor(this, Color.parseColor("#FFFFFF"));
        StatusBarUtil.setStatusBarLightMode(getWindow()); // 导航栏白色字体

        token = SPUtil.getPrefString(ActMyAccount.this, CommonTag.GLOABLE_TOKEN, "");
        id = getIntent().getStringExtra("ID");




//        mDataList = createDataList();
//        mAdapter = new MainAdapter(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(ActMyAccount.this, "第" + position + "个", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new MyAccountAdapter(this, list, R.layout.row_my_account, new com.hbird.base.listener.OnItemClickListener() {
            @Override
            public void onClick(int position, Object data, int type) {

            }
        });
        binding.recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        binding.recyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        binding.recyclerView.setAdapter(adapter);
//        mAdapter.notifyDataSetChanged(mDataList);
    }

    public class OnClick{
        // 关闭
        public void finish(View view){
            Utils.playVoice(ActMyAccount.this,R.raw.changgui02);
            onBackPressed();
        }

        // 添加
        public void add(View view){
            Utils.playVoice(ActMyAccount.this,R.raw.changgui02);
            startActivity(new Intent(ActMyAccount.this, ActChooseZhangBen.class));
        }
    }

    private void alertDialog(final int position, final int type) {
        //如果最后一个人提示“是否删除本账单？删除后所有数据不可恢复。（删除，取消）；
        //如果不是最后一个人删除，删除账本提示“是否删除？删除直接退出账本，并且数据不可以恢复。”
        new DialogUtils(ActMyAccount.this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("是否删除？删除直接退出账本，并且数据不可以恢复。")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", view -> {
                    Utils.playVoice(ActMyAccount.this,R.raw.changgui02);
                    int positions = position;
                    if (type == 0) {
                        positions = positions - 1;
                    }
//                    del(positions, swipe.getChildAt(positions + 1 - swipe.getFirstVisiblePosition()));
                    //删除账本接口
                    delAccount(position);
                }).show();
    }

    private void delAccount(int i) {
//        if (data.size() - 1 < i) {
//            return;
//        }
//        NetWorkManager.getInstance().setContext(ActMyAccount.this).delAccounts(data.get(i).getId(), token, new NetWorkManager.CallBack() {
//            @Override
//            public void onSuccess(BaseReturn b) {
//                GloableReturn b1 = (GloableReturn) b;
//                String sql = "delete from WATER_ORDER_COLLECT where  ACCOUNT_BOOK_ID = " + data.get(i).getId();
//                boolean b2 = DevRing.tableManager(WaterOrderCollect.class).execSQL(sql);
//            }
//
//            @Override
//            public void onError(String s) {
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getAllzb(token, new MyAccountModle.CallBack() {
            @Override
            public void result(List<AccountBean> temp) {
                list.clear();
                list.addAll(temp);
                adapter.notifyDataSetChanged();
            }
        });
    }

//    /**
//     * 删除item动画
//     *
//     * @param index
//     * @param v
//     */
//    private void del(final int index, View v) {
//        final Animation animation = (Animation) AnimationUtils.loadAnimation(v.getContext(), R.anim.list_anim);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationStart(Animation animation) {
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            public void onAnimationEnd(Animation animation) {
//                data.remove(index);
//                po = index;
//                adapter.notifyDataSetChanged();
//                animation.cancel();
//            }
//        });
//
//        v.startAnimation(animation);
//    }
    @Override
    public int initVariableId() {
        return 0;
    }


    // 菜单创建器，在Item要创建菜单的时候调用。
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_40_x);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

//            if (position == 0) {
//            } else if (position == mDataList.size() - 1) {
//                SwipeMenuItem closeItem = new SwipeMenuItem(ActMyAccount.this)
//                        .setImage(R.drawable.icon_zuohuabianji_normal)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(closeItem); // 添加菜单到右侧。
//            } else {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(ActMyAccount.this)
//                        .setImage(R.drawable.icon_zuohuabianji_normal)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
//
//                SwipeMenuItem closeItem = new SwipeMenuItem(ActMyAccount.this)
//                        .setImage(R.drawable.icon_zuohuashanchu_normal)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(closeItem); // 添加菜单到右侧。
//            }
        }
    };

    // RecyclerView的Item的Menu点击监听。
    private OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0){// 编辑

                }else if (menuPosition == 1){// 删除

                }
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                ToastUtil.showShort("list第" + position + "; 左侧菜单第" + menuPosition);
            }
        }
    };

    protected List<String> createDataList() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("菜单");
        }
        return strings;
    }
}