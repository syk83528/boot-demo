package com.ipaozha.demo1.enums;

import lombok.Getter;

/**
 * 商品状态
 */
@Getter
public enum ProductStatusEnum {
    UP(0),
    DOWN(1);


    private Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }
}
