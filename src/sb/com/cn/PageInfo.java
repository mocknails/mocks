package sb.com.cn;

public class PageInfo {
	private String name;
	private Integer id;
	private String htmlBody;
	private String url;
	private String followUrl;
	
	public PageInfo setFollowUrl(String followUrl)
	{
		this.followUrl = followUrl;
		return this;
	}
	
	public String getFollowUrl()
	{
		return followUrl;
	}

	public String getName() {
		return name;
	}

	public PageInfo setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public PageInfo setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public PageInfo setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public PageInfo setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public String toString()
	{
		String str = "name : "+this.name+"\n"
					+"id : "+this.id+"\n"
					+"url : "+this.url+"\n"
					+"followUrl : "+this.followUrl;
		return str;
	}
}
