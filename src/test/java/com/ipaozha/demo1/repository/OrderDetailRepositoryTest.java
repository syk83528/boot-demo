package com.ipaozha.demo1.repository;

import com.ipaozha.demo1.dataobject.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;
    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetailList = repository.findByOrderId("123456");

        for (OrderDetail orderDetail : orderDetailList) {
            System.out.println(orderDetail.toString());
        }
    }

    @Test
    public void testSave() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123123");
        orderDetail.setOrderId("123456");
        orderDetail.setProductId("asdasd");
        orderDetail.setProductName("小米手机");
        orderDetail.setProductPrice(new BigDecimal(234));
        orderDetail.setProductQuantity(2);
        orderDetail.setProductIcon("asdasd");

        repository.save(orderDetail);
    }
}