package com.hbird.ui.address;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.databinding.ActEditAddressBinding;
import com.hbird.base.util.SPUtil;
import com.hbird.bean.TitleBean;
import com.hbird.common.dialog.ChooseAddressDialog;
import com.hbird.util.Utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sing.common.base.BaseActivity;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActEditAddress
 * @date: 2019/1/5 11:37
 * @Description: 填写地址
 */
public class ActEditAddress extends BaseActivity<ActEditAddressBinding, EditAddressViewModel> {

    private String token;
    private EditAddressData data;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_edit_address;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initData() {
        Utils.initColor(this, Color.rgb(255, 255, 255));
        StatusBarUtil.setStatusBarLightMode(getWindow());

        binding.toolbar.ivBack.setOnClickListener(v -> onBackPressed());
        binding.toolbar.tvRight.setOnClickListener(v -> save());

        binding.setTitle(new TitleBean("编辑收货信息", "保存", ContextCompat.getColor(this, R.color.colorPrimary)));

        data = new EditAddressData();
        data.setCity("");
        data.setProvince("");
        data.setCounty("");
        binding.setData(data);

        binding.setListener(new OnClick());

        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");

        getData();
    }

    public class OnClick {
        // 选择所在地区
        public void chooseAddress(View view) {
            ChooseAddressDialog dialog = new ChooseAddressDialog(ActEditAddress.this, data.getProvince(), data.getCity(), data.getCounty());
            dialog.setListener((province, city, county) -> {
                data.setProvince(province.equals("未选择")?"":province);
                data.setCity(city.equals("未选择")?"":city);
                data.setCounty(county.equals("未选择")?"":county);
            });
            dialog.show();
        }
    }

    // 获取之前的地址信息
    private void getData() {
        viewModel.getAddress(token, bean -> {
            if (bean != null) {
                data.setName(bean.consigneeName);
                data.setPhone(bean.consigneeMobile);
                data.setProvince(bean.consigneeProvince);
                data.setCity(bean.consigneeCity);
                data.setCounty(bean.consigneeDistrict);
                data.setAddress(bean.consigneeDetail);
            }
        });
    }

    // 保存
    private void save() {
        if (!check()) {
            return;
        }
        JSONObject obj = new JSONObject();
        obj.put("consigneeName", data.getName());
        obj.put("consigneeMobile", data.getPhone());
        obj.put("consigneeProvince", data.getProvince());
        obj.put("consigneeCity", data.getCity());
        obj.put("consigneeDistrict", data.getCounty());
        obj.put("consigneeDetail", data.getAddress());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toJSONString());
        viewModel.saveAddress(token, body, bean -> {
            ToastUtil.showShort("修改成功");
            onBackPressed();
        });
    }

    // 非空验证
    private boolean check() {
        if (TextUtils.isEmpty(data.getName())) {
            ToastUtil.showShort("请填写收货人");
            return false;
        }
        if (TextUtils.isEmpty(data.getPhone())) {
            ToastUtil.showShort("请填写手机号");
            return false;
        }

        if (data.getPhone().length() != 11 || !data.getPhone().substring(0, 1).equals("1")) {
            ToastUtil.showShort("请填写正确的手机号");
            return false;
        }

        if (TextUtils.isEmpty(data.getProvince())) {
            ToastUtil.showShort("请选择所在地区");
            return false;
        }
        if (TextUtils.isEmpty(data.getAddress())) {
            ToastUtil.showShort("请填写详细地址");
            return false;
        }
        return true;
    }
}
