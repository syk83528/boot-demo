package com.ipaozha.demo1.service.impl;

import com.ipaozha.demo1.dataobject.ProductCategory;
import com.ipaozha.demo1.repository.ProductCategoryRepository;
import com.ipaozha.demo1.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.getOne(categoryId);

    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();

    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
