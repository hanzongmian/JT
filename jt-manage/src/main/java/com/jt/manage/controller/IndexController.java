package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/index")
	private String index(){
		
		return "index";
	}
	
	/**
	 * 通过restFull 实现页面跳转
	 * url: /page/item-add
	 * 		/page/item-list
	 * 	-获取动态变化的参数，实现页面动态跳转
	 *  -格式要求：参数拼接在URL中，以“/”分割
	 *  		参数位置固定
	 *  		后台服务端接收参数使用{}包裹变量  使用@PathVariable注解实现动态获取
	 *   
	 *   @RequestMapping("/page/
	 *   localhost：8080/addUser/1/tom/18
	 *   public String addUser(@PathVariable int id,String name,int age)
	 */
	@RequestMapping("/page/{moudle}")
	public String module(@PathVariable String moudle){
		return moudle;
	}
}
