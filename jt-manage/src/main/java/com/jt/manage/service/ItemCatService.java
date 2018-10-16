package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.EasyUITree;

public interface ItemCatService {

	List<EasyUITree> findItemCatList(Long parentId);

	//通过缓存查找
	List<EasyUITree> findItemCatCache(Long parentId);
}
