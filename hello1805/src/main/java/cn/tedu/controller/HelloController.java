package cn.tedu.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController  //ResponseBody  Controller
public class HelloController {

	@RequestMapping("/hello/{name}")
	public String hello(@PathVariable String name){
		return "springboot hello   : " + name;
	}
	
}
