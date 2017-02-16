package com.qiyu.paygateway.dao;

import com.qiyu.paygateway.base.DaoSupport;
import com.qiyu.paygateway.domain.entity.PayMerchantChannelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Qidi on 2017/1/18.
 */
@Repository
public class PayMerchantChannelDao {
    @Autowired
    private DaoSupport dao;

    public PayMerchantChannelInfo findPayMerchantId(Long merchantId) throws Exception {
        PayMerchantChannelInfo payMerchantChannelInfo = (PayMerchantChannelInfo) dao.findForObject("PayMerchantChannelInfoMapper.findPayMerchantId", merchantId);
        return payMerchantChannelInfo;
    }

}
