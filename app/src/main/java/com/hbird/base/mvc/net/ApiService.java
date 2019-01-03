package com.hbird.base.mvc.net;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Liul on 2018/7/7.
 */

public interface ApiService {

    /**
     * 登录
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("login/android")
    Observable<ResponseBody> login(@Body RequestBody body);

    /**
     * 主页记账流水获取集合
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("warterOrderList/android")
    Observable<ResponseBody> getInfo(@Header("X-AUTH-TOKEN") String token,@Query("year") int year,@Query("month") int month);

    /**
     * 主页记账流水- 明细详情删除操作
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteOrder/", hasBody = true)
    Observable<ResponseBody> clearInfo(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     * 主页记账流水- 明细详情编辑操作
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("updateOrderInfo/android")
    Observable<ResponseBody> editorInfo(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);


    /**
     * 获取单笔详情页数据
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getOrderInfo/android")
    Observable<ResponseBody> getSingleDateInfo(@Header("X-AUTH-TOKEN") String token,@Query("id") String id);

    /**
     * 获取支出类目标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getSpendTypeList/android")
    Observable<ResponseBody> getZhiChuAccountTag(@Header("X-AUTH-TOKEN") String token);

    /**
     * 获取收入类目标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getIncomeTypeList/android")
    Observable<ResponseBody> getShouRuAccountTag(@Header("X-AUTH-TOKEN") String token);

    /**
     * 记账
     * @param jzbody
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("toCharge/android")
    Observable<ResponseBody> postAccountjz(@Header("X-AUTH-TOKEN") String token, @Body RequestBody jzbody);

    /**
     *添加用户常用支出类目
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("addCommSpendType/")
    Observable<ResponseBody> postZhiChuType(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     *添加用户常用支出类目 添加常用标签接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("addSpendLabel/")
    Observable<ResponseBody> postAddSpendLabel(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     *添加用户常用收入类目
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("addCommIncomeType/")
    Observable<ResponseBody> postShouRuType(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     *添加常用标签接口 添加收入标签
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("addIncomeLabel/")
    Observable<ResponseBody> postAddIncomeLabel(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 删除用户常用支出类目
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteCommSpendType/", hasBody = true)
    Observable<ResponseBody> delZhiChuType(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);
    /**
     * 删除常用标签接口 删除支出标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteSpendLabel/", hasBody = true)
    Observable<ResponseBody> delZhiChuTypeLabel(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);
    /**
     * 删除用户常用收入类目
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteCommIncomeType/", hasBody = true)
    Observable<ResponseBody> delShouRuType(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     * 删除用户常用收入类目
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteIncomeLabel/", hasBody = true)
    Observable<ResponseBody> deldeleteIncomeLabel(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     *修改用户常用类目排序关系
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("uploadUserTypePriority/android")
    Observable<ResponseBody> postChangeType(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     *标签排序关系上传接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("uploadLabelPriority/android")
    Observable<ResponseBody> postUploadLabelPriority(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 获取页面头部数据
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getMyCount/android")
    Observable<ResponseBody> getHeaderDate(@Header("X-AUTH-TOKEN") String token);
    /**
     *用户建议
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("uploadFeedBack/android")
    Observable<ResponseBody> postSuggest(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     *获取七牛云的鉴权凭证
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("getQiNiuAuth/android")
    Observable<ResponseBody> postQiNiuToken(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 个人信息管理
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("updateUserInfo/android")
    Observable<ResponseBody> putPersionnalInfo(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     * 获取个人信息详情
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("userInfo/android")
    Observable<ResponseBody> getPersionalInfo(@Header("X-AUTH-TOKEN") String token);


    /**
     *版本更新
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("appCheck/android")
    Observable<ResponseBody> postCheckVersion(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *图表 - 统计界面 - 支出柱状图统计接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("statisticsForSpend/android")
    Observable<ResponseBody> postChartTozcBar(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *图表 - 统计界面 - 支出排行消费统计接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("statisticsForSpendTopAndHappiness/android")
    Observable<ResponseBody> postChartToRanking(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *图表 - 统计界面 - 收入柱状图统计接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("statisticsForIncome/android")
    Observable<ResponseBody> postChartToComeBar(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *图表 - 统计界面 - 收入排行消费统计接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("statisticsForIncomeTop/android")
    Observable<ResponseBody> postChartToComeRanking(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *修改登录密码
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("updatepwd/android")
    Observable<ResponseBody> postToLoginModifi(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *修改手机号  （获取旧手机号验证码）
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("verifycodeToChange/android")
    Observable<ResponseBody> postGetOldPhones(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     *绑定修改手机号
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("bindMobile/android")
    Observable<ResponseBody> postModifiPhoneNum(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *修改手机号-- 验证旧手机号接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("checkOldMobile/android")
    Observable<ResponseBody> postCheckOldPhoneNum(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     *修改手机号  （获取新手机号验证码）
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("verifycodeToBind/android")
    Observable<ResponseBody> postGetNewPhones(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 解绑微信
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("unbindWeChat/android")
    Observable<ResponseBody> putUnbindWeChat(@Header("X-AUTH-TOKEN") String token);

    /**
     * 个人信息管理
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("bindWeChat/android")
    Observable<ResponseBody> putBindWeChat(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);
    /**
     * 分接口-获取存钱效率接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getsavingefficiencyv2/android")
    Observable<ResponseBody> getSaveEfficientMoney(@Header("X-AUTH-TOKEN") String token,@Query("range") int range,@Query("month") int month);

    //更新/设置-->预算/固定支出
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("setFixedSpend/android")
    Observable<ResponseBody> postSetbudget(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    //更新/设置-->预算/固定支出
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("setbudgetv2/android")
    Observable<ResponseBody> postSetbudgetv2(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 分接口-获取消费结构比
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getconsumptionstructureratiov2/android")
    Observable<ResponseBody> getXiaoFeiBi(@Header("X-AUTH-TOKEN") String token,@Query("month") String month);

    /**
     * 分接口-获取预算完成率
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getbudgetcompletionratev2/android")
    Observable<ResponseBody> getYSFinish(@Header("X-AUTH-TOKEN") String token,@Query("month") String month,@Query("range") String range,@Query("abId") String abId);

    /**
     * 分接口-首页获取预算/记账天数
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getindexdatav2/android")
    Observable<ResponseBody> getIndexData(@Header("X-AUTH-TOKEN") String token,@Query("time") String time,@Query("abId") String abId);
    /**
     * pull下拉同步
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("offlinePullv2/android")
    Observable<ResponseBody> postSyncDate(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 启动页类目更新检查/获取同步间隔
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("checkSystemParamv2/android")
    Observable<ResponseBody> postCheckChargeTypes(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * push上传同步
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("offlinePush/android")
    Observable<ResponseBody> pushOffLines(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     * 历史邀请好友记录
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("historyInviteUsers/android")
    Observable<ResponseBody> getHistoryUsers(@Header("X-AUTH-TOKEN") String token,@Query("curPage") int curPage,@Query("pageSize") int pageSize);
    /**
     * 首页弹框
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("homeWindow/android")
    Observable<ResponseBody> getHomeWindow(@Header("X-AUTH-TOKEN") String token,@Query("version") String version);

    /**
     * 资产界面 修改初始时间接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("initDate/android")
    Observable<ResponseBody> putInitDate(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     * 设置/修改资产
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("saveOrUpdateAssets/android")
    Observable<ResponseBody> postSaveOrUpdateAssets(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     * 获取资产页数据(净资产/初始时间/资产列表)
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("assets/android")
    Observable<ResponseBody> getAssets(@Header("X-AUTH-TOKEN") String token);
    /**
     * 首页公告已读接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("homeWindow/hasRead/android")
    Observable<ResponseBody> putHasRead(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);
    /**
     * 获取我的账本接口--->切换账本页请求
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getABAll/android")
    Observable<ResponseBody> getAllzb(@Header("X-AUTH-TOKEN") String token);
    /**
     * 获取(日常/场景)账本预算/固定大额支出接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getbudgetv2/android")
    Observable<ResponseBody> getbudgetv2(@Header("X-AUTH-TOKEN") String token,@Query("abId") String abId,@Query("time") String time);
    /**
     * 获取(日常/场景)账本预算/固定大额支出接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getABTypeAll/android")
    Observable<ResponseBody> getABTypeAll(@Header("X-AUTH-TOKEN") String token);
    /**
     * 获取账本内组员情况 --->首页展示
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getABMembers/android")
    Observable<ResponseBody> getABMembers(@Header("X-AUTH-TOKEN") String token,@Query("abId") String abId);
    /**
     * 获取成员详情 --->成员管理页数据
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("membersInfo/android")
    Observable<ResponseBody> getMembersInfo(@Header("X-AUTH-TOKEN") String token,@Query("abId") String abId);

    /**
     * 删除成员接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteMembers/android", hasBody = true)
    Observable<ResponseBody> deleteMembers(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     * 主页记账流水- 明细详情编辑操作
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @PUT("updateAB/android")
    Observable<ResponseBody> updateAB(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);

    /**
     * 删除账本接口
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @HTTP(method = "DELETE", path = "deleteAB/android", hasBody = true)
    Observable<ResponseBody> deleteAB(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);
    /**
     * 获取账本成员数对应关系
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("checkABMembers/android")
    Observable<ResponseBody> checkABMembers(@Header("X-AUTH-TOKEN") String token);
    /**
     * 创建账本接口
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("createAB/android")
    Observable<ResponseBody> postCreateAB(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);
    /**
     * 获取丰丰通知
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("message/messageList/")
    Observable<ResponseBody> getMessageList(@Header("X-AUTH-TOKEN") String token,@Query("userId") String userId,@Query("page") String page,@Query("rows") String rows);
    /**
     * 更新丰丰通知状态（未读更新）
     * @param body
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("message/updateMessageStatus/")
    Observable<ResponseBody> postUpdateMessageStatus(@Header("X-AUTH-TOKEN") String token, @Body RequestBody body);

    /**
     * 获取支出类型标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getSpendLabel/android")
    Observable<ResponseBody> getSpendLabel(@Header("X-AUTH-TOKEN") String token,@Query("abTypeId") int abTypeId);
    /**
     * 获取收入类型标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getIncomeLabel/android")
    Observable<ResponseBody> getIncomeLabel(@Query("abTypeId") int abTypeId,@Header("X-AUTH-TOKEN") String token);

    /**
     * 获取收入类型标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("getHadABType/android")
    Observable<ResponseBody> getHadABType(@Header("X-AUTH-TOKEN") String token);

    /**
     * 获取收入类型标签
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("bindInvite/android")
    Observable<ResponseBody> bindInvite(@Header("X-AUTH-TOKEN") String token,@Body RequestBody body);
}
