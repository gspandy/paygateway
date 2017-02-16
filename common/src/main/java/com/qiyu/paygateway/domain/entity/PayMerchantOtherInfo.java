package com.qiyu.paygateway.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayMerchantOtherInfo implements Serializable {
    private Long id;

    private Long merchantId;

    private String citicIndustryCategory;

    private String industrialIndustryCategory;

    private Byte merchantType;

    private String citicCanalFlag;

    private BigDecimal industrialMinAmt;

    private BigDecimal industrialMaxAmt;

    private String industrialCanalFlag;

    private Date createAt;

    private Date updateAt;

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

    public String getCiticIndustryCategory() {
        return citicIndustryCategory;
    }

    public void setCiticIndustryCategory(String citicIndustryCategory) {
        this.citicIndustryCategory = citicIndustryCategory == null ? null : citicIndustryCategory.trim();
    }

    public String getIndustrialIndustryCategory() {
        return industrialIndustryCategory;
    }

    public void setIndustrialIndustryCategory(String industrialIndustryCategory) {
        this.industrialIndustryCategory = industrialIndustryCategory == null ? null : industrialIndustryCategory.trim();
    }

    public Byte getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(Byte merchantType) {
        this.merchantType = merchantType;
    }

    public String getCiticCanalFlag() {
        return citicCanalFlag;
    }

    public void setCiticCanalFlag(String citicCanalFlag) {
        this.citicCanalFlag = citicCanalFlag == null ? null : citicCanalFlag.trim();
    }

    public BigDecimal getIndustrialMinAmt() {
        return industrialMinAmt;
    }

    public void setIndustrialMinAmt(BigDecimal industrialMinAmt) {
        this.industrialMinAmt = industrialMinAmt;
    }

    public BigDecimal getIndustrialMaxAmt() {
        return industrialMaxAmt;
    }

    public void setIndustrialMaxAmt(BigDecimal industrialMaxAmt) {
        this.industrialMaxAmt = industrialMaxAmt;
    }

    public String getIndustrialCanalFlag() {
        return industrialCanalFlag;
    }

    public void setIndustrialCanalFlag(String industrialCanalFlag) {
        this.industrialCanalFlag = industrialCanalFlag == null ? null : industrialCanalFlag.trim();
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