/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
/**
 *
 * @author Kumar Sharma
 */
public class URLRequest {
    
    public URLRequest()
    {
        
    }
    
    public static String fireGETRequest(String endPoint, String requestParameter)
    {
        String response = null;
        
        try
        {
        String urlFormat = endPoint;
        if(requestParameter != null && requestParameter.length() > 0)
        {
            urlFormat += "?" + requestParameter;
        }

        System.out.println("URL LOOKUP: "+ urlFormat);
        URL url = new URL(urlFormat);
        URLConnection conn = url.openConnection ();

// Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null)
        {
            sb.append(line);
        }
        rd.close();
        response = sb.toString();
        } catch (Exception e)
         {
              e.printStackTrace();
         }
        return response;
    }
}
