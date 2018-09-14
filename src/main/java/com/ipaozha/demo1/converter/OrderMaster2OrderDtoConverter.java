package com.ipaozha.demo1.converter;

import com.ipaozha.demo1.dataobject.OrderMaster;
import com.ipaozha.demo1.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderMaster2OrderDtoConverter {
    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream()
                .map(e ->
                    convert(e)
                ).collect(Collectors.toList());
    }
}
