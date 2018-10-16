package com.jt.web.service;

import com.jt.web.pojo.Order;

public interface OrderSrevice {

	String saveOrder(Order order);

	Order findOrderById(String id);

}
