package com.hbird.base.util;

/**
 * Created by Liul on 2018/07/02.
 * 宇宙超级无敌接口回调
 */

public class SuperSelectManager {

    static SuperSelectManager superManager;

    ResCheck chooseCheck;

    public void setCallBackListen(ResCheck check) {
        this.chooseCheck = check;
    }

    public SuperSelectManager() {
    }

    public static SuperSelectManager getInstance(){

        if(superManager==null) {

            superManager = new SuperSelectManager();
        }

        return superManager;

    }

    public void setnum(int i,String str){
        if(chooseCheck!=null) {
            chooseCheck.setCheck(i,str);
        }
    }

    public interface ResCheck{

        void setCheck(int i, String str);

    }

}
