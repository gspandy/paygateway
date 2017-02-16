package com.qiyu.paygateway.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayChannelRateInfo implements Serializable {
    private Long id;

    private String channelRateName;

    private String channelNo;

    private String channelName;
    
    private Byte settleType;

    private Integer citicRateTunnel;

    private String payTypeCode;
    
    private Integer sort;

    private String payTypeName;

    private BigDecimal channelRate;

    private Integer paySource;

    private Date createAt;

    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelRateName() {
        return channelRateName;
    }

    public void setChannelRateName(String channelRateName) {
        this.channelRateName = channelRateName == null ? null : channelRateName.trim();
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo == null ? null : channelNo.trim();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
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
        this.payTypeName = payTypeName == null ? null : payTypeName.trim();
    }
    
    

    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public Integer getPaySource() {
        return paySource;
    }

    public void setPaySource(Integer paySource) {
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
}