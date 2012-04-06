package yagisanyubin;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class MailExporter
{
	private static MailExporter instance = new MailExporter();

	public static MailExporter getInstance()
	{
		return( instance );
	}

	private String workDirectoryPath = "";

	private MailExporter(){}

	public void initialize( String workDirectoryPath )
	{
		this.workDirectoryPath = workDirectoryPath;
	}

	public ExportedMail export( Mail mail ) throws IOException
	{
		String exportAttachmentFilePath = workDirectoryPath + mail.getAttachmentName();
		String exportJSONFilePath = workDirectoryPath + System.currentTimeMillis() + ".json";

		exportAttachmentFile( exportAttachmentFilePath, mail.getAttachment() );

		ExportedMail exportedMail = new ExportedMail( mail.getFrom(), mail.getSubject(), mail.getText(), exportAttachmentFilePath );

		// JSON出力に失敗しても構わず動かす。
		try
		{
			exportJSON( exportJSONFilePath, mail, exportAttachmentFilePath );
		}
		catch( JSONException e )
		{
			e.printStackTrace();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		return( exportedMail );
	}

	private void exportJSON( String outputJSONPath, Mail mail, String attachmentFilePath ) throws JSONException, IOException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put( "from", mail.getFrom() );
		jsonObject.put( "subject", mail.getSubject() );
		jsonObject.put( "text", mail.getText() );
		jsonObject.put( "attachment", attachmentFilePath );

		FileWriter fw = null;

		try
		{
			fw = new FileWriter( outputJSONPath );
			jsonObject.write( fw );

			fw.flush();
		}
		finally
		{
			if( fw != null )
			{
				try
				{
					fw.close();
				}
				catch( IOException ignore )
				{
					ignore.printStackTrace();
				}
				fw = null;
			}
		}
	}

	private void exportAttachmentFile( String filePath, byte[] file ) throws IOException
	{
		FileOutputStream fout = null;

		try
		{
			fout = new FileOutputStream( filePath );
			fout.write( file );

			fout.flush();
		}
		finally
		{
			if( fout != null )
			{
				try
				{
					fout.close();
				}
				catch( IOException ignore )
				{
					ignore.printStackTrace();
				}
				fout = null;
			}
		}
	}
}
