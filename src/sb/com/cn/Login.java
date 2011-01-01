package sb.com.cn;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;


public class Login 
{
	private static final String LOGIN_URI = "http://login.sina.com.cn/sso/login.php";
	
	/**
	 * @param userName	用户名
	 * @param password	用户密码
	 */
	public static void login(HttpClient httpClient, String username, String password) throws HttpException, IOException
	{
		PostMethod post = new PostMethod(LOGIN_URI);
		NameValuePair[] parametersBody = {
				new NameValuePair("username",username),
				new NameValuePair("password",password),
				new NameValuePair("service","miniblog"),
				new NameValuePair("entry","miniblog"),
				new NameValuePair("encoding","utf-8"),
				new NameValuePair("useticket","0"),
				new NameValuePair("gateway","1"),
				new NameValuePair("savestate","7"),
		};
		post.setRequestBody(parametersBody);
		try{
			if(httpClient.executeMethod(post) != HttpStatus.SC_OK) {
				throw new HttpException("request failed, HttpStatus is : "+HttpStatus.SC_OK);
			}
		} finally {
			post.releaseConnection();
		}
	}
	
	/**
	 * test
	 */
	public static void main(String[] args) throws HttpException, IOException
	{
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		String username = "mock_nail@yeah.net";
		String password = "mocknail";
		Login.login(httpClient, username, password);
	}
}
