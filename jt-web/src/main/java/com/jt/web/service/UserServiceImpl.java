package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private HttpClientService httpClientService;
	
	private static final ObjectMapper objectMapper =  new  ObjectMapper();
	
	/**
	 * 前台只负责数据展现，不负责数据更新
	 * 如何将数据传给sso单点登录系统
	 * HTTP协议： GET/POST
	 * HTTPClient : java 代码发起http请求
	 */
	@Override
	public void saveUser(User user) {
		String url = "http://sso.jt.com/user/register";
		Map<String,String> params = new HashMap<>();
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		
		//前台通过httpClient将数据通过远程传输，如果后台执行错误！！！ 进行判断返回值结果 ，看是否执行成功
		String result = httpClientService.doPost(url,params);
		try {
			//检查返回值结果
			SysResult sysResult =objectMapper.readValue(result, SysResult.class);
			if(sysResult.getStatus() !=200){
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 *  ：定义远程URL
	 *  ：封装 参数
	 *  ：发起HTTPclient 请求， 返回sysresultJSON
	 *  ：判断返回值是否正确
	 *  ：返回值正确 获取返回值的token值
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		String url= "http://sso.jt.com/user/login";
		Map<String,String> params = new HashMap<>();
		params.put("username", user.getUsername());
		params.put("password",user.getPassword() );
		String resultJSON = httpClientService.doPost(url, params);
		try {
			SysResult sysResult =
					objectMapper.readValue(resultJSON, SysResult.class);
			if(sysResult.getStatus() == 200){
				//获取token数据
				token = (String) sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
}
