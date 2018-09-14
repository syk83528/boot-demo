package com.ipaozha.demo1.service.impl;

import com.ipaozha.demo1.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void findOne() {
        ProductInfo serviceOne = productService.findOne("123456");

        Assert.assertNotNull(serviceOne);

        System.out.println(serviceOne.toString());
    }

    @Test
    public void findAll() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<ProductInfo> all = productService.findAll(pageRequest);

        System.out.println(all.getTotalElements());
    }

    @Test
    public void findAll1() {
    }

    @Test
    public void save() {
    }
}