package com.hbird.base.mvc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Liul on 2018/7/02.
 */

public class MyTypeItem implements Serializable {
    private String id;

    private String columnType;
    private String applyId;
    private String appId;
    private String version;
    private String versionUpdateTime;
    private String downAddress;
    private String applyTypeId;
    private String appSize;
    private String appEntry;
    private String appClassName;
    private String appApk;
    private String childappType;
    private String childappKey;
    private String childappShowType;




    //图片url
    private String imgAddress;
    //图片底部对应名称
    private String name;







    private String secondImgAddress;
    private String uninstIconAddr;
    private String childappChannel;
    private String childappWapaddr;
    private String province;
    private String updateIconAddr;
    private String pName;
    private Param initParam;
    private List<Children> childrenList;
    private boolean state;

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", columnType='" + columnType + '\'' +
                ", applyId='" + applyId + '\'' +
                ", appId='" + appId + '\'' +
                ", version='" + version + '\'' +
                ", versionUpdateTime='" + versionUpdateTime + '\'' +
                ", downAddress='" + downAddress + '\'' +
                ", applyTypeId='" + applyTypeId + '\'' +
                ", appSize='" + appSize + '\'' +
                ", appEntry='" + appEntry + '\'' +
                ", appClassName='" + appClassName + '\'' +
                ", appApk='" + appApk + '\'' +
                ", childappType='" + childappType + '\'' +
                ", childappKey='" + childappKey + '\'' +
                ", childappShowType='" + childappShowType + '\'' +
                ", imgAddress='" + imgAddress + '\'' +
                ", secondImgAddress='" + secondImgAddress + '\'' +
                ", uninstIconAddr='" + uninstIconAddr + '\'' +
                ", childappChannel='" + childappChannel + '\'' +
                ", childappWapaddr='" + childappWapaddr + '\'' +
                ", province='" + province + '\'' +
                ", updateIconAddr='" + updateIconAddr + '\'' +
                ", pName='" + pName + '\'' +
                ", initParam=" + initParam +
                ", childrenList=" + childrenList +
                ", state=" + state +
                '}';
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public class Children implements Serializable {
        private String id;
        private String name;
        private String columnType;
        private String applyId;
        private String appId;
        private String version;
        private String versionUpdateTime;
        private String downAddress;
        private String applyTypeId;
        private String appSize;
        private String appEntry;
        private String appClassName;
        private String appApk;
        private String childappType;
        private String childappKey;
        private String childappShowType;
        private String imgAddress;
        private String secondImgAddress;
        private String uninstIconAddr;
        private String childappChannel;
        private String childappWapaddr;
        private String province;
        private String updateIconAddr;
        private String pName;
        private Param initParam;
        private List<Children> childrenList;
        private String showType="1";//1:显示加号  2：显示下载符号  3：显示灰色
        private boolean state;

        @Override
        public String toString() {
            return "Children{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", columnType='" + columnType + '\'' +
                    ", applyId='" + applyId + '\'' +
                    ", appId='" + appId + '\'' +
                    ", version='" + version + '\'' +
                    ", versionUpdateTime='" + versionUpdateTime + '\'' +
                    ", downAddress='" + downAddress + '\'' +
                    ", applyTypeId='" + applyTypeId + '\'' +
                    ", appSize='" + appSize + '\'' +
                    ", appEntry='" + appEntry + '\'' +
                    ", appClassName='" + appClassName + '\'' +
                    ", appApk='" + appApk + '\'' +
                    ", childappType='" + childappType + '\'' +
                    ", childappKey='" + childappKey + '\'' +
                    ", childappShowType='" + childappShowType + '\'' +
                    ", imgAddress='" + imgAddress + '\'' +
                    ", secondImgAddress='" + secondImgAddress + '\'' +
                    ", uninstIconAddr='" + uninstIconAddr + '\'' +
                    ", childappChannel='" + childappChannel + '\'' +
                    ", childappWapaddr='" + childappWapaddr + '\'' +
                    ", province='" + province + '\'' +
                    ", updateIconAddr='" + updateIconAddr + '\'' +
                    ", pName='" + pName + '\'' +
                    ", initParam=" + initParam +
                    ", childrenList=" + childrenList +
                    ", showType='" + showType + '\'' +
                    ", state=" + state +
                    '}';
        }

        public String getpName() {
            return pName;
        }

        public void setpName(String pName) {
            this.pName = pName;
        }

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColumnType() {
            return columnType;
        }

        public void setColumnType(String columnType) {
            this.columnType = columnType;
        }

        public String getApplyId() {
            return applyId;
        }

        public void setApplyId(String applyId) {
            this.applyId = applyId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersionUpdateTime() {
            return versionUpdateTime;
        }

        public void setVersionUpdateTime(String versionUpdateTime) {
            this.versionUpdateTime = versionUpdateTime;
        }

        public String getDownAddress() {
            return downAddress;
        }

        public void setDownAddress(String downAddress) {
            this.downAddress = downAddress;
        }

        public String getApplyTypeId() {
            return applyTypeId;
        }

        public void setApplyTypeId(String applyTypeId) {
            this.applyTypeId = applyTypeId;
        }

        public String getAppSize() {
            return appSize;
        }

        public void setAppSize(String appSize) {
            this.appSize = appSize;
        }

        public String getAppEntry() {
            return appEntry;
        }

        public void setAppEntry(String appEntry) {
            this.appEntry = appEntry;
        }

        public String getAppClassName() {
            return appClassName;
        }

        public void setAppClassName(String appClassName) {
            this.appClassName = appClassName;
        }

        public String getAppApk() {
            return appApk;
        }

        public void setAppApk(String appApk) {
            this.appApk = appApk;
        }

        public String getChildappType() {
            return childappType;
        }

        public void setChildappType(String childappType) {
            this.childappType = childappType;
        }

        public String getChildappKey() {
            return childappKey;
        }

        public void setChildappKey(String childappKey) {
            this.childappKey = childappKey;
        }

        public String getChildappShowType() {
            return childappShowType;
        }

        public void setChildappShowType(String childappShowType) {
            this.childappShowType = childappShowType;
        }

        public String getImgAddress() {
            return imgAddress;
        }

        public void setImgAddress(String imgAddress) {
            this.imgAddress = imgAddress;
        }

        public String getSecondImgAddress() {
            return secondImgAddress;
        }

        public void setSecondImgAddress(String secondImgAddress) {
            this.secondImgAddress = secondImgAddress;
        }

        public String getUninstIconAddr() {
            return uninstIconAddr;
        }

        public void setUninstIconAddr(String uninstIconAddr) {
            this.uninstIconAddr = uninstIconAddr;
        }

        public String getChildappChannel() {
            return childappChannel;
        }

        public void setChildappChannel(String childappChannel) {
            this.childappChannel = childappChannel;
        }

        public String getChildappWapaddr() {
            return childappWapaddr;
        }

        public void setChildappWapaddr(String childappWapaddr) {
            this.childappWapaddr = childappWapaddr;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getUpdateIconAddr() {
            return updateIconAddr;
        }

        public void setUpdateIconAddr(String updateIconAddr) {
            this.updateIconAddr = updateIconAddr;
        }

        public Param getInitParam() {
            return initParam;
        }

        public void setInitParam(Param initParam) {
            this.initParam = initParam;
        }

        public List<Children> getChildrenList() {
            return childrenList;
        }

        public void setChildrenList(List<Children> childrenList) {
            this.childrenList = childrenList;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }
    }

    public class Param implements Serializable {
        private String encryptType;
        private String skey;
        private String ivstr;
        private String subjoinParam;

        public String getEncryptType() {
            return encryptType;
        }

        public void setEncryptType(String encryptType) {
            this.encryptType = encryptType;
        }

        public String getSkey() {
            return skey;
        }

        public void setSkey(String skey) {
            this.skey = skey;
        }

        public String getIvstr() {
            return ivstr;
        }

        public void setIvstr(String ivstr) {
            this.ivstr = ivstr;
        }

        public String getSubjoinParam() {
            return subjoinParam;
        }

        public void setSubjoinParam(String subjoinParam) {
            this.subjoinParam = subjoinParam;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionUpdateTime() {
        return versionUpdateTime;
    }

    public void setVersionUpdateTime(String versionUpdateTime) {
        this.versionUpdateTime = versionUpdateTime;
    }

    public String getDownAddress() {
        return downAddress;
    }

    public void setDownAddress(String downAddress) {
        this.downAddress = downAddress;
    }

    public String getApplyTypeId() {
        return applyTypeId;
    }

    public void setApplyTypeId(String applyTypeId) {
        this.applyTypeId = applyTypeId;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppEntry() {
        return appEntry;
    }

    public void setAppEntry(String appEntry) {
        this.appEntry = appEntry;
    }

    public String getAppClassName() {
        return appClassName;
    }

    public void setAppClassName(String appClassName) {
        this.appClassName = appClassName;
    }

    public String getAppApk() {
        return appApk;
    }

    public void setAppApk(String appApk) {
        this.appApk = appApk;
    }

    public String getChildappType() {
        return childappType;
    }

    public void setChildappType(String childappType) {
        this.childappType = childappType;
    }

    public String getChildappKey() {
        return childappKey;
    }

    public void setChildappKey(String childappKey) {
        this.childappKey = childappKey;
    }

    public String getChildappShowType() {
        return childappShowType;
    }

    public void setChildappShowType(String childappShowType) {
        this.childappShowType = childappShowType;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public String getSecondImgAddress() {
        return secondImgAddress;
    }

    public void setSecondImgAddress(String secondImgAddress) {
        this.secondImgAddress = secondImgAddress;
    }

    public String getUninstIconAddr() {
        return uninstIconAddr;
    }

    public void setUninstIconAddr(String uninstIconAddr) {
        this.uninstIconAddr = uninstIconAddr;
    }

    public String getChildappChannel() {
        return childappChannel;
    }

    public void setChildappChannel(String childappChannel) {
        this.childappChannel = childappChannel;
    }

    public String getChildappWapaddr() {
        return childappWapaddr;
    }

    public void setChildappWapaddr(String childappWapaddr) {
        this.childappWapaddr = childappWapaddr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUpdateIconAddr() {
        return updateIconAddr;
    }

    public void setUpdateIconAddr(String updateIconAddr) {
        this.updateIconAddr = updateIconAddr;
    }

    public Param getInitParam() {
        return initParam;
    }

    public void setInitParam(Param initParam) {
        this.initParam = initParam;
    }

    public List<Children> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<Children> childrenList) {
        this.childrenList = childrenList;
    }
}
