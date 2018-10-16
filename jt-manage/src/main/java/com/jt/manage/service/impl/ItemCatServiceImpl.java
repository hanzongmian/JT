package com.jt.manage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.EasyUITree;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

import redis.clients.jedis.JedisCluster;


@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private ItemCatMapper itemCatMapper;

	@Autowired
	private JedisCluster jedisCluster;
	
	//private RedisService redisService;
	//private Jedis jedis;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public List<EasyUITree> findItemCatList(Long parentId) {
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parentId);

		List<ItemCat> itemCatList = itemCatMapper.select(itemCat);

		List<EasyUITree> treeList = new ArrayList<>();

		for(ItemCat itemCatTemp : itemCatList){
			EasyUITree easyUITree = new EasyUITree();

			easyUITree.setId(itemCatTemp.getId());
			easyUITree.setText(itemCatTemp.getName());
			String state = itemCatTemp.getIsParent() ? "closed" : "open";

			easyUITree.setState(state);
			treeList.add(easyUITree);
		}

		return treeList;
	}

	/**
	 * 查询缓存
	 * 查询是否有数据
	 * 如果没有查询数据库，将结果转化为JSON 串，保存在redis
	 * 如果有数据，将json串转化为JSON对象
	 */
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		String key = "ITEM_CAT_"+ parentId;
		String result = jedisCluster.get(key);
		List<EasyUITree> treeList = null;
		try {
			if(StringUtils.isEmpty(result)){
				//说明缓存中没有数据
				treeList = findItemCatList(parentId);

				//将数据存入缓存
				String easyUIJSON = objectMapper.writeValueAsString(treeList);
				jedisCluster.set(key, easyUIJSON);
				System.out.println("查询数据库");	
			}else{
				EasyUITree[] trees = objectMapper.readValue(result, EasyUITree[].class);
				treeList = Arrays.asList(trees);
				System.out.println("查询缓存");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return treeList;
	}
}

























