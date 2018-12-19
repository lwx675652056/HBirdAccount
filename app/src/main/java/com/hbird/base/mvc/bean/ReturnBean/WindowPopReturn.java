package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.Date;
import java.util.List;

/**
 * Created by Liul on 2018/10/19.
 */

public class WindowPopReturn extends BaseReturn {

    private String code;
    private String msg;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private InviteCountBean inviteCount;
        private List<ActivityBean> activity;

        public InviteCountBean getInviteCount() {
            return inviteCount;
        }

        public void setInviteCount(InviteCountBean inviteCount) {
            this.inviteCount = inviteCount;
        }

        public List<ActivityBean> getActivity() {
            return activity;
        }

        public void setActivity(List<ActivityBean> activity) {
            this.activity = activity;
        }

        public static class InviteCountBean {
            private int inviteFriendsAware;
            private int inviteCount;

            public int getInviteFriendsAware() {
                return inviteFriendsAware;
            }

            public void setInviteFriendsAware(int inviteFriendsAware) {
                this.inviteFriendsAware = inviteFriendsAware;
            }

            public int getInviteCount() {
                return inviteCount;
            }

            public void setInviteCount(int inviteCount) {
                this.inviteCount = inviteCount;
            }
        }

        public static class ActivityBean {
            private String homeWindowName;
            private String jumpType;
            private String connectionAddress;
            private String androidShowVersion;
            private String iosShowVersion;
            private String smallprogramShowVersion;
            private String image;
            private String shareTitle;
            private String shareContent;
            private String shareImage;
            private Long downtime;
            private Long uptime;
            private int priority;
            private String id;

            public String getHomeWindowName() {
                return homeWindowName;
            }

            public void setHomeWindowName(String homeWindowName) {
                this.homeWindowName = homeWindowName;
            }

            public String getJumpType() {
                return jumpType;
            }

            public void setJumpType(String jumpType) {
                this.jumpType = jumpType;
            }

            public String getConnectionAddress() {
                return connectionAddress;
            }

            public void setConnectionAddress(String connectionAddress) {
                this.connectionAddress = connectionAddress;
            }

            public String getAndroidShowVersion() {
                return androidShowVersion;
            }

            public void setAndroidShowVersion(String androidShowVersion) {
                this.androidShowVersion = androidShowVersion;
            }

            public String getIosShowVersion() {
                return iosShowVersion;
            }

            public void setIosShowVersion(String iosShowVersion) {
                this.iosShowVersion = iosShowVersion;
            }

            public String getSmallprogramShowVersion() {
                return smallprogramShowVersion;
            }

            public void setSmallprogramShowVersion(String smallprogramShowVersion) {
                this.smallprogramShowVersion = smallprogramShowVersion;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getShareTitle() {
                return shareTitle;
            }

            public void setShareTitle(String shareTitle) {
                this.shareTitle = shareTitle;
            }

            public String getShareContent() {
                return shareContent;
            }

            public void setShareContent(String shareContent) {
                this.shareContent = shareContent;
            }

            public String getShareImage() {
                return shareImage;
            }

            public void setShareImage(String shareImage) {
                this.shareImage = shareImage;
            }

            public Long getDowntime() {
                return downtime;
            }

            public void setDowntime(Long downtime) {
                this.downtime = downtime;
            }

            public Long getUptime() {
                return uptime;
            }

            public void setUptime(Long uptime) {
                this.uptime = uptime;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
