package com.ziheng.cloud.service;

import com.ziheng.cloud.entities.Order;

public interface OrderService {
    /**
     * 创建订单
     */
    void create(Order order);

}
