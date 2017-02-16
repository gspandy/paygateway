package com.qiyu.paygateway.service;

import com.alibaba.fastjson.JSONObject;
import com.qiyu.bankpay.domain.request.CallBackReq;
import com.qiyu.bankpay.domain.request.cmbc.CMBCBarCodePayReq;
import com.qiyu.bankpay.domain.request.cmbc.CMBCPublicNumPayReq;
import com.qiyu.bankpay.domain.request.cmbc.CMBCScanPayReq;
import com.qiyu.bankpay.domain.result.cmbc.CMBCBarCodePayRlt;
import com.qiyu.bankpay.domain.result.cmbc.CMBCPublicNumPayRlt;
import com.qiyu.bankpay.domain.result.cmbc.CMBCScanPayCallBackRlt;
import com.qiyu.bankpay.domain.result.cmbc.CMBCScanPayRlt;
import com.qiyu.bankpay.domain.result.cttic.CTTICBaseRlt;
import com.qiyu.bankpay.domain.result.cttic.CTTICMicroPayRlt;
import com.qiyu.bankpay.domain.result.cttic.CTTICMicroQueryPayRlt;
import com.qiyu.bankpay.domain.result.industrial.IndustrialWXNotifyCallBackRlt;
import com.qiyu.bankpay.domain.result.kft.KFTPayCallBackRlt;
import com.qiyu.bankpay.domain.result.kft.KFTPayRlt;
import com.qiyu.bankpay.service.CMBCPayService;
import com.qiyu.bankpay.service.CTTICPayService;
import com.qiyu.bankpay.service.IndustrialPayService;
import com.qiyu.bankpay.service.KFTPayService;
import com.qiyu.common.result.ModelResult;
import com.qiyu.common.utils.UuidUtil;
import com.qiyu.paygateway.dao.PayChannelRateDao;
import com.qiyu.paygateway.dao.PayMerchantBaseDao;
import com.qiyu.paygateway.dao.PayMerchantChannelDao;
import com.qiyu.paygateway.dao.PayOrderDao;
import com.qiyu.paygateway.domain.entity.PayChannelRateInfo;
import com.qiyu.paygateway.domain.entity.PayMerchantBaseInfo;
import com.qiyu.paygateway.domain.entity.PayMerchantChannelInfo;
import com.qiyu.paygateway.domain.entity.PayOrder;
import com.qiyu.paygateway.domain.request.PayCallBackReq;
import com.qiyu.paygateway.domain.request.RequestPayReq;
import com.qiyu.paygateway.domain.result.RequestPayRlt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Qidi on 2017/1/11.
 */
@Service("payGateWayService")
public class PayGateWayServiceImpl implements IPayGateWayService {
    @Autowired
    private PayMerchantChannelDao payMerchantChannelDao;
    @Autowired
    private PayMerchantBaseDao payMerchantBaseDao;
    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private PayChannelRateDao payChannelRateDao;
    @Autowired
    private IndustrialPayService industrialPayService;
    @Autowired
    private CTTICPayService ctticPayService;
    @Autowired
    private CMBCPayService cmbcPayService;
    @Autowired
    private KFTPayService kftPayService;

    @Value("${pay.callBackUrl}")
    private String callBackUrl;
    @Value("${pay.mchId}")
    private String mchId;

