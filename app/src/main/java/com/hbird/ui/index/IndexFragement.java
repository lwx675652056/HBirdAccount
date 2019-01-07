package com.hbird.ui.index;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.databinding.FragementIndexBinding;
import com.hbird.base.mvc.activity.AskFriendsActivity;
import com.hbird.base.mvc.activity.BudgetActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.H5WebViewActivity;
import com.hbird.base.mvc.activity.InviteJiZhangActivity;
import com.hbird.base.mvc.activity.MemberManagerActivity;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.activity.MyZhangBenActivity;
import com.hbird.base.mvc.activity.NotificationMessageActivity;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.OffLine2Req;
import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountMembersBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.WindowPopReturn;
import com.hbird.base.mvc.bean.ReturnBean.indexDatasReturn;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.DialogToGig;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.view.dialog.IndexCommonDialog;
import com.hbird.base.mvc.view.dialog.InvitationFirendDialog;
import com.hbird.base.mvc.widget.HeaderViewPager;
import com.hbird.base.mvc.widget.NewGuidePop;
import com.hbird.base.mvc.widget.TabRadioButton;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.ui.calendar.ActCalendar;
import com.hbird.ui.detailed.ActAccountDetailed;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sing.common.base.BaseFragment;
import sing.common.util.StatusBarUtil;
import sing.util.AppUtil;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

import static com.hbird.base.app.constant.CommonTag.FIRST_COME_1_2_0;
import static com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN;
import static java.lang.Integer.parseInt;

/**
 * @author: LiangYX
 * @ClassName: IndexFragement
 * @date: 2018/12/25 17:29
 * @Description: 首页
 */
public class IndexFragement extends BaseFragment<FragementIndexBinding, IndexFragementModle> {

    ImageView mNoData;
    LinearLayout mNets;
    HeaderViewPager scrollableLayout;
    TextView mMoRen;

    private ArrayList<String> dataY;
    private ArrayList<String> dataM;
    private int yyyy = 2018;

    private ArrayList<indexBaseListBean> dates = new ArrayList<>();
    //代表第一次进入页面
    private int ii = 0;
    private String token;
    private String mMonth;
    private indexDatasReturn.ResultBean indexResult;
    private String accountId = "";
    private boolean isFirst;
    private boolean comeInForLogin;
    private boolean timeB = false;
    private final int FIRST_LENGHT = 4000;
    private TabRadioButton jizhangs;
    private int popOnces = 0;//首页展示弹窗次数
    private int tanci = 0;
    private List<WindowPopReturn.ResultBean.ActivityBean> windowPop;
    private WindowPopReturn.ResultBean.InviteCountBean windowPopInvite;

    private String typeBudget = "1";
    private String zhangbenId = "";

    private String deviceId;// 设备唯一标识
    private int mm = 12; // 当前月
    private IndexFragmentData data;

    private MemberAdapter memberAdapter;
    private List<String> memberList = new ArrayList<>();

    private IndexAdapter adapter;
    private List<AccountDetailedBean> list = new ArrayList<>();

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
        data = new IndexFragmentData();
        binding.setBean(data);
        binding.setListener(new OnClick());

        deviceId = Utils.getDeviceInfo(getActivity());

