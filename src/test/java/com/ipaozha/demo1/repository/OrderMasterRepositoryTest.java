package com.ipaozha.demo1.repository;

import com.ipaozha.demo1.dataobject.OrderMaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void testSave() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("eeeeeee");
        orderMaster.setBuyerName("苏义坤");
        orderMaster.setBuyerPhone("18857756012");
        orderMaster.setBuyerAddress("杭州市");
        orderMaster.setBuyerOpenid("sykopenid");
        orderMaster.setOrderAmount(new BigDecimal(300.00));
        orderMaster.setOrderStatus(0);
        orderMaster.setPayStatus(0);

        repository.save(orderMaster);
    }

    @Test
    public void findByBuyerOpenid() {

        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<OrderMaster> result = repository.findByBuyerOpenid("sykopenid", pageRequest);

        System.out.println(result.getTotalElements());
    }
}