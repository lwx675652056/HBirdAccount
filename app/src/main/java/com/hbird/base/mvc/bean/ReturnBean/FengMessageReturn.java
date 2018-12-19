package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/16.
 */

public class FengMessageReturn extends BaseReturn {

    private ResultBean result;
    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        private int unreadMessageNumber;
        private List<MessageListBean> messageList;

        public int getUnreadMessageNumber() {
            return unreadMessageNumber;
        }

        public void setUnreadMessageNumber(int unreadMessageNumber) {
            this.unreadMessageNumber = unreadMessageNumber;
        }

        public List<MessageListBean> getMessageList() {
            return messageList;
        }

        public void setMessageList(List<MessageListBean> messageList) {
            this.messageList = messageList;
        }

        public static class MessageListBean {
            private int id;
            private long create_date;
            private int create_by;
            private int user_info_id;
            private String content;
            private int status;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getCreate_date() {
                return create_date;
            }

            public void setCreate_date(long create_date) {
                this.create_date = create_date;
            }

            public int getCreate_by() {
                return create_by;
            }

            public void setCreate_by(int create_by) {
                this.create_by = create_by;
            }

            public int getUser_info_id() {
                return user_info_id;
            }

            public void setUser_info_id(int user_info_id) {
                this.user_info_id = user_info_id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
