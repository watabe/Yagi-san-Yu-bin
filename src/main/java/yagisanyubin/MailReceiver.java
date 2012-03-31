package yagisanyubin;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

public class MailReceiver
{
	public static List<Mail> receiveNewMail( Setting setting, String attachmentType ) throws MessagingException, FileNotFoundException, IOException
	{
		List<Mail> result = new ArrayList<Mail>();

		Properties systemProperties = System.getProperties();
		Session session = Session.getInstance( systemProperties, null );

		Store store = session.getStore( setting.getProtocol() );
		store.connect( setting.getHost(), setting.getProt(), setting.getAccount(), setting.getPassword() );

		Folder folder = store.getFolder( "INBOX" );
		if( folder.exists() )
		{
			folder.open( Folder.READ_WRITE );

			for( Message message: folder.getMessages() )
			{
				if( !message.getFlags().contains( Flag.SEEN ))
				{
//					System.out.printf("%s - %d\n", message.getSubject(), message.getSize());
					Mail mail = MailReceiver.createMail( message, attachmentType );
					if( mail != null )
					{
						result.add( mail );
					}
				}
            }

			folder.close( false );
		}

		return( result );
	}

	private static Mail createMail( Message message, String attachmentType ) throws FileNotFoundException, MessagingException, IOException
	{
		Object messageContent = message.getContent();
		Mail result = null;

		if( messageContent instanceof Multipart )
		{
			Multipart multiPart = (Multipart)messageContent;

			String text = "";
			byte[] attachment = null;
			String attachmentName = null;
			for( int i = 0; i < multiPart.getCount(); i++ )
			{
				Part part = multiPart.getBodyPart( i );

				String contentType = part.getContentType();
				if( contentType.startsWith( "TEXT/PLAIN" ) )
				{
					text = part.getContent().toString();
				}
				// Å‰‚Ì‚Ð‚Æ‚Â–Ú‚¾‚¯‚ð—LŒø‚Æ‚·‚éB
				else if( attachment == null && contentType.startsWith( attachmentType ) )
				{
					InputStream in = part.getInputStream();
					attachment = MailReceiver.readInputStream( in );
					attachmentName = part.getFileName();
				}
			}

			if( attachment != null )
			{
				String from = message.getFrom()[ 0 ].toString();
				String subject = message.getSubject();

				result = new Mail( from, subject, text, attachment, attachmentName );				
			}
		}

		return( result );
	}

	private static byte[] readInputStream( InputStream in ) throws IOException
	{
		byte[] result = new byte[ 0 ];
		ByteArrayOutputStream bout = null;

		try
		{
			byte[] buffer = new byte[ 1024 ];
			bout = new ByteArrayOutputStream();

//			int position = 0;
//			for( int cursor = in.read( buffer, position, buffer.length ); cursor != -1; cursor = in.read( buffer, position, buffer.length ) )
//			{
//				bout.write( buffer, position, cursor );
//				position += cursor;
//			}
			while( true )
			{
				int cursor = in.read( buffer, 0, buffer.length );
				if( cursor == -1 )
				{
					break;
				}

				bout.write( buffer, 0, cursor );
			}

			bout.flush();
			result = bout.toByteArray();
		}
		finally
		{
			if( bout != null )
			{
				try
				{
					bout.close();
				}
				catch( IOException ignore )
				{
					ignore.printStackTrace();
				}
				bout = null;
			}
		}

		return( result );
	}
}
