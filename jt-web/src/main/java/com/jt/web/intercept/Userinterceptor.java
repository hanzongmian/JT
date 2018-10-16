package com.jt.web.intercept;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.web.pojo.User;
import com.jt.web.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class Userinterceptor implements HandlerInterceptor{

	@Autowired
	private JedisCluster jedisCluter;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 调用controller方法之前拦截
	 *  ture 代表放行  false 代表拦截
	 *  拦截器登陆校验：
	 *  	获取前端cookie ，判断cookie是否有token
	 *  	判断redis是否有json数据
	 *  满足条件则放行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		//
		String token = null;
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())){
				token=cookie.getValue();
				break;
			}
		}
		if(!StringUtils.isEmpty(token)){
			//
			String userJSON = jedisCluter.get(token);
			if(!StringUtils.isEmpty(userJSON)){
				User user = objectMapper.readValue(userJSON, User.class);
				//将数据保存在ThreadLocal中
				UserThreadLocal.set(user);
				return true;
			}
		}
		//
		response.sendRedirect("/user/login.html");
		return false;
	}

	//在业务逻辑执行完成后拦截
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	//在业务逻辑执行之后返回数据给浏览器时拦截
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//防止内存泄漏，关闭ThreadLocal 
		UserThreadLocal.remove();
		
	}

}
