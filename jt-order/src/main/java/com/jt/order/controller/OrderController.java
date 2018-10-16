package com.jt.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.SysResult;
import com.jt.order.pojo.Order;
import com.jt.order.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	private static final ObjectMapper objectMapper =  new  ObjectMapper();
	
	//实现后台订单入库
	@RequestMapping("/create")
	@ResponseBody
	public SysResult create(String orderJSON){
		try {
			//json 转 对象
			Order order = objectMapper.readValue(orderJSON, Order.class);
			String orderId = orderService.saveOrder(order);
			if(StringUtils.isEmpty(orderId)){
				throw new RuntimeException();
			}
			return SysResult.oK(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "订单提交失败");
	}
	
	//
	@RequestMapping("/query/{orderId}")
	@ResponseBody
	public Order findOrderById(@PathVariable String orderId){
		return orderService.findOrderById(orderId);
	}
}
