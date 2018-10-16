package com.jt.common.facotry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterFactory implements FactoryBean<JedisCluster>{

	private JedisPoolConfig poolConfig;//注入配置文件
	private String redisNodePrefix;    //注入配置前缀
	private Resource propertySource;   //注入redis配置文件
	
	@Override
	public JedisCluster getObject() throws Exception {
		Set<HostAndPort> nodes = getNoeds();
		JedisCluster jedisCluster = 
				new JedisCluster(nodes, poolConfig);
		
		return jedisCluster;
	}
	/**
	 * 
	 * @return
	 */
	public Set<HostAndPort> getNoeds(){
		Set<HostAndPort> nodes = new HashSet<>();
		
		try {
			//获取Property对象
			Properties properties = new Properties();
			properties.load(propertySource.getInputStream());
			
			//获取redis节点数据
			for (Object key : properties.keySet()) {
				String strkey = (String) key;
				//判断当前遍历的key是否为redis的节点
				if(strkey.startsWith(redisNodePrefix)){
					//获取节点 IP:端口号
					String value = properties.getProperty(strkey);
					String[] agrs = value.split(":");
					HostAndPort hostAndPot =
							new HostAndPort(agrs[0],Integer.parseInt(agrs[1]));
					nodes.add(hostAndPot);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodes;
	}
	

	@Override
	public Class<?> getObjectType() {
		return JedisCluster.class;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	public JedisPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public String getRedisNodePrefix() {
		return redisNodePrefix;
	}

	public void setRedisNodePrefix(String redisNodePrefix) {
		this.redisNodePrefix = redisNodePrefix;
	}

	public Resource getPropertySource() {
		return propertySource;
	}

	public void setPropertySource(Resource propertySource) {
		this.propertySource = propertySource;
	}
	
	

}
