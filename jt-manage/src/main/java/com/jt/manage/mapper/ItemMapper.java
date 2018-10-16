package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{

	int findCount();
	
	/**
	 * MyBatis 不允许多值传参 ，可以将多值封装为单值
	 * 		1.封装为pojo
	 * 		2.封装为map map<key,value >   
	 * 			@Param("start")int start 底层实现封装为map集合
	 * 		3.封装为Array数组/List集合
	 * @param start
	 * @param rows
	 * @return
	 */
	List<Item> findItemByPage(@Param("start")int start,@Param("rows")int rows);

	@Select("select name from tb_item_cat where id=#{itemId}")
	String findIteamCatNameById(Long itemId);

	void updateStatus (@Param("ids")Long[] ids,@Param("status")int status);
	
	
}
