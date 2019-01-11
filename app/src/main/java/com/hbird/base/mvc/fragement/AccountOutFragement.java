package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.activity.AddMoreTypeActivity;
import com.hbird.base.mvc.activity.ChargeToAccount;
import com.hbird.base.mvc.adapter.MyTypeAdapter;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.MyTypeItem;
import com.hbird.base.mvc.bean.RequestBean.ExChangeReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountTypes;
import com.hbird.base.mvc.bean.ReturnBean.BiaoQianReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.widget.DragGridView;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseSpend;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SuperSelectManager;
import com.hbird.common.Constants;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sing.common.util.LogUtil;

/**
 * Created by Liul on 2018/7/02.
 * 支出界面
 */

public class AccountOutFragement extends BaseFragement{
    @BindView(R.id.draggridview)
    DragGridView draggridview;


    private MyTypeAdapter myAdapter;
    private AccountOutFragement mContext = this;
    private List<ZhiChuTagReturn.ResultBean.CommonListBean> commonList;
    private String tag;
    private List<ZhiChuTagReturn.ResultBean.CommonListBean> mydate;
    private String token;
    //private List<MyTypeItem> datas;


    @Override
    public int setContentId() {
        return R.layout.fragement_account_spend;
    }

    @Override
    public void initView() {
        LogUtil.e("AccountOutFragement");
    }

    @Override
    public void initData() {
        myAdapter = new MyTypeAdapter(getActivity(),mContext);
        draggridview.setAdapter(myAdapter);
        tag = getArguments().getString("TAG");
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");

    }

