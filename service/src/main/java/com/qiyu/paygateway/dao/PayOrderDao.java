package com.qiyu.paygateway.dao;

import com.qiyu.paygateway.base.DaoSupport;
import com.qiyu.paygateway.domain.entity.PayChannelBaseInfo;
import com.qiyu.paygateway.domain.entity.PayMerchantBaseInfo;
import com.qiyu.paygateway.domain.entity.PayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Qidi on 2017/1/18.
 */
@Repository
public class PayOrderDao {
    @Autowired
    private DaoSupport dao;

    public PayOrder findByOrderNo(String orderNo) throws Exception {
        PayOrder payOrder = (PayOrder) dao.findForObject("PayOrderMapper.findByOrderNo", orderNo);
        return payOrder;
    }

    public void addPayOrder(PayOrder payOrder) throws Exception{
        dao.save("PayOrderMapper.insertPayOrder", payOrder);
    }

    public void updatePayOrder(PayOrder payOrder) throws Exception{
        dao.update("PayOrderMapper.updatePayOrder", payOrder);
    }

}
