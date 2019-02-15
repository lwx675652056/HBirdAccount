package com.hbird.base.mvc.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.JzAccountReq;
import com.hbird.base.mvc.bean.RequestBean.OffLine2Req;
import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.CommonList2Bean;
import com.hbird.base.mvc.bean.ReturnBean.CommonListBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZiChanInfoReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.global.modle.GlobalVariables;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.APDUserDateDialog;
import com.hbird.base.mvc.view.dialog.ChooseAccountDialog;
import com.hbird.base.mvc.widget.NewGuide2Pop;
import com.hbird.base.mvc.widget.NewGuidePop;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;
import com.hbird.bean.AssetsBean;
import com.hbird.common.Constants;
import com.hbird.ui.account.ActEditAccount;
import com.ljy.devring.DevRing;
import com.ljy.devring.db.support.ITableManger;
import com.ljy.devring.util.NetworkUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import sing.common.util.LogUtil;
import sing.common.util.StatusBarUtil;
import sing.util.SharedPreferencesUtil;

/**
 * Created by Liul on 2018/7/2.
 * 支出 收入  （记账的录入界面）
 * 自定义键盘
 */

public class ChargeToAccount extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {

    @BindView(R.id.tv_center_title)
    TextView mCenterTitle;
    @BindView(R.id.et_record)
    EditText mRecord;
    @BindView(R.id.tv_record_num)
    TextView mRecordNum;
    @BindView(R.id.btnMoodRadio)
    RadioGroup mRadioGroup;
    @BindView(R.id.bt_save_add)
    TextView mBtnSaveAdd;
    @BindView(R.id.tv_up_num)
    TextView mUpNum;
    @BindView(R.id.tv_down_num)
    EditText mDownNum;
    @BindView(R.id.anime_finish)
    TextView addFinishBtn;
    @BindView(R.id.clear)
    ImageView mClear;
    @BindView(R.id.anime_add)
    TextView mAdd;
    @BindView(R.id.anime_jian)
    TextView mJian;
    @BindView(R.id.anime_date)
    TextView mDate;
    @BindView(R.id.iv_icon)
    ImageView mImages;
    @BindView(R.id.tv_type)
    TextView mType;
    @BindView(R.id.ll_xinqing)
    LinearLayout mXinqing;
    @BindView(R.id.btn_happy)
    RadioButton mHappy;
    @BindView(R.id.btn_normal)
    RadioButton mNormal;
    @BindView(R.id.btn_unhappy)
    RadioButton mUnhappy;
    @BindView(R.id.iv_happy)
    ImageView ivHappy;
    @BindView(R.id.iv_normal)
    ImageView ivNormal;
    @BindView(R.id.iv_unhappy)
    ImageView ivUnHappy;
    @BindView(R.id.ll_jianpan)
    LinearLayout mJianPan;

    @BindView(R.id.iv_account_icon)
    ImageView ivIcon;// 账户头像
    @BindView(R.id.ll_choose_account)
    LinearLayout llChooseAccount;// "选择账户"
    @BindView(R.id.tv_account)
    TextView tvAccount;// 账户名称

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private boolean inputTag;
    private int isFirst = 1;
    private String tag;
    private CommonListBean commonListBean;
    private CommonList2Bean commonList2Bean;
    private String moneyString;
    private Integer happys;
    private Long time = 0L;
    private String encode;
    private String decode;
    private String mBeiZhu;
    private ChargeToAccount.numberWatcher numberWatcher;
    private Vibrator vibrator;
    private boolean happysFlag = false;
    private int accountId;
    private final int FIRST_LENGHT = 4000;
    private final int FIRST_LENGHT_TO = 4000;
    private String token;
    private boolean comeInForLogin;
    private String p;
    private boolean b;

