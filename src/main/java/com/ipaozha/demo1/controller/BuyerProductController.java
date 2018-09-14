package com.ipaozha.demo1.controller;

import com.ipaozha.demo1.VO.ProductInfoVO;
import com.ipaozha.demo1.VO.ProductVO;
import com.ipaozha.demo1.VO.ResultVO;
import com.ipaozha.demo1.dataobject.ProductCategory;
import com.ipaozha.demo1.dataobject.ProductInfo;
import com.ipaozha.demo1.service.CategoryService;
import com.ipaozha.demo1.service.ProductService;
import com.ipaozha.demo1.util.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    public ResultVO list() {
        //1. 查询所有上架商品
        List<ProductInfo> productInfoList = productService.findAll();

        //2. 查询类目(一次性查询)
        List<Integer> categoryList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            categoryList.add(productInfo.getCategoryType());
        }
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryList);

//        List<Integer> collect = productInfoList.stream().map(e -> e.getCategoryType())
//                .collect(Collectors.toList());
        //3. 数据拼装
        //外层大数组
        List<ProductVO> list = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            //大数组item的值
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            // 构建foods数组
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);

            list.add(productVO);
        }

        return ResultVOUtil.success(list);
    }
}
