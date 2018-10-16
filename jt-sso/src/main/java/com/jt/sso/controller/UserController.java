package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluter;
	
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public MappingJacksonValue checkUser(
			@PathVariable String param,
      		@PathVariable int type,
      		String callback){
		boolean flag = userService.findCheckUser(param,type);
		MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.oK(flag));
		jacksonValue.setJsonpFunction(callback);
		System.out.println(jacksonValue);
		return jacksonValue;
	}
	
	//编辑sso后台用户新增
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			userService.saveUser(user);
			return SysResult.oK();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "新增失败");
	}
	
	//实现sso用户登录
	@RequestMapping("/login")
	@ResponseBody
	public SysResult findUserByUP(User user){
		try {
			String token = userService.findUserByUP(user);
			
			if(StringUtils.isEmpty(token)){
				return SysResult.build(201, "用户查询失败！");
			}
			return SysResult.oK(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "用户查询失败！");
	}
	
	//根据cookie 查询用户信息
	@RequestMapping("/query/{token}")
	@ResponseBody
	public MappingJacksonValue findUserByTicket(
				@PathVariable String token,
				String callback){
		String userJSON  = jedisCluter.get(token);
		MappingJacksonValue jacksonValue = null;
		if(!StringUtils.isEmpty(userJSON)){
			//redis 中的数据不为空
			jacksonValue = new MappingJacksonValue(SysResult.oK(userJSON));
		}else{
			jacksonValue = new MappingJacksonValue(SysResult.build(201, "查询失败"));
			
		}
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;	
	}
	
}



















