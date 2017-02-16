package com.qiyu.paygateway.service;

import com.qiyu.common.result.ModelResult;
import com.qiyu.paygateway.domain.request.PayCallBackReq;
import com.qiyu.paygateway.domain.request.RequestPayReq;

import java.util.SortedMap;

/**
 * Created by Qidi on 2016/12/15.
 */
public interface IPayGateWayService {
     ModelResult requestPay(RequestPayReq requestPayReq, SortedMap<String,String> map) ;
     ModelResult payCallBack(PayCallBackReq payCallBackReq) throws Exception;
     ModelResult merchSign();
}
