package com.hbird.ui.detailed;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.databinding.ActAccountDetailedBinding;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.DialogToGig;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.widget.MyTimerPop;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AccountDetailedBean;
import com.hbird.bean.TitleBean;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.NetworkUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sing.common.base.BaseActivity;
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
            alertDialog(data.getId());
        }
    }

    private void alertDialog(String id) {
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
                        Boolean b = DBUtil.updateOneDate(id, accountId);
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


    private String getNumToNumber(Double d) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(d);
    }

    private void getData() {
        //查询指定月份记录
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
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
                            ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " where  ACCOUNT_BOOK_ID in " + "(" + substring + ")" + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
                }
            }
        } else {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
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

                    list.clear();
                    for (int i = 0; i < dates.size(); i++) {
                        AccountDetailedBean temp = new AccountDetailedBean();
                        temp.setBean(dates.get(i));
                        list.add(temp);
                    }
//                    Collections.sort(list);
                    adapter.notifyDataSetChanged();
                } else {
                    data.setInComeMoney("0.00");
                    data.setSpendingMoney("0.00");
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            data.setInComeMoney("0.00");
            data.setSpendingMoney("0.00");
            list.clear();
            adapter.notifyDataSetChanged();
        }
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
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
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
        String MonthFirstDay = DateUtil.getMonthday2First(data.getYyyy(), data.getMm());
        String MonthLastDay = DateUtil.getMonthday2Last(data.getYyyy(), data.getMm());
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Set<String> sets = new LinkedHashSet<>();
        Set<String> prefSet = SPUtil.getPrefSet(ActAccountDetailed.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, sets);
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
}
