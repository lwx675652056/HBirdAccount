package com.hbird.base.util;

/**
 * Created by Liul on 2018/07/02.
 * 宇宙超级无敌接口回调
 */

public class SuperSelectComeManager {

    static SuperSelectComeManager superManager;

    ResCheck chooseCheck;

    public void setCallBackListen(ResCheck check) {
        this.chooseCheck = check;
    }

    public SuperSelectComeManager() {
    }

    public static SuperSelectComeManager getInstance(){

        if(superManager==null) {

            superManager = new SuperSelectComeManager();
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
