package com.ipaozha.demo1.service;

import com.ipaozha.demo1.dto.OrderDTO;

/**
 * 买家接口
 */
public interface BuyerService {
    /**
     * 查询一个订单
     */
    OrderDTO findOrderOne(String openid, String orderid);

    /**
     * 取消订单
     */
    OrderDTO cancelOrder(String openid, String orderid);


}
