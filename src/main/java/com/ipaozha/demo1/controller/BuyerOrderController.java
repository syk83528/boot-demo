package com.ipaozha.demo1.controller;


import com.ipaozha.demo1.VO.ResultVO;
import com.ipaozha.demo1.converter.OrderForm2OrderDTOConverter;
import com.ipaozha.demo1.dto.OrderDTO;
import com.ipaozha.demo1.enums.ResultEnum;
import com.ipaozha.demo1.exception.SellException;
import com.ipaozha.demo1.form.OrderForm;
import com.ipaozha.demo1.service.BuyerService;
import com.ipaozha.demo1.service.OrderService;
import com.ipaozha.demo1.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @RequestMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", orderForm);
            throw new SellException(ResultEnum.参数不正确.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (orderDTO == null) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.购物车不能为空);
        }

        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        return ResultVOUtil.success(map);
    }

    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.参数不正确);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDTO> list = orderService.findList(openid, pageRequest);
        return ResultVOUtil.success(list.getContent());
    }


    //订单详情
    @GetMapping("/detail")
    public ResultVO detail(String openid, String orderid) {
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderid);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单

    @RequestMapping("/cancel")
    public ResultVO cancel(String openid, String orderid) {
        buyerService.cancelOrder(openid, orderid);
        return ResultVOUtil.success();
    }

}
