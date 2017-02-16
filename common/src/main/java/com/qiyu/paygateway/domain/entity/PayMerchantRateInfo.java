package com.qiyu.paygateway.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PayMerchantRateInfo {
    private Long id;

    private Long merchantId;

    private String channelRateName;

    private String channelNo;

    private String channelName;

    private Byte settleType;

    private Integer citicRateTunnel;

    private String payTypeName;

    private BigDecimal channelRate;

    private Byte paySource;

    private Date createAt;

    private Date updateAt;

    private String payTypeCode;

    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannelRateName() {
        return channelRateName;
    }

    public void setChannelRateName(String channelRateName) {
        this.channelRateName = channelRateName;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Byte getSettleType() {
        return settleType;
    }

    public void setSettleType(Byte settleType) {
        this.settleType = settleType;
    }

    public Integer getCiticRateTunnel() {
        return citicRateTunnel;
    }

    public void setCiticRateTunnel(Integer citicRateTunnel) {
        this.citicRateTunnel = citicRateTunnel;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public Byte getPaySource() {
        return paySource;
    }

    public void setPaySource(Byte paySource) {
        this.paySource = paySource;
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

    public String getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}