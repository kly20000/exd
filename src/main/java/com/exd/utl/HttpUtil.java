package com.exd.utl;

import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	/**执行GET请求*/
	public static String doGet(String url, Map<String, String> parameter){
		String result="";
        try {
        	// Get请求
    		HttpGet httpget = new HttpGet(url);
    		// 设置参数
			String str = EntityUtils.toString(new UrlEncodedFormEntity(generateNameValuePairList(parameter)));
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			HttpClient httpClient = getHttpClient(url.startsWith("https"));
			// 发送请求
			logger.info("[Http get] : "+httpget.getURI());
            HttpResponse response = httpClient.execute(httpget);
            int statusCode = response.getStatusLine().getStatusCode();
	        logger.info("[status] : "+statusCode);
	        if (statusCode != HttpStatus.SC_OK) {
	        	return "";
	        }
            // 获取返回数据
            HttpEntity entity = response.getEntity();
            if (entity == null) {
	        	return "";
	        }
            result = EntityUtils.toString(entity);
            logger.info("[result] : "+result);
            entity.consumeContent();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}
	
	/**执行POST请求*/
	public static String doPost(String url, Map<String, String> parameter){
		String result="";
		logger.info("[url] : "+url);
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		for(Iterator<String> it = parameter.keySet().iterator(); it.hasNext();){
			String key = it.next();
			logger.info("[key-"+key+"] : "+parameter.get(key));
			pairList.add(new BasicNameValuePair(key, parameter.get(key)));
		}
		try {
			// URL使用基本URL即可，其中不需要加参数
	        HttpPost httpPost = new HttpPost(url);
	        if(pairList.size()>0){
	        	HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,Charset.forName("utf-8"));
	        	// 将请求体内容加入请求中
		        httpPost.setEntity(requestHttpEntity);
	        }
	        // 需要客户端对象来发送请求
	        HttpClient httpClient = getHttpClient(url.startsWith("https"));
	        // 发送请求
	        HttpResponse response = httpClient.execute(httpPost);
	        int statusCode = response.getStatusLine().getStatusCode();
	        logger.info("[status] : "+statusCode);
	        if (statusCode != HttpStatus.SC_OK) {
	        	logger.info("[info] : "+EntityUtils.toString(response.getEntity()));
	        	return "";
	        }
	        HttpEntity entity = response.getEntity();
	        if (entity == null) {
	        	return "";
	        }
	        result = EntityUtils.toString(entity,"utf-8");
	        logger.info("[result] : "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取HTTP对象 
	 * */
	private static HttpClient getHttpClient(boolean isSSL) throws NoSuchAlgorithmException, KeyManagementException{
		if(isSSL){
			HttpClient httpclient = new DefaultHttpClient();
			//Secure Protocol implementation.
			SSLContext ctx = SSLContext.getInstance("SSL");
			//Implementation of a trust manager for X509 certificates
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
 
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			//register https protocol in httpclient's scheme registry
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			return httpclient;
		}else{
			return HttpClients.createDefault();
		}
	}
	
	private static List<NameValuePair> generateNameValuePairList(Map<String, String> params){
		List<NameValuePair> nvp = Collections.EMPTY_LIST;
		for(Iterator<String> it = params.keySet().iterator(); it.hasNext();){
			String name = it.next();
			nvp.add(new BasicNameValuePair(name, params.get(name)));
		}
		return nvp;
	}
}
