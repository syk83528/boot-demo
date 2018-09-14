package com.ipaozha.demo1.service.impl;

import com.ipaozha.demo1.dto.OrderDTO;
import com.ipaozha.demo1.enums.ResultEnum;
import com.ipaozha.demo1.exception.SellException;
import com.ipaozha.demo1.service.BuyerService;
import com.ipaozha.demo1.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderid) {
        return checkOrderOwner(openid, orderid);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderid) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderid);
        if (orderDTO == null) {
            log.error("[取消订单] 查不到该订单, orderId={}", orderid);
            throw new SellException(ResultEnum.订单不存在);
        }
        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String openid, String orderid) {
        OrderDTO orderDTO = orderService.findOne(orderid);
        if (orderDTO == null) {
            return null;
        }

        if (!orderDTO.getBuyerOpenid().equals(openid)) {
            log.error("[查询订单] 订单的openid不一致, openid={}, orderid={}", openid, orderid);
            throw new SellException(ResultEnum.订单主人错误);
        }
        return orderDTO;
    }
}
