package com.ipaozha.demo1.service.impl;

import com.ipaozha.demo1.dataobject.OrderDetail;
import com.ipaozha.demo1.dto.OrderDTO;
import com.ipaozha.demo1.enums.OrderStatusEnum;
import com.ipaozha.demo1.enums.PayStatusEnum;
import com.ipaozha.demo1.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "1101110";

    private final String ORDER_ID = "1536828426179545218";
    @Test
    public void create() throws Exception{
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("廖师兄");
        orderDTO.setBuyerAddress("幕课网");
        orderDTO.setBuyerPhone("123456789012");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("12345678");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("zzzzz");
        o2.setProductQuantity(2);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单】result={}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {
        OrderDTO orderDto = orderService.findOne(ORDER_ID);
        Assert.assertNotNull(orderDto);
        log.info("[查询单个订单] result={}", orderDto.toString());
    }

    @Test
    public void findList() {
        PageRequest page = PageRequest.of(0, 2);
        Page<OrderDTO> list = orderService.findList(BUYER_OPENID, page);
        log.info("[查询订单列表] result={}", list);
    }

    @Test
    public void cancel() {
        OrderDTO orderDto = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDto);
        Assert.assertNotNull(result);
    }

    @Test
    public void finish() {
        OrderDTO orderDto = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDto);
        Assert.assertEquals(OrderStatusEnum.已完成.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDto = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDto);
        Assert.assertEquals(PayStatusEnum.已支付.getCode(),result.getPayStatus());
    }
}