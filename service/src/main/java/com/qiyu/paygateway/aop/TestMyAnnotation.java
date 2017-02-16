package com.qiyu.paygateway.aop;

/**
 * Created by Qidi on 2017/2/15.
 */


public class TestMyAnnotation {
    @MyAnnotation(date="22010",author = "qidi")
    public String test(){
        return null;
    }

}
