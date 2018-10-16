package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUITree;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITree> findItemCatList(@RequestParam(value="id",defaultValue="0") Long parentId){
		// @RequestParam(value="id")  接收参数名与定义不同
		//Long parentId = 0L;
		//return itemCatService.findItemCatList(parentId);
		return itemCatService.findItemCatCache(parentId);
		
	}
}
