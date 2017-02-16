package com.qiyu.paygateway.domain.request;

import java.io.Serializable;
import java.util.SortedMap;

/**
 * Created by Qidd on 2016/12/10.
 */
public class PayCallBackReq implements Serializable {
    //平台来源（weixin、ali）
    private String platForm;
    //商户号
    private String merchantCode;
    //交易类型
    private String tranType;
    //获取请求数据
    private String callBackStr;
    /**
     * 订单号
     */
    private String outTradeNo;

    private SortedMap<String,String> sortedMap;

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getCallBackStr() {
        return callBackStr;
    }

    public void setCallBackStr(String callBackStr) {
        this.callBackStr = callBackStr;
    }

    public SortedMap<String, String> getSortedMap() {
        return sortedMap;
    }

    public void setSortedMap(SortedMap<String, String> sortedMap) {
        this.sortedMap = sortedMap;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}