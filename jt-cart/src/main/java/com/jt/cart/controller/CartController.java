package com.jt.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.cart.pojo.Cart;
import com.jt.cart.service.CartService;
import com.jt.common.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@RequestMapping("/query/{userId}")
	@ResponseBody
	public SysResult findCartByUserId(
			@PathVariable Long userId){
		try {
			List<Cart> cartList = cartService.findCartByUserId(userId);
			return SysResult.oK(cartList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "购物车查询失败");
	}
	
	//商品加入购物车
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveCart(String cartJSON){
		try {
			 Cart cart = objectMapper.readValue(cartJSON,Cart.class);
			cartService.saveCart(cart);
		return 	SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "加入购物车失败！");
	}
	
	//
	@RequestMapping("/update/num/{userId}/{itemId}/{num}")
	@ResponseBody
	public SysResult updatsCartNum(
			@PathVariable Long userId,
			@PathVariable Long itemId,
			@PathVariable Integer num){
		try {
			Cart cart =  new Cart();
			cart.setUserId(userId);
			cart.setItemId(itemId);
			cart.setNum(num);
			cartService.updatsCartNum(cart);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "修改购物车数量失败");
	}
	
	@RequestMapping("/delete/{userId}/{itemId}")
	@ResponseBody
	public SysResult deleteCart(@PathVariable Long userId,@PathVariable Long itemId){
		try {
			cartService.deleteCart(userId,itemId);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"购物车删除失败");
		
	}
}

























