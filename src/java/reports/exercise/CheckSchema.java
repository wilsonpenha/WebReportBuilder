package reports.exercise;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.facebook.presto.jdbc.internal.guava.io.CharStreams;
import com.facebook.presto.jdbc.internal.spi.ErrorCodeSupplier;
import com.facebook.presto.jdbc.internal.spi.PrestoException;


public class CheckSchema {
	public static void main(String[] args) {
		String schema = readSchema("/data1/presto/event.avsc");
	}
	private static String readSchema(String dataSchemaLocation)
    {
        InputStream inputStream = null;
        try {
            if (isURI(dataSchemaLocation.trim().toLowerCase())) {
                try {
                    inputStream = new URL(dataSchemaLocation).openStream();
                }
                catch (MalformedURLException e) {
                    // try again before failing
                    inputStream = new FileInputStream(dataSchemaLocation);
                }
            }
            else {
                inputStream = new FileInputStream(dataSchemaLocation);
            }
            return CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
        }
        catch (IOException e) {
        	ErrorCodeSupplier errCode = null;
            throw new PrestoException(errCode, "Could not parse the Avro schema at: " + dataSchemaLocation, e);
        }
        finally {
            closeQuietly(inputStream);
        }
    }

	private static void closeQuietly(InputStream stream)
    {
        try {
            if (stream != null) {
                stream.close();
            }
        }
        catch (IOException ignored) {
        }
    }
	private static boolean isURI(String location)
    {
        try {
            URI.create(location);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
}
