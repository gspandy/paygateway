package com.qiyu.paygateway.domain.entity;

import java.io.Serializable;
import java.util.Date;

public class PaySubCanal implements Serializable {
    private Long id;

    private String canalName;

    private String canalNo;

    private Date createAt;

    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCanalName() {
        return canalName;
    }

    public void setCanalName(String canalName) {
        this.canalName = canalName == null ? null : canalName.trim();
    }

    public String getCanalNo() {
        return canalNo;
    }

    public void setCanalNo(String canalNo) {
        this.canalNo = canalNo == null ? null : canalNo.trim();
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}