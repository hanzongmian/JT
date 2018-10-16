package com.jt.test;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {

	@Test 
	public void test01() throws Exception{
		//获取HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//定义网址
		String url = "https://item.jd.com/7498933.html";
		//定义请求方式
		HttpGet httpGet = new HttpGet(url);
		HttpPost httpPost = new HttpPost();
		//发送请求
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		//判断返回值类型是否正确
		if(httpResponse.getStatusLine().getStatusCode()==200){
			System.out.println("表示请求有效");
			//
			String result =EntityUtils.toString(httpResponse.getEntity());
			System.out.println(result);
		}
			
		
		
	}
}
