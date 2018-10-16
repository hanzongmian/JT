package com.jt.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster; 
	
	private static final ObjectMapper objectMapper =  new  ObjectMapper();
	/**
	 * 校验 用户名、邮箱、电话是否存在
	 * 因为type 类型，js 设计 1 2 3 分别代表 用户名、电话、邮箱
	 * 查询数据库是sql 语句需要做对应的调整
	 * 将type转换成数据库里对应的 字段名
	 */
	@Override
	public boolean findCheckUser(String param, int type) {
		String cloumn = null;
		switch (type) {
		case 1:cloumn="username"; break;
		case 2:cloumn="phone"; break;
		case 3:cloumn="email"; break;
		}
		
		int count = userMapper.findCheckUser(param,cloumn);
		return count == 0 ? false : true;
	}

	//编辑sso新增业务
	@Override
	public void saveUser(User user) {
	
		String md5Pass = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5Pass);   //密码加密
		user.setEmail(user.getPhone()); //email 暂时用电话代替
		user.setCreated(new Date());
		user.setUpdated(user.getUpdated());
		
		userMapper.insert(user);
	}

	/**
	 * 编辑sso业务实现
	 * ：根据用户名 密码查询数据库。判断是否正确
	 *    如果数据为空，用户名、密码错误。直接返回 null throw
	 *  ：查询数据有结果，根据加密算法生成 token md5 
	 *  : 将用户信息 转化 为userJSON 数据 ，并且将token：userJSON 保存在 redis中
	 *  ：  返回结果
	 */
	@Override
	public String findUserByUP(User user) {
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		User userDB = userMapper.findUserByUP(user);
		
		String returnToken = null;
		if(userDB==null){
			throw new RuntimeException();
		}
		String token  ="JT_TICKET"+System.currentTimeMillis()+user.getId(); 
		 returnToken = DigestUtils.md5Hex(token);
		try {
			String userJSON  = objectMapper.writeValueAsString(userDB);
			jedisCluster.setex( returnToken, 3600*24*7, userJSON);
			System.out.println("UserServiceImpl.findUserByUP()"+"登陆成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  returnToken;
	}

}








