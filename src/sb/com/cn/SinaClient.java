package sb.com.cn;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

public class SinaClient extends HttpClient
{
	byte[] contentBuffer = new byte[1024*1024*6];
	
	/**
	 * constructor 获取登陆状态
	 * @param username
	 * @param password
	 */
	SinaClient(String username, String password) throws HttpException, IOException 
	{
		getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		Login.login(this, username, password);
	}
	
	/**
	 * 获取页面内容的缓存reader
	 */
	public BufferedReader getPageBufferedReader(String url) throws HttpException, IOException
	{
		byte[] buffer = null;
		GetMethod get = null;
		synchronized (this) {
			get = new GetMethod(url);
			get.setRequestHeader("Cookie", getCookiesString());
			try{
				if(executeMethod(get)==HttpStatus.SC_OK){
					int size = 0;
					while (true) {
						int off = get.getResponseBodyAsStream().read(contentBuffer, size, 1000);
						if (off == -1) break;
						size += off;
					}
					buffer = new byte[size];
					System.arraycopy(contentBuffer, 0, buffer, 0, size);
				}
			} finally {
				if(get!=null) get.releaseConnection(); get=null;
				notifyAll();
			}
		}
		if (buffer != null) {
			return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));
		} else {
			return null;
		}
	}
	
	/**
	 * 获取当前请求的cookie信息
	 */
	private String getCookiesString()
	{
		String cookiesString="";
		Cookie[] cookies = getState().getCookies();
		for (int i=0; i<cookies.length; i++) {
			cookiesString += ((i==0)?"":";") + cookies[i].toString();
		}
		return cookiesString;
	}
}
