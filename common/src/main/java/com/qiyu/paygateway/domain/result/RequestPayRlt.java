package com.qiyu.paygateway.domain.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qidi on 2016/9/13.
 */
public class RequestPayRlt implements Serializable {

    /**
     * 返回字符串
     */
    private String returnStr;
    /***
     * 图片路径
     */
    private String imageUrl;


    public String getReturnStr() {
        return returnStr;
    }

    public void setReturnStr(String returnStr) {
        this.returnStr = returnStr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