        memberAdapter = new MemberAdapter(getActivity(), memberList, R.layout.row_member);
        binding.recyclerView.setAdapter(memberAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));

        mNoData = binding.ivNoData;
        mNets = binding.llNets;
        scrollableLayout = binding.scrollableLayout;
        mMoRen = binding.abMoren;

        //数据库相关操作
        jizhangs = getActivity().findViewById(R.id.btn_jizhang);

        popOnces = 0;//重新执行则将其置为0

        adapter = new IndexAdapter(getActivity(), list, R.layout.row_index, (position, data, type) -> onItemClick((AccountDetailedBean) data, type));
        binding.recyclerView1.setAdapter(adapter);
        binding.recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));

        zhangbenId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
        LogUtil.e("zhangbenId:" + zhangbenId);
        if (TextUtils.isEmpty(zhangbenId)) {
            //账本id
            accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
            zhangbenId = accountId;
        }
        //账本id
        accountId = zhangbenId;
        LogUtil.e("accountId:" + accountId);

        yyyy = parseInt(DateUtils.getCurYear("yyyy"));

        mMonth = DateUtils.date2Str(new Date(), "MM");
        String s = mMonth.substring(0, 2);
        mm = parseInt(s);

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
                    if (l > v) {
                        //超过了最大间隔时间则自动上传
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
    }

    // 点击记账的条目
    private void onItemClick(AccountDetailedBean data, int type) {
        if (type == 0) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent = new Intent(getActivity(), MingXiInfoActivity.class);
            intent.putExtra("ID", data.getId());
            startActivityForResult(intent, 101);
        } else if (type == 1) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            alertDialog(data);
        }
    }

    public class OnClick {
        // 选择账户
        public void chooseAccount(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent2 = new Intent();
            intent2.setClass(getActivity(), MyZhangBenActivity.class);
            String id = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
            intent2.putExtra("ID", id);
            startActivityForResult(intent2, 131);
        }

        // 日历
        public void calendar(View view) {
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
        }

        // 编辑预算
        public void editor(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
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
            startActivity(intent3);
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {

            Utils.initColor(getActivity(),Color.rgb(246, 246, 246));
            StatusBarUtil.setStatusBarLightMode(getActivity().getWindow()); // 导航栏黑色字体


            getIndexInfo();
            if (popOnces >= 0) {
                //继续弹窗
                tanDialog(windowPop);
            }
            loadDataForNet();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        ii = ii + 1;
//        if (ii > 1) {
        getIndexInfo();
        if (popOnces >= 0) {
            //继续弹窗
            tanDialog(windowPop);
        }
        loadDataForNet();
//        }
    }


    public void loadDataForNet() {
        //获取首页明细数据
        if (ii > 0) {
            return;
        }
        //下拉将数据同步到本地数据库 (如果是从登陆界面过来的 注册 或微信则需要重新拉数据 而非直接展示
        comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
        isFirst = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, true);

        // 因为前辈的数据库不能升级，现阶段数据库新增了字段，故这里废弃之前的数据库，强制同步最新数据到新数据库
        boolean mustUpload = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.MUST_UPDATE, false);

        if (NetworkUtil.isNetWorkAvailable(getActivity())) {
            if (isFirst || !mustUpload) {
                SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.MUST_UPDATE, true);
                pullToSyncDate();
                SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
            } else {
                if (comeInForLogin) {
                    pullToSyncDate();
                } else {
                    //判断上次同步时间与本次登录的时间对比 大于一天则执行pull push操作
                    if (timeB) {
                        pullToSyncDate();
                    } else {
                        getIndexInfo();
                    }
                }
            }
        } else {
            getIndexInfo();
        }
    }

    private DialogToGig dialogToGig;

    public void showGifProgress(String title) {
        if (dialogToGig == null) {
            dialogToGig = new DialogToGig(getActivity());
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
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(getActivity());
        setCurrentNets();
        if (!netWorkAvailable) {
            return;
        }
        showGifProgress("");
        if (AppUtil.getVersionCode(getActivity()) < 10) {
            comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
        } else {
            boolean a = SPUtil.getPrefBoolean(getActivity(), FIRST_COME_1_2_0, false);
            if (!a) {// 主动更新一下数据库
                SPUtil.setPrefBoolean(getActivity(), FIRST_COME_1_2_0, true);
                comeInForLogin = true;
            }
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
                hideGifProgress();
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
                hideGifProgress();
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
            NetWorkManager.getInstance().setContext(getActivity())
                    .getIndexDatas(t, zhangbenId, token, new NetWorkManager.CallBack() {
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
        mMoRen.setText(moRenAcc);
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
            final NewGuidePop pop = new NewGuidePop(getActivity(), jizhangs, "点击这里开始记账~", 2, new NewGuidePop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, false);
                }
            });
            pop.showPopWindowToUp();
            new Handler().postDelayed(() -> {
                if (pop != null) {
                    pop.hidePopWindow();
                }
            }, FIRST_LENGHT);
        });
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.rl_top_btn: //来记一笔
//                Utils.playVoice(getActivity(), R.raw.jizhang);
//                LogUtil.e("11");
//                startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
//                LogUtil.e("22");
//                break;
//        }
//    }

    private void getIndexInfo() {
        //查询指定月份记录
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";
        String sql = "";
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        zhangbenId = accountId;
        if (TextUtils.isEmpty(accountId)) {
            Set<String> set = new LinkedHashSet<>();
            Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
            if (prefSet != null) {
                String ids = "";
                Iterator<String> iterator = prefSet.iterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    ids += s + ",";
                }
                if (!TextUtils.isEmpty(ids)) {
                    String substring = ids.substring(0, ids.length() - 1);
                    sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                            ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                            ", remark, USER_PRIVATE_LABEL_ID,ASSETS_NAME, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " where  ACCOUNT_BOOK_ID in " + "(" + substring + ")" + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
                }
            }
        } else {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, ASSETS_NAME,  REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                    " where  ACCOUNT_BOOK_ID=" + accountId + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> dbList = new ArrayList<>();
        dbList.clear();
        if (null != cursor) {
            try {
                dbList = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != dbList && dbList.size() > 0) {
            Collections.sort(dbList);
            Map<String, Object> dbDate = getDBDate(dbList);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);

            if (bean != null) {
                dates = getDBDates(bean);
                if (dates != null && dates.size() > 0) {
                    double monthIncome = bean.getMonthIncome();
                    double monthSpend = bean.getMonthSpend();
                    String monthIncomes = getNumToNumber(monthIncome);
                    String monthSpends = getNumToNumber(monthSpend);

                    data.setSpendingMoney(monthSpends);// 支出
                    data.setInComeMoney(monthIncomes);// 收入

                    list.clear();
                    int a = 0;// 只记录3天数据
                    for (int i = 0; i < dates.size(); i++) {
                        AccountDetailedBean temp = new AccountDetailedBean();
                        temp.setBean(dates.get(i));
                        if (dates.get(i).getIndexBeen() == null || dates.get(i).getIndexBeen().size() < 1) {// 代表是真实记录的，不是时间标题
                            a += 1;
                        }
                        if (a <= 3) {
                            list.add(temp);
                        }
                        if (a == 3) {
                            break;
                        }
                    }
//                    Collections.sort(list);
                    adapter.notifyDataSetChanged();
                } else {
                    data.setSpendingMoney("0.00");// 支出
                    data.setInComeMoney("0.00");// 收入

                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            data.setSpendingMoney("0.00");// 支出
            data.setInComeMoney("0.00");// 收入

            list.clear();
            adapter.notifyDataSetChanged();
        }
        getHeadNetInfo();
        //获取账本内组员情况 --->首页展示
        if (!TextUtils.isEmpty(zhangbenId)) {
            getAccountMembers();
        }
    }

    private void getAccountMembers() {
        NetWorkManager.getInstance().setContext(getActivity())
                .getAccountMember(token, zhangbenId, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        AccountMembersBean b1 = (AccountMembersBean) b;

                        String yourSelf = b1.getResult().getYourSelf();
                        String owner = b1.getResult().getOwner();
                        List<String> members = b1.getResult().getMembers();
                        LogUtil.e("空代表是管理員：" + owner);

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

    private String getNumToNumber(Double d) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(d);
    }

    private Map<String, Object> getDBDate(List<WaterOrderCollect> list) {
        //获取到当月所有记录
        Map<Date, Object> map = new LinkedHashMap<>();
        for (Iterator<WaterOrderCollect> it = list.iterator(); it.hasNext(); ) {
            WaterOrderCollect warter = it.next();
            //判断是否包含日期
            Date chargeDate = warter.getChargeDate();
            Date day = DateUtil.getDay(chargeDate);
            if (map.containsKey(day)) {
                ((ArrayList) map.get(day)).add(warter);
            } else {
                List<WaterOrderCollect> lists = new ArrayList<>();
                lists.add(warter);
                map.put(day, lists);
            }
        }
        Map<String, Object> ja = new HashMap();
        if (map.size() > 0) {
            //Map<Date, Object> resultMap = sortMapByKey(map);
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            for (Map.Entry<Date, Object> entry : map.entrySet()) {
                //封装成key value格式
                JSONObject obj = new JSONObject();
                if (!TextUtils.equals(entry.getKey() + "", "dayTime")) {
                    obj.put("dayTime", entry.getKey());
                }

                if (!TextUtils.equals(entry.getKey() + "", "dayTime")) {
                    obj.put("dayArrays", entry.getValue());
                }
                array.add(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
            }
            //获取日统计
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = JSON.parseObject((String) array.get(i));
                if (!TextUtils.isEmpty(jsonObject.getString("dayArrays"))) {
                    BigDecimal dayIncome = new BigDecimal(0);
                    BigDecimal daySpend = new BigDecimal(0);
                    List lists = JSONArray.parseArray(jsonObject.getString("dayArrays"));
                    for (int j = 0; j < lists.size(); j++) {
                        //支出
                        WaterOrderCollect warter = JSONObject.parseObject(JSONObject.toJSONString(lists.get(j)), WaterOrderCollect.class);
                        if (warter.getOrderType() == 1) {
                            daySpend = daySpend.add(new BigDecimal(warter.getMoney()));
                        }
                        if (warter.getOrderType() == 2) {
                            dayIncome = dayIncome.add(new BigDecimal(warter.getMoney()));
                        }
                    }
                    jsonObject.put("dayIncome", dayIncome);
                    jsonObject.put("daySpend", daySpend);
                    array2.add(jsonObject);
                    //jsonObject转json
                }
            }
            //获取月份统计数据
            if (!TextUtils.isEmpty(accountId)) {
                Map<String, BigDecimal> account = getAccount(Integer.parseInt(accountId));
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            } else {
                Map<String, BigDecimal> account = getAccounts();
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            }

        }
        return ja;
    }

    public Map<String, BigDecimal> getAccount(int accountBookId) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                " AND account_book_id = '" + accountBookId + "' AND delflag = 0;";
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            int count = cursor.getCount();
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }

    public Map<String, BigDecimal> getAccounts() {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Set<String> sets = new LinkedHashSet<>();
        Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, sets);
        String sql = "";
        Cursor cursor = null;
        if (prefSet != null) {
            String ids = "";
            Iterator<String> iterator = prefSet.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                ids += s + ",";
            }
            String substring = ids.substring(0, ids.length() - 1);
            sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                    "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                    " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                    " AND account_book_id in " + "(" + substring + ")" + " AND delflag = 0;";
            cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        }


        if (cursor != null) {
            int count = cursor.getCount();
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }

    private ArrayList<indexBaseListBean> getDBDates(dayListBean.ResultBean bean) {
        List<dayListBean.ResultBean.ArraysBean> arrays = bean.getArrays();
        if (arrays == null) return null;
        //数据处理 抽取到同一个集合indexBeans中去
        ArrayList<indexBaseListBean> been = new ArrayList<>();
        for (int i = 0; i < arrays.size(); i++) {
            List<dayListBean.ResultBean.ArraysBean.DayArraysBean> dayArrays = arrays.get(i).getDayArrays();
            indexBaseListBean indexBeans = new indexBaseListBean();
            if (dayArrays != null && dayArrays.size() > 0) {
                ArrayList<indexBaseListBean.indexBean> iBeen = new ArrayList<>();
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, "");
                indexBaseListBean.indexBean xBean = new indexBaseListBean.indexBean();
                xBean.setDayIncome(arrays.get(i).getDayIncome());
                xBean.setDaySpend(arrays.get(i).getDaySpend());
                xBean.setDayTime(arrays.get(i).getDayTime());
                iBeen.add(xBean);
                indexBeans.setIndexBeen(iBeen);
                been.add(indexBeans);
            }
            for (int j = 0; j < dayArrays.size(); j++) {
                indexBaseListBean indexBeans2 = new indexBaseListBean();
                dayListBean.ResultBean.ArraysBean.DayArraysBean dates = dayArrays.get(j);
                indexBeans2.setTag(1);//1为数据条目 （自定义标签）
                indexBeans2.setOrderType(dates.getOrderType());
                indexBeans2.setIcon(dates.getIcon());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setIsStaged(dates.getIsStaged());
                indexBeans2.setRemark(dates.getRemark());
                if (null != dates.getSpendHappiness()) {
                    indexBeans2.setSpendHappiness(dates.getSpendHappiness());
                }
                indexBeans2.setMoney(dates.getMoney());
                indexBeans2.setTypePid(dates.getTypePid());
                indexBeans2.setTypeId(dates.getTypeId());
                indexBeans2.setId(dates.getId());
                indexBeans2.setAccountBookId(dates.getAccountBookId());
                indexBeans2.setChargeDate(dates.getChargeDate());
                indexBeans2.setCreateDate(dates.getCreateDate());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setReporterAvatar(dates.getReporterAvatar());
                indexBeans2.setReporterNickName(dates.getReporterNickName());
                been.add(indexBeans2);
            }
        }
        return been;
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

    private void alertDialog(final AccountDetailedBean data) {
        new DialogUtils(getActivity())
                .builder()
                .setTitle("温馨提示")
                .setMsg("确认删除吗？")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", view -> {
                    String id = data.getId();
                    //数据库的操作 （删除显示的是 数据库的更新）
                    Boolean b = DBUtil.updateOneDate(id, accountId);
                    if (b) {
                        //刷新界面数据
                        getIndexInfo();
                        //删除 并同步上传到服务器
                        pullToSyncDate();
                    }
                }).show();
    }

