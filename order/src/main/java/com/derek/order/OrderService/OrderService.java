package com.derek.order.OrderService;

import com.derek.order.dto.OrderDTO;

public interface OrderService {

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);
}
