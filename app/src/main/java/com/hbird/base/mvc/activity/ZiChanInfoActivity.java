package com.hbird.base.mvc.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import butterknife.BindView;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Liul on 2018/10/11.
 * 设置修改 资产
 */

public class ZiChanInfoActivity extends BaseActivity<BasePresenter> implements View.OnClickListener {
   @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.center_title)
    TextView mCenterTit;
    @BindView(R.id.rl_save_btn)
    RelativeLayout mSaveBtn;
    @BindView(R.id.tv_money)
    EditText mMoney;
    @BindView(R.id.iv_type)
    ImageView mImgType;
    @BindView(R.id.xianjin_name)
    TextView mNames;

    private String type;
    private String token;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_zichan_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRightTitle.setVisibility(View.GONE);
        mCenterTit.setText("编辑记录");
        //弹出软键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        token = SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.GLOABLE_TOKEN, "");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        type = getIntent().getStringExtra("type");

        if(TextUtils.equals(type,"xianjin")){
            String xianjin = SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.XIANJIANTAG, "0.0");
            mMoney.setText(xianjin);
            mMoney.setSelection(xianjin.length());
            mImgType.setImageResource(R.mipmap.ic_xianjin01);
            mNames.setText("现金");
        }else if(TextUtils.equals(type,"zhifubao")){
            String zhifubao =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.ZHIFUBAOTAG,"0.0");
            mMoney.setText(zhifubao);
            mMoney.setSelection(zhifubao.length());
            mImgType.setImageResource(R.mipmap.ic_zhifubao02);
            mNames.setText("支付宝");
        }else if(TextUtils.equals(type,"weixin")){
            String weixin =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.WEIXINTAG,"0.0");
            mMoney.setText(weixin);
            mMoney.setSelection(weixin.length());
            mImgType.setImageResource(R.mipmap.ic_weixin03);
            mNames.setText("微信");
        }else if(TextUtils.equals(type,"licai")){
            String licai =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.LICAITAG,"0.0");
            mMoney.setText(licai);
            mMoney.setSelection(licai.length());
            mImgType.setImageResource(R.mipmap.ic_licai04);
            mNames.setText("理财");
        }else if(TextUtils.equals(type,"shebao")){
            String shebao =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.SHEBAOTAG,"0.0");
            mMoney.setText(shebao);
            mMoney.setSelection(shebao.length());
            mImgType.setImageResource(R.mipmap.ic_shebao05);
            mNames.setText("社保公积金");
        }else if(TextUtils.equals(type,"jieji")){
            String jieji =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.JIEJITAG,"0.0");
            mMoney.setText(jieji);
            mMoney.setSelection(jieji.length());
            mImgType.setImageResource(R.mipmap.ic_jiejika06);
            mNames.setText("借记/储蓄卡");
        }else if(TextUtils.equals(type,"gongjiao")){
            String gongjiao =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.GONGJIAOTAG,"0.0");
            mMoney.setText(gongjiao);
            mMoney.setSelection(gongjiao.length());
            mImgType.setImageResource(R.mipmap.ic_gongjiao07);
            mNames.setText("公交/校园/等充值卡");
        }else if(TextUtils.equals(type,"chujie")){
            String chujie =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.CHUJIETAG,"0.0");
            mMoney.setText(chujie);
            mMoney.setSelection(chujie.length());
            mImgType.setImageResource(R.mipmap.ic_chujie08);
            mNames.setText("出借待收");
        }else if(TextUtils.equals(type,"fuzhai")){
            String fuzhai =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.FUZHAITAG,"0.0");
            mMoney.setText(fuzhai);
            mMoney.setSelection(fuzhai.length());
            mImgType.setImageResource(R.mipmap.ic_fuzhai09);
            mNames.setText("负债待还");
        }else if(TextUtils.equals(type,"qita")){
            String qita =SPUtil.getPrefString(ZiChanInfoActivity.this, CommonTag.QITATAG,"0.0");
            mMoney.setText(qita);
            mMoney.setSelection(qita.length());
            mImgType.setImageResource(R.mipmap.ic_qita10);
            mNames.setText("其它账户");
        }
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_save_btn:
                //保存
                String money = mMoney.getText().toString().trim();

                if(TextUtils.isEmpty(money) || TextUtils.equals(money,"0.0") ){
                    showMessage("请输入金额");
                    return;
                }
                int i=1;
                double moneys = Double.parseDouble(money);

                //如果有值则保存 并跳转到上个界面带回数据
                if(TextUtils.equals(type,"xianjin")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.XIANJIANTAG,money);
                    setNet(1,moneys);
                }else if(TextUtils.equals(type,"zhifubao")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.ZHIFUBAOTAG,money);
                    setNet(2,moneys);
                }else if(TextUtils.equals(type,"weixin")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.WEIXINTAG,money);
                    setNet(3,moneys);
                }else if(TextUtils.equals(type,"licai")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.LICAITAG,money);
                    setNet(4,moneys);
                }else if(TextUtils.equals(type,"shebao")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.SHEBAOTAG,money);
                    setNet(5,moneys);
                }else if(TextUtils.equals(type,"jieji")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.JIEJITAG,money);
                    setNet(6,moneys);
                }else if(TextUtils.equals(type,"gongjiao")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.GONGJIAOTAG,money);
                    setNet(7,moneys);
                }else if(TextUtils.equals(type,"chujie")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.CHUJIETAG,money);
                    setNet(8,moneys);
                }else if(TextUtils.equals(type,"fuzhai")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.FUZHAITAG,money);
                    setNet(9,moneys);
                }else if(TextUtils.equals(type,"qita")){
                    SPUtil.setPrefString(ZiChanInfoActivity.this, CommonTag.QITATAG,money);
                    setNet(10,moneys);
                }

                break;
        }
    }

    private void setNet(int i,double money) {
        showProgress("");
        NetWorkManager.getInstance()
                .setContext(ZiChanInfoActivity.this)
                .postZiChan(i, money, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                GloableReturn b1 = (GloableReturn) b;
                hideProgress();
                finish();
            }

            @Override
            public void onError(String s) {
                hideProgress();
                showMessage(s);
            }
        });
    }

}
