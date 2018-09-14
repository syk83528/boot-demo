package com.ipaozha.demo1.repository;

import com.ipaozha.demo1.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;


    @Test
    public void findOneTest() {
        ProductCategory productCatregory = repository.getOne(1);
        System.out.println(productCatregory.toString());
//        Assert.assertNotNull(productCatregory);
    }

    @Test
    public void saveTest() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("网易优选");
        productCategory.setCategoryType(3);
        repository.save(productCategory);
    }

    @Test
    public void findListTest() {

        List<Integer> list = Arrays.asList(2, 3);
        List<ProductCategory> byCategoryTypeIn = repository.findByCategoryTypeIn(list);

        for (ProductCategory productCategory : byCategoryTypeIn) {
            System.out.println(productCategory.toString());
        }
    }

}