package com.jt.manage.controller.web;

import java.io.IOException;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.manage.pojo.User;

@Controller
@RequestMapping("/web")
public class WebJSONPController {

	//@RequestMapping(value="/testJSONP" ,produces="text/html;charset=utf-8")
	//@ResponseBody
	public String jsonp(String callback) throws IOException{
		User user =  new User();
		user.setId(100);
		
		ObjectMapper objectaMapper = new ObjectMapper();
		String userJSONP = objectaMapper.writeValueAsString(user);
		
		return callback+"("+ userJSONP + ")";
	}
	
	//Spring 
	@RequestMapping(value="/testJSONP")
	@ResponseBody
	public MappingJacksonValue jsonpSuper(String callback){
		User user =  new User();
		user.setId(100);
		
		MappingJacksonValue value = new MappingJacksonValue(user);
		value.setJsonpFunction(callback);
		return value;
		
	}
	
}


















