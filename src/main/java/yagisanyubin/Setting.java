package yagisanyubin;

public class Setting
{
	public final String protocol;
	public final String host;
	public final int port;
	public final String account;
	public final String password;

	public Setting( String protocol, String host, int port, String account, String password )
	{
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.account = account;
		this.password = password;
	}

	public String getProtocol()
	{
		return( protocol );
	}

	public String getHost()
	{
		return( host );
	}

	public int getProt()
	{
		return( port );
	}

	public String getAccount()
	{
		return( account );
	}

	public String getPassword()
	{
		return( password );
	}
}
