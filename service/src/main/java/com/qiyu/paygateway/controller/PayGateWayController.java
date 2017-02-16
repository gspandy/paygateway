package com.qiyu.paygateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiyu.common.result.ModelResult;
import com.qiyu.common.utils.MediaTypes;
import com.qiyu.paygateway.domain.request.PayCallBackReq;
import com.qiyu.paygateway.domain.request.RequestPayReq;
import com.qiyu.paygateway.service.IPayGateWayService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.InputSource;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Qidi on 2017/2/9.
 */
@Controller
@RequestMapping("gateWay")
public class PayGateWayController {
    @Autowired
    private IPayGateWayService payGateWayService;

    @RequestMapping(value = "pay", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Object pay(HttpServletRequest request,
                      HttpServletResponse response, RequestPayReq requestPayReq) {
        try {
            SortedMap sortedMap = getParameterMap(request);
            return payGateWayService.requestPay(requestPayReq,sortedMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "callBack", produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Object callBack(HttpServletRequest request,
                           HttpServletResponse response){
        PayCallBackReq payCallBackReq = getCallBackReq(request);
        ModelResult modelResult = new ModelResult();
        if (null != payCallBackReq){
            try {
               return  payGateWayService.payCallBack(payCallBackReq);
            } catch (Exception e) {
                e.printStackTrace();
                modelResult.setCode(1);
                modelResult.setMsg("回调失败");
                return modelResult;
            }
        }
        modelResult.setCode(1);
        modelResult.setMsg("没有收到任何回调参数");
        return modelResult;
    }



    private PayCallBackReq getCallBackReq(HttpServletRequest request) {
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        PayCallBackReq callBackReq = new PayCallBackReq();
        if (map.size()>0){
            String returnStr = JSONObject.toJSONString(map);
            callBackReq.setCallBackStr(returnStr);
            if (map.containsKey("outTradeNo")){
                callBackReq.setOutTradeNo(String.valueOf(map.get("outTradeNo")));
            }else if (map.containsKey("reqMsgId")){
                callBackReq.setOutTradeNo(String.valueOf(map.get("reqMsgId")));
            }else if (map.containsKey("out_trade_no")){
                callBackReq.setOutTradeNo(String.valueOf(map.get("out_trade_no")));
            }else
                callBackReq.setOutTradeNo(null);
            return callBackReq;
        }else{
            String requestData = parseRequst(request);
            if (null != requestData){
                callBackReq.setCallBackStr(requestData);
                try {
                    map = toMap(requestData.getBytes(), "utf-8");
                    if (null != map && map.size()>0){
                        if (map.containsKey("outTradeNo")){
                            callBackReq.setOutTradeNo(String.valueOf(map.get("outTradeNo")));
                        }else if (map.containsKey("reqMsgId")){
                            callBackReq.setOutTradeNo(String.valueOf(map.get("reqMsgId")));
                        }else if (map.containsKey("out_trade_no")){
                            callBackReq.setOutTradeNo(String.valueOf(map.get("out_trade_no")));
                        }else
                            callBackReq.setOutTradeNo(null);
                        return callBackReq;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return callBackReq;
            }else{
                return null;
            }
        }
    }

    public static String parseRequst(HttpServletRequest request){
        String body = "";
        try {
            ServletInputStream inputStream = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while(true){
                String info = br.readLine();
                if(info == null){
                    break;
                }
                if(body == null || "".equals(body)){
                    body = info;
                }else{
                    body += info;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    private static SortedMap getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();

        // 返回值Map
        SortedMap returnMap = new TreeMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value.trim());
        }
        return returnMap;
    }

    public static Map<String, String> toMap(byte[] xmlBytes,String charset) throws Exception{
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        Map<String, String> params = toMap(doc.getRootElement());
        return params;
    }

    public static Map<String, String> toMap(Element element){
        Map<String, String> rest = new HashMap<String, String>();
        List<Element> els = element.elements();
        for(Element el : els){
            rest.put(el.getName().toLowerCase(), el.getTextTrim());
        }
        return rest;
    }

}