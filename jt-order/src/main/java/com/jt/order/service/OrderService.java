package com.jt.order.service;

import com.jt.order.pojo.Order;

public interface OrderService {

	Order findOrderById(String orderId);

	String saveOrder(Order order);

}
