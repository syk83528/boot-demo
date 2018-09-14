package com.ipaozha.demo1.dto;

import lombok.Data;

@Data
public class CartDto {
    /** 商品id */
    private String productId;
    /** 商品数量 */
    private Integer productQuantity;

    public CartDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
