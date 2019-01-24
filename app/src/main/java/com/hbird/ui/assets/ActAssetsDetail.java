package com.hbird.ui.assets;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.ActAssetsDetailBinding;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.OffLine2Req;
import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.widget.MyTimerPop;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;
import com.hbird.common.calendar.utils.CalendarUtil;
import com.hbird.ui.detailed.AccountDetailedAdapter;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.NetworkUtil;

import java.math.BigDecimal;
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

import sing.common.base.BaseActivity;
import sing.common.base.BaseViewModel;
import sing.common.util.StatusBarUtil;
import sing.util.ScreenUtil;
import sing.util.ToastUtil;

import static com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN;
import static java.lang.Integer.parseInt;

/**
 * @author: LiangYX
 * @ClassName: ActAssetsDetail
 * @date: 2019/1/2 10:51
 * @Description: 资产详情
 */
public class ActAssetsDetail extends BaseActivity<ActAssetsDetailBinding, BaseViewModel> implements IBaseActivity {

    private String token;
    private String accountId;

    private AssetsDetailData data;// 页面的值
    private AssetsBean bean;// 传过来的值
    private ArrayList<String> dataY = new ArrayList<>();
    private ArrayList<String> dataM = new ArrayList<>();

    private AccountDetailedAdapter adapter;
    private List<AccountDetailedBean> list = new ArrayList<>();

    private int[] currentDate = CalendarUtil.getCurrentDate();
    private int width_15;
    private int maxWidth = -1;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_assets_detail;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        accountId = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
        currentDate = CalendarUtil.getCurrentDate();
        width_15 = getResources().getDimensionPixelSize(R.dimen.dp_15_x);
        maxWidth = ScreenUtil.getScreenWidth(this) - width_15 * 6 - width_15 * 3 - width_15 / 15;

