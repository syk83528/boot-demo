package com.ipaozha.demo1.service.impl;

import com.ipaozha.demo1.converter.OrderMaster2OrderDtoConverter;
import com.ipaozha.demo1.dataobject.OrderDetail;
import com.ipaozha.demo1.dataobject.OrderMaster;
import com.ipaozha.demo1.dataobject.ProductInfo;
import com.ipaozha.demo1.dto.CartDto;
import com.ipaozha.demo1.dto.OrderDTO;
import com.ipaozha.demo1.enums.OrderStatusEnum;
import com.ipaozha.demo1.enums.PayStatusEnum;
import com.ipaozha.demo1.enums.ResultEnum;
import com.ipaozha.demo1.exception.SellException;
import com.ipaozha.demo1.repository.OrderDetailRepository;
import com.ipaozha.demo1.repository.OrderMasterRepository;
import com.ipaozha.demo1.service.OrderService;
import com.ipaozha.demo1.service.ProductService;
import com.ipaozha.demo1.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal orderTotalAmount = new BigDecimal(0);
        String orderId = KeyUtil.getUniqueKey();
        //1.查询商品
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new  SellException(ResultEnum.商品不存在);
            }
            //2.计算订单总价
            BigDecimal orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()));
            orderTotalAmount = orderTotalAmount.add(orderAmount);
            //3.写入orderdetail
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            //3.1赋值商品详情
            BeanUtils.copyProperties(productInfo, orderDetail);
            //3.2写入orderdetail
            orderDetailRepository.save(orderDetail);
        }
        //4.写入ordermaster
        OrderMaster orderMaster = new OrderMaster();
        //需要先拷贝再设置id等
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.新订单.getCode());
        orderMaster.setPayStatus(PayStatusEnum.未支付.getCode());
        orderMaster.setOrderAmount(orderTotalAmount);
        orderMasterRepository.save(orderMaster);
        //5.扣库存
        List<CartDto> cartDtoList = new ArrayList<>();
        cartDtoList = orderDTO.getOrderDetailList()
                .stream()
                .map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.decreaseStock(cartDtoList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.getOne(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.订单不存在);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.订单详情不存在);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDtoConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.新订单.getCode())) {
            log.error("[取消订单],订单状态不正确,orderId={},orderStatu={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.订单状态不正确);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.已取消.getCode());
        //修改完状态后再进行拷贝
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[订单更新失败],orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.订单更新失败);

        }
        //返回库存
        if (orderDTO.getOrderDetailList().size() <= 0) {
            log.error("[取消订单], 订单中无商品详情,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.订单详情为空);
        }
        List<CartDto> cartDtoList = orderDTO.getOrderDetailList()
                .stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDtoList);

        //如果已支付,需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.已支付.getCode())) {
            //TODO
        }

        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.新订单.getCode())) {
            log.error("[完结订单],订单状态不正确,orderId={},orderStatu={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.订单状态不正确);
        }
        //修改状态
        orderDTO.setOrderStatus(OrderStatusEnum.已完成.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[完结订单],更新失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.订单更新失败);
        }

        return orderDTO;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {

        //判断订单状态
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.新订单.getCode())) {
            log.error("[订单支付],订单状态不正确,orderId={},orderStatu={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.订单状态不正确);
        }
        //判断订单支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.未支付.getCode())) {
            log.error("[订单支付],订单支付状态不正确,orderId={},payStatus={}",orderDTO.getOrderId(),orderDTO.getPayStatus());
            throw new SellException(ResultEnum.订单支付状态不正确);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.已支付.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[订单支付],更新失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.订单更新失败);
        }
        return orderDTO;
    }
}