//    @Override
//    public void onRefresh() {
//        getIndexInfo();
//        pullToSyncDate();
//        onLoad();
//    }

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
                myList2.add(synDataBean);
            }
            req2.setSynData(myList2);
        }


        if (myList2 != null && myList2.size() > 0) {
            req2.setSynData(myList2);
        }
        String jsonInfo = new Gson().toJson(req2);
        setCurrentNets();
        NetWorkManager.getInstance().setContext(getActivity())
                .pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GloableReturn b1 = (GloableReturn) b;
                        //更新同步成功 调用预算接口
                        getHeadNetInfo();
                    }

                    @Override
                    public void onError(String s) {
                    }
                });
    }

    private void setCurrentNets() {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(getActivity());
        if (!netWorkAvailable) {
            //网络不可用  添加一下判断的原因看下面
            if (null != mNets) {
                mNets.setVisibility(View.VISIBLE);
            }
        } else {
            //莫名其妙这个地方会报空指针异常 ？ 只能加此判断先（也只能是mNets可能为空了）
            if (null != mNets) {
                mNets.setVisibility(View.GONE);
            }
        }
    }

//    @Override
//    public void onLoadMore() {
//
//    }

//    private void onLoad() {
//        String time = DateUtil.formatDate("HH:mm", new Date());
//        lv.setRefreshTime("今天:" + time);
//        lv.stopRefresh();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 131 && resultCode == 130) {
            mMoRen.setText(data.getStringExtra("TITLE"));
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, data.getStringExtra("TITLE"));
            zhangbenId = data.getStringExtra("ID");
            accountId = zhangbenId;
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, zhangbenId);
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, zhangbenId);
            typeBudget = data.getStringExtra("typeBudget");
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, data.getStringExtra("abTypeId"));
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, data.getStringExtra("typeBudget"));
        }
    }

