package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.adapter.MemberManagerAdapter;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.MembersBean;
import com.hbird.base.mvc.bean.ReturnBean.AccountMemberBeans;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/7.
 * 成员管理 （多账本）
 */

public class MemberManagerActivity extends BaseActivity<BaseActivityPresenter> {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.right_title2)
    TextView rightTitle2;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_add_member)
    LinearLayout llAddMember;
    @BindView(R.id.tv_editor)
    RelativeLayout tvEditor;
    @BindView(R.id.tv_del)
    RelativeLayout tvDel;

    private MemberManagerAdapter adapter;
    private List<Boolean> checkedStatus;
    private ArrayList<MembersBean> memberList;
    private String token;
    private String id;
    private List<AccountMemberBeans.ResultBean.MembersBean> members;
    private ArrayList<String> listCheck;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_member_manager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        centerTitle.setText("成员管理");
        rightTitle2.setText("取消");
        rightTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVoice(R.raw.changgui02);
                adapter.setData(memberList, false, checkedStatus);
                tvDel.setVisibility(View.GONE);
                tvEditor.setVisibility(View.VISIBLE);
                rightTitle2.setVisibility(View.GONE);
            }
        });
        rightTitle2.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("ID");
        token = SPUtil.getPrefString(MemberManagerActivity.this, CommonTag.GLOABLE_TOKEN, "");
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setMemberDate();
    }

    private void setMemberDate() {
        NetWorkManager.getInstance().setContext(MemberManagerActivity.this).getMemberInfo(token, id, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                AccountMemberBeans b1 = (AccountMemberBeans) b;

                members = b1.getResult().getMembers();
                if (members != null) {
                    if (members.size() > 3) {
                        llAddMember.setVisibility(View.GONE);
                    } else {
                        llAddMember.setVisibility(View.VISIBLE);
                    }

                    memberList = new ArrayList<>();
                    for (int i = 0; i < members.size(); i++) {
                        MembersBean bean = new MembersBean();
                        bean.setMemberName(members.get(i).getNickName());
                        bean.setMemberImgUrl(members.get(i).getAvatarUrl());
                        long createDate = members.get(i).getCreateDate();
                        String day = DateUtils.getYearMonthDay(createDate);
                        bean.setMemberTime(day + "加入");
                        bean.setId(members.get(i).getId() + "");
                        memberList.add(bean);
                    }

                    checkedStatus = new ArrayList<>();
                    checkedStatus.clear();
                    for (int i = 0; i < memberList.size(); i++) {
                        checkedStatus.add(false);
                    }
                    adapter = new MemberManagerAdapter(MemberManagerActivity.this, false, checkedStatus);
                    lv.setAdapter(adapter);

                    tvDel.setVisibility(View.GONE);
                    tvEditor.setVisibility(View.VISIBLE);
                    rightTitle2.setVisibility(View.GONE);

                    adapter.setData(memberList, false, checkedStatus);
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_add_member, R.id.tv_editor, R.id.tv_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.ll_add_member:// 添加成员
                playVoice(R.raw.changgui02);
                Intent intent = new Intent();
                intent.setClass(MemberManagerActivity.this, InviteJiZhangActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
                break;
            case R.id.tv_editor: // 编辑
                playVoice(R.raw.changgui02);
                adapter.setData(memberList, true, checkedStatus);
                tvDel.setVisibility(View.VISIBLE);
                tvEditor.setVisibility(View.GONE);
                rightTitle2.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_del:// 删除
                playVoice(R.raw.changgui02);
                List<Boolean> checkedStatus = adapter.getCheckedStatus();
                listCheck = new ArrayList<>();
                listCheck.clear();
                for (int i = 0; i < checkedStatus.size(); i++) {
                    if (checkedStatus.get(i)) {
                        listCheck.add(members.get(i).getId() + "");
                    }
                }
                delMembers();
                break;
        }
    }

    private void delMembers() {
        if (listCheck.size() == 0) {
            showMessage("请选择成员");
            return;
        }
        showProgress("");
        NetWorkManager.getInstance().setContext(MemberManagerActivity.this).clearMemberInfo(id, listCheck, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                GloableReturn b1 = (GloableReturn) b;
                hideProgress();
                setMemberDate();

                for (Iterator<MembersBean> it = memberList.iterator(); it.hasNext(); ) {
                    MembersBean s = it.next();
                    if (listCheck.contains(s.getId())) {
                        it.remove();
                    }
                }
                tvDel.setVisibility(View.GONE);
                tvEditor.setVisibility(View.VISIBLE);
                rightTitle2.setVisibility(View.GONE);
                adapter.setData(memberList, false, checkedStatus);
            }

            @Override
            public void onError(String s) {
                showMessage(s);
                hideProgress();
            }
        });
    }
}