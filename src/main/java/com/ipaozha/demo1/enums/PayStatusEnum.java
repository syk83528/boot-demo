package com.ipaozha.demo1.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum {
    未支付(0),
    已支付(1);

    private Integer code;

    PayStatusEnum(Integer code) {
        this.code = code;
    }
}
