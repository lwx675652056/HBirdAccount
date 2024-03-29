package com.hbird.ui.index;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.chart_3_0_1v.charts.PieChart;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.PieData;
import com.github.mikephil.chart_3_0_1v.data.PieDataSet;
import com.github.mikephil.chart_3_0_1v.data.PieEntry;
import com.github.mikephil.chart_3_0_1v.highlight.Highlight;
import com.github.mikephil.chart_3_0_1v.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.databinding.FragementIndexBinding;
import com.hbird.base.mvc.activity.ActSetBudget;
import com.hbird.base.mvc.activity.AskFriendsActivity;
import com.hbird.base.mvc.activity.BudgetActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.H5WebViewActivity;
import com.hbird.base.mvc.activity.InviteJiZhangActivity;
import com.hbird.base.mvc.activity.MemberManagerActivity;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.activity.MyZhangBenActivity;
import com.hbird.base.mvc.activity.NotificationMessageActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.OffLine2Req;
import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountMembersBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.WindowPopReturn;
import com.hbird.base.mvc.bean.ReturnBean.indexDatasReturn;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.view.dialog.IndexCommonDialog;
import com.hbird.base.mvc.view.dialog.InvitationFirendDialog;
import com.hbird.base.mvc.widget.Guide2Pop;
import com.hbird.base.mvc.widget.Guide4Pop;
import com.hbird.base.mvc.widget.NewGuidePop;
import com.hbird.base.mvc.widget.TabRadioButton;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.StatisticsSpendTopArraysBean;
import com.hbird.common.Constants;
import com.hbird.common.refreshLayout.MaterialRefreshLayout;
import com.hbird.common.refreshLayout.MaterialRefreshListener;
import com.hbird.ui.MainActivity;
import com.hbird.ui.calendar.ActCalendar;
import com.hbird.ui.detailed.ActAccountDetailed;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import sing.common.base.BaseFragment;
import sing.common.util.StatusBarUtil;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

import static com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN;
import static java.lang.Integer.parseInt;

/**
 * @author: LiangYX
 * @ClassName: IndexFragement
 * @date: 2018/12/25 17:29
 * @Description: 首页
 */
public class IndexFragement extends BaseFragment<FragementIndexBinding, IndexFragementModle> {

    private ArrayList<String> dataY;
    private ArrayList<String> dataM;

    //代表第一次进入页面
    private int ii = 0;
    private String token;
    private String mMonth;
    private indexDatasReturn.ResultBean indexResult;
    private String accountId = "";
    private boolean isFirst;
    private boolean comeInForLogin;
    private boolean timeB = false;
    private boolean shouldSetChat = true;
    private final int FIRST_LENGHT = 4000;
    private TabRadioButton jizhangs;
    private int popOnces = 0;//首页展示弹窗次数
    private int tanci = 0;
    private List<WindowPopReturn.ResultBean.ActivityBean> windowPop;
    private WindowPopReturn.ResultBean.InviteCountBean windowPopInvite;

    private String typeBudget = "1";
    private String zhangbenId = "";

    private String deviceId;// 设备唯一标识
    private int yyyy = 2018;
    private int mm = 12; // 当前月
    private IndexFragmentData data;

    private MemberAdapter memberAdapter;
    private List<String> memberList = new ArrayList<>();

    private IndexAdapter adapter;
    private List<AccountDetailedBean> list = new ArrayList<>();

    private List<WaterOrderCollect> pieChatList = new ArrayList<>();
    private PieChatAdapter pieChatAdapter;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragement_index;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        LogUtil.e("1111", "1");
        ViewGroup.LayoutParams params = binding.llParent1.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(getActivity());
        binding.llParent1.setLayoutParams(params);
        binding.llParent1.setPadding(0, StatusBarUtil.getStateBarHeight(getActivity()), 0, 0);

        data = new IndexFragmentData();
        binding.setBean(data);

        binding.setListener(new OnClick());

        binding.tvDate.setText(DateUtils.getMonthDay(new Date().getTime()));
        binding.tvWeek.setText(DateUtil.dateToWeek(DateUtils.getDay(new Date().getTime())));

        deviceId = Utils.getDeviceInfo(getActivity());

