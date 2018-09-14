package com.ipaozha.demo1.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipaozha.demo1.dataobject.OrderDetail;
import com.ipaozha.demo1.dto.OrderDTO;
import com.ipaozha.demo1.enums.ResultEnum;
import com.ipaozha.demo1.exception.SellException;
import com.ipaozha.demo1.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTOConverter {
    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.参数不正确);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
