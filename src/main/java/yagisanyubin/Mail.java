package yagisanyubin;

public class Mail
{
	private final String from;
	private final String subject;
	private final String text;
	private final byte[] attachment;
	private final String attachmentName;

	public Mail( String from, String subject, String text, byte[] attachment, String attachmentName )
	{
		this.from = from;
		this.subject = subject;
		this.text = text;
		this.attachment = (byte[])attachment.clone();
		this.attachmentName = attachmentName;
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

	public byte[] getAttachment()
	{
		return( attachment );
	}

	public String getAttachmentName()
	{
		return( attachmentName );
	}
}
