package com.hbird.base.mvc.net;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.bean.AccountBookBean;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.ChartToBarReq;
import com.hbird.base.mvc.bean.RequestBean.CheckVersionReq;
import com.hbird.base.mvc.bean.RequestBean.ClearMembersReq;
import com.hbird.base.mvc.bean.RequestBean.ClearReq;
import com.hbird.base.mvc.bean.RequestBean.LoginListReq;
import com.hbird.base.mvc.bean.RequestBean.MyAccount2TypeReq;
import com.hbird.base.mvc.bean.RequestBean.MyAccountTypeReq;
import com.hbird.base.mvc.bean.RequestBean.QiNiuReq;
import com.hbird.base.mvc.bean.RequestBean.SystemParamReq;
import com.hbird.base.mvc.bean.RequestBean.delZcTypeReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountCheckABReturn;
import com.hbird.base.mvc.bean.ReturnBean.AccountMemberBeans;
import com.hbird.base.mvc.bean.ReturnBean.AccountMembersBean;
import com.hbird.base.mvc.bean.ReturnBean.AccountTypes;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.BiaoQianReturn;
import com.hbird.base.mvc.bean.ReturnBean.ChangJingBean;
import com.hbird.base.mvc.bean.ReturnBean.CheckVersionReturn;
import com.hbird.base.mvc.bean.ReturnBean.CreatAccountReturn;
import com.hbird.base.mvc.bean.ReturnBean.FengMessageReturn;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.HeaderInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.LoginReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.QiNiuReturn;
import com.hbird.base.mvc.bean.ReturnBean.SaveMoney2Return;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturn;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturnNew;
import com.hbird.base.mvc.bean.ReturnBean.SingleReturn;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.bean.ReturnBean.UserFirendsReturn;
import com.hbird.base.mvc.bean.ReturnBean.WindowPopReturn;
import com.hbird.base.mvc.bean.ReturnBean.XiaoFeiBiLiReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanFinishReturn;
import com.hbird.base.mvc.bean.ReturnBean.YuSuanZbFinishReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturnNew;
import com.hbird.base.mvc.bean.ReturnBean.ZiChanInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToBarReturn;
import com.hbird.base.mvc.bean.ReturnBean.chartToRanking2Return;
import com.hbird.base.mvc.bean.ReturnBean.chartToRankingReturn;
import com.hbird.base.mvc.bean.ReturnBean.indexDatasReturn;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.util.JSONUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.checkInterCode;
import com.hbird.ui.login_register.ActLogin;
import com.ljy.devring.DevRing;
import com.ljy.devring.http.support.observer.CommonObserver;
import com.ljy.devring.http.support.throwable.HttpThrowable;
import com.ljy.devring.util.RxLifecycleUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import sing.common.util.LogUtil;


public class NetWorkManager {
    static NetWorkManager networkManager;
    Gson gson;
    private Context context;

    public NetWorkManager() {
        gson = new Gson();
    }

    public static NetWorkManager getInstance() {
        if (networkManager == null) {
            networkManager = new NetWorkManager();
        }
        return networkManager;
    }

    public NetWorkManager setContext(Context context) {
        this.context = context;
        return networkManager;
    }

