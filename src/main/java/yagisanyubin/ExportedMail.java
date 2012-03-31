package yagisanyubin;

public class ExportedMail
{
	private final String from;
	private final String subject;
	private final String text;
	private final String attachmentFilePath;

	public ExportedMail( String from, String subject, String text, String attachmentFilePath )
	{
		this.from = from;
		this.subject = subject;
		this.text = text;
		this.attachmentFilePath = attachmentFilePath;
	}

	public String getFrom()
	{
		return( from );
	}

	public String getSubject()
	{
		return( subject );
	}

	public String getText()
	{
		return( text );
	}

	public String getAttachmentFilePath()
	{
		return( attachmentFilePath );
	}
}
