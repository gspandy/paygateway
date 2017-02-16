package com.qiyu.paygateway.dao;

import com.qiyu.paygateway.base.DaoSupport;
import com.qiyu.paygateway.domain.entity.PayMerchantBaseInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Qidi on 2017/1/18.
 */
@Repository
public class PayMerchantBaseDao {
    @Autowired
    private DaoSupport dao;

    public PayMerchantBaseInfo findByMerchantNo(@Param("merchantNo") String merchantNo) throws Exception {
        PayMerchantBaseInfo payMerchantBaseInfo = (PayMerchantBaseInfo) dao.findForObject("PayMerchantBaseInfoMapper.findByMerchantNo", merchantNo);
        return payMerchantBaseInfo;
    }

    public PayMerchantBaseInfo findById(Long id) throws Exception {
        PayMerchantBaseInfo payMerchantBaseInfo = (PayMerchantBaseInfo) dao.findForObject("PayMerchantBaseInfoMapper.findById", id);
        return payMerchantBaseInfo;
    }

}
