package com.ipaozha.demo1.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ipaozha.demo1.dataobject.OrderDetail;
import com.ipaozha.demo1.enums.OrderStatusEnum;
import com.ipaozha.demo1.enums.PayStatusEnum;
import com.ipaozha.demo1.util.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    /**
     * id
     */
    @Id
    private String orderId;
    /**
     * 买家名字
     */
    private String buyerName;
    /**
     * 买家电话
     */
    private String buyerPhone;
    /**
     * 买家地址
     */
    private String buyerAddress;
    /**
     * 买家微信openid
     */
    private String buyerOpenid;
    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;
    /**
     * 订单状态, 默认为新下单
     */
    private Integer orderStatus = OrderStatusEnum.新订单.getCode();
    /**
     * 支付状态, 默认未支付
     */
    private Integer payStatus = PayStatusEnum.未支付.getCode();
    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;


    private List<OrderDetail> orderDetailList = new ArrayList<>();
}
