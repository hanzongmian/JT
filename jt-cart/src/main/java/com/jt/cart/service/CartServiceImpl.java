package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long userId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		return cartMapper.select(cart);
	}
	
	/**
	 * 购物车新增商品
	 */
	@Override
	public void saveCart(Cart cart){
		//根据itemId和userId查询数据库
		Cart cartDB = cartMapper.findCartByUI(cart);
		if(cartDB == null){
			//表示用户没有购买过该商品 做新增操作
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else{
			//表示用户购买过商品 做数量的更新
			int num = cart.getNum() + cartDB.getNum();
			cartDB.setNum(num);
			cartDB.setUpdated(new Date());
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}
	}

	@Override
	public void updatsCartNum(Cart cart) {
		cart.setUpdated(new Date());
		cartMapper.updateCartNum(cart);
		
	}

	@Override
	public void deleteCart(Long userId, Long itemId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cartMapper.delete(cart);
	}
	
}












