package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/26.
 */

public class AcceptFirendReq extends BaseBean {
    private String bookId;//账本id
    private String bookName;//账本名称
    private String typeBudget;//账本的预算类型
    private String abTypeId;//账本的abTypeId
    private String username;//邀请人的昵称
    private String userAvatar;//邀请人的头像
    private String createId;//账本创建人id

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getTypeBudget() {
        return typeBudget;
    }

    public void setTypeBudget(String typeBudget) {
        this.typeBudget = typeBudget;
    }

    public String getAbTypeId() {
        return abTypeId;
    }

    public void setAbTypeId(String abTypeId) {
        this.abTypeId = abTypeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Override
    public String toString() {
        return "{" +
                "bookId:'" + bookId + '\'' +
                ", bookName:'" + bookName + '\'' +
                ", typeBudget:'" + typeBudget + '\'' +
                ", abTypeId:'" + abTypeId + '\'' +
                ", username:'" + username + '\'' +
                ", userAvatar:'" + userAvatar + '\'' +
                ", createId:'" + createId + '\'' +
                '}';
    }
}
