package com.ipaozha.demo1.service.impl;

import com.ipaozha.demo1.dataobject.ProductInfo;
import com.ipaozha.demo1.dto.CartDto;
import com.ipaozha.demo1.enums.ProductStatusEnum;
import com.ipaozha.demo1.enums.ResultEnum;
import com.ipaozha.demo1.exception.SellException;
import com.ipaozha.demo1.repository.ProductInfoRepository;
import com.ipaozha.demo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;
    @Override
    public ProductInfo findOne(String productId) {
        return repository.getOne(productId);
    }

    @Override
    public List<ProductInfo> findAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = repository.getOne(cartDto.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.商品不存在);
            }
            Integer stock = productInfo.getProductStock() + cartDto.getProductQuantity();
            productInfo.setProductStock(stock);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = repository.getOne(cartDto.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.商品不存在);
            }

            Integer result = productInfo.getProductStock() - cartDto.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.库存不正确);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }
}
