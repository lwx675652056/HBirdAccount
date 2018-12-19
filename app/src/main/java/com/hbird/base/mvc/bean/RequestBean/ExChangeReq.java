package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * Created by Liul on 2018/7/10.
 */

public class ExChangeReq extends BaseBean {
    private int type;
    private List<relation> relation;
    private int abTypeId;

    public int getAbTypeId() {
        return abTypeId;
    }

    public void setAbTypeId(int abTypeId) {
        this.abTypeId = abTypeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ExChangeReq.relation> getRelation() {
        return relation;
    }

    public void setRelation(List<ExChangeReq.relation> relation) {
        this.relation = relation;
    }

    public static class relation{
        private String id;
        private int priority;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }
    //relation
}
