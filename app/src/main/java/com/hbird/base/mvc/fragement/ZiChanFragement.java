package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.activity.ZiChanInfoActivity;
import com.hbird.base.mvc.activity.ZiChanJieShaoActivity;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZiChanInfoReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.APDUserDateDialog;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AssetsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static java.lang.Double.parseDouble;

/**
 * Created by Liul on 2018/10/11.
 * 资产
 */

public class ZiChanFragement extends BaseFragement implements View.OnClickListener {
    @BindView(R.id.ll_zc)
    LinearLayout mZcInfo;
    @BindView(R.id.tv_times)
    TextView mTimes;
    @BindView(R.id.iv_cz)
    RelativeLayout mChongZhi;
    @BindView(R.id.tv_zong_zichan)
    TextView mZongZiChan;

    @BindView(R.id.ll_xianjin)
    LinearLayout mXianjin01;
    @BindView(R.id.ll_zhifubao)
    LinearLayout mZhiFuBao02;
    @BindView(R.id.ll_weixin)
    LinearLayout mWeiXin03;
    @BindView(R.id.ll_licai)
    LinearLayout mLiCai04;
    @BindView(R.id.ll_shebao)
    LinearLayout mSheBao05;
    @BindView(R.id.ll_jieji)
    LinearLayout mJieJi06;
    @BindView(R.id.ll_gongjiao)
    LinearLayout mGongJiao07;
    @BindView(R.id.ll_chujie)
    LinearLayout mChuJie08;
    @BindView(R.id.ll_fuzhai)
    LinearLayout mFuZhai09;
    @BindView(R.id.ll_qita)
    LinearLayout mQiTa10;

    @BindView(R.id.tv_xianjin)
    TextView xJin;
    @BindView(R.id.tv_weixin)
    TextView wXin;
    @BindView(R.id.tv_zhifubao)
    TextView zfubao;
    @BindView(R.id.tv_licai)
    TextView lCai;
    @BindView(R.id.tv_shebao)
    TextView sBao;
    @BindView(R.id.tv_jieji)
    TextView jJi;
    @BindView(R.id.tv_gongjiao)
    TextView gJiao;
    @BindView(R.id.tv_chujie)
    TextView cJie;
    @BindView(R.id.tv_fuzhai)
    TextView fZhai;
    @BindView(R.id.tv_qita)
    TextView qTa;
    private String ssTime;
    private String token;


    @Override
    public int setContentId() {
        return R.layout.fragement_zichan;
    }