//    public void remove(View view) {
//    }
//
//    public void add(View view) {
//    }

//    /**
//     * 动画加载水波纹进度
//     */
//    private int mysProgress;

//    public void setGrade(final WaterWaveProgress watercircleview) {
//        final int progress = watercircleview.getProgress();
//        mysProgress = 1;
//        final Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0x1223) {
//                    watercircleview.setProgress(mysProgress * (1));
//                } else if (msg.what == 0x1224) {
//                    watercircleview.setProgress(progress);
//                }
//            }
//        };
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                if (mysProgress < progress) {
//                    msg.what = 0x1223;
//                    mysProgress++;
//                } else {
//                    msg.what = 0x1224;
//                    this.cancel();
//                }
//                handler.sendMessage(msg);
//            }
//        }, 0, 50);
//    }

    private void setHasRed(String id, final int m) {
        //首页公告已读接口
        NetWorkManager.getInstance().setContext(getActivity())
                .putHasRead(id, token, new NetWorkManager.CallBack() {
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
            homeActivity activity;
            switch (connectionAddress) {
                case "mxsy":
                    Utils.playVoice(getActivity(), R.raw.changgui02);
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(0, 0);
                    break;
                case "tbtj":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 0);
                    break;
                case "tbfx":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 1);
                    break;
                case "tbzc":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 2);
                    break;
                case "jzsy":
                    Utils.playVoice(getActivity(), R.raw.changgui02);
                    startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                    break;
                case "lppsy":
                    activity = (homeActivity) getActivity();
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
}
