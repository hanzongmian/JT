package com.jt.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {

	@Test
	public void Test01(){
		
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.26.140:26379");
		//定义  哨兵
		JedisSentinelPool sentinelPool 
				= new  JedisSentinelPool("mymaster",sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("name", "tomcate");
		System.out.println(jedis.get("name"));
	}
}