    @Override
    public void initListener() {
        draggridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                //这个地方的判断是为了解决 当用户长按最后一个添加按钮时 不消失的问题
                // 判断对最后一个做长按操作时则标志位-1向前移动一位 手动更改长按点选所选项 从而达到隐藏最后一个的目的
                if(!NetworkUtil.isNetWorkAvailable(getActivity())){
                    showMessage("当前网络不可用，请检查网络");
                    return true;
                }
                if(position==commonList.size()-1){
                    if(position==0){
                        return false;
                    }else {
                        position=position-1;
                    }
                }
                draggridview.startDrag(position);
                myAdapter.setShowType("2");
                //activity中的保存按钮 显示与否 再此设置  最外层的保存按钮要传入myAdapter.setShowType("1");
                getActivity().findViewById(R.id.tv_right_title).setVisibility(View.VISIBLE);

                return true;
            }
        });

        SuperSelectManager.getInstance().setCallBackListen(new SuperSelectManager.ResCheck() {
            @Override
            public void setCheck(int i, String str) {
                if(i==1){
                    //从ChooseAccountTypeActivity界面传过来的值再此监听并响应回调
                    myAdapter.setShowType("1");
                    getActivity().findViewById(R.id.tv_right_title).setVisibility(View.GONE);
                }
                //刷新界面
                //loadDataForNet();
                loadUrlForNet();
            }
        });

        draggridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.equals(myAdapter.getShowType() , "1")){
                    //点击每个条目要做的操作
                    if(position==commonList.size()-1){
                        //说明是最后一个
                        //showMessage("添加。。");
                        playVoice(R.raw.typevoice);
                        if(!NetworkUtil.isNetWorkAvailable(getActivity())){
                            showMessage("当前网络不可用，请检查网络");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),AddMoreTypeActivity.class);
                        ArrayList<String> list = new ArrayList<>();
                        list.clear();
                        for (int i = 0; i < commonList.size(); i++) {
                            list.add(commonList.get(i).getSpendName());
                        }
                        intent.putStringArrayListExtra("object",list);
                        startActivityForResult(intent,120);
                        //addSomeDate();
                    }else {
                        if(TextUtils.equals(tag,"choose")){
                            //带数据跳转回上个页面 编辑页面
                            playVoice(R.raw.typevoice);
                            Intent intent = new Intent();
                            intent.putExtra("Object",new Gson().toJson(commonList.get(position)));
                            getActivity().setResult(202,intent);
                            getActivity().finish();
                        }else {
                            //跳转到下一个界面
                            playVoice(R.raw.typevoice);
                            Intent intent = new Intent();
                            intent.setClass(getActivity(),ChargeToAccount.class);
                            ZhiChuTagReturn.ResultBean.CommonListBean commonListBean = commonList.get(position);
                            String sJson = new Gson().toJson(commonListBean);
                            intent.putExtra("JSONSTR",sJson);
                            intent.putExtra("TAG","支出");
                            startActivityForResult(intent,110);
                        }

                    }
                }else{
                    //删除条目 item
                    playVoice(R.raw.changgui01);
                   /* if(!Utils.isFast2Click()){
                        showMessage("手速不要太快哦");
                        return;
                    }*/
                    deleteItems(position);
                    myAdapter.deleteItem(position);
                }
            }
        });
        myAdapter.setCommondataChangeInterface(new MyTypeAdapter.CommondataChangeInterface() {
            @Override
            public void setdata(MyTypeItem workContent) {

            }

            @Override
            public void setdatas(List<ZhiChuTagReturn.ResultBean.CommonListBean> datas) {
                mydate = datas;
                //调换位置后 上传到服务器
                ExChangeReq req = new ExChangeReq();
                req.setType(1);
                ArrayList<ExChangeReq.relation> relations = new ArrayList<>();
                relations.clear();
                for(int i=0;i<datas.size()-1;i++){
                    ExChangeReq.relation relation = new ExChangeReq.relation();
                    relation.setId(datas.get(i).getId()+"");
                    relation.setPriority(i+1);
                    relations.add(relation);
                }
                req.setRelation(relations);
                String abID = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "0");
                if(TextUtils.equals(abID,"0")){
                    //说明是默认的总账本
                    showMessage("请选择账本");
                    return;
                }
                Integer abid = Integer.valueOf(abID);
                req.setAbTypeId(abid);
                String json = new Gson().toJson(req);
                NetWorkManager.getInstance().setContext(getActivity())
                        .postChangeType(json, token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                AccountTypes b1 = (AccountTypes) b;
                                String version03 = b1.getResult().getVersion();
                                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version03);
                                /*AccountTypes b1 = (AccountTypes) b;
                                String version03 = b1.getResult().getVersion();
                                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ALL_USER_COMM_USE_SPEND_TYPE,version03);*/
                               /* DevRing.tableManager(HbirdUserCommUseSpend.class).deleteAll();
                                List<AccountTypes.ResultBean.CommonListBean> ll = b1.getResult().getCommonList();
                                for (int i = 0; i < ll.size(); i++) {
                                    AccountTypes.ResultBean.CommonListBean bs = ll.get(i);
                                    HbirdUserCommUseSpend w = new HbirdUserCommUseSpend();
                                    w.setId(bs.getId()+"");
                                    w.setPriority(bs.getPriority());
                                    w.setIcon(bs.getIcon());
                                    w.setMark(bs.getMark());
                                    w.setParentId(bs.getParentId());
                                    w.setParentName(bs.getParentName());
                                    w.setSpendName(bs.getSpendName());
                                    DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(w);
                                }*/

                            }

                            @Override
                            public void onError(String s) {
                                showMessage(s);
                            }
                        });
            }
        });
    }

    private void loadUrlForNet() {
        if(null!=mydate && mydate.size()>1){
            DevRing.tableManager(HbirdUserCommUseSpend.class).deleteAll();
            String ss = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
            Integer userInfoId = Integer.valueOf(ss);
            if(null!=mydate){
                for (int i = 0; i < mydate.size()-1; i++) {
                    ZhiChuTagReturn.ResultBean.CommonListBean bs = mydate.get(i);
                    HbirdUserCommUseSpend w = new HbirdUserCommUseSpend();
                    w.setId(bs.getId()+"");
                    w.setPriority(bs.getPriority());
                    w.setIcon(bs.getIcon());
                    w.setMark(bs.getMark());
                    w.setParentId(bs.getParentId());
                    w.setParentName(bs.getParentName());
                    w.setSpendName(bs.getSpendName());
                    w.setAbTypeId(bs.getAbTypeId());
                    w.setUserInfoId(userInfoId);

                    DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(w);
                }
            }

        }
        loadDataForNet();
      /*  String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .getZhiChuTag(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        DevRing.tableManager(HbirdUserCommUseSpend.class).deleteAll();
                        ZhiChuTagReturn b1 = (ZhiChuTagReturn) b;
                        commonList = b1.getResult().getCommonList();
                        for (int i = 0; i < commonList.size(); i++) {
                            ZhiChuTagReturn.ResultBean.CommonListBean bs = commonList.get(i);
                            HbirdUserCommUseSpend w = new HbirdUserCommUseSpend();
                            w.setId(bs.getId()+"");
                            w.setPriority(bs.getPriority());
                            w.setIcon(bs.getIcon());
                            w.setMark(bs.getMark());
                            w.setParentId(bs.getParentId());
                            w.setParentName(bs.getParentName());
                            w.setSpendName(bs.getSpendName());
                            DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(w);
                        }

                        loadDataForNet();
                       *//* //添加一个 "添加"
                        ZhiChuTagReturn.ResultBean.CommonListBean commonListBean = new ZhiChuTagReturn.ResultBean.CommonListBean();
                        commonListBean.setSpendName("添加");
                        commonListBean.setIcon(CommonUserIInfo.account_add_url);
                        commonList.add(commonListBean);
                        myAdapter.setDatas(commonList, "1");*//*
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });*/
    }

    private void deleteItems(final int i) {
        //如果没有网 则不执行删除操作
        boolean net = NetworkUtil.isNetWorkAvailable(getActivity());
        if(!net){
            showMessage("当前网络不可用，请检查网络");
            return;
        }

        String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .delZhiChuType(commonList.get(i).getId()+"", token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        BiaoQianReturn b1 = (BiaoQianReturn) b;
                        String version = b1.getResult().getVersion();
                        SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version);
                        //删除指定支出类目
                        String sqlDel = "delete from HBIRD_USER_COMM_USE_SPEND where id='"+commonList.get(i).getId()+"'";
                        boolean ba = DevRing.tableManager(HbirdUserCommUseSpend.class).execSQL(sqlDel);
                        if(ba){
                            commonList.remove(i);
                        }
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                        hideProgress();
                    }
                });

    }

    /* private void addSomeDate() {
         //每次添加之前 把最后一个 "添加" 删除 (添加完数据后 再加上)
         datas.remove(datas.get(datas.size()-1));
         MyTypeItem items = new MyTypeItem();
         items.setName("工资");
         items.setImgAddress("http://p9twjlzxw.bkt.clouddn.com/1528970084225_DIY_30px.png");
         datas.add(items);
         MyTypeItem itemsAdd = new MyTypeItem();
         itemsAdd.setName("添加");
         itemsAdd.setImgAddress(CommonUserIInfo.account_add_url);
         datas.add(itemsAdd);
         myAdapter.setDatas(datas, "1");
     }*/
    @Override
    public void loadData() {

    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();
        //List list = DevRing.tableManager(HbirdUserCommUseSpend.class).loadAll();
        int abtype = SPUtil.getPrefInt(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, 0);
        String userId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        String sql = "select * from HBIRD_USER_COMM_USE_SPEND where AB_TYPE_ID ="+abtype+" and USER_INFO_ID="+userId+"";
        Cursor cursor = DevRing.tableManager(HbirdUserCommUseSpend.class).rawQuery(sql, null);
        commonList = new ArrayList<>();
        commonList.clear();
        if(cursor!=null){
            commonList = DBUtil.changeToListTypes(cursor, commonList, ZhiChuTagReturn.ResultBean.CommonListBean.class);
        }

        ZhiChuTagReturn.ResultBean.CommonListBean commonListBean = new ZhiChuTagReturn.ResultBean.CommonListBean();
        commonListBean.setSpendName("更多");
        commonListBean.setIcon(Constants.ACCOUNT_ADD_URL);
        commonList.add(commonListBean);
        myAdapter.setDatas(commonList, "1");

       /* String token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(getActivity())
                .getZhiChuTag(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        ZhiChuTagReturn b1 = (ZhiChuTagReturn) b;
                        commonList = b1.getResult().getCommonList();
                        //添加一个 "添加"
                        ZhiChuTagReturn.ResultBean.CommonListBean commonListBean = new ZhiChuTagReturn.ResultBean.CommonListBean();
                        commonListBean.setSpendName("添加");
                        commonListBean.setIcon(CommonUserIInfo.account_add_url);
                        commonList.add(commonListBean);
                        myAdapter.setDatas(commonList, "1");
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });*/

     /*   datas = new ArrayList<>();
        for (int i=0;i<11;i++){
            MyTypeItem items = new MyTypeItem();
            if(i==10){
                items.setName("添加");
                items.setImgAddress(CommonUserIInfo.account_add_url);
            }else {
                items.setName("饮食"+i);
                items.setImgAddress("http://p9twjlzxw.bkt.clouddn.com/1528970084225_DIY_30px.png");
            }
            datas.add(items);
        }
        myAdapter.setDatas(datas, "1");*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==110 && resultCode==111){
            getActivity().setResult(322);//TuBiaoFragement给这个界面用
            getActivity().finish();
        }
        if(requestCode==120 && resultCode==121){
            //刷新界面数据
            loadDataForNet();
        }
    }
}
