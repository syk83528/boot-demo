package com.ipaozha.demo1.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    success(0),
    参数不正确(1, "参数不正确"),
    商品不存在(10, "商品不存在"),
    库存不正确(11, "库存不正确"),
    订单不存在(12, "订单不存在"),
    订单详情不存在(13, "订单详情不存在"),
    订单状态不正确(14, "订单状态不正确"),
    订单更新失败(15, "订单更新失败"),
    订单详情为空(16, "订单详情为空"),
    订单支付状态不正确(17, "订单支付状态不正确"),
    购物车不能为空(18, "购物车不能为空"),
    订单主人错误(19, "该订单不属于当前用户"),
    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code) {
        this.code = code;
    }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
