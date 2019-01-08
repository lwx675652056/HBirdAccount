package com.hbird.bean;

/**
 * @author: LiangYX
 * @ClassName: UserInfo
 * @date: 2019/1/7 20:10
 * @Description: 个人信息
 */
public class UserInfo extends BaseBean {

    public long updateDate;
    public String sex;
    public String mobile;
    public String wechatAuth;
    public String nickName;
    public String age;
    public long birthday;
    public String constellation;
    public String provinceId;
    public String provinceName;
    public String cityId;
    public String cityName;
    public String profession;
    public String avatarUrl;
    public long registerDate;
    public String position;
    public int id;
    public Double integrity;

    @Override
    public String toString() {
        return "{" +
                "updateDate:" + updateDate +
                ", sex:'" + sex + '\'' +
                ", mobile:'" + mobile + '\'' +
                ", wechatAuth:'" + wechatAuth + '\'' +
                ", nickName:'" + nickName + '\'' +
                ", age:'" + age + '\'' +
                ", birthday:" + birthday +
                ", constellation:'" + constellation + '\'' +
                ", provinceId:'" + provinceId + '\'' +
                ", provinceName:'" + provinceName + '\'' +
                ", cityId:'" + cityId + '\'' +
                ", cityName:'" + cityName + '\'' +
                ", profession:'" + profession + '\'' +
                ", avatarUrl:'" + avatarUrl + '\'' +
                ", registerDate:" + registerDate +
                ", position:'" + position + '\'' +
                ", id:" + id +
                ", integrity:" + integrity +
                '}';
    }
}
