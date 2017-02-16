package com.qiyu.paygateway.domain.entity;

import java.io.Serializable;
import java.util.Date;

public class PayMaterialBaseInfo implements Serializable {
    private Long id;

    private String materialName;

    private Integer materialOrder;

    private Date createAt;

    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    public Integer getMaterialOrder() {
        return materialOrder;
    }

    public void setMaterialOrder(Integer materialOrder) {
        this.materialOrder = materialOrder;
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