    @Override
    public ModelResult requestPay(RequestPayReq requestPayReq, SortedMap<String, String> map) {
        ModelResult modelResult = new ModelResult();
        try {
            PayMerchantBaseInfo payMerchantBaseInfo = payMerchantBaseDao.findByMerchantNo(requestPayReq.getMerchantCode());
            if (null != payMerchantBaseInfo) {
                PayMerchantChannelInfo payMerchantChannelInfo = payMerchantChannelDao.findPayMerchantId(payMerchantBaseInfo.getId());
                if (null != payMerchantChannelInfo) {
                    if (payMerchantChannelInfo.getChannelName().contains("兴业")) {
                        if (requestPayReq.getPayType().equals("barCode")) {
                            modelResult.setCode(10001);
                            modelResult.setMsg("该通道暂不支持条码支付,请选择其他支付通道！");
                            return modelResult;
                        }
                        if (requestPayReq.getPayType().equals("scanCode")) {
                            String service = null;
                            if (requestPayReq.getPlatForm().equals("ali")) {
                                service = "pay.alipay.native";
                            }
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                service = "pay.weixin.native";
                            }
                            map.put("service", service);
                            map.put("mch_id", payMerchantChannelInfo.getChannelMerchantNo());
                            map.put("nonce_str", UuidUtil.get32UUID());
                            map.put("out_trade_no", requestPayReq.getOutTradeNo());
                            map.put("body", requestPayReq.getOutTradeNo() + "订单");
                            BigDecimal totalAmount = new BigDecimal(requestPayReq.getTotalAmount());
                            totalAmount.setScale(2, BigDecimal.ROUND_DOWN);
                            BigDecimal totalFee = totalAmount.multiply(new BigDecimal(100));
                            map.put("total_fee", String.valueOf(totalFee.intValue()));
                            map.put("mch_create_ip", getIp());
                            map.put("notify_url", callBackUrl);
                            String key = payMerchantChannelInfo.getMerchantKey();
                            ModelResult modelResult1 = industrialPayService.scanPay(map, key);
                            if (modelResult1.getCode() == 0) {
                                PayOrder payOrder = new PayOrder();
                                insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                RequestPayRlt requestPayRlt = new RequestPayRlt();
                                requestPayRlt.setImageUrl((String) modelResult1.getData());
                                modelResult.setData(requestPayRlt);
                                return modelResult;
                            } else {
                                return modelResult1;
                            }
                        }
                        if (requestPayReq.getPayType().equals("publicCode")) {
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                String service = "pay.weixin.jspay";
                                map.put("service", service);
                                map.put("mch_id", payMerchantChannelInfo.getChannelMerchantNo());
                                map.put("nonce_str", UuidUtil.get32UUID());
                                map.put("out_trade_no", requestPayReq.getOutTradeNo());
                                if (null == requestPayReq.getSubject() || "".equals(requestPayReq.getSubject())) {
                                    map.put("body", requestPayReq.getOutTradeNo() + "订单");
                                } else {
                                    map.put("body", requestPayReq.getSubject());
                                }
                                BigDecimal totalAmount = new BigDecimal(requestPayReq.getTotalAmount());
                                totalAmount.setScale(2, BigDecimal.ROUND_DOWN);
                                BigDecimal totalFee = totalAmount.multiply(new BigDecimal(100));
                                map.put("total_fee", String.valueOf(totalFee.intValue()));
                                //TODO
                                 map.put("mch_create_ip",getIp());
                                map.put("notify_url", callBackUrl);
                                String key = payMerchantChannelInfo.getMerchantKey();
                                ModelResult modelResult1 = industrialPayService.industrialInitRequestPay(map, key);
                                if (modelResult1.getCode() == 0) {
                                    PayOrder payOrder = new PayOrder();
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    RequestPayRlt requestPayRlt = new RequestPayRlt();
                                    String returnStr = JSONObject.toJSONString(modelResult1.getData());
                                    requestPayRlt.setReturnStr(returnStr);
                                    modelResult.setData(requestPayRlt);
                                    return modelResult;
                                } else {
                                    return modelResult1;
                                }
                            }
                        }

                    }
                    if (payMerchantChannelInfo.getChannelName().contains("中信")) {
                        if (requestPayReq.getPayType().equals("barCode")) {
                            map.put("service", "unified.trade.micropay");
                            map.put("key", payMerchantChannelInfo.getMerchantKey());
                            map.put("mch_id", payMerchantChannelInfo.getChannelMerchantNo());
                            map.put("nonce_str", UuidUtil.get32UUID());
                            map.put("auth_code", requestPayReq.getAuthCode());
                            map.put("out_trade_no", requestPayReq.getOutTradeNo());
                            if (null == requestPayReq.getSubject() || "".equals(requestPayReq.getSubject())) {
                                map.put("body", requestPayReq.getOutTradeNo() + "订单");
                            } else {
                                map.put("body", requestPayReq.getSubject());
                            }
                            BigDecimal totalAmount = new BigDecimal(requestPayReq.getTotalAmount());
                            totalAmount.setScale(2, BigDecimal.ROUND_DOWN);
                            BigDecimal totalFee = totalAmount.multiply(new BigDecimal(100));
                            map.put("total_fee", String.valueOf(totalFee.intValue()));
                            //TODO
                            map.put("mch_create_ip", getIp());
                            ModelResult microPayResult = ctticPayService.microPay(map);
                            if (microPayResult.getCode() == 1 || microPayResult.getCode() == 200001) {
                                return microPayResult;
                            }
                            //1001是支付返回结果
                            if (microPayResult.getCode() == 1001) {
                                //支付
                                CTTICMicroPayRlt ctticMicroPayRlt = (CTTICMicroPayRlt) microPayResult.getData();
                                PayOrder payOrder = new PayOrder();
                                payOrder.setTradeNo(ctticMicroPayRlt.getOut_transaction_id());
                                payOrder.setPayerId(ctticMicroPayRlt.getOpenid());
                                payOrder.setStatus(0);
                                insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);

                            }
                            if (microPayResult.getCode() == 1002) {
                                //查询订单
                                CTTICMicroQueryPayRlt ctticMicroQueryPayRlt = (CTTICMicroQueryPayRlt) microPayResult.getData();
                                PayOrder payOrder = new PayOrder();
                                payOrder.setTradeNo(ctticMicroQueryPayRlt.getOut_transaction_id());
                                payOrder.setPayerId(ctticMicroQueryPayRlt.getOpenid());
                                payOrder.setStatus(0);
                                insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                            }
                            if (microPayResult.getCode() == 1003) {
                                //冲正(更改支付订单状态)
                                CTTICBaseRlt ctticBaseRlt = (CTTICBaseRlt) microPayResult.getData();
                                modelResult.setMsg("关闭订单成功！");
                                return modelResult;
                            }

                        }
                        if (requestPayReq.getPayType().equals("scanCode")) {
                            modelResult.setCode(10001);
                            modelResult.setMsg("该通道暂不支持扫码支付,请选择其他支付通道！");
                        }
                        if (requestPayReq.getPayType().equals("publicCode")) {
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                CMBCPublicNumPayReq publicNumPayReq = new CMBCPublicNumPayReq();
                                publicNumPayReq.setMerchantCode(payMerchantChannelInfo.getChannelMerchantNo());
                                publicNumPayReq.setReqMsgId(requestPayReq.getOutTradeNo());
                                if (null == requestPayReq.getSubject() || "".equals(requestPayReq.getSubject())) {
                                    publicNumPayReq.setSubject(requestPayReq.getOutTradeNo() + "订单");
                                } else {
                                    publicNumPayReq.setSubject(requestPayReq.getSubject());
                                }
                                publicNumPayReq.setTotalAmount(requestPayReq.getTotalAmount());
                                publicNumPayReq.setCallBack(callBackUrl);
                                ModelResult<CMBCPublicNumPayRlt> modelResult1 = cmbcPayService.publicNumPay(publicNumPayReq);
                                if (null != modelResult1 && null != modelResult1.getData()) {

                                    return modelResult1;
                                } else {
                                    return modelResult1;
                                }
                            }
                        }
                    }
                    if (payMerchantChannelInfo.getChannelName().contains("民生")) {
                        if (requestPayReq.getPayType().equals("barCode")) {
                            CMBCBarCodePayReq cmbcBarCodePayReq = new CMBCBarCodePayReq();
                            cmbcBarCodePayReq.setAuthCode(requestPayReq.getAuthCode());
                            cmbcBarCodePayReq.setCallBack(callBackUrl);
                            cmbcBarCodePayReq.setScene("1");
                            cmbcBarCodePayReq.setMerchantCode(payMerchantChannelInfo.getChannelMerchantNo());
                            cmbcBarCodePayReq.setTotalAmount(requestPayReq.getTotalAmount());
                            cmbcBarCodePayReq.setReqMsgId(requestPayReq.getOutTradeNo());
                            if (null != requestPayReq.getSubject()){
                                cmbcBarCodePayReq.setSubject(requestPayReq.getSubject());
                            }else{
                                cmbcBarCodePayReq.setSubject(requestPayReq.getOutTradeNo()+"订单");
                            }
                            if (requestPayReq.getPlatForm().equals("ali")) {
                                ModelResult<CMBCBarCodePayRlt> modelResult1 = cmbcPayService.barCodePay(cmbcBarCodePayReq);
                                if (null != modelResult1.getData()){
                                    CMBCBarCodePayRlt cmbcBarCodePayRlt = modelResult1.getData();
                                    PayOrder payOrder = new PayOrder();
                                    payOrder.setPayType(1);
                                    payOrder.setPayerId(cmbcBarCodePayRlt.getBuyerId());
                                    payOrder.setPayerName(cmbcBarCodePayRlt.getBuyerAccount());
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }

                            }
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                cmbcBarCodePayReq.setSubAppid(requestPayReq.getSubAppId());
                                ModelResult<CMBCBarCodePayRlt> modelResult1 = cmbcPayService.barCodePay(cmbcBarCodePayReq);
                                if (null != modelResult1.getData()){
                                    CMBCBarCodePayRlt cmbcBarCodePayRlt = modelResult1.getData();
                                    PayOrder payOrder = new PayOrder();
                                    payOrder.setPayType(1);
                                    payOrder.setPayerId(cmbcBarCodePayRlt.getBuyerId());
                                    payOrder.setPayerName(cmbcBarCodePayRlt.getBuyerAccount());
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                        }
                        if (requestPayReq.getPayType().equals("scanCode")) {
                            CMBCScanPayReq cmbcScanPayReq = new CMBCScanPayReq();
                            cmbcScanPayReq.setCallBack(callBackUrl);
                            cmbcScanPayReq.setMerchantCode(payMerchantChannelInfo.getChannelMerchantNo());
                            cmbcScanPayReq.setTotalAmount(requestPayReq.getTotalAmount());
                            cmbcScanPayReq.setReqMsgId(requestPayReq.getOutTradeNo());
                            if (null != requestPayReq.getSubject()){
                                cmbcScanPayReq.setSubject(requestPayReq.getSubject());
                            }else{
                                cmbcScanPayReq.setSubject(requestPayReq.getOutTradeNo()+"订单");
                            }
                            if (requestPayReq.getPlatForm().equals("ali")) {
                                ModelResult<CMBCScanPayRlt> modelResult1 = cmbcPayService.scanPay(cmbcScanPayReq);
                                if (null != modelResult1.getData()){
                                    CMBCScanPayRlt cmbcScanPayRlt = modelResult1.getData();
                                    PayOrder payOrder = new PayOrder();
                                 /*   payOrder.setPayerId(cmbcScanPayRlt.getBuyerId());
                                    payOrder.setPayerName(cmbcScanPayRlt.getBuyerAccount());*/
                                    payOrder.setPayType(2);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    modelResult.setData(cmbcScanPayRlt);
                                    return modelResult;
                                } else{
                                    return modelResult1;
                                }

                            }
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                cmbcScanPayReq.setSubAppid(requestPayReq.getSubAppId());
                                ModelResult<CMBCScanPayRlt> modelResult1 = cmbcPayService.scanPay(cmbcScanPayReq);
                                if (null != modelResult1.getData()){
                                    CMBCScanPayRlt cmbcScanPayRlt = modelResult1.getData();
                                    PayOrder payOrder = new PayOrder();
                                 /*   payOrder.setPayerId(cmbcScanPayRlt.getBuyerId());
                                    payOrder.setPayerName(cmbcScanPayRlt.getBuyerAccount());*/
                                    payOrder.setPayType(2);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                        }
                        if (requestPayReq.getPayType().equals("publicCode")) {
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                CMBCPublicNumPayReq cmbcPublicNumPayReq = new CMBCPublicNumPayReq();
                                cmbcPublicNumPayReq.setCallBack(callBackUrl);
                                cmbcPublicNumPayReq.setMerchantCode(payMerchantChannelInfo.getChannelMerchantNo());
                                cmbcPublicNumPayReq.setTotalAmount(requestPayReq.getTotalAmount());
                                cmbcPublicNumPayReq.setReqMsgId(requestPayReq.getOutTradeNo());
                                ModelResult<CMBCPublicNumPayRlt> modelResult1 = cmbcPayService.publicNumPay(cmbcPublicNumPayReq);
                                if (null != modelResult1.getData()){
                                    PayOrder payOrder = new PayOrder();
                                    payOrder.setPayType(3);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                        }

                    }
                    if (payMerchantChannelInfo.getChannelName().contains("快付通")) {
                        if (requestPayReq.getPayType().equals("barCode")) {
                            map.put("version", "1.3");
                            map.put("tradeType", "cs.pay.submit");
                            map.put("mchId", mchId);
                            map.put("subMchId", payMerchantChannelInfo.getChannelMerchantNo());
                            map.put("key", payMerchantChannelInfo.getMerchantKey());
                            map.put("authCode", requestPayReq.getAuthCode());
                            map.put("outTradeNo", requestPayReq.getOutTradeNo());
                            map.put("notifyUrl", callBackUrl);
                            if (null != requestPayReq.getSubject()) {
                                map.put("body", requestPayReq.getSubject());
                            }
                            map.put("body", requestPayReq.getOutTradeNo() + "订单");
                            map.put("amount", requestPayReq.getTotalAmount());
                            if (requestPayReq.getPlatForm().equals("ali")) {
                                map.put("channel", "alipayMicro");
                                ModelResult<KFTPayRlt> modelResult1 = kftPayService.kftPay(map);
                                if (null != modelResult1.getData()){
                                    PayOrder payOrder = new PayOrder();
                                    //TODO
                                    payOrder.setPayType(3);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                map.put("channel", "wxMicro");
                                ModelResult<KFTPayRlt> modelResult1 = kftPayService.kftPay(map);
                                if (null != modelResult1.getData()){
                                    PayOrder payOrder = new PayOrder();
                                    //TODO
                                    payOrder.setPayType(3);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                        }
                        if (requestPayReq.getPayType().equals("scanCode")) {
                            map.put("version", "1.3");
                            map.put("tradeType", "cs.pay.submit");
                            map.put("mchId", mchId);
                            map.put("subMchId", payMerchantChannelInfo.getChannelMerchantNo());
                            map.put("key", payMerchantChannelInfo.getMerchantKey());
                            map.put("outTradeNo", requestPayReq.getOutTradeNo());
                            map.put("notifyUrl", callBackUrl);
                            if (null != requestPayReq.getSubject()) {
                                map.put("body", requestPayReq.getSubject());

                            } else {
                                map.put("body", requestPayReq.getOutTradeNo() + "订单");
                            }
                            map.put("amount", requestPayReq.getTotalAmount());
                            if (requestPayReq.getPlatForm().equals("ali")) {
                                map.put("channel", "alipayQR");
                                ModelResult<KFTPayRlt> modelResult1 = kftPayService.kftPay(map);
                                if (null != modelResult1.getData()){
                                    PayOrder payOrder = new PayOrder();
                                    //TODO
                                    payOrder.setPayType(3);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                map.put("channel", "wxPubQR");
                                ModelResult<KFTPayRlt> modelResult1 = kftPayService.kftPay(map);
                                if (null != modelResult1.getData()){
                                    PayOrder payOrder = new PayOrder();
                                    //TODO
                                    payOrder.setPayType(3);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                        }
                        if (requestPayReq.getPayType().equals("publicCode")) {
                            if (requestPayReq.getPlatForm().equals("weixin")) {
                                map.put("version", "1.3");
                                map.put("tradeType", "cs.pay.submit");
                                map.put("mchId", mchId);
                                map.put("subMchId", payMerchantChannelInfo.getChannelMerchantNo());
                                map.put("key", payMerchantChannelInfo.getMerchantKey());
                                map.put("outTradeNo", requestPayReq.getOutTradeNo());
                                if (null != requestPayReq.getSubject()) {
                                    map.put("body", requestPayReq.getSubject());
                                } else {
                                    map.put("body", requestPayReq.getOutTradeNo() + "订单");
                                }
                                map.put("amount", requestPayReq.getTotalAmount());
                                map.put("notifyUrl", callBackUrl);
                                ModelResult<KFTPayRlt> modelResult1 = kftPayService.kftPay(map);
                                if (null != modelResult1.getData()){
                                    PayOrder payOrder = new PayOrder();
                                    //TODO
                                    payOrder.setPayType(3);
                                    insertPayOrder(payOrder, requestPayReq, payMerchantBaseInfo, payMerchantChannelInfo);
                                    return modelResult1;
                                } else{
                                    return modelResult1;
                                }
                            }
                        }
                    }
                } else {
                    modelResult.setMsg("该商户未启用此通道或未开通次通道，请联系管理员开通！");
                    modelResult.setCode(10001);
                    return modelResult;
                }
            } else {
                modelResult.setMsg("未找到该商户！");
                modelResult.setCode(10001);
                return modelResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void insertPayOrder(PayOrder payOrder, RequestPayReq requestPayReq, PayMerchantBaseInfo payMerchantBaseInfo, PayMerchantChannelInfo payMerchantChannelInfo) throws Exception {
        payOrder.setOrderNo(requestPayReq.getOutTradeNo());
        payOrder.setMerchantId(payMerchantBaseInfo.getId());
        payOrder.setCanalId(payMerchantBaseInfo.getCanalId());
        payOrder.setChannelNo(payMerchantChannelInfo.getChannelNo());
        BigDecimal orderAmt = new BigDecimal(requestPayReq.getTotalAmount());
        orderAmt = orderAmt.setScale(2, BigDecimal.ROUND_DOWN);
        payOrder.setOrderAmt(orderAmt);
        HashMap<String, Object> param = new HashMap<>();
        param.put("channelNo", payMerchantChannelInfo.getChannelNo());
        param.put("channelName", payMerchantChannelInfo.getChannelName());
        param.put("payTypeCode", requestPayReq.getPayType());
        Integer paySource = 0;
        if (requestPayReq.getPlatForm().equals("ali")) {
            paySource = 1;
        }
        if (requestPayReq.getPlatForm().equals("weixin")) {
            paySource = 2;
        }
        param.put("paySource", paySource);
        PayChannelRateInfo payChannelRateInfo = payChannelRateDao.findByChannelNameAndPayTypeCode(param);
        if (null != payChannelRateInfo) {
            BigDecimal merchantRate = payChannelRateInfo.getChannelRate();
            payOrder.setFeeAmt(orderAmt.add(merchantRate).setScale(4, BigDecimal.ROUND_DOWN));
        }
        payOrder.setReceiverId(payMerchantBaseInfo.getId());
        payOrder.setTradeTime(new Date());
        payOrder.setCallBackUrl(requestPayReq.getCallBackUrl());
        payOrderDao.addPayOrder(payOrder);
    }


    public ModelResult payCallBack1(PayCallBackReq payCallBackReq) throws Exception{
        ModelResult modelResult = new ModelResult();
        PayMerchantBaseInfo payMerchantBaseInfo = payMerchantBaseDao.findByMerchantNo(payCallBackReq.getMerchantCode());
        if (null != payMerchantBaseInfo) {
            PayMerchantChannelInfo payMerchantChannelInfo = payMerchantChannelDao.findPayMerchantId(payMerchantBaseInfo.getId());
            if (null != payMerchantChannelInfo) {
                PayOrder payOrder = payOrderDao.findByOrderNo(payCallBackReq.getOutTradeNo());
                if (null != payOrder) {
                    if (payMerchantChannelInfo.getChannelName().contains("兴业")) {
                        ModelResult modelResult1 = industrialPayService.industrialCallBack(payCallBackReq.getCallBackStr(), payMerchantChannelInfo.getMerchantKey());
                        if (null != modelResult1.getData()) {
                            payOrder.setStatus(0);
                            IndustrialWXNotifyCallBackRlt industrialWXNotifyCallBackRlt = (IndustrialWXNotifyCallBackRlt) modelResult1.getData();
                            payOrder.setPayerId(industrialWXNotifyCallBackRlt.getOpenid());
                            payOrder.setTradeNo(industrialWXNotifyCallBackRlt.getOut_transaction_id());
                            payOrderDao.updatePayOrder(payOrder);
                            modelResult.setData(industrialWXNotifyCallBackRlt);
                            return modelResult;
                        } else {
                            payOrder.setStatus(1);
                            payOrderDao.updatePayOrder(payOrder);
                        }
                        return modelResult1;
                    }
                    if (payMerchantChannelInfo.getChannelName().contains("民生")) {
                        CallBackReq callBackReq = new CallBackReq();
                        callBackReq.setReturnStr(payCallBackReq.getCallBackStr());
                        try {
                          ModelResult modelResult1 = cmbcPayService.scanPayCallBack(callBackReq);
                            if (null != modelResult.getData()) {
                                CMBCScanPayCallBackRlt cmbcScanPayCallBackRlt = (CMBCScanPayCallBackRlt) modelResult.getData();
                                payOrder.setPayerId(cmbcScanPayCallBackRlt.getBuyerId());
                                payOrder.setTradeNo(cmbcScanPayCallBackRlt.getChannelNo());
                                payOrderDao.updatePayOrder(payOrder);
                                modelResult.setData(cmbcScanPayCallBackRlt);
                                return modelResult;
                            } else {
                                payOrder.setStatus(1);
                                payOrderDao.updatePayOrder(payOrder);
                                return modelResult1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            modelResult.setCode(200001);
                            modelResult.setMsg("回调异常");
                            return modelResult;
                        }
                    }if (payMerchantChannelInfo.getChannelName().contains("快付通")){
                        JSONObject jsonObject = JSONObject.parseObject(payCallBackReq.getCallBackStr());
                        Map map = (Map)jsonObject;
                        ModelResult<KFTPayCallBackRlt> modelResult1 =  kftPayService.kftPayCallBack(map,payMerchantChannelInfo.getMerchantKey());
                        if (null != modelResult1 && null != modelResult1.getData()) {
                            KFTPayCallBackRlt kftPayCallBackRlt = (KFTPayCallBackRlt) modelResult1.getData();
                            modelResult.setData(kftPayCallBackRlt);
                            return modelResult;
                        } else {
                            return modelResult1;
                        }
                    }
                } else {
                    modelResult.setMsg("未找到该订单");
                    modelResult.setCode(1);
                    return modelResult;
                }
            } else {
                modelResult.setMsg("该商户未启用此通道或未开通次通道，请联系管理员开通！");
                modelResult.setCode(10001);
                return modelResult;
            }

        } else {
            modelResult.setMsg("未找到该商户！");
            modelResult.setCode(10001);
            return modelResult;
        }
        return modelResult;
    }

    @Override
    public ModelResult payCallBack(PayCallBackReq payCallBackReq) throws Exception{
        ModelResult modelResult = new ModelResult();
        PayOrder payOrder = payOrderDao.findByOrderNo(payCallBackReq.getOutTradeNo());
        if (null != payOrder) {
            PayMerchantBaseInfo payMerchantBaseInfo = payMerchantBaseDao.findById(payOrder.getMerchantId());
            if (null != payMerchantBaseInfo){
                PayMerchantChannelInfo payMerchantChannelInfo = payMerchantChannelDao.findPayMerchantId(payMerchantBaseInfo.getId());
                if (null != payMerchantChannelInfo) {
                    if (payMerchantChannelInfo.getChannelName().contains("兴业")) {
                        ModelResult modelResult1 = industrialPayService.industrialCallBack(payCallBackReq.getCallBackStr(), payMerchantChannelInfo.getMerchantKey());
                        if (null != modelResult1.getData()) {
                            payOrder.setStatus(0);
                            IndustrialWXNotifyCallBackRlt industrialWXNotifyCallBackRlt = (IndustrialWXNotifyCallBackRlt) modelResult1.getData();
                            payOrder.setPayerId(industrialWXNotifyCallBackRlt.getOpenid());
                            payOrder.setTradeNo(industrialWXNotifyCallBackRlt.getOut_transaction_id());
                            payOrderDao.updatePayOrder(payOrder);
                            modelResult.setData(industrialWXNotifyCallBackRlt);
                            return modelResult;
                        } else {
                            payOrder.setStatus(1);
                            payOrder.setErrorMsg(modelResult1.getMsg());
                            payOrderDao.updatePayOrder(payOrder);
                        }
                        return modelResult1;
                    }
                    if (payMerchantChannelInfo.getChannelName().contains("民生")) {
                        CallBackReq callBackReq = new CallBackReq();
                        callBackReq.setReturnStr(payCallBackReq.getCallBackStr());
                        try {
                            ModelResult modelResult1 = cmbcPayService.scanPayCallBack(callBackReq);
                            if (null != modelResult1.getData()) {
                                CMBCScanPayCallBackRlt cmbcScanPayCallBackRlt = (CMBCScanPayCallBackRlt) modelResult1.getData();
                                payOrder.setPayerId(cmbcScanPayCallBackRlt.getBuyerId());
                                payOrder.setTradeNo(cmbcScanPayCallBackRlt.getChannelNo());
                                payOrder.setStatus(0);
                                payOrder.setErrorMsg("交易成功");
                                payOrderDao.updatePayOrder(payOrder);
                                modelResult.setData(cmbcScanPayCallBackRlt);
                                return modelResult;
                            } else {
                                payOrder.setStatus(1);
                                payOrder.setErrorMsg(modelResult1.getMsg());
                                payOrderDao.updatePayOrder(payOrder);
                                return modelResult1;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            modelResult.setCode(200001);
                            modelResult.setMsg("回调异常");
                            payOrder.setStatus(1);
                            payOrder.setErrorMsg("回调异常");
                            payOrderDao.updatePayOrder(payOrder);
                            return modelResult;
                        }
                    }if (payMerchantChannelInfo.getChannelName().contains("快付通")){
                        JSONObject jsonObject = JSONObject.parseObject(payCallBackReq.getCallBackStr());
                        Map map = (Map)jsonObject;
                        ModelResult<KFTPayCallBackRlt> modelResult1 =  kftPayService.kftPayCallBack(map,payMerchantChannelInfo.getMerchantKey());
                        if (null != modelResult1 && null != modelResult1.getData()) {
                            KFTPayCallBackRlt kftPayCallBackRlt = (KFTPayCallBackRlt) modelResult1.getData();
                            modelResult.setData(kftPayCallBackRlt);
                            payOrder.setStatus(0);
                            payOrderDao.updatePayOrder(payOrder);
                            return modelResult;
                        } else {
                            payOrder.setStatus(1);
                            payOrder.setErrorMsg(modelResult1.getMsg());
                            payOrderDao.updatePayOrder(payOrder);
                            return modelResult1;
                        }
                    }
                }
                } else {
                    modelResult.setMsg("该商户未启用此通道或未开通次通道，请联系管理员开通！");
                    modelResult.setCode(10001);
                    return modelResult;
                }

        } else {
            modelResult.setMsg("未找到该订单");
            modelResult.setCode(1);
            return modelResult;
        }
        return modelResult;
    }


    @Override
    public ModelResult merchSign() {
        return null;
    }

    private String getIp() {
        return "127.0.0.1";
    }
}
