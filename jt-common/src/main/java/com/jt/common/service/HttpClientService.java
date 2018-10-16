package com.jt.common.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;

@Service
public class HttpClientService {

	String result = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientService.class);

	@Autowired(required=false)
	private CloseableHttpClient httpClient;

	@Autowired(required=false)
	private RequestConfig requestConfig;

	/**
	 * 实现httpClientPost方法
	 *  ：需要设定URL参数 
	 *  ：参数封装  Map<Stirng,String
	 *  ：设定字符集编码utf-8
	 * 难点：post如何传递参数 
	 *   post请求将参数转换为二进制字节流信息进行数据传递
	 *   一般from表单提交为POST
	 */
	public String doPost(String url,Map<String,String>params,String charset){
		//判断字符集编码是否为空
		if(StringUtils.isEmpty(charset)){
			charset="utf-8";
		}

		//获取请求对象的实体
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);

		try {
			//判断用户是否传递参数
			if(params !=null){
				//将用户传入的参数Map 封装到List 
				List<NameValuePair> parameters = new ArrayList<>();
				for (Map.Entry<String, String> entity : params.entrySet()) {
					BasicNameValuePair nameValuePair =
							new BasicNameValuePair(entity.getKey(),entity.getValue());
					parameters.add(nameValuePair);
				}
				//封装参数
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, charset);

				//将参数封装成 from 进行数据传输
				httpPost.setEntity(formEntity);

				//发送post请求
				CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

				//判断请求结果是否正确
				if(httpResponse.getStatusLine().getStatusCode() ==200){
					result = EntityUtils.toString(httpResponse.getEntity(), charset);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String doPost(String url){
		return doPost(url,null,null);
	}

	public String doPost(String url,Map<String,String> params){
		return doPost(url,params,null);
	}


	/**
	 *  实现get请求
	 *  ：get请求中的参数 是拼接形成的 localhost：8091/addUser?id=4546
	 * @throws Exception 
	 */
	public String doGet(String url,Map<String,String>params,String charset){
		String result = null ;
		//判断字符集编码是否为空
		if(StringUtils.isEmpty(charset)){
			charset="utf-8";
		}
		try {
			//判断参数是否为空
			if(params!=null){
				//工具类 实现 拼接 URL 
				URIBuilder builder = new URIBuilder(url);
				for (Map.Entry<String, String> entry : params.entrySet()) {
					builder.addParameter(entry.getKey(), entry.getValue());
				}
				//将路径进行拼接
				url  =builder.build().toString();
			}
			//
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			
			//发送get请求
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(httpResponse.getEntity(), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String doGet(String url){
		return doGet(url,null,null);
	}

	public String doGet(String url,Map<String,String> params){
		return doGet(url,params,null);
	}

























	/* 
	 *//**
	 * 执行get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 *//*
    public String doGet(String url,Map<String, String> params,String encode) throws Exception {
        LOGGER.info("执行GET请求，URL = {}", url);
        if(null != params){
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.setParameter(entry.getKey(), entry.getValue());
            }
            url = builder.build().toString();
        }
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                if(encode == null){
                    encode = "UTF-8";
                }
                return EntityUtils.toString(response.getEntity(), encode);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            // 此处不能关闭httpClient，如果关闭httpClient，连接池也会销毁
        }
        return null;
    }

    public String doGet(String url, String encode) throws Exception{
        return this.doGet(url, null, encode);
    }

    public String doGet(String url) throws Exception{
        return this.doGet(url, null, null);
    }

	  *//**
	  * 带参数的get请求
	  * 
	  * @param url
	  * @param params
	  * @return
	  * @throws Exception
	  *//*
    public String doGet(String url, Map<String, String> params) throws Exception {
        return this.doGet(url, params, null);
    }

	   *//**
	   * 执行POST请求
	   * 
	   * @param url
	   * @param params
	   * @return
	   * @throws Exception
	   *//*
    public String doPost(String url, Map<String, String> params,String encode) throws Exception {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        if (null != params) {
            // 设置2个post参数，一个是scope、一个是q
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = null;
            if(encode!=null){
                formEntity = new UrlEncodedFormEntity(parameters,encode);
            }else{
                formEntity = new UrlEncodedFormEntity(parameters);
            }
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }


	    *//**
	    * 执行POST请求
	    * 
	    * @param url
	    * @param params
	    * @return
	    * @throws Exception
	    *//*
    public String doPost(String url, Map<String, String> params) throws Exception {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        if (null != params) {
            // 设置2个post参数，一个是scope、一个是q
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public String doPostJson(String url, String json) throws Exception {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        if(null != json){
            //设置请求体为 字符串
            StringEntity stringEntity = new StringEntity(json,"UTF-8");
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
	     */
}

