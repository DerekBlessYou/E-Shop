package com.derek.order.OrderService.impl;

import com.derek.order.OrderService.OrderService;
import com.derek.order.client.ProductClient;
import com.derek.order.dataobject.OrderDetail;
import com.derek.order.dataobject.OrderMaster;
import com.derek.order.dataobject.ProductInfo;
import com.derek.order.dto.CartDTO;
import com.derek.order.dto.OrderDTO;
import com.derek.order.enums.OrderStatus;
import com.derek.order.enums.PayStatus;
import com.derek.order.repository.OrderDetailRepository;
import com.derek.order.repository.OrderMasterRepository;
import com.derek.order.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        // TODO 1.参数校验
        //  2.查询商品信息（调用商品服务）
        //  3.计算总价
        //  4.扣库存（调用商品服务）

        //生成订单id
        String orderId = KeyUtil.genUniqueKey();

        //  2.查询商品信息（调用商品服务）
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfo> productInfoList = productClient.listForOrder(productIdList);

        //  3.计算总价
        BigDecimal orderAmout = new BigDecimal(BigInteger.ZERO);
        for(OrderDetail orderDetail : orderDTO.getOrderDetailList()){
            for (ProductInfo productInfo : productInfoList){
                if (orderDetail.getProductId().equals(productInfo.getProductId())){
                    // 计算总价， 单价 * 数量
                    orderAmout = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    //订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

        //  4.扣库存（调用商品服务）
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        //远程调用减少库存(Feign)
        productClient.decreaseStock(cartDTOList);

        // 订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatus.NEW.getCode());
        orderMaster.setPayStatus(PayStatus.WAIT.getCode());

        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }
}
