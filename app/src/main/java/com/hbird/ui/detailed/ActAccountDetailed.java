package com.hbird.ui.detailed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.hbird.base.R;
import com.hbird.base.databinding.ActAccountDetailedBinding;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.DialogToGig;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.widget.MyTimerPop;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.TitleBean;
import com.hbird.util.Utils;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sing.common.base.BaseActivity;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

import static com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN;
import static java.lang.Integer.parseInt;

/**
 * @author: LiangYX
 * @ClassName: ActAccountDetailed
 * @date: 2018/12/20 14:47
 * @Description: 首页的更多 账本明细
 */
public class ActAccountDetailed extends BaseActivity<ActAccountDetailedBinding, AccountDetailedModle> implements IBaseActivity {

    private AccountDetailedData data;
    private String accountId;
    private AccountDetailedAdapter adapter;
    private List<AccountDetailedBean> list = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_account_detailed;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        binding.setTitle(new TitleBean("账本明细"));
        binding.toolbar.ivBack.setOnClickListener(v -> finish());
        binding.setClick(new OnClick());
        binding.toolbar.flParent.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_main_bg));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.toolbar.flParent.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(this);
        binding.toolbar.flParent.setLayoutParams(params);
        binding.toolbar.flParent.setPadding(0, StatusBarUtil.getStateBarHeight(this), 0, 0);

        accountId = getIntent().getExtras().getString("accountId", "");

        setInitData();

        data = new AccountDetailedData();
        data.setYyyy(parseInt(DateUtils.getCurYear("yyyy")));
        data.setMm(DateUtils.getMonth(new Date()));
        data.setInComeMoney("0.00");
        data.setSpendingMoney("0.00");
        binding.setData(data);

        adapter = new AccountDetailedAdapter(this, list, R.layout.row_account_detailed, (position, data, type) -> onItemClick((AccountDetailedBean) data, type));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(null);//设置动画为null来解决闪烁问题
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    // Item点击，type=0 为点击，type=1 为长按
    private void onItemClick(AccountDetailedBean data, int type) {
        if (type == 0) {
            Utils.playVoice(this, R.raw.changgui02);
            Intent intent = new Intent();
            intent.setClass(this, MingXiInfoActivity.class);
            intent.putExtra("ID", data.getId());
            startActivityForResult(intent, 101);
        } else if (type == 1) {
            Utils.playVoice(this, R.raw.changgui02);
            WaterOrderCollect bean = new WaterOrderCollect();
            bean.setId(data.getId());
            bean.setMoney(data.getMoney());
            bean.setAccountBookId(data.getAccountBookId());
            bean.setOrderType(data.getOrderType());
            bean.setIsStaged(data.getIsStaged());
            bean.setSpendHappiness(data.getSpendHappiness());
            bean.setTypePid(data.getTypePid());
            bean.setTypePname(data.getTypePname());
            bean.setTypeId(data.getTypeId());
            bean.setTypeName(data.getTypeName());
            bean.setCreateDate(new Date(data.getCreateDate()));
            bean.setChargeDate(new Date(data.getChargeDate()));
            bean.setCreateBy(data.getCreateBy());
            bean.setCreateName(data.getCreateName());
            bean.setUpdateBy(data.getUpdateBy());
            bean.setUpdateName(data.getUpdateName());
            bean.setRemark(data.getRemark());
            bean.setIcon(data.getIcon());
            bean.setUserPrivateLabelId(data.getUserPrivateLabelId());
            bean.setReporterAvatar(data.getReporterAvatar());
            bean.setReporterNickName(data.getReporterNickName());
            bean.setAbName(data.getAbName());//所属账本名称
            bean.setAssetsId(data.getAssetsId());
            bean.setAssetsName(data.getAssetsName());
            alertDialog(bean);
        }
    }

    private void alertDialog(WaterOrderCollect bean) {
        new DialogUtils(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("确认删除吗？")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //数据库的操作 （删除显示的是 数据库的更新）
                        Boolean b = DBUtil.updateOneDate(bean);
                        if (b) {
                            //刷新界面数据
                            getData();
                            //删除 并同步上传到服务器
                            pullToSyncDate();
                        }
                    }
                }).show();
    }

    //初始化 年（2010-2050），月（1-12）
    private void setInitData() {
        for (int i = 2010; i < 2051; i++) {
            dataY.add(i + "");
        }
        String temp = "月份";
        for (int i = 1; i < 13; i++) {
            dataM.add(i + temp);
        }
    }

    private ArrayList<String> dataY = new ArrayList<>();
    private ArrayList<String> dataM = new ArrayList<>();

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public boolean isUseFragment() {
        return false;
    }


    public class OnClick {
        // 选择账户
        public void chooseAccount(View view) {
            Utils.playVoice(ActAccountDetailed.this, R.raw.changgui02);
            new MyTimerPop(ActAccountDetailed.this, view, dataY, dataM, data.getYyyy() - 2010, data.getMm() - 1, new MyTimerPop.OnDateListener() {
                @Override
                public void getYearList(String s) {
                    data.setYyyy(parseInt(s));
                }

                @Override
                public void getMonthList(String s) {
                    String[] ss = s.split("月份");
                    data.setMm(parseInt(ss[0]));
                }
            }, new MyTimerPop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    getData();
                }
            }).showPopWindow();
        }
    }

    private void getData() {
        Set<String> prefSet = SPUtil.getPrefSet(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, new LinkedHashSet<>());
        viewModel.getData(data.getYyyy(), data.getMm(), accountId, prefSet, new AccountDetailedModle.CallBack() {
            @Override
            public void result(List<AccountDetailedBean> l, String monthIncomes, String monthSpends) {
                data.setInComeMoney(monthIncomes);
                data.setSpendingMoney(monthSpends);
                if (l != null && l.size() > 0) {
                    list.clear();
                    list.addAll(l);
                    adapter.notifyItemRangeChanged(0,list.size());
                    data.setNoData(false);
                }else {
                    data.setNoData(true);
                }
            }
        });
    }


    private DialogToGig dialogToGig;

    public void showGifProgress(String title) {
        if (dialogToGig == null) {
            dialogToGig = new DialogToGig(this);
        }
        dialogToGig.builder().show();
    }

    public void hideGifProgress() {
        if (dialogToGig != null) {
            dialogToGig.hide();
        }
    }

    private void pullToSyncDate() {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(this);

        if (!netWorkAvailable) {
            return;
        }
        showGifProgress("");
        String mobileDevice = Utils.getDeviceInfo(this);

        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        NetWorkManager.getInstance().setContext(ActAccountDetailed.this).postPullToSyncDate(mobileDevice, false, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                PullSyncDateReturn b1 = (PullSyncDateReturn) b;
                String synDate = b1.getResult().getSynDate();
                long L = 0;
                if (null != synDate) {
                    try {
                        L = Long.parseLong(synDate);
                    } catch (Exception e) {

                    }
                }
                SPUtil.setPrefLong(ActAccountDetailed.this, com.hbird.base.app.constant.CommonTag.SYNDATE, L);
                List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                //插入本地数据库
                DBUtil.insertLocalDB(synData);
                SPUtil.setPrefBoolean(ActAccountDetailed.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);
                hideGifProgress();

                getData();
                SPUtil.setPrefBoolean(ActAccountDetailed.this, OFFLINEPULL_FIRST_LOGIN, false);
            }

            @Override
            public void onError(String s) {
                hideGifProgress();
                ToastUtil.showShort(s);
            }
        });
    }
}