    //登录接口（手机号登录）
    public void getLoginToken(String mobile, String password, final CallBack callBack) {
        //String jsonInfo = "{\"mobile\":\"" + mobile + "\", \"password\":\"" + password + "\"}";
        LoginListReq req = new LoginListReq();
        req.setMobile(mobile);
        req.setPassword(password);
        String jsonInfo = new JSONUtil<String, LoginListReq>().ObjectToJsonStr(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).login(body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        LoginReturn request = new Gson().fromJson(json, LoginReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                //尚不清楚 错误信息怎么提示 暂时提示错误
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    private void tokenSetting(String json) {
        if (checkInterCode.tokenInfos(json)) {
            //token过期操作
            DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN, "");
            DevRing.cacheManager().spCache(CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN_EXPIRE, "");
            //
            SPUtil.setPrefString(context, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
            SPUtil.setPrefString(context, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
            //
            //清空手势密码
            GestureUtil.clearGesturePassword();
            //关闭手势密码开关（必须重新打开设置）
            DevRing.cacheManager().spCache(CommonTag.SPCACH).
                    put(CommonTag.SHOUSHI_PASSWORD_OPENED, false);

            //杀掉所有Activity，返回打开登录界面
            DevRing.activityListManager().killAll();
            context.startActivity(new Intent(context, ActLogin.class));
        }
    }

    //主页记账流水获取集合
    public void getAccountInfo(int year, int month, String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getInfo(token, year, month);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        dayListBean request = new Gson().fromJson(json, dayListBean.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取单笔详情页数据
    public void getAccountInfo(String id, String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getSingleDateInfo(token, id);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        SingleReturn request = new Gson().fromJson(json, SingleReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //删除单笔订单详情（明细详情删除）
    public void clearDingDanInfo(String id, String token, final CallBack callBack) {
        ClearReq clearReq = new ClearReq();
        clearReq.setId(id);
        String jsonInfo = new JSONUtil<String, ClearReq>().ObjectToJsonStr(clearReq);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).clearInfo(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //编辑单笔订单详情（明细详情编辑）
    public void editorDingDanInfo(String jsonInfo, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).editorInfo(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取支出类目标签
    public void getZhiChuTag(String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getZhiChuAccountTag(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ZhiChuTagReturn request = new Gson().fromJson(json, ZhiChuTagReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取支出类目标签 新的
    public void getZhiChuTag(int abTypeId, String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getIncomeLabel(abTypeId, token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        json = json.replaceAll("\"id\"", "\"serviceIds\"");
                        ZhiChuTagReturn request = new Gson().fromJson(json, ZhiChuTagReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取收入类目标签
    public void getShouRuTag(String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getShouRuAccountTag(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ShouRuTagReturn request = new Gson().fromJson(json, ShouRuTagReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //记账接口
    public void postJZ(String jsonInfo, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postAccountjz(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //用户常用支出类目
    public void postZhiChuType(String id, int abTypeId, String token, final CallBack callBack) {
        MyAccountTypeReq req = new MyAccountTypeReq();
        req.setAbTypeId(abTypeId + "");
        req.setLabelId(id);
        String jsonInfo = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postAddSpendLabel(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        BiaoQianReturn request = new Gson().fromJson(json, BiaoQianReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //用户常用收入类目
    public void postShouRuType(String id, int abTypeId, String token, final CallBack callBack) {
        MyAccount2TypeReq req = new MyAccount2TypeReq();
        //req.setIncomeTypeId(id);
        req.setLabelId(id);//
        req.setAbTypeId(abTypeId + "");
        String jsonInfo = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postAddIncomeLabel(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        BiaoQianReturn request = new Gson().fromJson(json, BiaoQianReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //删除用户常用支出类目
    public void delZhiChuType(String id, String token, final CallBack callBack) {

        delZcTypeReq clearReq = new delZcTypeReq();
        //clearReq.setSpendTypeIds(id);
        String bookIdType = SPUtil.getPrefString(context, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "0");
        int types = Integer.valueOf(bookIdType);
        clearReq.setAbTypeId(types);
        ArrayList<Integer> lable = new ArrayList<>();
        lable.add(Integer.valueOf(id));
        clearReq.setLabelIds(lable);
        String jsonInfo = new Gson().toJson(clearReq);
        /*JsonArray list = new JsonArray();
        list.add(id);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("spendTypeIds",list);*/
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).delZhiChuTypeLabel(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        BiaoQianReturn request = new Gson().fromJson(json, BiaoQianReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //删除用户常用支出类目  新的
    public void delZhiChuType(int abTypeId, String id, String token, final CallBack callBack) {
        delZcTypeReq clearReq = new delZcTypeReq();
        clearReq.setAbTypeId(abTypeId);
        ArrayList<Integer> lable = new ArrayList<>();
        lable.add(Integer.valueOf(id));
        clearReq.setLabelIds(lable);
        String jsonInfo = new Gson().toJson(clearReq);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).delZhiChuTypeLabel(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        BiaoQianReturn request = new Gson().fromJson(json, BiaoQianReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //删除用户常用收入类目
    public void delShouRuType(String id, int accountBookId, String token, final CallBack callBack) {
        delZcTypeReq clearReq = new delZcTypeReq();
        clearReq.setAbTypeId(accountBookId);
        ArrayList<Integer> lable = new ArrayList<>();
        lable.add(Integer.valueOf(id));
        clearReq.setLabelIds(lable);
        String jsonInfo = new Gson().toJson(clearReq);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).deldeleteIncomeLabel(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        BiaoQianReturn request = new Gson().fromJson(json, BiaoQianReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改用户常用类目排序关系
    public void postChangeType(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postUploadLabelPriority(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountTypes request = new Gson().fromJson(json, AccountTypes.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取我的页面头部信息
    public void getHeaderInfo(String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getHeaderDate(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        HeaderInfoReturn request = new Gson().fromJson(json, HeaderInfoReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //用户反馈
    public void postSuggest(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postSuggest(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取七牛云的token
    public void postQiNiuToken(String token, final CallBack callBack) {
        QiNiuReq req = new QiNiuReq();
        req.setFlag(1);
        String json = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postQiNiuToken(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        QiNiuReturn request = new Gson().fromJson(json, QiNiuReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //个人信息管理
    public void setPersionalInfos(String jsonInfo, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).putPersionnalInfo(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取个人信息详情
    public void getPersionalInfos(String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getPersionalInfo(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GeRenInfoReturn request = new Gson().fromJson(json, GeRenInfoReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //版本更新
    public void getCheckVersion(String token, String version, final CallBack callBack) {
        CheckVersionReq req = new CheckVersionReq();
        req.setVersion(version);
        String json = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postCheckVersion(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        CheckVersionReturn request = new Gson().fromJson(json, CheckVersionReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //图表 - 统计界面 - 支出柱状图统计接口
    public void postChartToZcBar(String token, String flag, String beginTime, String endTime, String beginWeek, String endWeek,
                                 final CallBack callBack) {
        ChartToBarReq req = new ChartToBarReq();
        req.setFlag(flag);
        if (flag.equals("1")) {
            req.setBeginTime(beginTime);
            req.setEndTime(endTime);
        } else if (flag.equals("2")) {
            req.setBeginWeek(beginWeek);
            req.setEndWeek(endWeek);
        }

        String json = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postChartTozcBar(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        chartToBarReturn request = new Gson().fromJson(json, chartToBarReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //图表 - 统计界面 - 支出排行消费统计接口
    public void postChartToRanking(String token, String flag, String dayTime, String time, String times,
                                   final CallBack callBack) {
        ChartToBarReq req = new ChartToBarReq();
        req.setFlag(flag);
        if (flag.equals("1")) {
            req.setDayTime(dayTime);
        } else if (flag.equals("2")) {
            //周27
            req.setTime(time);
        } else {
            //月 06
            req.setTime(times);
        }

        String json = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postChartToRanking(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        chartToRankingReturn request = new Gson().fromJson(json, chartToRankingReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //图表 - 统计界面 - 收入柱状图统计接口
    public void postChartToComeBar(String token, String flag, String beginTime, String endTime, String beginWeek, String endWeek,
                                   final CallBack callBack) {
        ChartToBarReq req = new ChartToBarReq();
        req.setFlag(flag);
        if (flag.equals("1")) {
            req.setBeginTime(beginTime);
            req.setEndTime(endTime);
        } else if (flag.equals("2")) {
            req.setBeginWeek(beginWeek);
            req.setEndWeek(endWeek);
        }


        String json = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postChartToComeBar(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        chartToBarReturn request = new Gson().fromJson(json, chartToBarReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //图表 - 统计界面 - 收入排行消费统计接口
    public void postChartToComeRanking(String token, String flag, String dayTime, String time, String times,
                                       final CallBack callBack) {
        ChartToBarReq req = new ChartToBarReq();
        req.setFlag(flag);
        if (flag.equals("1")) {
            req.setDayTime(dayTime);
        } else if (flag.equals("2")) {
            //周27
            req.setTime(time);
        } else {
            //月 06
            req.setTime(times);
        }

        String json = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postChartToComeRanking(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        chartToRanking2Return request = new Gson().fromJson(json, chartToRanking2Return.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改登录密码
    public void postToLoginModifi(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postToLoginModifi(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改手机号  （获取旧手机号验证码）
    public void postGetPhonesYzm(String mobile, String token, final CallBack callBack) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postGetOldPhones(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //绑定修改手机号
    public void postModifiPhoneNumber(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postModifiPhoneNum(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改手机号 验证旧手机号
    public void postCheckOldPhoneNumber(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postCheckOldPhoneNum(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改手机号  （获取新手机号验证码）
    public void postGetPhonesNewYzm(String mobile, String token, final CallBack callBack) {
        String jsonInfo = "{\"mobile\":\"" + mobile + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postGetNewPhones(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //解绑微信
    public void unBindWeiXin(String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).putUnbindWeChat(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //绑定微信
    public void bindWeiXin(String code, String token, final CallBack callBack) {
        String jsonInfo = "{\"code\":\"" + code + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).putBindWeChat(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取存钱效率 分接口
    public void getSaveEfficients(int month, int rang, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getSaveEfficientMoney(token, rang, month);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        SaveMoney2Return request = new Gson().fromJson(json, SaveMoney2Return.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //更新/设置-->预算/固定支出
    public void postBudgets(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postSetbudget(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改/设置预算接口
    public void postBudgetsV2(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postSetbudgetv2(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取消费结构比的接口
    public void getXiaoFeiBiLi(String month, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getXiaoFeiBi(token, month);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        XiaoFeiBiLiReturn request = new Gson().fromJson(json, XiaoFeiBiLiReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取预算完成率
    public void getYuSuanFinish(String month, String range, String abId, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getYSFinish(token, month, range, abId);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        try {// 日常账本
                            YuSuanFinishReturn request = new Gson().fromJson(json, YuSuanFinishReturn.class);
                            callBack.onSuccess(request);
                        } catch (Exception e) {// 场景账本
                            YuSuanZbFinishReturn bean = new Gson().fromJson(json, YuSuanZbFinishReturn.class);
                            callBack.onSuccess(bean);
                        }
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取 首页获取预算/记账天数 getIndexData
    public void getIndexDatas(String time, String abId, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getIndexData(token, time, abId);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        indexDatasReturn request = new Gson().fromJson(json, indexDatasReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //pull下拉同步服务器段数据
    public void postPullToSyncDate(String mobileDevice, boolean isFirst, String token, final CallBack callBack) {
        String jsonInfo = "{\"mobileDevice\":\"" + mobileDevice + "\",\"isFirst\":\"" + isFirst + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postSyncDate(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        PullSyncDateReturn request = new Gson().fromJson(json, PullSyncDateReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //启动页类目更新检查/获取同步间隔
    public void postCheckChargeType(String version, String token, final CallBack callBack) {
        SystemParamReq req = new SystemParamReq();
        req.setLabelVersion(version);
        String jsonInfo = new Gson().toJson(req);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postCheckChargeTypes(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        SystemBiaoqReturn request = new Gson().fromJson(json, SystemBiaoqReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //push上传同步 （本地数据上传到服务器）
    public void pushOffLineToFwq(String json, String token, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).pushOffLines(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //历史邀请记录
    public void getAllHistoryUser(int curpage, int pagesize, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getHistoryUsers(token, curpage, pagesize);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        UserFirendsReturn request = new Gson().fromJson(json, UserFirendsReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //首页弹框
    public void getHomeWindows(String version, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getHomeWindow(token, version);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        WindowPopReturn request = new Gson().fromJson(json, WindowPopReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改初始时间接口
    public void putInitDates(long initDate, String token, final CallBack callBack) {
        String jsonInfo = "{\"initDate\":\"" + initDate + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).putInitDate(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //设置/修改资产
    public void postZiChan(int assetsType, double money, String token, final CallBack callBack) {
        String jsonInfo = "{\"assetsType\":\"" + assetsType + "\",\"money\":\"" + money + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postSaveOrUpdateAssets(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //设置/修改资产
    public void saveOrUpdateAssets(int assetsType, double money, String assetsName,String token, final CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("assetsType",assetsType);
        obj.put("money",money);
        obj.put("assetsName",assetsName);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postSaveOrUpdateAssets(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取资产页数据(净资产/初始时间/资产列表 flag为1查询用户默认账户,为null为资产
    public void getZiChanInfo(String token, Object flag, final CallBack callBack) {
        Observable<ResponseBody> observable;
        if (flag == null) {
            observable = DevRing.httpManager().getService(ApiService.class).getAssets(token);
        } else {
            observable = DevRing.httpManager().getService(ApiService.class).getAssets1(token, (Integer) flag);
        }
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ZiChanInfoReturn request = new Gson().fromJson(json, ZiChanInfoReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //首页公告已读接口
    public void putHasRead(String activityId, String token, final CallBack callBack) {
        String jsonInfo = "{\"activityId\":\"" + activityId + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).putHasRead(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取我的账本接口 切换账本页请求
    public void getMyAccounts(String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getAllzb(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountZbBean request = new Gson().fromJson(json, AccountZbBean.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取(日常/场景)账本预算/固定大额支出接口
    public void getbudgetAccounts(String token, String id, String time, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getbudgetv2(token, id, time);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ChangJingBean request = new Gson().fromJson(json, ChangJingBean.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取账本类型接口
    public void getAccountsType(String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getABTypeAll(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountBookBean request = JSON.parseObject(json, AccountBookBean.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取账本内组员情况 首页展示
    public void getAccountMember(String token, String abId, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getABMembers(token, abId);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountMembersBean request = new Gson().fromJson(json, AccountMembersBean.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取成员详情 --->成员管理页数据
    public void getMemberInfo(String token, String abId, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getMembersInfo(token, abId);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountMemberBeans request = new Gson().fromJson(json, AccountMemberBeans.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //删除成员接口
    public void clearMemberInfo(String abId, List<String> memberIds, String token, final CallBack callBack) {
        ClearMembersReq clearReq = new ClearMembersReq();
        clearReq.setAbId(abId);
        clearReq.setMemberIds(memberIds);
        String jsonInfo = new JSONUtil<String, ClearMembersReq>().ObjectToJsonStr(clearReq);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).deleteMembers(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //修改账本接口
    public void putModifiAccount(String abName, String abId, String token, final CallBack callBack) {
        String jsonInfo = "{\"abName\":\"" + abName + "\",\"abId\":\"" + abId + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).updateAB(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //删除账本接口
    public void delAccounts(String abId, String token, final CallBack callBack) {
        String jsonInfo = "{\"abId\":\"" + abId + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).deleteAB(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取账本成员数对应关系
    public void getCheckABMyAccounts(String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).checkABMembers(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountCheckABReturn request = new Gson().fromJson(json, AccountCheckABReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //创建账本接口
    public void postCreatAccount(String abName, String accountBookTypeId, String token, final CallBack callBack) {
        String jsonInfo = "{\"abName\":\"" + abName + "\",\"accountBookTypeId\":\"" + accountBookTypeId + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postCreateAB(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        CreatAccountReturn request = new Gson().fromJson(json, CreatAccountReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    // 获取丰丰通知
    public void getFengMessage(String userId, String page, String rows, String token, final CallBack callBack) {

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getMessageList(token, userId, page, rows);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        FengMessageReturn request = new Gson().fromJson(json, FengMessageReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    // 获取丰丰通知
    public void postUpDateStaus(String type, String userinfoId, String messageId, String token, final CallBack callBack) {
        String jsonInfo = "{\"messageUpdateType\":\"" + type + "\",\"userinfoId\":\"" + userinfoId + "\",\"messageId\":\"" + messageId + "\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).postUpdateMessageStatus(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        GloableReturn request = new Gson().fromJson(json, GloableReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取记账标签接口---->标签增删改查前调用( 获取支出类型标签)
    public void getSpendLabels(int abTypeId, String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getSpendLabel(token, abTypeId);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ZhiChuTagReturn request = new Gson().fromJson(json, ZhiChuTagReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取记账标签接口---->标签增删改查前调用( 获取支出类型标签)
    public void getSpendLabelsNew(int abTypeId, String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getSpendLabel(token, abTypeId);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ZhiChuTagReturnNew request = new Gson().fromJson(json, ZhiChuTagReturnNew.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取记账标签接口---->标签增删改查前调用( 获取收入类型标签)
    public void getIncomeLabels(int abTypeId, String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getIncomeLabel(abTypeId, token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ShouRuTagReturn request = new Gson().fromJson(json, ShouRuTagReturn.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    //获取记账标签接口---->标签增删改查前调用( 获取收入类型标签)
    public void getIncomeLabelsNew(int abTypeId, String token, final CallBack callBack) {
        LogUtil.e("abTypeId = " + abTypeId);
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getIncomeLabel(abTypeId, token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        ShouRuTagReturnNew request = new Gson().fromJson(json, ShouRuTagReturnNew.class);
                        callBack.onSuccess(request);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    // 获取用户已拥有的账本类型--->类目设置调用
    public void getHadABType(String token, final CallBack callBack) {
        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).getHadABType(token);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        AccountBookBean bean = JSON.parseObject(json, AccountBookBean.class);
                        callBack.onSuccess(bean);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }


    // 上传code
    public void uploadCode(String token, String code, final CallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"code\":\"" + code + "\"}");

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).uploadCode(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        callBack.onSuccess(null);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                String message = httpThrowable.message;
                LogUtil.e(message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }

    // 添加默认记账账户接口
    public void addAT2Mark(String token, int id, final CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("ats",new int[]{id});

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).addAT2Mark(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        callBack.onSuccess(null);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                LogUtil.e(httpThrowable.message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }


    // 删除默认记账账户接口
    public void deleteAT2Mark(String token, Integer[] id, final CallBack callBack) {
        JSONObject obj = new JSONObject();
        obj.put("ats",id);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).deleteAT2Mark(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        callBack.onSuccess(null);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                LogUtil.e(httpThrowable.message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }
    // 提交邀请码
    public void bindInvite(String token, String inviteCode, final CallBack callBack) {
        String jsonInfo = "{\"inviteCode\":\""+inviteCode+"\"}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInfo);

        Observable<ResponseBody> observable = DevRing.httpManager().getService(ApiService.class).bindInvite(token, body);
        DevRing.httpManager().commonRequest(observable, new CommonObserver<ResponseBody>() {
            @Override
            public void onResult(ResponseBody result) {
                try {
                    String json = result.string();
                    LogUtil.e(json);
                    if (checkInterCode.isSuccess(json)) {
                        BaseReturn bean = JSON.parseObject(json, BaseReturn.class);
                        callBack.onSuccess(bean);
                    } else {
                        callBack.onError(checkInterCode.MSG);
                        tokenSetting(json);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError("解析错误");
                }
            }

            @Override
            public void onError(HttpThrowable httpThrowable) {
                LogUtil.e(httpThrowable.message);
                callBack.onError("失败");
            }
        }, RxLifecycleUtil.bindUntilEvent(context, ActivityEvent.DESTROY));
    }
    public interface CallBack {
        void onSuccess(BaseReturn b);
        void onError(String s);
    }
}
