package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;
import com.jt.order.pojo.Order;
import com.jt.order.pojo.OrderItem;
import com.jt.order.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderShippingMapper orderShippingMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;


	public String saveOrder(Order order){
		String orderId = ""+order.getUserId()+System.currentTimeMillis();

		Date date = new Date();

		//实现订单入库操作
		order.setOrderId(orderId);
		order.setStatus(1);
		order.setCreated(date);
		order.setUpdated(date);
		orderMapper.insert(order);

		//
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);

		//
		List<OrderItem> orderItemList = order.getOrderItems();
		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}

		return orderId;
	}


	@Override
	public Order findOrderById(String orderId) {
		Order order = orderMapper.selectByPrimaryKey(orderId);
		
		OrderShipping orserShipping = orderShippingMapper.selectByPrimaryKey(orderId);
		
		OrderItem orderItem = new OrderItem();
		
		orderItem.setOrderId(orderId);
		
		List<OrderItem> orderItemList = orderItemMapper.select(orderItem) ;
		
		//将数据封装
		order.setOrderShipping(orserShipping);
		order.setOrderItems(orderItemList);
		return order;

	}
}



