        memberAdapter = new MemberAdapter(getActivity(), memberList, R.layout.row_member);
        binding.recyclerView.setAdapter(memberAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));

        pieChatAdapter = new PieChatAdapter(getActivity(), pieChatList, R.layout.row_piechat, (position, data, type) -> onItemClick(data, type));
        binding.recyclerView2.setAdapter(pieChatAdapter);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

        //数据库相关操作
        jizhangs = getActivity().findViewById(R.id.btn_jizhang);

        popOnces = 0;//重新执行则将其置为0

        adapter = new IndexAdapter(getActivity(), list, R.layout.row_index, (position, data, type) -> onItemClick(data, type));
        binding.recyclerView1.setAdapter(adapter);
        binding.recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView1.setItemAnimator(null);//设置动画为null来解决闪烁问题

        accountId = (String) SharedPreferencesUtil.get(com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
//        zhangbenId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
//        LogUtil.e("zhangbenId:" + zhangbenId);
//        if (TextUtils.isEmpty(zhangbenId)) {
//            账本id
//            accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        zhangbenId = accountId;
//        }
        //账本id
//        accountId = zhangbenId;
        LogUtil.e("accountId:" + accountId);

        yyyy = parseInt(DateUtils.getCurYear("yyyy"));
        mMonth = DateUtils.date2Str(new Date(), "MM");
        String s = mMonth.substring(0, 2);
        mm = parseInt(s);

        data.setYyyy(yyyy);
        data.setMm(mm);
        data.setShow(true);

        typeBudget = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, "1");
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        LogUtil.e("token:" + token);
        //间隔上传时间
        String jiange = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.SYNINTERVAL, "");
        long prefLong = SPUtil.getPrefLong(getActivity(), com.hbird.base.app.constant.CommonTag.SYNDATE, 0);
        long time = new Date().getTime();
        if (prefLong != 0) {
            long l = time - prefLong;
            try {
                if (!TextUtils.isEmpty(jiange)) {
                    double v = Double.parseDouble(jiange);
                    if (l > v) {  //超过了最大间隔时间则自动上传
                        timeB = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setInitData();
        //新手指引
        boolean firstGuide = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, true);
        if (firstGuide) {
            //第一次进入
            setNewGuide();
        }

        //首页弹框接口
        if (getUserVisibleHint()) {
            setHomePage();
        }

        binding.refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                timeB = true;
                shouldSetChat = true;
                getIndexInfo();
                loadDataForNet(false);
            }
        });

        loadDataForNet(true);
        getIndexInfo();
        setChart();
    }

    // 点击记账的条目
    private void onItemClick(Object obj, int type) {
        Utils.playVoice(getActivity(), R.raw.changgui02);
        WaterOrderCollect bean;
        if (obj instanceof WaterOrderCollect) {
            bean = (WaterOrderCollect) obj;
        } else if (obj instanceof AccountDetailedBean) {
            AccountDetailedBean data = (AccountDetailedBean) obj;
            bean = new WaterOrderCollect();
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
        } else {
            return;
        }

        if (type == 0) {
            Intent intent = new Intent(getActivity(), MingXiInfoActivity.class);
            intent.putExtra("ID", bean.id);
            startActivityForResult(intent, 101);
//        } else if (type == 1) {
//            Utils.playVoice(getActivity(), R.raw.changgui02);
//            alertDialog(bean);
        }
    }

    public class OnClick {
        // 选择账户
        public void chooseAccount(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent2 = new Intent();
            intent2.setClass(getActivity(), MyZhangBenActivity.class);
//            intent2.setClass(getActivity(), ActMyAccount.class);
            String id = (String) SharedPreferencesUtil.get(com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
            intent2.putExtra("ID", id);
            startActivityForResult(intent2, 131);
        }

        // 日历
        public void calendar(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent = new Intent(getActivity(), ActCalendar.class);
            intent.putExtra("account_id", accountId);
            startActivity(intent);
        }

        // 邀请记账
        public void invitation(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent = new Intent();
            intent.setClass(getActivity(), InviteJiZhangActivity.class);
            intent.putExtra("ID", zhangbenId);
            startActivity(intent);
        }

        // 展开
        public void open(View view) {
            data.setShow(true);
            data.setShowArrow(false);
            data.setShowMember(false);
            data.setShowMemberSeting(false);
            Utils.playVoice(getActivity(), R.raw.changgui02);
        }

        //收起
        public void retract(View view) {
            data.setShow(false);
            data.setShowArrow(true);
            data.setShowMember(false);
            data.setShowMemberSeting(false);
            Utils.playVoice(getActivity(), R.raw.changgui02);
        }

        // 暂时隐藏金额
        public void showMoney(View view) {
            data.setShowMoney(!data.isShowMoney());
            if (data.isShowMoney()) {
                String textStr = "<font color=\"#808080\">总支出</font>\n" + data.getSpendingMoney();
                mChart.setCenterText(Html.fromHtml(textStr)); //设置中心文本
            } else {
                String textStr = "<font color=\"#808080\">总支出</font>\n总支出\n****";
                mChart.setCenterText(Html.fromHtml(textStr)); //设置中心文本
            }
            mChart.postInvalidate();
        }

        // 编辑预算
        public void editor(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            if (TextUtils.equals(typeBudget, "1")) {// 日常账本
                Intent intent3 = new Intent(getActivity(), BudgetActivity.class);
                intent3.putExtra("MONTH", mm + "");
                intent3.putExtra("YEAR", yyyy + "");
                if (indexResult.getBudget() != null) {
                    String s = "";
                    if (indexResult.getBudget().getBudgetMoney() != null) {
                        s = indexResult.getBudget().getBudgetMoney() + "";
                    }
                    intent3.putExtra("MONEY", s);
                }
                intent3.putExtra("accountBookId", zhangbenId);
                startActivityForResult(intent3, 1000);
            } else {// 场景账本
                Intent intent3 = new Intent(getActivity(), ActSetBudget.class);
                intent3.putExtra("MONTH", data.getMm() + "");
                intent3.putExtra("YEAR", data.getYyyy() + "");
                String abId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
                intent3.putExtra("accountBookId", abId + "");
                intent3.putExtra("topTime", "- - 年 - - 月 - - 日");
                intent3.putExtra("endTime", "- - 年 - - 月 - - 日");
                startActivityForResult(intent3, 1000);
            }
        }

        // 账本成员设置
        public void setMemberSetting(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent = new Intent();
            intent.setClass(getActivity(), MemberManagerActivity.class);
            intent.putExtra("ID", zhangbenId);
            startActivity(intent);
        }

        // 查看更多
        public void more(View view) {
            Intent intent = new Intent(getActivity(), ActAccountDetailed.class);
            intent.putExtra("accountId", accountId);
            startActivity(intent);
        }

        // 饼图的某一种类别支出详情
        public void typeDeatil(View view) {
            if (bean == null) {
                return;
            }

            String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
            String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
            String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";

            String SQL = "";
            if (bean.typeName.equals("剩余消费")) {
                String typeName1 = pieList.get(0).getTypeName();
                String typeName2 = pieList.get(1).getTypeName();
                String typeName3 = pieList.get(2).getTypeName();
                String typeName4 = pieList.get(3).getTypeName();

                if (TextUtils.isEmpty(accountId)) {// 总账本里的
                    SQL = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                            ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                            ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, ASSETS_NAME,  REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " WHERE"
                            + " DELFLAG = 0 "
                            + " AND CHARGE_DATE >=" + MonthFirstDay
                            + " AND CHARGE_DATE<" + MonthLastDays
                            + " AND type_name != '" + typeName1
                            + "' AND type_name != '" + typeName2
                            + "' AND type_name != '" + typeName3
                            + "' AND type_name != '" + typeName4
                            + "' AND order_type = 1"
                            + " ORDER BY  money DESC";
                } else {// 单个账本的
                    SQL = "SELECT * FROM WATER_ORDER_COLLECT " +
                            " WHERE ACCOUNT_BOOK_ID=" + accountId
                            + " AND  DELFLAG = 0 "
                            + " AND CHARGE_DATE >=" + MonthFirstDay
                            + " AND CHARGE_DATE<" + MonthLastDays
                            + " AND type_name != '" + typeName1
                            + "' AND type_name != '" + typeName2
                            + "' AND type_name != '" + typeName3
                            + "' AND type_name != '" + typeName4
                            + "' AND order_type = 1"
                            + " ORDER BY  money DESC";
                }
            } else {
                if (TextUtils.isEmpty(accountId)) {// 总账本里的
                    SQL = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                                ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                                ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, ASSETS_NAME,  REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                                " where"
                                + " DELFLAG = 0 "
                                + " AND CHARGE_DATE >=" + MonthFirstDay
                                + " AND CHARGE_DATE<" + MonthLastDays
                                + " AND type_name = '" + bean.typeName
                                + "' AND order_type = 1"
                                + " ORDER BY money DESC";

                } else {// 单个账本的
                    SQL = "SELECT * FROM WATER_ORDER_COLLECT " +
                            " where"
                            + " ACCOUNT_BOOK_ID=" + accountId
                            + " AND  DELFLAG = 0 "
                            + " AND CHARGE_DATE >=" + MonthFirstDay
                            + " AND CHARGE_DATE<" + MonthLastDays
                            + " AND type_name = '" + bean.typeName
                            + "' AND order_type = 1"
                            + " ORDER BY  money DESC";
                }
            }

            Utils.playVoice(getActivity(), R.raw.changgui02);

            Intent intent = new Intent(getActivity(), ActTypeDetails.class);
            intent.putExtra(Constants.START_INTENT_A, bean.typeName);
            intent.putExtra(Constants.START_INTENT_B, String.valueOf(bean.money));
            intent.putExtra(Constants.START_INTENT_C, data.getYyyy() + "-" + data.getMm() + "-01");
            intent.putExtra(Constants.START_INTENT_D, data.getYyyy() + "-" + (data.getMm() + 1) + "-01");
            intent.putExtra(Constants.START_INTENT_E, SQL);
            startActivityForResult(intent, 141);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);// 导航栏黑色字体

            if (popOnces >= 0) {  //继续弹窗
                tanDialog(windowPop);
            }

            typeBudget = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, "");
            String abMoren = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, "");
            if (binding.abMoren != null) {
                binding.abMoren.setText(abMoren);
            }
            accountId = (String) SharedPreferencesUtil.get(com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
            zhangbenId = accountId;
            shouldSetChat = false;

            getIndexInfo();
            loadDataForNet(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (popOnces >= 0) {
            tanDialog(windowPop);
        }
        if (first) {
            shouldSetChat = true;
        } else {
            shouldSetChat = false;
        }

        LogUtil.e("1111", "1");
    }

    public void loadDataForNet(boolean showDialog) {
        //获取首页明细数据
        if (ii > 0) {
            return;
        }
        //下拉将数据同步到本地数据库 (如果是从登陆界面过来的 注册 或微信则需要重新拉数据 而非直接展示
        comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
        isFirst = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, true);

        String currentVersion = (String) SharedPreferencesUtil.get(Constants.CURRENT_VERSION, "");
        if (!currentVersion.equals("2.0.0")) {
//            SharedPreferencesUtil.put(Constants.CURRENT_VERSION, "2.0.0");// pullToSyncDate()方法中更新
            comeInForLogin = true;
        }

        if (NetworkUtil.isNetWorkAvailable(getActivity())) {
            if (isFirst) {
                pullToSyncDate(showDialog);
                SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
            } else {
                if (comeInForLogin) {
                    pullToSyncDate(showDialog);
                } else {  //判断上次同步时间与本次登录的时间对比 大于一天则执行pull push操作
                    if (timeB) {
                        pullToSyncDate(showDialog);
                    } else {
                        getIndexInfo();
                    }
                }
            }
        } else {
            getIndexInfo();
        }
    }

    private void pullToSyncDate(boolean showDialog) {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(getActivity());
        if (!netWorkAvailable) {
            return;
        }
        if (showDialog) {
            showDialog();
        }
        comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);

        String currentVersion = (String) SharedPreferencesUtil.get(Constants.CURRENT_VERSION, "");
        if (!currentVersion.equals("2.0.0")) {
            SharedPreferencesUtil.put(Constants.CURRENT_VERSION, "2.0.0");
            comeInForLogin = true;
        }

        NetWorkManager.getInstance().setContext(getActivity()).postPullToSyncDate(deviceId, comeInForLogin, token, new NetWorkManager.CallBack() {
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
                SPUtil.setPrefLong(getActivity(), com.hbird.base.app.constant.CommonTag.SYNDATE, L);
                List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                //插入本地数据库
                DBUtil.insertLocalDB(synData);
                SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);
                dismissDialog();
                binding.refresh.finishRefresh();

                if (isFirst) {
                    getIndexInfo();
                    SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
                } else {
                    comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
                    if (comeInForLogin) {
                        getIndexInfo();
                    } else {
                        pushOffLine();
                        getIndexInfo();
                    }
                }
                isFirst = false;
                SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
            }

            @Override
            public void onError(String s) {
                dismissDialog();
                ToastUtil.showShort(s);
            }
        });
    }

    private void getHeadNetInfo() {
        String mm2 = null;
        if (mm < 10) {
            mm2 = "0" + mm;
        } else {
            mm2 = mm + "";
        }
        final String t = yyyy + "-" + mm2;
        if (TextUtils.isEmpty(zhangbenId)) {
            setZhangBenAbout();
        } else {
            NetWorkManager.getInstance().setContext(getActivity()).getIndexDatas(t, zhangbenId, token, new NetWorkManager.CallBack() {
                @Override
                public void onSuccess(BaseReturn b) {
                    indexDatasReturn indexData = (indexDatasReturn) b;
                    indexResult = indexData.getResult();
                    if (indexResult.getBudget() != null) {
                        String s = "";
                        if (indexResult.getBudget().getBudgetMoney() != null) {
                            s = indexResult.getBudget().getBudgetMoney() + "";
                        }
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYMONEY, s);
                    }
                    String sql = "select count(day) from (select count(charge_date2) day  from " +
                            "(select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1)" +
                            " where account_book_id= '" + accountId + "' AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + t + "' group by strftime('%Y-%m-%d', charge_date2)  );";
                    Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                    }
                    setZhangBenAbout();
                }

                @Override
                public void onError(String s) {
                    setZhangBenAbout();
                }
            });
        }
    }

    private void setZhangBenAbout() {
        String moRenAcc = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, "默认账本");
        binding.abMoren.setText(moRenAcc);
        zhangbenId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
        if (TextUtils.isEmpty(zhangbenId)) { // 表示这个地方是总账本
            SharedPreferencesUtil.put("index_is_show", data.isShow());
            SharedPreferencesUtil.put("index_is_show_arrow", data.isShowArrow());
            SharedPreferencesUtil.put("index_is_show_member", data.isShowMember());
            SharedPreferencesUtil.put("index_is_show_member_seting", data.isShowMemberSeting());
            data.setShow(false);
            data.setShowArrow(false);
            data.setShowMember(false);
            data.setShowMemberSeting(false);
        } else {
            data.setShow((Boolean) SharedPreferencesUtil.get("index_is_show", false));
            data.setShowArrow((Boolean) SharedPreferencesUtil.get("index_is_show_arrow", false));
            data.setShowMember((Boolean) SharedPreferencesUtil.get("index_is_show_member", false));
            data.setShowMemberSeting((Boolean) SharedPreferencesUtil.get("index_is_show_member_seting", false));

            if (data.isShow() && !data.isShowArrow()) {// 如果显示为邀请，则修改为默认显示三角
                data.setShow(false);
                data.setShowArrow(true);
            }

            if (TextUtils.equals(typeBudget, "1")) {
                setWaveDate(indexResult);
            } else {
                //场景账本
                setWaveDate(indexResult);
            }
        }
    }

    private void setWaveDate(indexDatasReturn.ResultBean result) {
        if (null == result || result.getCount() == null) {
            return;
        }

        if (result.getBudget() != null) {
            if (result.getBudget().getBudgetMoney() != null) {
                String s = data.getSpendingMoney();// 支出
                Double i1 = Double.parseDouble(s);
                Double i3 = result.getBudget().getBudgetMoney();//预算
                DecimalFormat d = new DecimalFormat("######0.00");

                data.setBudgety(d.format(i3 - i1));// 剩余预算
            } else {
                data.setBudgety("- -");
            }
        } else {
            data.setBudgety("- -");
        }
    }

    private void setNewGuide() {
        View cv = getActivity().getWindow().getDecorView();
        cv.post(() -> {  //改为popwindow
            final NewGuidePop pop = new NewGuidePop(getActivity(), jizhangs, "1.点击这里开始记账~", 2, new NewGuidePop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, false);
                }
            });
            pop.showPopWindowToUp();
            new Handler().postDelayed(() -> {
                if (pop != null) {
                    pop.hidePopWindow();
                    setGuide2();
                }
            }, FIRST_LENGHT);
        });
    }

    private void setGuide2() {
        if (!getUserVisibleHint()) {
            return;
        }
        View cv = getActivity().getWindow().getDecorView();
        cv.post(() -> {  //改为popwindow
            final Guide2Pop pop = new Guide2Pop(getActivity(), binding.tvEditor, "2.点击这里设置预算~", new Guide2Pop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, false);
                }
            });
            pop.showPopWindow();
            new Handler().postDelayed(() -> {
                if (pop != null) {
                    pop.hidePopWindow();
                    setGuide3();
                }
            }, 2000);
        });
    }

    private void setGuide3() {
        if (!getUserVisibleHint()) {
            return;
        }
        View cv = getActivity().getWindow().getDecorView();
        cv.post(() -> {  //改为popwindow
            final NewGuidePop pop = new NewGuidePop(getActivity(), binding.ivArrow, "3.点击邀请好友记账~", 1, new NewGuidePop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, false);
                }
            });
            pop.showPopWindow();
            new Handler().postDelayed(() -> {
                if (pop != null) {
                    pop.hidePopWindow();
                    setGuide4();
                }
            }, 2000);
        });
    }

    private void setGuide4() {
        if (!getUserVisibleHint()) {
            return;
        }
        View cv = getActivity().getWindow().getDecorView();
        cv.post(() -> {  //改为popwindow
            final Guide4Pop pop = new Guide4Pop(getActivity(), binding.ivChange, "4.点击这里切换账本~", new Guide4Pop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, false);
                }
            });
            pop.showPopWindow();
            new Handler().postDelayed(() -> {
                if (pop != null) {
                    pop.hidePopWindow();
                }
            }, 2000);
        });
    }

    private void getIndexInfo() {
        Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, new LinkedHashSet<>());
        viewModel.getMonthData(data.getYyyy(), data.getMm(), accountId, prefSet, new IndexFragementModle.OnMonthCallBack() {

            @Override
            public void result(double spend, double income, ArrayList<indexBaseListBean> temp) {
                data.setNoData(true);
                data.setShowMore(false);
                if (temp != null && temp.size() > 0) {
                    data.setNoData(false);

                    int a = 0;// 只记录3条数据
                    list.clear();
                    for (int i = 0; i < temp.size(); i++) {
                        AccountDetailedBean b = new AccountDetailedBean();
                        b.setBean(temp.get(i));
                        if (temp.get(i).getIndexBeen() == null || temp.get(i).getIndexBeen().size() < 1) {// 代表是真实记录的，不是时间标题
                            a += 1;
                        }
                        if (a <= 3) {
                            list.add(b);
                        }
                        if (a == 3) {
                            break;
                        }
                    }
                    data.setShowMore(a == 3);
                    adapter.notifyItemRangeChanged(0, list.size());
                }

                data.setSpendingMoney(String.valueOf(spend));// 支出
                data.setInComeMoney(String.valueOf(income));// 收入

                if (shouldSetChat) {
                    setChart();
                }
            }
        });

        getHeadNetInfo();
        //获取账本内组员情况 --->首页展示
        if (!TextUtils.isEmpty(zhangbenId)) {
            getAccountMembers();
        }
    }

    private void getAccountMembers() {
        NetWorkManager.getInstance().setContext(getActivity()).getAccountMember(token, zhangbenId, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                AccountMembersBean b1 = (AccountMembersBean) b;

                String yourSelf = b1.getResult().getYourSelf();
                String owner = b1.getResult().getOwner();
                List<String> members = b1.getResult().getMembers();
                LogUtil.e("空代表是管理員：" + owner);

                if (null != owner) {//不是管理员  显示成员头像，不显示邀请，不显示设置
                    members.add(owner);
                    SharedPreferencesUtil.put("index_is_show", false);
                    SharedPreferencesUtil.put("index_is_show_arrow", false);
                    SharedPreferencesUtil.put("index_is_show_member", true);
                    SharedPreferencesUtil.put("index_is_show_member_seting", false);
                } else {  //代表是管理员   显示邀请，显示设置
                    LogUtil.e(memberList.size() + "");

                    if (memberList.size() == 1) {// 只有自己
                        SharedPreferencesUtil.put("index_is_show", true);
                        SharedPreferencesUtil.put("index_is_show_arrow", false);
                        SharedPreferencesUtil.put("index_is_show_member", false);
                        SharedPreferencesUtil.put("index_is_show_member_seting", false);
                    } else {
                        SharedPreferencesUtil.put("index_is_show", false);
                        SharedPreferencesUtil.put("index_is_show_arrow", false);
                        SharedPreferencesUtil.put("index_is_show_member", true);
                        SharedPreferencesUtil.put("index_is_show_member_seting", true);
                    }
                }

                memberList.clear();
                if (yourSelf != null) {
                    memberList.add(yourSelf);
                } else {
                    memberList.add("");// 你没有头像
                }

                if (members != null) {
                    memberList.addAll(members);
                }

                memberAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String s) {
                SharedPreferencesUtil.put("index_is_show", false);
                SharedPreferencesUtil.put("index_is_show_arrow", false);
                SharedPreferencesUtil.put("index_is_show_member", false);
                SharedPreferencesUtil.put("index_is_show_member_seting", false);
            }
        });
    }

    //初始化 年（2010-2050），月（1-12）
    private void setInitData() {
        dataY = new ArrayList<>();
        dataY.clear();
        for (int i = 2010; i < 2051; i++) {
            dataY.add(i + "");
        }
        dataM = new ArrayList<>();
        dataM.clear();
        String temp = "月份";
        for (int i = 1; i < 13; i++) {
            dataM.add(i + temp);
        }
    }

    private void alertDialog(final WaterOrderCollect temp) {
        new DialogUtils(getActivity())
                .builder()
                .setTitle("温馨提示")
                .setMsg("确认删除吗？")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", view -> {
                    Boolean b = DBUtil.updateOneDate(temp);
                    if (b) {
                        //刷新界面数据
                        getIndexInfo();
                        //删除 并同步上传到服务器
                        pullToSyncDate(true);
                    }
                }).show();
    }

    private void pushOffLine() {
        OffLineReq req = new OffLineReq();
        req.setMobileDevice(deviceId);
        Long time = SPUtil.getPrefLong(getActivity(), com.hbird.base.app.constant.CommonTag.SYNDATE, new Date().getTime());
        String times = String.valueOf(time);
        req.setSynDate(times);

        //本地数据库查找未上传数据 上传至服务器
        String sql = "SELECT * FROM WATER_ORDER_COLLECT wo where wo.ACCOUNT_BOOK_ID = " + zhangbenId + " AND wo.UPDATE_DATE >= " + time;
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        List<OffLineReq.SynDataBean> myList = new ArrayList<>();
        myList.clear();
        if (null != cursor) {
            myList = DBUtil.changeToListPull(cursor, myList, OffLineReq.SynDataBean.class);
        }
        List<OffLine2Req.SynDataBean> myList2 = new ArrayList<>();
        myList2.clear();
        OffLine2Req req2 = new OffLine2Req();
        req2.setMobileDevice(req.getMobileDevice());
        req2.setSynDate(req.getSynDate());
        if (myList != null) {
            for (int i = 0; i < myList.size(); i++) {
                OffLineReq.SynDataBean s1 = myList.get(i);
                OffLine2Req.SynDataBean synDataBean = new OffLine2Req.SynDataBean();
                synDataBean.setId(s1.getId());
                synDataBean.setAccountBookId(s1.getAccountBookId());
                if (s1.getChargeDate() != null) {
                    synDataBean.setChargeDate(s1.getChargeDate().getTime());
                }
                synDataBean.setCreateBy(s1.getCreateBy());
                if (s1.getCreateDate() != null) {
                    synDataBean.setCreateDate(s1.getCreateDate().getTime());
                }
                synDataBean.setCreateName(s1.getCreateName());
                if (s1.getDelDate() != null) {
                    synDataBean.setDelDate(s1.getDelDate().getTime());
                }

                synDataBean.setDelflag(s1.getDelflag());
                synDataBean.setIcon(s1.getIcon());
                synDataBean.setIsStaged(s1.getIsStaged());
                synDataBean.setMoney(s1.getMoney());
                synDataBean.setOrderType(s1.getOrderType());
                synDataBean.setParentId(s1.getParentId());
                synDataBean.setPictureUrl(s1.getPictureUrl());
                synDataBean.setRemark(s1.getRemark());
                synDataBean.setSpendHappiness(s1.getSpendHappiness());
                synDataBean.setTypeId(s1.getTypeId());
                synDataBean.setTypeName(s1.getTypeName());
                if (s1.getUpdateDate() != null) {
                    synDataBean.setUpdateDate(s1.getUpdateDate().getTime());
                }
                synDataBean.setTypePname(s1.getTypePname());
                synDataBean.setUpdateBy(s1.getUpdateBy());
                synDataBean.setUseDegree(s1.getUseDegree());
                synDataBean.setUpdateName(s1.getUpdateName());
                synDataBean.setUserPrivateLabelId(s1.getUserPrivateLabelId());
                synDataBean.setAssetsId(s1.getAssetsId());
                synDataBean.setAssetsName(s1.getAssetsName());
                myList2.add(synDataBean);
            }
            req2.setSynData(myList2);
        }


        if (myList2 != null && myList2.size() > 0) {
            req2.setSynData(myList2);
        }
        String jsonInfo = new Gson().toJson(req2);
        NetWorkManager.getInstance().setContext(getActivity()).pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                getIndexInfo();
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent datas) {
        super.onActivityResult(requestCode, resultCode, datas);

        if (requestCode == 131 && resultCode == 130) {// 选择账户
            binding.abMoren.setText(datas.getStringExtra("TITLE"));
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, datas.getStringExtra("TITLE"));
            zhangbenId = datas.getStringExtra("ID");
            accountId = zhangbenId;
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, zhangbenId);
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, zhangbenId);
            typeBudget = datas.getStringExtra("typeBudget");
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, datas.getStringExtra("abTypeId"));
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, datas.getStringExtra("typeBudget"));

            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, zhangbenId);
            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, datas.getStringExtra("TITLE"));
            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, zhangbenId);
            String temp = datas.getStringExtra("abTypeId");
            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, temp == null ? "null" : temp);
            SharedPreferencesUtil.put(com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, datas.getStringExtra("typeBudget"));

            getIndexInfo();
            loadDataForNet(true);

            first = true;
            iii = -1;
            pieChatList.clear();
            bean = null;
            binding.setPiechat(bean);
            data.setSelestStr("");
            pieChatAdapter.notifyDataSetChanged();

            setChart();
        } else if (requestCode == 101) {// 明细回来的
            getIndexInfo();
            loadDataForNet(false);

            if (iii == -1) {
                return;
            }

            setChart();

            pieChatList.clear();
            if (pieList.size() > iii) {
                binding.setPiechat(pieList.get(iii));
                data.setSelestStr(pieList.get(iii).typeName);
                if (iii == 4) {// 其它
                    List<WaterOrderCollect> temp = viewModel.getOthereRanking(pieList.get(0).getTypeName(), pieList.get(1).getTypeName(), pieList.get(2).getTypeName(), pieList.get(3).getTypeName(), data.getYyyy(), data.getMm(), accountId);
                    pieChatList.addAll(temp);
                } else {
                    List<WaterOrderCollect> temp = viewModel.getTypeRanking(pieList.get(iii).getTypeName(), data.getYyyy(), data.getMm(), accountId);
                    pieChatList.addAll(temp);
                }
            }
            pieChatAdapter.notifyDataSetChanged();
        } else if (requestCode == 141) {// 记账回来的
            shouldSetChat = true;
            getIndexInfo();
            loadDataForNet(false);
        } else if (requestCode == 1000) {// 设置预算
            getHeadNetInfo();
        }
    }

    private void setHasRed(String id, final int m) {
        //首页公告已读接口
        NetWorkManager.getInstance().setContext(getActivity()).putHasRead(id, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                GloableReturn b1 = (GloableReturn) b;
                dialog2TiaoZhuan(m);
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }

    private void setHomePage() {
        String version = Utils.getVersion(getActivity());
        NetWorkManager.getInstance().setContext(getActivity()).getHomeWindows(version, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                WindowPopReturn b1 = (WindowPopReturn) b;
                windowPop = b1.getResult().getActivity();
                windowPopInvite = b1.getResult().getInviteCount();
                tanci = 0;
                //首页普通弹窗（跳转内链外链）
                if (null != windowPop) {
                    if (windowPop.size() > 0) {
                        popOnces = windowPop.size();
                        //先只展示第一条弹窗
                        final String id = windowPop.get(0).getId();
                        new IndexCommonDialog(getActivity(), IndexFragement.this)
                                .builder()
                                .setMsg(windowPop.get(0).getImage())
                                .setChaKan(view -> {
                                    popOnces = popOnces - 1;
                                    setHasRed(id, 0);
                                })
                                .setCancleButton(view -> {
                                    popOnces = popOnces - 1;
                                    if (popOnces > 0) {  //继续弹窗
                                        tanDialog(windowPop);
                                    } else {  //是否要弹出好友推荐框
                                        tanHaoYouDialog();
                                    }
                                }).show();
                    } else {
                        tanHaoYouDialog();
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    private void tanHaoYouDialog() {
        if (null != windowPopInvite) {
            if (windowPopInvite.getInviteCount() > 0) {
                //邀请好友的弹窗
                int inviteCount = windowPopInvite.getInviteCount();
                int inviteFriendsAware = windowPopInvite.getInviteFriendsAware();
                new InvitationFirendDialog(getActivity()).builder()
                        .setMsg(inviteCount + "", inviteFriendsAware + "")
                        .setChaKan(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                windowPopInvite.setInviteCount(0);//查看后现将其设置为0 再回来即使不请求接口 也不再调用此弹窗
                                String ids = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYIDS, "");
                                String name = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYNAME, "");
                                Utils.playVoice(getActivity(), R.raw.changgui02);
                                //showMessage("邀请好友");
                                Intent intents = new Intent();
                                intents.setClass(getActivity(), AskFriendsActivity.class);
                                intents.putExtra("IDS", ids);
                                intents.putExtra("NAME", name);
                                startActivity(intents);
                            }
                        })
                        .setCancleButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                windowPopInvite.setInviteCount(0);//查看后现将其设置为0 再回来即使不请求接口 也不再调用此弹窗
                            }
                        }).show();
            }
        }
    }

    private void tanDialog(final List<WindowPopReturn.ResultBean.ActivityBean> windowPop) {
        tanci = tanci + 1;
        if (null == windowPop) {
            return;
        }
        int size = windowPop.size();
        if (tanci >= size) {
            //还要判断是否要弹出好友推荐框
            if (getUserVisibleHint()) {
                tanHaoYouDialog();
            }
            return;
        }
        final String id = windowPop.get(tanci).getId();
        new IndexCommonDialog(getActivity(), IndexFragement.this)
                .builder()
                .setMsg(windowPop.get(tanci).getImage())
                .setChaKan(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popOnces = popOnces - 1;
                        setHasRed(id, tanci);
                    }
                })
                .setCancleButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popOnces = popOnces - 1;
                        if (popOnces > 0) {
                            //继续弹窗
                            tanDialog(windowPop);
                        } else {
                            //是否要弹出好友推荐框
                            tanHaoYouDialog();
                        }
                    }
                }).show();
    }

    private void dialog2TiaoZhuan(int m) {
        String jumpType = windowPop.get(m).getJumpType();
        if (TextUtils.equals(jumpType, "0")) {
            //内链
            String connectionAddress = windowPop.get(m).getConnectionAddress();
            MainActivity activity;
            switch (connectionAddress) {
                case "mxsy":
                    Utils.playVoice(getActivity(), R.raw.changgui02);
                    activity = (MainActivity) getActivity();
                    activity.setTiaozhuanFragement(0, 0);
                    break;
                case "tbtj":
                    activity = (MainActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 0);
                    break;
                case "tbfx":
                    activity = (MainActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 1);
                    break;
                case "tbzc":
                    activity = (MainActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 2);
                    break;
                case "jzsy":
                    Utils.playVoice(getActivity(), R.raw.changgui02);
                    startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                    break;
                case "lppsy":
                    activity = (MainActivity) getActivity();
                    activity.setTiaozhuanFragement(2, 0);
                    break;
                case "fftz":
                    Utils.playVoice(getActivity(), R.raw.changgui02);
                    startActivity(new Intent(getActivity(), NotificationMessageActivity.class));
                    break;
            }

        } else if (TextUtils.equals(jumpType, "1")) {
            //外链
            String address = windowPop.get(m).getConnectionAddress();
            String shareTitle = windowPop.get(m).getShareTitle();
            String shareImage = windowPop.get(m).getShareImage();
            String shareContent = windowPop.get(m).getShareContent();
            Intent intent = new Intent();
            intent.setClass(getActivity(), H5WebViewActivity.class);
            intent.putExtra("URL", address);
            intent.putExtra("TITLE", shareTitle);
            intent.putExtra("SHAREIMAGE", shareImage);
            intent.putExtra("SHARECONTENT", shareContent);
            startActivity(intent);
        }
    }

    private PieChart mChart;

    private void initChart() {
        mChart = new PieChart(getActivity());
        int width_150 = getResources().getDimensionPixelSize(R.dimen.dp_150_x);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width_150, width_150);
        mChart.setLayoutParams(params);
        binding.pieParent.removeAllViews();
        binding.pieParent.addView(mChart);

        //mChart的半径是根据整体的控件大小来动态计算的，设置外边距等都会影响到圆的半径
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);  //禁止显示图例
        mChart.setCenterTextSize(14f); //设置文本字号
        mChart.setHoleRadius(50f); //设置中心孔半径占总圆的百分比
        mChart.setRotationAngle(90); //初始旋转角度
        mChart.setData(generatePieData()); //设置数据源
        mChart.setRotationEnabled(false);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float[] mDrawAngles = mChart.getDrawAngles();
                float[] mAbsoluteAngles = mChart.getAbsoluteAngles();
                float start = mChart.getRotationAngle();
                int i = (int) h.getX();
                float offset = mDrawAngles[i] / 2;
                float end = 90 - (mAbsoluteAngles[i] - offset);

                if (end < 0) {
                    end = 360 + end;
                }

                if (data.getSize() > 0) {
                    data.setSelect(i);
                }

