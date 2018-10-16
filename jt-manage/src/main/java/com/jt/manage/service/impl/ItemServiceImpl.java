package com.jt.manage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private ItemMapper  itemMapper;

	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUIResult findItemByPage(int page, int rows) {

		//查询总记录数
		//int total = itemMapper.findCount();
		//null 表示查询语法没有where条件
		int total = itemMapper.selectCount(null);
		/**
		 * limit 起始位置 ，查询总数
		 * 	第一页 select * from tb_user limit 0,20
		 * 	第二页 select * from tb_user limit 20,20
		 * 	第三页 select * from tb_user limit 40,20
		 * 	第N页 select * from tb_user limit (page - 1)*rows , rows
		 * 
		 */
		int start = (page - 1)*rows ;
		List<Item> itemlist = itemMapper.findItemByPage(start, rows);

		return new EasyUIResult(total,itemlist); 
	}

	@Override
	public String findIteamCatNameById(Long itemId) {

		return itemMapper.findIteamCatNameById(itemId);
	}

	//当用户添加商品时,需要同时入库2张表数据
	/**
	 * 1,2,3,4,6,8,9,10 主键自增时 即使数据回滚那么该id则不会被其他线程使用
	 */
	@Override
	public void saveItem(Item item,String desc) {
		//封装数据
		item.setStatus(1);  //表示商品状态正常
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);   //利用mybatis实现入库


		//没有item的主键如何获取数据????
		//Item itemDB = itemMapper.select(item);????
		/**
		 * 入库itemDesc表
		 *  Preparing: INSERT INTO tb_item (SELL_POINT,PRICE,NUM,TITLE,CID,STATUS,ID,IMAGE,CREATED,BARCODE,UPDATED) VALUES ( ?,?,?,?,?,?,?,?,?,?,? ) 
 			Parameters: 第四届牛奶节盛大开启！囤奶日就今天了！精选牛奶低至三件七折，更有蜂蜜麦片超值赠品等你来拿！点此抢199-(String), 5900(Long), 100(Integer), 京东超市 澳大利亚 进口奶粉 德运 （Devondale）调制乳粉（全脂）成人奶粉 1kg 袋装(String), 298(Long), 1(Integer), null, (String), 2018-09-12 17:41:09.627(Timestamp), 10086(String), 2018-09-12 17:41:09.627(Timestamp)
			Updates: 1
            Executing: SELECT LAST_INSERT_ID() 
			Total: 1
		 */
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDesc.setItemDesc(desc);
		itemDescMapper.insert(itemDesc);


	}

	@Override
	public void updateItem(Item item,String desc) {
		item.setUpdated(new Date());
		//动态更新操作 将对象部位null的数据充当set条件 主键除外
		itemMapper.updateByPrimaryKeySelective(item);
		
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateByPrimaryKey(itemDesc);
	}

	@Override
	public void deletesItem(Long[] ids) {
		//关联删除先删除从表数据，再删除主表数据
		itemMapper.deleteByIDS(ids);
		itemDescMapper.deleteByIDS(ids);
	}

	@Override
	public void updateStatus(Long[] ids, int status) {
		itemMapper.updateStatus(ids, status);

	}

	@Override
	public ItemDesc findIteamDescById(Long itemId) {
		
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public Item finItemById(Long itemId) {
		
		return itemMapper.selectByPrimaryKey(itemId);
	}



}















