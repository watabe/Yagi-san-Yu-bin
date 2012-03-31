package yagisanyubin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MovieConvertor
{
	public static boolean convert( String srcMovieFilePath, String dstMovieFilePath ) throws IOException
	{
		boolean result = false;
		InputStream stream = null;

		try
		{
			ProcessBuilder builder = new ProcessBuilder( "/opt/local/bin/ffmpeg", "-i", srcMovieFilePath, "-f", "mp4", "-vcodec", "libx264", "-acodec", "copy", "-vb", "256k", "-ab", "64k", dstMovieFilePath );
			Process process = builder.start();
 
			stream = process.getErrorStream();
			while( true )
			{
				int c = stream.read();
				if( c == -1 )
				{
					break;
				}
			}

			File dstFile = new File( dstMovieFilePath );
			result = dstFile.exists();
		}
		finally
		{
			if( stream != null )
			{
				try
				{
					stream.close();
				}
				catch( IOException ignore )
				{
					ignore.printStackTrace();
				}
				stream = null;
			}
		}

		return( result );
	}
}
