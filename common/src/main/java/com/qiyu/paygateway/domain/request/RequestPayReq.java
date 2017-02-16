package com.qiyu.paygateway.domain.request;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Qidi on 2016/12/10.
 */
public class RequestPayReq implements Serializable {
    /**
     * 渠道来源
     */
    private String source;
    /**
     * 商户号
     */
    private String merchantCode;
    /**
     * 平台（ali,weixin）
     */
    private String platForm;
    /**
     * 支付类型（scanCode扫码，barCode条码，publicCode公众号）
     */
    private String payType;
    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 金额
     */
    private String totalAmount;
    /**
     * 授权码
     */
    private String authCode;
    /**
     * 前台通知地址
     */
    private String notifyUrl;
    /**
     * 异步回调地址
     */
    private String callBackUrl;
    /**
     * 描述
     */
    private String subject;

    /**
     * 商户端微信公众号Id
     */
    private String subAppId;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }
}
