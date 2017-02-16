package com.qiyu.paygateway.domain.entity;

import java.io.Serializable;
import java.util.Date;

public class PayMerchantBaseInfo implements Serializable {
    private Long id;

    private Long canalId;

    private String  merchantNo;

    private String merchantName;

    private String merchantShortName;

    private String leaderName;

    private String leaderPhone;

    private Integer merchantType;

    private String servicePhone;

    private String email;

    private String province;

    private String city;

    private String address;

    private Integer businessEntity;

    private String wechatCategory;

    private String wechatMerchantId;

    private String alipayCategory;

    private String alipayPid;

    private Date createAt;

    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCanalId() {
        return canalId;
    }

    public void setCanalId(Long canalId) {
        this.canalId = canalId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName == null ? null : merchantShortName.trim();
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName == null ? null : leaderName.trim();
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone == null ? null : leaderPhone.trim();
    }


    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone == null ? null : servicePhone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }


    public String getWechatCategory() {
        return wechatCategory;
    }

    public void setWechatCategory(String wechatCategory) {
        this.wechatCategory = wechatCategory == null ? null : wechatCategory.trim();
    }

    public String getWechatMerchantId() {
        return wechatMerchantId;
    }

    public void setWechatMerchantId(String wechatMerchantId) {
        this.wechatMerchantId = wechatMerchantId == null ? null : wechatMerchantId.trim();
    }

    public String getAlipayCategory() {
        return alipayCategory;
    }

    public void setAlipayCategory(String alipayCategory) {
        this.alipayCategory = alipayCategory == null ? null : alipayCategory.trim();
    }

    public String getAlipayPid() {
        return alipayPid;
    }

    public void setAlipayPid(String alipayPid) {
        this.alipayPid = alipayPid == null ? null : alipayPid.trim();
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

    public Integer getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(Integer merchantType) {
        this.merchantType = merchantType;
    }

    public Integer getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(Integer businessEntity) {
        this.businessEntity = businessEntity;
    }

}