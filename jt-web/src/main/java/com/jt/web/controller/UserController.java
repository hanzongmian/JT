package com.jt.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;
import com.mysql.fabric.Response;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName){
		return moduleName;
	}
	
	//用户注册
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			userService.saveUser(user);
			return SysResult.build(200, "注册成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"新增用户失败！");
	}
	
	//实现用户登陆
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response){
		try {
			//获取用户 秘钥token
			String token =userService.findUserByUP(user); 
			//判断登陆是否有效
			if(StringUtils.isEmpty(token)){
				return SysResult.build(201, "登陆失败！");
			}
			//将token数据保存在cookie中
			Cookie cookie = new Cookie("JT_TICKET",token);
			cookie.setPath("/");   //一般都用 /
			/**
			 * cookie 生命周期 s
			 * 时间大于 0 表示存活时间
			 *  = 0 表示立即删除
			 *  - 1 表示会话结束删除 cookie
			 */
			cookie.setMaxAge(3600*24*7); 
			response.addCookie(cookie);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "用户查询失败");
	}
	
	/**
	 * 退出登陆
	 * ：删除redis缓存  从cookie中获取token，从redis中删除 key
	 * ：删除 cookie  
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		//获取cookie
		Cookie[] cookies = request.getCookies();
		String token  = null;
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())){
				token  = cookie.getValue();
				break;
			}
		}
		//删除redis中的数据
		jedisCluster.del(token);
		//删除cookie
		Cookie cookie  = new Cookie("JT_TICKET","");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		//重定向到系统首页
		return "redirect:/index.html";
	}
	
}















