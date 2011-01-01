package sb.com.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.commons.httpclient.HttpException;


public class SinaSpider 
{
	private UrlExtractor urletr;
	private Connection conn;
	
	public SinaSpider() throws SQLException
	{
		urletr = new UrlExtractor();
		conn = SQLUtil.getConn();
	}
	
	/**
	 * 开爬......
	 */
	public void crawling() throws HttpException, IOException, SQLException
	{
		BufferedReader br = null;
		while (true) {
			String seedUrl = getUrlFromDB();
			if(seedUrl==null) break;
			try {
				br = urletr.getBufferedReader(seedUrl);
				Source source = new Source(br);
				List<StartTag> st = source.getAllStartTags("li id=");
				int insertNum = 0;
				for (int i=0; i<st.size(); i++) {
					Element ele = st.get(i).getElement();
					String id = ele.getAttributeValue("id");
					String charUrl = ele.getFirstElement("a").getAttributeValue("href");
					String name = charUrl.substring(charUrl.lastIndexOf('/')+1);
					if(insertToDB(id, name, charUrl)) insertNum++;
				}
				System.out.println("seedUrl : "+seedUrl);
				System.out.println("follow number is : "+st.size()+"   successfully insert number is : "+insertNum);
			} finally {
				if (br != null) br.close(); br = null;
			}
		}
	}
	
	/**
	 * get a follow url
	 * @return
	 * @throws SQLException
	 */
	private String getUrlFromDB() throws SQLException
	{
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		PreparedStatement ptmt2 = null;
		String sql = "SELECT"+" "
		 			+"id,sina_id,name,url,status"+" "
		 			+"FROM baseinfo WHERE"+" "
		 			+"status = 0"+" "
		 			+"ORDER BY id ASC LIMIT 0,1";
		try{
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				sql = "UPDATE baseinfo SET status=1 WHERE id = "+rs.getInt("id");
				ptmt2 = conn.prepareStatement(sql);
				ptmt2.executeUpdate();
				return "http://"+UrlExtractor.hostDomain+"/"+rs.getInt("sina_id")+"/follow";
			}
			return null;
		} finally {
			if(ptmt2!= null) ptmt2.close(); ptmt2 = null;
			if(rs != null) rs.close(); rs = null;
			if(ptmt != null) ptmt.close(); ptmt = null;
		}
	}
	
	/**
	 * 将找到的新的fellow url 写到库中去
	 */
	private boolean insertToDB(String id, String name, String url) throws SQLException
	{
		PreparedStatement ptmt = null;
		try {
			String sql = "INSERT INTO baseinfo"+" " 
			  			+"(sina_id, name, url)"+" "
			  			+"VALUES"+" " 
			  			+"("
			  			+id+", "
			  			+"'"+name+"', "
			  			+"'"+url+"' "
			  			+")";
			ptmt = conn.prepareStatement(sql);
			ptmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if(ptmt!=null) ptmt.close(); ptmt = null;
		}
	}
	
	/**
	 * 工程入口
	 */
	public static void main(String[] args) throws Exception
	{
		SinaSpider ss = new SinaSpider();
		ss.crawling();
	}
}
