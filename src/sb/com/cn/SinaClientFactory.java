package sb.com.cn;


public class SinaClientFactory 
{
	private static SinaClient sinaClient;
	
	static {
		try {
			sinaClient = new SinaClient("mock_nail@yeah.net", "mocknail");
		} catch(Exception e) {
			throw new RuntimeException("initiate client failed");
		}
	}
	
	public static SinaClient getInstance()
	{
		return sinaClient;
	}
	
}
