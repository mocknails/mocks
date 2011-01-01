package sb.com.cn;

//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import java.util.Properties;




/**
 * get connection
 * @author _duanjt
 */
public class SQLUtil {
	
//	private static String PropertiesPath = "./conf/db.properties";
	

	public static Connection getConn() 
	throws SQLException
	{
		Connection conn = null;
//		BufferedInputStream in = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			in = new BufferedInputStream(new FileInputStream(PropertiesPath));
//			Properties ppt = new Properties(); 
//			ppt.load(in);
//			String url = ppt.getProperty("url");
			String url = "jdbc:mysql://127.0.0.1:3306/crawl?user=root&password=123&autoReconnect=true&characterEncoding=utf-8";
			conn = DriverManager.getConnection(url);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		return conn;
	}
	//~~~
}
