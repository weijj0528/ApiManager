package cn.crap.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpPostGet {
	public final static String ACCEPT_JSON = "application/json";

	public static String get(String path, Map<String, Object> params, Map<String, Object> headers) throws Exception {
		return get(path, params, headers, 5000);
	}
	
	public static String get(String path, Map<String, Object> params, Map<String, Object> headers, int timeout) throws Exception {
		path = gethPath(path, params);
		HttpGet method = new HttpGet(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
		method.setConfig(requestConfig);
		return getMethod(method, headers);
	}
	
	public static String delete(String path, Map<String, Object> params, Map<String, Object> headers) throws Exception {
		path = gethPath(path, params);
		HttpDelete method = new HttpDelete(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		method.setConfig(requestConfig);
		return getMethod(method, headers);
	}
	
	public static String options(String path, Map<String, Object> params, Map<String, Object> headers) throws Exception {
		path = gethPath(path, params);
		HttpOptions method = new HttpOptions(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		method.setConfig(requestConfig);
		return getMethod(method, headers);
	}
	
	public static String trace(String path, Map<String, Object> params, Map<String, Object> headers) throws Exception {
		path = gethPath(path, params);
		HttpTrace method = new HttpTrace(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		method.setConfig(requestConfig);
		return getMethod(method, headers);
	}
	
	public static String head(String path, Map<String, Object> params, Map<String, Object> headers) throws Exception {
		path = gethPath(path, params);
		HttpHead method = new HttpHead(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		method.setConfig(requestConfig);
		return getHead(method, headers);
	}

	public static String put(String path, Map<String, Object> params, Map<String, Object> headers) throws Exception {
		HttpPut method = new HttpPut(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		// 请求的参数信息传递
		List<NameValuePair> paires = new ArrayList<NameValuePair>();
		if (params != null) {
			Set<String> keys = params.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				paires.add(new BasicNameValuePair(key, URLDecoder.decode(params.get(key).toString(), "UTF-8")));
			}
		}
		if (paires.size() > 0) {
			HttpEntity entity = new UrlEncodedFormEntity(paires, "utf-8");
			method.setEntity(entity);
		}
		method.setConfig(requestConfig);
		return postMethod(method, params, headers);
	}
	
	public static String post(String path, Map<String, Object> params, Map<String, Object> headers,String debugIsLogin) throws Exception {
		if(path.contains("/handle/request")){
			JSONObject fromObject = JSONObject.fromObject(params);
			JSONObject demoTest = HttpTest.demoTest(path, fromObject, debugIsLogin);
			return demoTest.optInt("status") != 200 ? "调试接口出错："+demoTest.optInt("status") : demoTest.optJSONObject("result").toString();
		}
		if(path.contains("/front/user")){
			JSONObject fromObject = JSONObject.fromObject(params);
			JSONObject demoTest = HttpTest.demoTestLogin(path, fromObject);
			return demoTest.optInt("status") != 200 ? "调试接口出错："+demoTest.optInt("status") : demoTest.optJSONObject("result").toString();
		}
		
		HttpPost method = new HttpPost(path);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		// 请求的参数信息传递
		List<NameValuePair> paires = new ArrayList<NameValuePair>();
		if (params != null) {
			Set<String> keys = params.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				paires.add(new BasicNameValuePair(key, URLDecoder.decode(params.get(key).toString(), "UTF-8")));
			}
		}
		if (paires.size() > 0) {
			HttpEntity entity = new UrlEncodedFormEntity(paires, "utf-8");
			method.setEntity(entity);
		}
		method.setConfig(requestConfig);
		return postMethod(method, params, headers);
	}


	private static String postMethod(HttpUriRequest method, Map<String, Object> params, Map<String, Object> headers)
			throws Exception {
		HttpClient client = HttpClients.createDefault();
		method.setHeader("charset", "utf-8");

		if (headers != null) {
			Set<String> keys = headers.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				method.setHeader(key, headers.get(key).toString());
			}
		}

		HttpResponse response = client.execute(method);
		int status = response.getStatusLine().getStatusCode();
		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Path:" + method.getURI() + "-Unexpected response status: " + status);
		}
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity, "UTF-8");
		return body;
	}

	// 获取页面代码
	private static String getMethod(HttpUriRequest method, Map<String, Object> headers) throws Exception {
		HttpClient client = HttpClients.createDefault();
		method.setHeader("charset", "utf-8");
		// 默认超时时间为15s。
		if (headers != null) {
			Set<String> keys = headers.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				method.setHeader(key, headers.get(key).toString());
			}
		}
		HttpResponse response = client.execute(method);
		int status = response.getStatusLine().getStatusCode();
		if (status < 200 || status >= 300) {
			throw new ClientProtocolException("Path:" + method.getURI() + "-Unexpected response status: " + status);
		}
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity, "UTF-8");
		return body;
	}
	
	// 获取页面代码
		private static String getHead(HttpUriRequest method, Map<String, Object> headers) throws Exception {
			HttpClient client = HttpClients.createDefault();
			method.setHeader("charset", "utf-8");
			// 默认超时时间为15s。
			if (headers != null) {
				Set<String> keys = headers.keySet();
				Iterator<String> iterator = keys.iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					method.setHeader(key, headers.get(key).toString());
				}
			}
			HttpResponse response = client.execute(method);
			Header[] responseHeaders = response.getAllHeaders();
			StringBuilder sb = new StringBuilder("");
			for(Header h:responseHeaders){
				sb.append(h.getName()+":"+h.getValue()+"\r\n");
			}
			return sb.toString();
		}

	

	public static String postBody(String url, String body, Map<String, Object> headers,String debugIsLogin) throws Exception {
		if(url.contains("/handle/request")){
			Object fromObject = body;
			if(body.startsWith("{")){
				fromObject = JSONObject.fromObject(body);
			}else if(body.startsWith("[")){
				fromObject = JSONArray.fromObject(body);
			}
			JSONObject demoTest = HttpTest.demoTest(url, fromObject, debugIsLogin);
			return demoTest.optInt("status") != 200 ? "调试接口出错："+demoTest.optInt("status") : demoTest.optString("result");
		}
		
		HttpClient client = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("charset", "utf-8");
		if (headers != null) {
			Set<String> keys = headers.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				httppost.setHeader(key, headers.get(key).toString());
			}
		}
		
		BasicHttpEntity requestBody = new BasicHttpEntity();
		requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
		requestBody.setContentLength(body.getBytes("UTF-8").length);
		httppost.setEntity(requestBody);
		// 执行客户端请求
		HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}

	private static String gethPath(String path, Map<String, Object> params) {
		if (params != null) {
			if (path.indexOf("?") > -1) {
				path += "&";
			} else {
				path += "?";
			}
			Set<String> keys = params.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				path += key + "=" + params.get(key) + "&";
			}
			if(path.endsWith("&"))
				path = path.substring(0, path.length()-1);
		}
		return path;
	}

	// 获取页面代码
	public static InputStream getInputStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(3 * 1000);
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty("User-Agent",
				"User-Agent:Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setUseCaches(false);// 不进行缓存
		// 头部必须设置不缓存，否则第二次获取不到sessionID
		conn.setUseCaches(false);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}
}
