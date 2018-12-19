package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/7/9.
 */

public class HeaderInfoReturn extends BaseReturn {

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
        private int chargeTotal;
        private int clockInDays;
        private int inviteUsers;
        private long clockInTime;
        private String daysCount;

        public int getInviteUsers() {
            return inviteUsers;
        }

        public void setInviteUsers(int inviteUsers) {
            this.inviteUsers = inviteUsers;
        }

        public int getChargeTotal() {
            return chargeTotal;
        }

        public void setChargeTotal(int chargeTotal) {
            this.chargeTotal = chargeTotal;
        }

        public int getClockInDays() {
            return clockInDays;
        }

        public void setClockInDays(int clockInDays) {
            this.clockInDays = clockInDays;
        }

        public long getClockInTime() {
            return clockInTime;
        }

        public void setClockInTime(long clockInTime) {
            this.clockInTime = clockInTime;
        }

        public String getDaysCount() {
            return daysCount;
        }

        public void setDaysCount(String daysCount) {
            this.daysCount = daysCount;
        }
    }
}
