package com.qiyu.paygateway.dao;

import com.qiyu.paygateway.base.DaoSupport;
import com.qiyu.paygateway.domain.entity.PayChannelRateInfo;
import com.qiyu.paygateway.domain.entity.PayMerchantBaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Qidi on 2017/1/18.
 */
@Repository
public class PayChannelRateDao {
    @Autowired
    private DaoSupport dao;

    public PayChannelRateInfo findByChannelNameAndPayTypeCode(Map<String,Object> map) throws Exception {
        PayChannelRateInfo payChannelRateInfo = (PayChannelRateInfo) dao.findForObject("PayChannelRateInfoMapper.findByChannelNameAndPayTypeCode", map);
        return payChannelRateInfo;
    }

}
