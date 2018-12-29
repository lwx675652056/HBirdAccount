package com.hbird.ui.assets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.ActEditAccountValueBinding;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.ColorBar;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import sing.common.base.BaseActivity;
import sing.common.base.BaseViewModel;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActEditAccountValue
 * @date: 2018/12/28 16:30
 * @Description: 编辑账户值
 */
public class ActEditAccountValue extends BaseActivity<ActEditAccountValueBinding, BaseViewModel> implements IBaseActivity {

    private String token;
    private AssetsBean bean;// 传过来的值
    private AssetsData data;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarLightMode(getWindow());
        return R.layout.act_edit_account_value;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        ColorBar.newColorBuilder()
                .applyNav(true)
                .navColor(Color.parseColor("#FFFFFF"))
                .navDepth(0)
                .statusColor(Color.parseColor("#FFFFFF"))
                .statusDepth(0)
                .build(this)
                .apply();

        binding.setListener(new OnClick());
        binding.toolbar.findViewById(R.id.iv_backs).setOnClickListener(v -> onBackPressed());
        ((TextView) binding.toolbar.findViewById(R.id.tv_center_title)).setText("编辑记录");

        bean = (AssetsBean) getIntent().getExtras().getSerializable(Constants.START_INTENT_A);
        if (bean == null) {
            finish();
            return;
        }
        binding.setBean(bean);

        data = new AssetsData();
        data.setEdit(false);
        binding.setData(data);

        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
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

        // 编辑
        public void edit(View view) {
            data.setEdit(true);
            sing.util.KeyboardUtil.closeSoftInput(ActEditAccountValue.this);
            sing.util.KeyboardUtil.showSoftInput(ActEditAccountValue.this);
        }

        // 点击金额
        public void clickMoney(View view) {
            data.setEdit(false);
            sing.util.KeyboardUtil.closeSoftInput(ActEditAccountValue.this);
            sing.util.KeyboardUtil.showSoftInput(ActEditAccountValue.this);
        }

        // 保存
        public void save(View view) {
            saveOrUpdateAssets();
        }

        // 查看公积金
        public void look(View view) {
            IWXAPI api = WXAPIFactory.createWXAPI(ActEditAccountValue.this, com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);

            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = "gh_3bde01ceba6a"; // 填小程序原始id
//            req.path = "/pages/home/home?kx=kxx";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        }
    }

    // 保存
    private void saveOrUpdateAssets() {
        String nameS = binding.etAssetsName.getText().toString().trim();
        String moneyS = binding.etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(nameS)) {
            nameS = bean.assetsName;
        }
        Double money;
        try {
            money = Double.parseDouble(moneyS);
        } catch (Exception e) {
            ToastUtil.showShort("请输入正确的金额");
            return;
        }
        String finalNameS = nameS;
        NetWorkManager.getInstance().setContext(this).saveOrUpdateAssets(bean.assetsType, money, nameS, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                bean.assetsName = finalNameS;
                bean.money = money;
                Intent intent = new Intent();
                intent.putExtra(Constants.START_INTENT_A, bean);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(String s) {
            }
        });
    }
}
