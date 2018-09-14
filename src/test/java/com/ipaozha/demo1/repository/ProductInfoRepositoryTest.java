package com.ipaozha.demo1.repository;

import com.ipaozha.demo1.dataobject.ProductCategory;
import com.ipaozha.demo1.dataobject.ProductInfo;
import org.junit.Assert;
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
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    public void findByProductStatus() {
        List<ProductInfo> list = repository.findByProductStatus(0);

        Assert.assertNotEquals(0,list.size());
    }

    @Test
    public void testSave() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("臭豆腐");
        productInfo.setProductPrice(new BigDecimal(10.8));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("香香的臭豆腐啊");
        productInfo.setProductIcon("https://avatar.csdn.net/0/6/8/3_fengsh998.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(3);

        ProductInfo productInfo1 = repository.save(productInfo);
        Assert.assertNotNull(productInfo);
    }

    @Test
    public void testGet() {
        ProductInfo productInfo = repository.getOne("123456");

        Assert.assertNotNull(productInfo);
    }
}