    @Override
    public void initView() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String times = SPUtil.getPrefString(getActivity(), CommonTag.ZICHANCHONGZHI, dateNowStr);
        mTimes.setText(times);
        mZongZiChan.setText("￥ 0");
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
    }

    @Override
    public void initData() {
        getNetWorkInfo();
    }

    private void getNetWorkInfo() {
        NetWorkManager.getInstance().setContext(getActivity()).getZiChanInfo(token, null, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                ZiChanInfoReturn b1 = (ZiChanInfoReturn) b;
                try {
                    ZiChanInfoReturn.ResultBean result = b1.getResult();
                    List<AssetsBean> list = result.getAssets();
                    if (null != list) {
                        for (int i = 0; i < list.size(); i++) {
//                            int assetsType = list.get(i).getAssetsType();
//                            double money = list.get(i).getMoney();
//                            setArgumentsDate(assetsType, money);
                        }
                    }
                    long initDate = result.getInitDate();
                    double netAssets = result.getNetAssets();
                    mZongZiChan.setText("￥ " + netAssets);
                    String dates = DateUtils.getYearMonthDay(initDate);
                    mTimes.setText(dates);
                    setZiChanDate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private void setArgumentsDate(int i, double money) {
        switch (i) {
            case 1:
                SPUtil.setPrefString(getActivity(), CommonTag.XIANJIANTAG, money + "");
                break;
            case 2:
                SPUtil.setPrefString(getActivity(), CommonTag.ZHIFUBAOTAG, money + "");
                break;
            case 3:
                SPUtil.setPrefString(getActivity(), CommonTag.WEIXINTAG, money + "");
                break;
            case 4:
                SPUtil.setPrefString(getActivity(), CommonTag.LICAITAG, money + "");
                break;
            case 5:
                SPUtil.setPrefString(getActivity(), CommonTag.SHEBAOTAG, money + "");
                break;
            case 6:
                SPUtil.setPrefString(getActivity(), CommonTag.JIEJITAG, money + "");
                break;
            case 7:
                SPUtil.setPrefString(getActivity(), CommonTag.GONGJIAOTAG, money + "");
                break;
            case 8:
                SPUtil.setPrefString(getActivity(), CommonTag.CHUJIETAG, money + "");
                break;
            case 9:
                SPUtil.setPrefString(getActivity(), CommonTag.FUZHAITAG, money + "");
                break;
            case 10:
                SPUtil.setPrefString(getActivity(), CommonTag.QITATAG, money + "");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNetWorkInfo();

    }

    private void setZiChanDate() {
        String xianjin = SPUtil.getPrefString(getActivity(), CommonTag.XIANJIANTAG, "0.00");
        String zhifubao = SPUtil.getPrefString(getActivity(), CommonTag.ZHIFUBAOTAG, "0.00");
        String weixin = SPUtil.getPrefString(getActivity(), CommonTag.WEIXINTAG, "0.00");
        String licai = SPUtil.getPrefString(getActivity(), CommonTag.LICAITAG, "0.00");
        String shebao = SPUtil.getPrefString(getActivity(), CommonTag.SHEBAOTAG, "0.00");
        String jieji = SPUtil.getPrefString(getActivity(), CommonTag.JIEJITAG, "0.00");
        String gongjiao = SPUtil.getPrefString(getActivity(), CommonTag.GONGJIAOTAG, "0.00");
        String chujie = SPUtil.getPrefString(getActivity(), CommonTag.CHUJIETAG, "0.00");
        String fuzhai = SPUtil.getPrefString(getActivity(), CommonTag.FUZHAITAG, "0.00");
        String qita = SPUtil.getPrefString(getActivity(), CommonTag.QITATAG, "0.00");

        Double Sum = Double.parseDouble(xianjin) + parseDouble(zhifubao) + parseDouble(weixin) + parseDouble(licai) +
                parseDouble(shebao) + parseDouble(jieji) + parseDouble(gongjiao) + parseDouble(chujie) +
                parseDouble(fuzhai) + parseDouble(qita);

        xJin.setText("￥" + xianjin);
        zfubao.setText("￥" + zhifubao);
        wXin.setText("￥" + weixin);
        lCai.setText("￥" + licai);
        sBao.setText("￥" + shebao);
        jJi.setText("￥" + jieji);
        gJiao.setText("￥" + gongjiao);
        cJie.setText("￥" + chujie);
        fZhai.setText("￥" + fuzhai);
        qTa.setText("￥" + qita);

        //mZongZiChan.setText("￥"+Sum);
    }

    @Override
    public void initListener() {
        mZcInfo.setOnClickListener(this);
        mChongZhi.setOnClickListener(this);

        mXianjin01.setOnClickListener(this);
        mZhiFuBao02.setOnClickListener(this);
        mWeiXin03.setOnClickListener(this);
        mLiCai04.setOnClickListener(this);
        mSheBao05.setOnClickListener(this);
        mJieJi06.setOnClickListener(this);
        mGongJiao07.setOnClickListener(this);
        mChuJie08.setOnClickListener(this);
        mFuZhai09.setOnClickListener(this);
        mQiTa10.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        getNetWorkInfo();
    }

    @Override
    public void loadDate() {
        getNetWorkInfo();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_zc:
                //showMessage("资产介绍");
                playVoice(R.raw.changgui02);
                Intent intent3 = new Intent(getActivity(), ZiChanJieShaoActivity.class);
                startActivity(intent3);
                break;
            case R.id.iv_cz:
                new APDUserDateDialog(getActivity()) {
                    @Override
                    protected void onBtnOkClick(String year, String month, String day) {
                        //判断今天跟选取的年月日 是否为同一天
                        String days = DateUtils.getDays(System.currentTimeMillis());
                        if (!TextUtils.isEmpty(days)) {
                            ssTime = year + "年" + month + "月" + day + "日";
                            long l = 0;
                            try {
                                l = DateUtils.dateToStamp(ssTime);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //调用服务端接口
                            setNetWork2Time(l);
                        }
                    }
                }.show();
                break;
            case R.id.ll_xianjin:
                //showMessage("现金");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "xianjin");
                startActivity(intent);
                break;
            case R.id.ll_zhifubao:
                //showMessage("支付宝");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "zhifubao");
                startActivity(intent);
                break;
            case R.id.ll_weixin:
                //showMessage("微信");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "weixin");
                startActivity(intent);
                break;
            case R.id.ll_licai:
                //showMessage("理财");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "licai");
                startActivity(intent);
                break;
            case R.id.ll_shebao:
                //showMessage("社保");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "shebao");
                startActivity(intent);
                break;
            case R.id.ll_jieji:
                //showMessage("借记");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "jieji");
                startActivity(intent);
                break;
            case R.id.ll_gongjiao:
                //showMessage("公交");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "gongjiao");
                startActivity(intent);
                break;
            case R.id.ll_chujie:
                //showMessage("出借");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "chujie");
                startActivity(intent);
                break;
            case R.id.ll_fuzhai:
                //showMessage("负债");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "fuzhai");
                startActivity(intent);
                break;
            case R.id.ll_qita:
                //showMessage("其它");
                intent = new Intent();
                intent.setClass(getActivity(), ZiChanInfoActivity.class);
                intent.putExtra("type", "qita");
                startActivity(intent);
                break;
        }
    }

    private void setNetWork2Time(long date) {
        NetWorkManager.getInstance()
                .setContext(getActivity())
                .putInitDates(date, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GloableReturn b1 = (GloableReturn) b;
                        mTimes.setText(ssTime);
                        //保存当前的重置时间
                        SPUtil.setPrefString(getActivity(), CommonTag.ZICHANCHONGZHI, ssTime);
                        //重新调用接口 填充总资产 分项资产
                        getNetWorkInfo();
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }
}
