package sb.com.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

public class UrlExtractor 
{
	public static final String hostDomain = "t.sina.com.cn";
	private SinaClient sinaClient;
	
	public UrlExtractor()
	{
		this.sinaClient = SinaClientFactory.getInstance();
	}
	
	/**
	 * 根据_regex从buffer中提取_url
	 */
	public Set<String> extractLinks(String url, String regex) throws IOException
	{
		BufferedReader br = null;
		regex = "href=[\"\']"+regex+"[\"\'|]";
		Set<String> urlSet = new HashSet<String>();
		try{
			br = sinaClient.getPageBufferedReader(url);
			if (br != null) {
				try{
					for (String line=br.readLine(); line!=null; line=br.readLine()) {
						extractSubUrl(line, regex, urlSet);
					}
				} finally {
					br.close();
				}
			}
			return urlSet;
		} finally {
			if(br!=null) br.close(); br=null;
		}
	}
	
	public BufferedReader getBufferedReader(String url) throws HttpException, IOException
	{
		return sinaClient.getPageBufferedReader(url);
	}
	/**
	 * 根据_regex从htmlBody中提取_url
	 * @param htmlBody
	 * @param regex
	 * @return 一个满足要求的_url的set
	 */
	public Set<String> extractLinksFromBody(String htmlBody, String regex) throws IOException
	{
		regex = "href=[\"\']"+regex+"[\"\'|]";
		Set<String> urlSet = new HashSet<String>();
		String[] lines = htmlBody.split("\n");
		for (int i=0; i<lines.length; i++) {
			String line = lines[i];
			extractSubUrl(line, regex, urlSet);
		}
		return urlSet;
	}
	
	/**
	 * @param url
	 * @return 当获取失败的时候将会返回null
	 */
	public PageInfo getHtmlInfo(String url) throws HttpException, IOException
	{
		PageInfo pinfo = null;
		BufferedReader br = null;
		String followRegex = "href=[\"\']"+"[^\"\']+/follow"+"[\"\'|]";
		Set<String> urlSet = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		
		br = sinaClient.getPageBufferedReader(url);
		if (br != null) {
			try{
				for (String line=br.readLine(); line!=null; line=br.readLine()) {
					sb.append(line);
					sb.append("\n");
					extractSubUrl(line, followRegex, urlSet);
				}
			} finally {
				br.close(); br = null;
			}
			
			String followUrl = urlSet.iterator().next();
			String tag = "http://"+hostDomain;
			String id = followUrl.substring(followUrl.indexOf(tag)+tag.length()+1, followUrl.lastIndexOf("/follow"));
			String name = url.substring(url.indexOf(tag)+tag.length()+1, url.length());
			
			pinfo = new PageInfo().setId(Integer.parseInt(id))
								  .setName(name)
								  .setUrl(url)
								  .setFollowUrl(followUrl)
								  .setHtmlBody(sb.toString());
		}
		return pinfo;
	}
	
	public String getName(String url)
	{
		String tag = "http://"+hostDomain;
		String name = url.substring(url.indexOf(tag)+tag.length()+1, url.length());
		return name;
	}
	
	public Integer getId(String url)
	{
		String tag = "http://"+hostDomain;
		String id = url.substring(url.indexOf(tag)+tag.length()+1, url.lastIndexOf("/follow"));
		return Integer.parseInt(id);
	}
	
	/**
	 * 按照_regex提取相应的_url到_urlSet
	 * @param line
	 * @param urlRegex
	 * @return
	 */
	private void extractSubUrl(String line, String urlRegex , Set<String> urlSet)
	{
		Pattern pt = Pattern.compile(urlRegex);
		Matcher mat = pt.matcher(line.trim());
		while(mat.find()){
			String href = mat.group();
			href = href.replace('\'', '"');
			if (href.indexOf("http://") < 0) {
				href = "http://"+hostDomain + href.substring(href.indexOf("\"")+1, href.lastIndexOf("\""));
			} else {
				href = href.substring(href.indexOf("\"")+1, href.lastIndexOf("\""));
			}
			urlSet.add(href);
		}
	}
}
