package com.ipaozha.demo1.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    新订单(0),
    已完成(1),
    已取消(2);

    private Integer code;

    OrderStatusEnum(Integer code) {
        this.code = code;
    }
}
