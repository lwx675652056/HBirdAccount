package com.hbird.ui.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.databinding.ActEditInfoBinding;
import com.hbird.base.listener.OnMyTextChangedListener;
import com.hbird.base.mvc.bean.RequestBean.persionalReq;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.view.dialog.MyChooseSexDialog;
import com.hbird.base.mvc.view.dialog.MyHangYeDialog;
import com.hbird.base.mvc.view.dialog.MyZhiWeiDialog;
import com.hbird.base.mvc.widget.CityPickerDialog;
import com.hbird.base.mvc.widget.TimePickerDialog;
import com.hbird.base.util.SPUtil;
import com.hbird.util.Utils;
import com.hbird.util.image.PicChooserHelper;

import java.text.SimpleDateFormat;

import sing.common.base.BaseActivity;
import sing.common.util.LogUtil;
import sing.common.util.StatusBarUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: ActEditInfo
 * @date: 2019/1/28 15:27
 * @Description: 个人信息管理
 */
public class ActEditInfo extends BaseActivity<ActEditInfoBinding, EditInfoModle> {

    private EditInfoData data;
    private String token;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.act_edit_info;
    }

    @Override
    public void initData() {
        token = SPUtil.getPrefString(this, com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");

        Utils.initColor(this, Color.rgb(255, 255, 255));
        StatusBarUtil.setStatusBarLightMode(getWindow());
        data = new EditInfoData();
        binding.setData(data);
        binding.setListener(new OnClick());
        initDialog();

        String jsons = getIntent().getStringExtra("JSON");
        if (!TextUtils.isEmpty(jsons)) {
            GeRenInfoReturn info = new Gson().fromJson(jsons, GeRenInfoReturn.class);

            String nickName = info.getResult().getNickName();
            if (TextUtils.isEmpty(nickName)) {
                String phones = Utils.getHiddenPhone(info.getResult().getMobile());
                data.setNickName(phones);
            } else {
                data.setNickName(nickName);
            }
            LogUtil.e(info.getResult().getAvatarUrl());
            data.setHeadUrl(info.getResult().getAvatarUrl());
            data.setFengniaoId(info.getResult().getId());
            data.setSex(info.getResult().getSex());

            long birthday = info.getResult().getBirthday();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (birthday != 0) {
                data.setBirthday(dateFormat.format(birthday));
            }

            data.setProvince(info.getResult().getProvinceName());
            data.setCity(info.getResult().getCityName());
            data.setProfession(info.getResult().getProfession());
            data.setProfion(info.getResult().getPosition());

            binding.etNickName.requestFocus();
            binding.etNickName.setCursorVisible(false);
            binding.etNickName.setSelection(binding.etNickName.getText().toString().trim().length());
        }
        binding.etNickName.addTextChangedListener(new OnMyTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                data.setNickName(s.toString());
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(v -> {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            onBackPressed();
        });

        findViewById(R.id.right_title2).setOnClickListener(v -> {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            persionalReq req = new persionalReq();
            req.setAvatarUrl(TextUtils.isEmpty(data.getHeadUrl()) ? "" : data.getHeadUrl());
            req.setBirthday(birthdayTime);
            req.setProvinceName(data.getProvince());
            req.setCityName(data.getCity());
            req.setNickName(data.getNickName());
            req.setPosition(data.getProfion());//职位
            req.setProfession(data.getProfession());//行业
            req.setSex(data.getSex());

            String json = new Gson().toJson(req);
            viewModel.save(token, json, url -> {
                ToastUtil.showShort("修改成功");
                setResult(Activity.RESULT_OK);
                onBackPressed();
            });
        });
    }


    @Override
    public int initVariableId() {
        return 0;
    }

    PicChooserHelper helper;// 拍照的

    public class OnClick {
        // 修改头像
        public void changeHead(View view) {
            helper = new PicChooserHelper(ActEditInfo.this, path -> {
                if (!TextUtils.isEmpty(path)) {
                    viewModel.getQiNiuToken(token, path, url -> {
                        data.setHeadUrl("http://head.image.fengniaojizhang.cn/" + url);
                        LogUtil.e(url);
                    });
                }
            });
            helper.show();
        }

        // 修改昵称
        public void changeNickName(View view) {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            binding.etNickName.requestFocus();
            binding.etNickName.setCursorVisible(true);
            binding.etNickName.setSelection(binding.etNickName.getText().toString().trim().length());
            InputMethodManager inputManager = (InputMethodManager) binding.etNickName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(binding.etNickName, 0);
        }

        // 修改性别
        public void changeSex(View view) {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            sex_dialog.show();
        }

        // 修改生日
        public void changeBirthday(View view) {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            time_Dialog.show();
        }

        // 修改城市
        public void changeCity(View view) {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            cityPickerDialog.show();
        }

        // 修改行业
        public void changeProfession(View view) {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            hangye_dialog.show();
        }

        // 修改职位
        public void changeProfion(View view) {
            Utils.playVoice(ActEditInfo.this, R.raw.changgui02);
            zhiwei_dialog.show();
        }
    }

    private CityPickerDialog cityPickerDialog;
    private TimePickerDialog time_Dialog;
    private String birthdayTime = "";// 上传服务器的 生日
    private MyZhiWeiDialog zhiwei_dialog;
    private MyHangYeDialog hangye_dialog;
    private MyChooseSexDialog sex_dialog;

    private void initDialog() {
        cityPickerDialog = new CityPickerDialog(this);
        cityPickerDialog.setCallback(new CityPickerDialog.OnClickCallback() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSure(String provinces, String city, String county, String d) {
                data.setProvince(provinces);
                data.setCity(city);
            }
        });
        time_Dialog = new TimePickerDialog(this);
        time_Dialog.setCallback(new TimePickerDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                time_Dialog.dismiss();
            }

            @Override
            public void onSure(int year, int month, int day, int hour, int minter, long time) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                data.setBirthday(dateFormat.format(time));
                birthdayTime = time + "";
                time_Dialog.dismiss();
            }
        });
        zhiwei_dialog = new MyZhiWeiDialog(this);
        zhiwei_dialog.setCallback(new MyZhiWeiDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                zhiwei_dialog.dismiss();
            }

            @Override
            public void onSure(String d) {
                data.setProfion(d);
                zhiwei_dialog.dismiss();
            }
        });
        hangye_dialog = new MyHangYeDialog(this);
        hangye_dialog.setCallback(new MyHangYeDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                hangye_dialog.dismiss();
            }

            @Override
            public void onSure(String d) {
                data.setProfession(d);
                hangye_dialog.dismiss();
            }
        });
        sex_dialog = new MyChooseSexDialog(this);
        sex_dialog.setCallback(new MyChooseSexDialog.OnClickCallback() {
            @Override
            public void onCancel() {
                sex_dialog.dismiss();
            }

            @Override
            public void onSure(String d) {
                data.setSex(d.equals("男") ? "1" : (d.equals("2") ? "女" : ""));
                sex_dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (helper != null) {
            helper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