    @Override
    protected int getContentLayout() {
        StatusBarUtil.setStatusBarLightMode(getWindow());

        return R.layout.act_charge_to_account;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        initBarColor(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setDate();

        p = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        accountId = Integer.parseInt(p);

        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        getMyAccount();
    }

    private void setNew2Guide() {
        View cv = getWindow().getDecorView();
        cv.post(new Runnable() {
            @Override
            public void run() {
                final NewGuide2Pop pops = new NewGuide2Pop(ChargeToAccount.this, mDate, "这里切换记账日期~", 1, new NewGuide2Pop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {
                        SPUtil.setPrefBoolean(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN_ZZ, false);
                    }
                });
                pops.showPopWindow();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (pops != null) {
                            pops.hidePopWindow();
                        }
                    }

                }, FIRST_LENGHT_TO);
            }
        });
    }

    private void setNewGuide() {
        View cv = getWindow().getDecorView();
        cv.post(new Runnable() {
            @Override
            public void run() {
                final NewGuidePop pop = new NewGuidePop(ChargeToAccount.this, mRadioGroup, "记录每个瞬间的消费心情~", 1, new NewGuidePop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {
                        //SPUtil.setPrefBoolean(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN_ZZ,false);
                        setNewPop();
                    }
                });
                pop.showPopWindow();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (pop != null) {
                            pop.hidePopWindow();
                        }
                    }

                }, FIRST_LENGHT);
            }
        });
    }

    private void setNewPop() {
        final NewGuide2Pop pops = new NewGuide2Pop(ChargeToAccount.this, mDate, "这里切换记账日期~", 1, new NewGuide2Pop.PopDismissListener() {
            @Override
            public void PopDismiss() {
                SPUtil.setPrefBoolean(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN_ZZ, false);
            }
        });
        pops.showPopWindow();
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                if (pops != null) {
                    pops.hidePopWindow();
                }
            }

        }, FIRST_LENGHT_TO);
    }

    private void setDate() {
        setAccount();

        tag = getIntent().getStringExtra("TAG");
        if (TextUtils.equals(tag, "收入")) {
            mCenterTitle.setText("收入");
            mXinqing.setVisibility(View.GONE);
            String jsonstr = getIntent().getStringExtra("JSONSTR");
            commonListBean = new Gson().fromJson(jsonstr, CommonListBean.class);
            mType.setText(commonListBean.getIncomeName());
            mUpNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            mUpNum.setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_coin2), null, null, null);
            mUpNum.setCompoundDrawablePadding(6);
            Glide.with(this).load(commonListBean.getIcon()).into(mImages);
        } else {
            mCenterTitle.setText("支出");
            mXinqing.setVisibility(View.VISIBLE);
            String jsonstr = getIntent().getStringExtra("JSONSTR");
            commonList2Bean = new Gson().fromJson(jsonstr, CommonList2Bean.class);
            mType.setText(commonList2Bean.getSpendName());
            mUpNum.setTextColor(getResources().getColor(R.color.text_333333));
            mUpNum.setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_coin), null, null, null);
            mUpNum.setCompoundDrawablePadding(6);
            Glide.with(this).load(commonList2Bean.getIcon()).into(mImages);
        }
        boolean firstCome = SPUtil.getPrefBoolean(this, com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN_ZZ, true);
        //第一次进入
        if (firstCome) {
            if (TextUtils.equals(tag, "收入")) {
                setNew2Guide();
            } else {
                setNewGuide();
            }

        }
        mDownNum.setText("0.00");
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.iv_backs).setOnClickListener(this);
        findViewById(R.id.tv_right_title).setVisibility(View.GONE);
        mBtnSaveAdd.setOnClickListener(this);
        addFinishBtn.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mJian.setOnClickListener(this);
        mDate.setOnClickListener(this);
        mDownNum.setOnClickListener(this);
        mUpNum.setOnClickListener(this);
        mClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                calculatorClear();
                mUpNum.setText("0.00");
                return true;
            }
        });
        //设置计算器的计算监听 实时计算结果并展示
        numberWatcher = new numberWatcher();
        mDownNum.addTextChangedListener(numberWatcher);
        mRecord.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                encode = Utils.encode(s.toString());

                mBeiZhu = mRecord.getText().toString();
                mRecordNum.setText(mBeiZhu.length() + "/20");
                if (temp.length() >= 20) {
                    showMessage("最多只能输入20字符");
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.btn_happy:// 开心
                        playVoice(R.raw.changgui01);
                        happysFlag = true;
                        happys = 0;
                        ivHappy.setVisibility(View.VISIBLE);
                        ivNormal.setVisibility(View.GONE);
                        ivUnHappy.setVisibility(View.GONE);
                        setGif(happys);

                        break;
                    case R.id.btn_normal: // 一般
                        playVoice(R.raw.changgui01);
                        happysFlag = true;
                        happys = 1;
                        ivHappy.setVisibility(View.GONE);
                        ivNormal.setVisibility(View.VISIBLE);
                        ivUnHappy.setVisibility(View.GONE);
                        setGif(happys);

                        break;
                    case R.id.btn_unhappy: // 不开心
                        playVoice(R.raw.changgui01);
                        happysFlag = true;
                        happys = 2;
                        ivHappy.setVisibility(View.GONE);
                        ivNormal.setVisibility(View.GONE);
                        ivUnHappy.setVisibility(View.VISIBLE);
                        setGif(happys);
                        break;
                }
            }
        });

        ivIcon.setOnClickListener(v -> chooseAccount());
        llChooseAccount.setOnClickListener(v -> chooseAccount());
    }

    private void setGif(Integer happys) {
        if (happys == 0) {
            Glide.with(ChargeToAccount.this)
                    .asGif()
                    .load(R.drawable.gif_happy)
                    .into(ivHappy);
        } else if (happys == 1) {
            Glide.with(ChargeToAccount.this)
                    .asGif()
                    .load(R.drawable.gif_normal)
                    .into(ivNormal);
        } else if (happys == 2) {
            Glide.with(ChargeToAccount.this)
                    .asGif()
                    .load(R.drawable.gif_unhappy)
                    .into(ivUnHappy);
        }
    }

    private class numberWatcher implements TextWatcher {
        private CharSequence temp;
        private int selectionStart;
        private int selectionEnd;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String ss = editable + "";
            try {
                if (TextUtils.isEmpty(ss)) {
                    return;
                }
                if (null != ss) {
                    if (ss.startsWith("+") || ss.startsWith("-")) {
                        ss = "0" + ss;
                    }
                    setNumForResult(ss);
                }

            } catch (Exception e) {
                e.printStackTrace();
                showMessage("数字非法，请重新输入");
                calculatorClear();
            }


        }

    }

    private void setNumForResult(String ss) {
        double sum = Utils.sum(ss);
        //支取小数点后两位数
        DecimalFormat df = new DecimalFormat("######0.00");
        String format = df.format(sum);
        //对负数的处理
       /* if(sum<0){
            reset();
            return;
        }*/
        mUpNum.setText(format);
    }

    private void reset() {
        mUpNum.setText("");
        mDownNum.setText("0.00");
        inputTag = false;
        GlobalVariables.setmInputMoney("");
        showMessage("不能为负值，请重新输入");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_backs:
                calculatorClear();
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_down_num:
                playVoice(R.raw.changgui01);
                mDownNum.setFocusable(false);
                mDownNum.setFocusableInTouchMode(false);
                break;
            case R.id.bt_save_add:
                //showMessage("保存并继续添加");
                playVoice(R.raw.jizhangfinish);
                moneyString = mUpNum.getText().toString().trim();
                if (moneyString.equals("0.00") || GlobalVariables.getmInputMoney().equals("")) {
                    showMessage("请输记账金额");
                    return;
                }
                saveAndFinish(112);
                break;
            case R.id.anime_finish:
                playVoice(R.raw.jizhangfinish);
                moneyString = mUpNum.getText().toString().trim();
                if (moneyString.equals("0.00") || GlobalVariables.getmInputMoney().equals(""))
                    showMessage("请输记账金额");
                else {
                    //数据存储
                    //putItemInData(Double.parseDouble(moneyText.getText().toString()));
                    //calculatorClear();
                    //showMessage("保存数据跳转回上个页面");
                    saveAndFinish(111);

                }
                break;
            case R.id.clear:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                if (inputTag) {
                    inputTag = false;
                }
                //如果是 点 那么则删除点
                if (GlobalVariables.getmHasDot()) {
                    //已经点过了
                    GlobalVariables.setHasDot(false);
                }
                if (TextUtils.isEmpty(mDownNum.getText().toString())) {
                    return;
                }
                String str = mDownNum.getText().toString().substring(0, mDownNum.length() - 1);
                GlobalVariables.setmInputMoney(str);
                mDownNum.setText(str);
                if (TextUtils.isEmpty(str)) {
                    mUpNum.setText("0.00");
                }

                break;
            case R.id.anime_jian:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                if (inputTag) {
                    showMessage("已输入过");
                    return;
                }
                inputTag = true;
                String jian = "-";
                GlobalVariables.setHasDot(false);
                setNumberKey(jian);
                break;
            case R.id.anime_date:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                //开始时间
                new APDUserDateDialog(this) {
                    @Override
                    protected void onBtnOkClick(String year, String month, String day) {
                        //判断今天跟选取的年月日 是否为同一天
                        String days = DateUtils.getDays(System.currentTimeMillis());
                        if (!TextUtils.isEmpty(days)) {
                            if (TextUtils.equals(year + "/" + month + "/" + day, days)) {
                                mDate.setText("今天");
                            } else {
                                mDate.setText(year + "/" + month + "/" + day);
                            }
                        }

                        time = DateUtil.dateToLong(mDate.getText().toString().trim());
                    }
                }.show();
                break;
            case R.id.anime_add:
                playVoice(R.raw.changgui01);
                vibrator.vibrate(20);
                if (inputTag) {
                    showMessage("已输入过");
                    return;
                }
                inputTag = true;
                String add = "+";
                GlobalVariables.setHasDot(false);
                setNumberKey(add);
                break;
            case R.id.tv_up_num:
                playVoice(R.raw.changgui01);
                showMessage("请在下方数字键盘上直接操作~");
                //点击收起软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                BounceInterpolator bounceInterpolator = new BounceInterpolator();
                showTranslate(bounceInterpolator);
                break;
        }
    }

    private void showTranslate(BounceInterpolator interpolator) {
        //创建平移动画对象
        TranslateAnimation translateAnimation = new TranslateAnimation(
                //参数：Animation.RELATIVE_TO_SELF（先对自身）
                //Animation.RELATIVE_TO_PARENT（相对父容器）
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, -0.03f
        );
        //设置持续的时间
        translateAnimation.setDuration(500);
        //设置循环的次数
        translateAnimation.setRepeatCount(1);
        //设置重复的模式，Animation.REVERSE：会进行一次倒序播放
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setFillAfter(false);
        //设置插值器
        translateAnimation.setInterpolator(interpolator);
        //设置动画
        mJianPan.startAnimation(translateAnimation);
    }

    private void saveAndFinish(final int resultCode) {
        //调用记账接口
        JzAccountReq req = new JzAccountReq();
        if (TextUtils.equals(tag, "收入")) {
            req.setTypeId(commonListBean.getId());
            req.setTypeName(commonListBean.getIncomeName());
            req.setTypePname(commonListBean.getParentName());
            req.setTypePid(commonListBean.getParentId());
            req.setMoney(moneyString);
            req.setIsStaged(1);
            long times;
            if (time == 0) {
                times = new Date().getTime();
            } else {
                times = time;
            }
            req.setChargeDate(times + "");
            req.setOrderType(2);
            if (null != encode) {
                String decode = Utils.decode(encode).toString().trim();
                req.setRemark(decode);
            } else {
                req.setRemark(mBeiZhu);
            }
            req.setSpendHappiness(-1);//收入没有心情
        } else {
            req.setTypeId(commonList2Bean.getId());
            req.setTypeName(commonList2Bean.getSpendName());
            req.setTypePname(commonList2Bean.getParentName());
            req.setTypePid(commonList2Bean.getParentId());
            req.setMoney(moneyString);
            req.setIsStaged(1);
            long times;
            if (time == 0) {
                times = new Date().getTime();
            } else {
                times = time;
            }
            req.setChargeDate(times + "");
            req.setOrderType(1);
            if (null != encode) {
                String decode = Utils.decode(encode).toString().trim();
                req.setRemark(decode);
            } else {
                req.setRemark(mBeiZhu);
            }

            req.setSpendHappiness(happys);

        }
        String json = new Gson().toJson(req);
        //本地数据库存储
        ITableManger mManger = DevRing.tableManager(WaterOrderCollect.class);
        WaterOrderCollect w = new WaterOrderCollect();
        w.setId(UUID.randomUUID() + "");//流水记录号
        w.setMoney(Double.parseDouble(req.getMoney()));//单笔记账金额
        w.setAccountBookId(accountId);//记账本id
        if (TextUtils.equals(tag, "收入")) {
            w.setOrderType(2);//订单类型
        } else {
            w.setOrderType(1);
        }

        w.setIsStaged(1);//付款方式
        if (happysFlag) {
            w.setSpendHappiness(happys);//愉悦度
        }
        w.setUserPrivateLabelId(Integer.parseInt(req.getTypeId()));
        w.setTypePid(req.getTypePid());//二级类目
        w.setTypePname(req.getTypePname());
//        w.setTypeId(req.getTypeId());//三级条目 这个不用客户端上传
        w.setTypeName(req.getTypeName());
        String da = System.currentTimeMillis() / 1000 + "000";
        long ss = Long.parseLong(da);
        w.setUpdateDate(new Date(ss));//更新时间
        w.setCreateDate(new Date(ss));//创建时间
        String chargeDate = req.getChargeDate();
        String chargeDates = chargeDate.substring(0, chargeDate.length() - 3) + "000";
        Date date = new Date(Long.parseLong(chargeDates));
        w.setChargeDate(date);//记账时间
        w.setDelflag(0);//删除状态,0:有效 1:删除
        String persionId = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        w.setUpdateBy(Integer.parseInt(persionId));
        w.setCreateBy(Integer.parseInt(persionId));//创建者id (多账本时用到 检查更新时给返回的userID)
        w.setRemark(req.getRemark());
        String zhangbenName = SPUtil.getPrefString(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, "");
        w.setAbName(zhangbenName);//对应的账本ID
        String headers = SPUtil.getPrefString(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_HEADER, "");
        String nickName = SPUtil.getPrefString(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_USER_NICK_NAME, "");
        if (TextUtils.isEmpty(nickName)){
            nickName = (String) SharedPreferencesUtil.get(Constants.USER_MOBILE,"");
            nickName = com.hbird.util.Utils.getHiddenPhone(nickName);
        }
        w.setReporterNickName(nickName);//记录人名字
        w.setReporterAvatar(headers);//记录人头像
        String icon = "";
        if (TextUtils.equals(tag, "收入")) {
            icon = commonListBean.getIcon();
        } else {
            icon = commonList2Bean.getIcon();
        }
        w.setIcon(icon);
        int id = (int) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_ID, 0);
        if (id == 0) {
            w.setAssetsId(0);
        } else {
            w.setAssetsId(id);
        }
        String accountName = (String) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_DESC, "");
        w.setAssetsName(accountName.equals("未选择")?"":accountName);
        b = mManger.insertOne(w);
        pullToSyncDate(resultCode);
    }

    private void setNumberKey(String add) {
       /* if(!Utils.isFastClick()){
            //showMessage("手速太快了");
            return;
        }*/
        String money = GlobalVariables.getmInputMoney();
        if (GlobalVariables.getmHasDot() && GlobalVariables.getmInputMoney().length() > 2) {
            String dot = money.substring(money.length() - 3, money.length() - 2);
            LogUtil.e("calculatorNumOnclick: " + dot);
            if (dot.equals(".")) {
                //showMessage("陛下，不到一分钱了");
                return;
            }
        }
        //mDownNum.addTextChangedListener(numberWatcher);
        //判断
        String[] positive = money.split("\\+");
        String endStr = null;
        for (String pos : positive) {
            if (pos.contains("-")) {
                String[] negative = pos.split("\\-");
                Double temp = 0d;
                for (int is = 0; is < negative.length; is++) {
                    endStr = negative[is];
                }
                //endStr = negative[0];
            } else {
                endStr = pos;
            }
        }
        //判断超过8位数不再继续输入
        if (null != endStr && endStr.contains(".")) {
            String[] split = endStr.split("\\.");
            if (null != split && split.length > 0) {
                if (split[0].length() > 8) {
                    showMessage("您输入的位数过大");
                    return;
                }
            }

        } else {
            if (!TextUtils.isEmpty(endStr)) {
                if (endStr.length() >= 8) {
                    String sub = money.substring(money.length() - 1, money.length());
                    if (TextUtils.equals(sub, "+") || TextUtils.equals(sub, "-")) {

                    } else {
                        if (TextUtils.equals(add, "+") || TextUtils.equals(add, "-")) {

                        } else {
                            showMessage("您输入的位数过大");
                            return;
                        }
                    }
                }
            }

        }
        GlobalVariables.setmInputMoney(money + add);
        mDownNum.setText(GlobalVariables.getmInputMoney());
        mDownNum.setSelection(GlobalVariables.getmInputMoney().length());
    }

    // 清零按钮
    public void calculatorClear() {
        GlobalVariables.setmInputMoney("");
        mUpNum.setText("");
        mDownNum.setText("0.00");
        GlobalVariables.setHasDot(false);
    }

    // 数字输入按钮
    public void calculatorNumOnclick(View v) {
        playVoice(R.raw.changgui01);
        Button view = (Button) v;
        String digit = view.getText().toString();
        inputTag = false;
        vibrator.vibrate(20);
        setNumberKey(digit);
    }

    // 小数点处理工作
    public void calculatorPushDot(View view) {
        if (GlobalVariables.getmHasDot()) {
            Toast.makeText(getApplicationContext(), "已经输入过小数点了 ━ω━●", Toast.LENGTH_SHORT).show();
        } else {
            GlobalVariables.setmInputMoney(GlobalVariables.getmInputMoney() + ".");
            GlobalVariables.setHasDot(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        calculatorClear();
    }

    private void pullToSyncDate(final int resultCode) {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(ChargeToAccount.this);
        if (!netWorkAvailable) {
            if (b) {
                //创建成功
                setResult(resultCode);
                calculatorClear();
                finish();
            } else {
                showMessage("创建失败");
            }
            return;
        }
        String mobileDevice = com.hbird.util.Utils.getDeviceInfo(ChargeToAccount.this);
        comeInForLogin = SPUtil.getPrefBoolean(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN, false);
        NetWorkManager.getInstance().setContext(ChargeToAccount.this).postPullToSyncDate(mobileDevice, comeInForLogin, token, new NetWorkManager.CallBack() {
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
                SPUtil.setPrefLong(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.SYNDATE, L);
                List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                //插入本地数据库
                DBUtil.insertLocalDB(synData);
                SPUtil.setPrefBoolean(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);
                pushOffLine(resultCode);
            }

            @Override
            public void onError(String s) {
                if (b) {
                    //创建成功
                    setResult(resultCode);
                    calculatorClear();
                    finish();
                } else {
                    showMessage("创建失败");
                }
            }
        });
    }

    private void pushOffLine(final int code) {
        OffLineReq req = new OffLineReq();
        String deviceId = com.hbird.util.Utils.getDeviceInfo(ChargeToAccount.this);
        req.setMobileDevice(deviceId);
        Long time = SPUtil.getPrefLong(ChargeToAccount.this, com.hbird.base.app.constant.CommonTag.SYNDATE, new Date().getTime());
        String times = String.valueOf(time);
        req.setSynDate(times);

        // 本地数据库查找未上传数据 上传至服务器
//        String sql = "SELECT * FROM WATER_ORDER_COLLECT where ACCOUNT_BOOK_ID= " + accountId + " AND UPDATE_DATE >= " + time;
        String sql = "SELECT * FROM WATER_ORDER_COLLECT where UPDATE_DATE >= " + time;
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> l = new ArrayList<>();
        if (null != cursor) {
            l = DBUtil.changeToList(cursor, l, WaterOrderCollect.class);
        }

        List<OffLineReq.SynDataBean> myList = new ArrayList<>();
        myList.clear();

        if (l != null && l.size()>0){
            for (int i = 0; i < l.size(); i++) {
                OffLineReq.SynDataBean t = new OffLineReq.SynDataBean();
                WaterOrderCollect w = l.get(i);
                t.setTypeName(w.typeName);
                t.setId(w.id);
                t.setIsStaged(w.isStaged);
                t.setUseDegree(w.useDegree);
                t.setPictureUrl(w.getPictureUrl());
                t.setParentId(String.valueOf(w.getParentId()));
                t.setRemark(w.getRemark());
                t.setAccountBookId(w.getAccountBookId());
                t.setOrderType(w.getOrderType());
                t.setMoney(w.getMoney());
                t.setCreateDate(w.getCreateDate());
                t.setCreateName(w.getCreateName());
                t.setTypeId(w.getTypeId());
                t.setTypeName(w.getTypeName());
                t.setSpendHappiness(w.getSpendHappiness());
                t.setChargeDate(w.getChargeDate());
                t.setTypePid(w.getTypePid());
                t.setTypePname(w.getTypePname());
                t.setUpdateDate(w.getUpdateDate());
                t.setDelflag(w.getDelflag());
                t.setCreateBy(w.getCreateBy());
                t.setDelDate(w.getDelDate());
                t.setUpdateBy(w.getUpdateBy());
                t.setUpdateName(w.getUpdateName());
                t.setIcon(w.getIcon());
                t.setUserPrivateLabelId(w.getUserPrivateLabelId());
                t.setReporterAvatar(w.getReporterAvatar());
                t.setReporterNickName(w.getReporterNickName());
                t.setAbName(w.getAbName());
                t.setAssetsId(w.getAssetsId());
                t.setAssetsName(w.getAssetsName());
                myList.add(t);
            }
        }
//        if (null != cursor) {
//            try {
//                myList = DBUtil.changeToListPull(cursor, myList, OffLineReq.SynDataBean.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

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
                synDataBean.setUpdateBy(s1.getCreateBy());
                synDataBean.setUseDegree(s1.getUseDegree());
                synDataBean.setUpdateName(s1.getUpdateName());
                synDataBean.setUserPrivateLabelId(s1.getUserPrivateLabelId());
                synDataBean.setAssetsId(s1.getAssetsId());
                synDataBean.setAssetsName(s1.getAssetsName());
                myList2.add(synDataBean);
            }
            req2.setSynData(myList2);
        }


        if (myList2 != null && myList2.size() > 0) {
            req2.setSynData(myList2);
        }
        String jsonInfo = new Gson().toJson(req2);
        NetWorkManager.getInstance().setContext(ChargeToAccount.this).pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b2) {
                GloableReturn b1 = (GloableReturn) b2;
                if (b) {
                    //创建成功
                    setResult(code);
                    calculatorClear();
                    finish();
                } else {
                    showMessage("创建失败");
                }
            }

            @Override
            public void onError(String s) {
                if (b) {
                    //创建成功
                    setResult(code);
                    calculatorClear();
                    finish();
                } else {
                    showMessage("创建失败");
                }
            }
        });
    }

    private void getMyAccount() {
        NetWorkManager.getInstance().setContext(ChargeToAccount.this).getZiChanInfo(token, 1, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b2) {
                ZiChanInfoReturn bean = (ZiChanInfoReturn) b2;
                if (bean != null && bean.getResult().getAssets() != null && bean.getResult().getAssets().size() > 0) {
                    List<AssetsBean> temp = bean.getResult().getAssets();
                    // mark 为1时是默认账户里的
                    Iterator<AssetsBean> iter = temp.iterator();
                    while (iter.hasNext()) {  //执行过程中会执行数据锁定，性能稍差，若在循环过程中要去掉某个元素只能调用iter.remove()方法。
                        AssetsBean a = iter.next();
                        if (a.mark != 1) {
                            iter.remove();
                        }
                    }
                    String str = JSON.toJSONString(temp);
                    // 保存默认账户
                    SharedPreferencesUtil.put(Constants.MY_ACCOUNT, str);
                }
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    // 选择账户
    private void chooseAccount() {
        ChooseAccountDialog dialog = new ChooseAccountDialog(this);
        dialog.setListener((data, type) -> {
            dialog.dismiss();
            if (type == 0) {
                Intent intent = new Intent(ChargeToAccount.this, ActEditAccount.class);
                startActivityForResult(intent, 1000);
            } else if (type == 1) {
                if (data == null) {     // 不选择账户
                    SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ID, 0);
                    SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_DESC, "未选择");
                    SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ICON, "");
                } else {
                    AssetsBean bean = (AssetsBean) data;
                    SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ID, bean.assetsType);
                    SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_DESC, bean.assetsName);
                    SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ICON, bean.icon);
                }
                setAccount();
            }
        });

        dialog.show();
    }

    // 设置界面上账户的值
    private void setAccount() {
        String account = (String) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_DESC, "未选择");
        if ("未选择".equals(account)) {
            Glide.with(ivIcon.getContext()).load(R.mipmap.jzzhxz_icon_bxzh_normal).into(ivIcon);
        } else {
            String icon = (String) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_ICON, "");
            Glide.with(ivIcon.getContext()).load(icon).into(ivIcon);
        }
        tvAccount.setText(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            chooseAccount();
            // 要更新UI，判断已选的账户是否被删除
            if (!isExist()) {
                SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ID, 0);
                SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_DESC, "未选择");
                SharedPreferencesUtil.put(Constants.CHOOSE_ACCOUNT_ICON, "");
                setAccount();
            }
        }
    }

    private boolean isExist() {
        String str = (String) SharedPreferencesUtil.get(Constants.MY_ACCOUNT, "");
        List<AssetsBean> temp = JSON.parseArray(str, AssetsBean.class);
        if (temp == null || temp.size() < 1) {
            return false;// 已删除，不存在
        }
        int id = (int) SharedPreferencesUtil.get(Constants.CHOOSE_ACCOUNT_ID, 0);
        for (int i = 0; i < temp.size(); i++) {
            if (id == temp.get(i).assetsType) {
                return true;// 没有删，还存在
            }
        }
        return false;
    }

}