        findViewById(R.id.id_back).setOnClickListener(v -> onBackPressed());
        FrameLayout toolBar = findViewById(R.id.toolbar);
        toolBar.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_main_bg));
        ((TextView) findViewById(R.id.id_title)).setText("资产详情");
        ViewGroup.LayoutParams params = toolBar.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(this);
        toolBar.setLayoutParams(params);
        toolBar.setPadding(0, StatusBarUtil.getStateBarHeight(this), 0, 0);

        bean = (AssetsBean) getIntent().getExtras().getSerializable(Constants.START_INTENT_A);
        if (bean == null) {
            ToastUtil.showShort("数据传值遗异常");
            finish();
            return;
        }

        data = new AssetsDetailData();
        binding.setData(data);
        binding.setListener(new OnClick());

        setInitData();

        data.setIcon(bean.icon);
        data.setAssetsName(bean.assetsName);
        data.setMoney(bean.money);
        data.setAssetsType(bean.assetsType);
        data.setYyyy(currentDate[0]);
        data.setMm(currentDate[1]);
        data.setInComeMoney("0.00");
        data.setSpendingMoney("0.00");

        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");

        adapter = new AccountDetailedAdapter(this, list, R.layout.row_account_detailed, (position, data, type) -> onItemClick((AccountDetailedBean) data, type));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setNestedScrollingEnabled(false);//禁止rcyc嵌套滑动

        getData(currentDate);
    }

    // 点击记账的条目
    private void onItemClick(AccountDetailedBean data, int type) {
        if (type == 0) {
            Utils.playVoice(this, R.raw.changgui02);
            Intent intent = new Intent(this, MingXiInfoActivity.class);
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


    private void alertDialog( WaterOrderCollect bean) {
        new DialogUtils(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("确认删除吗？")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", view -> {
                    //数据库的操作 （删除显示的是 数据库的更新）
                    Boolean b = DBUtil.updateOneDate(bean);
                    if (b) {
                        //删除 并同步上传到服务器
                        pullToSyncDate();
                    }
                }).show();
    }


    // 获取数据
    private void getData(int[] day) {
        String MonthFirstDay = DateUtil.getMonthday2First(day[0], day[1]);
        String MonthLastDay = DateUtil.getMonthday2Last(day[0], day[1]);

        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";
        String sql = "";
        if (TextUtils.isEmpty(accountId)) {
            Set<String> set = new LinkedHashSet<>();
            Set<String> prefSet = SPUtil.getPrefSet(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
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
                            ", remark, USER_PRIVATE_LABEL_ID, ASSETS_NAME,REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " where  ACCOUNT_BOOK_ID in " + "(" + substring + ")" + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " and ASSETS_ID=" + data.getAssetsType() + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
                }
            }
        } else {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, ASSETS_NAME,REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                    " where  ACCOUNT_BOOK_ID=" + accountId + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " and ASSETS_ID=" + data.getAssetsType() + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";

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
                ArrayList<indexBaseListBean> dates = getDBDates(bean);
                if (dates != null && dates.size() > 0) {
                    double monthIncome = bean.getMonthIncome();
                    double monthSpend = bean.getMonthSpend();
                    String monthIncomes = getNumToNumber(monthIncome);
                    String monthSpends = getNumToNumber(monthSpend);
                    data.setInComeMoney(monthIncomes);
                    data.setSpendingMoney(monthSpends);
                    // 设置流入、流出长度
                    if (monthIncome > monthSpend) {// 收入大于支出
                        if (monthSpend == 0) { //
                            setLength(width_15, monthIncome == 0 ? width_15 : maxWidth);
                        } else {
                            double scale = monthIncome / monthSpend;
                            if (scale > 10) {
                                setLength(width_15, maxWidth);
                            } else {
                                setLength(width_15 * 2, (int) (width_15 * 2 * scale));
                            }
                        }
                    } else { // 支出大于等于收入
                        if (monthIncome == 0) { //
                            setLength(monthSpend == 0 ? width_15 : maxWidth, width_15);
                        } else {
                            double scale = monthSpend / monthIncome;
                            if (scale > 10) {
                                setLength(maxWidth, width_15);
                            } else {
                                setLength((int) (width_15 * 2 * scale), width_15 * 2);
                            }
                        }
                    }

                    list.clear();
                    for (int i = 0; i < dates.size(); i++) {
                        AccountDetailedBean temp = new AccountDetailedBean();
                        temp.setBean(dates.get(i));
                        list.add(temp);
                    }
//                    Collections.sort(list);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.ivNoData.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.ivNoData.setVisibility(View.VISIBLE);
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.ivNoData.setVisibility(View.VISIBLE);
            list.clear();
            adapter.notifyDataSetChanged();

            data.setInComeMoney("0.00");
            data.setSpendingMoney("0.00");

            setLength(width_15, width_15);
        }
    }

    private String getNumToNumber(Double d) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(d);
    }

    // 设置流入流出的进度条
    private void setLength(int spendWidth, int incomeWidth) {
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) binding.vSpend.getLayoutParams();
        params1.width = spendWidth;
        binding.vSpend.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) binding.vIncome.getLayoutParams();
        params2.width = incomeWidth;
        binding.vIncome.setLayoutParams(params2);
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
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, 0,"");
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

    public Map<String, BigDecimal> getAccount(int accountBookId) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                " AND account_book_id = '" + accountBookId + "' and ASSETS_ID=" + data.getAssetsType() + " AND delflag = 0;";
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
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Set<String> sets = new LinkedHashSet<>();
        Set<String> prefSet = SPUtil.getPrefSet(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, sets);
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
                    " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" + " and ASSETS_ID=" + data.getAssetsType() +
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

    @Override
    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public boolean isUseFragment() {
        return false;
    }

    public class OnClick {
        // 记账
        public void add(View view) {
            Utils.playVoice(ActAssetsDetail.this, R.raw.jizhang);
            Intent intent = new Intent(ActAssetsDetail.this, ChooseAccountTypeActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivityForResult(intent, 1001, ActivityOptions.makeSceneTransitionAnimation(ActAssetsDetail.this).toBundle());
            } else {
                startActivityForResult(intent, 1001);
            }
        }

        // 編輯
        public void edit(View view) {
            if (bean != null) {
                Intent intent = new Intent(ActAssetsDetail.this, ActEditAccountValue.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.START_INTENT_A, bean);
                intent.putExtras(b);
                startActivityForResult(intent, 1000);
            }
        }

        // 选择日期
        public void chooseDate(View view) {
            Utils.playVoice(ActAssetsDetail.this, R.raw.changgui02);
            new MyTimerPop(ActAssetsDetail.this, view, dataY, dataM, data.getYyyy() - 2010, data.getMm() - 1, new MyTimerPop.OnDateListener() {
                @Override
                public void getYearList(String s) {
                    data.setYyyy(parseInt(s));
                    currentDate[0] = parseInt(s);
                }

                @Override
                public void getMonthList(String s) {
                    String[] ss = s.split("月份");
                    data.setMm(parseInt(ss[0]));
                    currentDate[1] = parseInt(ss[0]);
                }
            }, new MyTimerPop.PopDismissListener() {
                @Override
                public void PopDismiss() {
                    getData(currentDate);
                }
            }).showPopWindow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {// 修改编辑回来的
            AssetsBean temp = (AssetsBean) data.getExtras().getSerializable(Constants.START_INTENT_A);
            bean = temp;
            this.data.setMoney(bean.money);
            this.data.setAssetsName(bean.assetsName);
        } else if (requestCode == 1001) {
            getData(currentDate);
        }
    }

    private String deviceId;// 设备唯一标识

    private void pullToSyncDate() {
        deviceId = Utils.getDeviceInfo(this);
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(this);
        if (!netWorkAvailable) {
            return;
        }
//        showGifProgress("");
        NetWorkManager.getInstance().setContext(this).postPullToSyncDate(deviceId, false, token, new NetWorkManager.CallBack() {
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
                SPUtil.setPrefLong(ActAssetsDetail.this, com.hbird.base.app.constant.CommonTag.SYNDATE, L);
                List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                //插入本地数据库
                DBUtil.insertLocalDB(synData);
                SPUtil.setPrefBoolean(ActAssetsDetail.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);
//                hideGifProgress();

                pushOffLine();
                getData(currentDate);

                SPUtil.setPrefBoolean(ActAssetsDetail.this, OFFLINEPULL_FIRST_LOGIN, false);
            }

            @Override
            public void onError(String s) {
//                hideGifProgress();
                getData(currentDate);// 将本地新插入的更新到UI
                ToastUtil.showShort(s);
            }
        });
    }


    private void pushOffLine() {
        OffLineReq req = new OffLineReq();
        req.setMobileDevice(deviceId);
        Long time = SPUtil.getPrefLong(this, com.hbird.base.app.constant.CommonTag.SYNDATE, new Date().getTime());
        String times = String.valueOf(time);
        req.setSynDate(times);

        //本地数据库查找未上传数据 上传至服务器
        String sql = "SELECT * FROM WATER_ORDER_COLLECT wo where wo.ACCOUNT_BOOK_ID = " + accountId + " AND wo.UPDATE_DATE >= " + time;
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
        NetWorkManager.getInstance().setContext(this).pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {

            }

            @Override
            public void onError(String s) {

            }
        });
    }
}