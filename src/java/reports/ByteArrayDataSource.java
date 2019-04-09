 
package reports;

import java.io.*;
import javax.activation.DataSource;

public class ByteArrayDataSource
    implements DataSource
{

    public ByteArrayDataSource(InputStream inputstream, String s)
    {
        type = s;
        try
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            int i;
            while((i = inputstream.read()) != -1) 
                bytearrayoutputstream.write(i);

            data = bytearrayoutputstream.toByteArray();
        }
        catch(IOException ioexception) {}
    }

    public ByteArrayDataSource(byte abyte0[], String s)
    {
        data = abyte0;
        type = s;
    }

    public ByteArrayDataSource(String s, String s1)
    {
        try
        {
            data = s.getBytes("iso-8859-1");
        }
        catch(UnsupportedEncodingException unsupportedencodingexception) {}
        type = s1;
    }

    public InputStream getInputStream()
        throws IOException
    {
        if(data == null)
            throw new IOException("no data");
        else
            return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        throw new IOException("cannot do this");
    }

    public String getContentType()
    {
        return type;
    }

    public String getName()
    {
        return "dummy";
    }

    private byte data[];
    private String type;
}