//                fromangle = start;
//                toangle = end;
                iii = i;
                spin(start, end, i);
            }

            @Override
            public void onNothingSelected() {
                data.setSelect(-1);// 没有选择任何
                binding.setPiechat(null);
                data.setSelestStr("");
            }
        });
    }

    private boolean first = true;// 第一次需要初始化饼图，之后不再初始化只设置值
    private int iii = -1;

    private void setChart() {
//        if (first) {
        initChart();
//            first = false;
//        } else {
//            mChart.setData(generatePieData()); //设置数据源
//        }

        Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, new LinkedHashSet<>());
        double spend1 = Double.parseDouble(data.getSpendingMoney());
        viewModel.getLastMonthData(data.getYyyy(), data.getMm(), accountId, prefSet, (spend2, income, list) -> {
            if (spend2 == 0 || spend1 == 0) {
                data.setComparison("");
            } else {  // 设置比例
                String s;// 比例结果
                if (spend1 - spend2 > 0) {
                    s = "+" + Utils.to2Digit((spend1 - spend2) / spend2 * 100) + "%";
                } else {
                    s = "-" + Utils.to2Digit((spend2 - spend1) / spend2 * 100) + "%";
                }
                data.setComparison("相比上月支出 " + s);
            }
        });
        String textStr = "<font color=\"#808080\">总支出</font>\n" + spend1;
        mChart.setCenterText(Html.fromHtml(textStr)); //设置中心文本
        mChart.invalidate();
    }

    private StatisticsSpendTopArraysBean bean;// 选择的饼图，没选择为空

    private void spin(float fromangle, float toangle, final int i) {
        mChart.setRotationAngle(fromangle);
        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(mChart, "rotationAngle", fromangle, toangle);
        spinAnimator.setDuration(500);
//        spinAnimator.setInterpolator(Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad));

        spinAnimator.addUpdateListener(animation -> mChart.postInvalidate());
        spinAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pieChatList.clear();
                if (pieList.size() > i) {
                    bean = pieList.get(i);
                    binding.setPiechat(bean);
                    data.setSelestStr(pieList.get(i).typeName);
                    if (i == 4) {// 其它
                        List<WaterOrderCollect> temp = viewModel.getOthereRanking(pieList.get(0).getTypeName(), pieList.get(1).getTypeName(), pieList.get(2).getTypeName(), pieList.get(3).getTypeName(), data.getYyyy(), data.getMm(), accountId);
                        pieChatList.addAll(temp);
                    } else {
                        List<WaterOrderCollect> temp = viewModel.getTypeRanking(pieList.get(i).getTypeName(), data.getYyyy(), data.getMm(), accountId);
                        pieChatList.addAll(temp);
                    }
                }
                pieChatAdapter.notifyDataSetChanged();
            }
        });
        spinAnimator.start();
    }

    // 生成数据,将数据有由到小排列
    List<StatisticsSpendTopArraysBean> pieList = new ArrayList<>();

    protected PieData generatePieData() {
        data.setStr1("--");// 清空数据
        data.setCop1("");
        data.setStr2("--");
        data.setCop2("");
        data.setStr3("--");
        data.setCop3("");
        data.setStr4("--");
        data.setCop4("");
        data.setStr5("--");
        data.setCop5("");
        data.setSelect(iii);// 没有选择任何
        binding.setPiechat(bean);
        data.setSelestStr("");

        Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, new LinkedHashSet<>());


        List<StatisticsSpendTopArraysBean> temp = viewModel.getRankingBar(data.getYyyy(), data.getMm(), accountId, prefSet);
        pieList.clear();
        data.setSize(temp == null ? 0 : temp.size());

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        if (data.getSize() == 0) {
            entries1.add(new PieEntry((float) (100), ""));
            data.setNoData(true);
            data.setShowMore(false);
            data.setComparison("");
            bean = null;
            data.setSelect(-1);// 没有选择任何
            binding.setPiechat(bean);
            data.setSelestStr("");
        }

        double t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0;// 饼图显示的比例，太小的显示10%的大小
        double a1 = 0; // 正确的百分比
        if (data.getSize() > 0) {// 至少有一条
            pieList.add(temp.get(0));

            data.setStr1(pieList.get(0).typeName);
            a1 = pieList.get(0).money / Double.parseDouble(data.getSpendingMoney());
            t1 = Utils.to4Digit(a1);
            entries1.add(new PieEntry(t1 < 0.1 ? (float) 0.1 : (float) t1, ""));
            data.setCop1(Utils.to2Digit(a1 * 100) + "%");
        }
        double a2 = 0;
        if (data.getSize() > 1) {// 至少有两条
            pieList.add(temp.get(1));

            data.setStr2(pieList.get(1).typeName);
            a2 = pieList.get(1).money / Double.parseDouble(data.getSpendingMoney());
            t2 = Utils.to4Digit(a2);
            entries1.add(new PieEntry(t2 < 0.1 ? (float) 0.1 : (float) t2, ""));
            data.setCop2(Utils.to2Digit(a2 * 100) + "%");
        }
        double a3 = 0;
        if (data.getSize() > 2) {// 至少有三条
            pieList.add(temp.get(2));

            data.setStr3(pieList.get(2).typeName);
            a3 = pieList.get(2).money / Double.parseDouble(data.getSpendingMoney());
            t3 = Utils.to4Digit(a3);
            entries1.add(new PieEntry(t3 < 0.1 ? (float) 0.1 : (float) t3, ""));
            data.setCop3(Utils.to2Digit(a3 * 100) + "%");
        }
        double a4 = 0;
        if (data.getSize() > 3) {// 至少有四条
            pieList.add(temp.get(3));

            data.setStr4(pieList.get(3).typeName);
            a4 = pieList.get(3).money / Double.parseDouble(data.getSpendingMoney());
            t4 = Utils.to4Digit(a4);
            entries1.add(new PieEntry(t4 < 0.1 ? (float) 0.1 : (float) t4, ""));
            data.setCop4(Utils.to2Digit(a4 * 100) + "%");
        }
        if (data.getSize() > 4) {// 至少有五条以上
            data.setStr5("剩余消费");
            double a5 = 1 - a1 - a2 - a3 - a4;
            t5 = a4 / t4 * a5;
            entries1.add(new PieEntry((float) t5, ""));
            data.setCop5(Utils.to2Digit(a5 * 100) + "%");

            StatisticsSpendTopArraysBean t = new StatisticsSpendTopArraysBean();
            t.setIcon(Constants.ICON_OTHER);
            t.setMoney(Utils.to4Digit(Double.parseDouble(data.getSpendingMoney()) * a5));
            t.setTypeName("剩余消费");
            pieList.add(t);
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");

        if (data.getSize() == 0) {
            ds1.setColors(Color.parseColor("#BDBDBD")); //添加默认的颜色组
        } else {
            ds1.setColors(ContextCompat.getColor(getActivity(), R.color.index_1),
                    ContextCompat.getColor(getActivity(), R.color.index_2),
                    ContextCompat.getColor(getActivity(), R.color.index_3),
                    ContextCompat.getColor(getActivity(), R.color.index_4),
                    ContextCompat.getColor(getActivity(), R.color.index_5)); //添加默认的颜色组
        }
        ds1.setSliceSpace(1f); //饼块之间的间隙
        ds1.setDrawValues(false);  //将饼状图上的默认百分比子去掉
        ds1.setHighlightEnabled(true); // 允许突出显示饼块

        return new PieData(ds1);
    }
}