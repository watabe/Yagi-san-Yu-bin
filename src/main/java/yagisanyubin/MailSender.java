package yagisanyubin;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class MailSender
{
	public static void execute( Setting setting, String to, ExportedMail exportedMail ) throws AddressException, MessagingException, IOException
	{
		Properties systemProperties = new Properties();
		systemProperties.put( "mail.smtp.host", setting.getHost() );
		systemProperties.put( "mail.smtp.port", setting.getProt() );
		systemProperties.put( "mail.smtp.auth", "true" );
		systemProperties.put( "mail.smtp.starttls.enable", "true" );

		Session session = Session.getInstance( systemProperties );
//		session.setDebug( true );

		// TODO ここをリストで回す感じ？
		MimeMessage mimeMessage = createSendMessage( session, exportedMail.getFrom(), to, exportedMail.getSubject(), exportedMail.getText(), exportedMail.getAttachmentFilePath() );

		Transport transport = session.getTransport( setting.getProtocol() );
		transport.connect( setting.getAccount(), setting.getPassword() );
		transport.sendMessage( mimeMessage, mimeMessage.getAllRecipients() );
	}

	private static MimeMessage createSendMessage( Session session, String from, String to, String subject, String text, String attachmentFilePath ) throws IOException, AddressException, MessagingException
	{
		MimeMessage mimeMessage = new MimeMessage( session );
		mimeMessage.setFrom( new InternetAddress( from ) );
		mimeMessage.setRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
		mimeMessage.setSubject( subject );
		mimeMessage.setHeader( "Content-Transfer-Encoding", "7bit" );

		Multipart multipart = new MimeMultipart();

		// テキスト部分
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText( text, "UTF-8" );
		multipart.addBodyPart( textPart );

		// 添付ファイル部分
		BodyPart attachmentPart = new MimeBodyPart();
		// 常にバイナリデータであるものとしてファイルを添付する。
		attachmentPart.setDataHandler( new DataHandler( new ByteArrayDataSource( new BufferedInputStream( new FileInputStream( attachmentFilePath ) ), "application/octet-stream" ) ) );

		// 添付ファイル名をセット
		attachmentPart.setFileName( attachmentFilePath );
		multipart.addBodyPart( attachmentPart );

		mimeMessage.setContent( multipart );

		return( mimeMessage );
	}
}
