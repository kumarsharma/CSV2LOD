/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LinkDataset;

import Network.URLRequest;
import org.w3c.dom.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//import java.io.in;
import java.io.ByteArrayInputStream;

/**
 *
 * @author u
 */
public class URiLookup {
    
    public URiLookup()
    {
        
    }
    
    public static String lookUpDBPediaURiForResourceTitle(String resourceTitle, String queryClass)
    {
        System.out.println("Looking UP: " + resourceTitle);
        
        String resourceURI = null;
       //http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString=West%20Bengal
        
        //first lookup using prefix search
        String urlFormat = "";
        String requestParams = "";
        resourceTitle = resourceTitle.replace(' ', '_');
        
        urlFormat = "http://lookup.dbpedia.org/api/search.asmx/PrefixSearch?";
        requestParams = "QueryClass=" + queryClass + "&MaxHits=1" + "&QueryString=" + resourceTitle;
        String searchResult = null;//URLRequest.fireGETRequest(urlFormat, requestParams);
        
        
        if(searchResult == null)
        {
            //lookup using keyword search         
             urlFormat = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?";
             requestParams = "QueryClass=" + queryClass + "&QueryString=" + resourceTitle;
             searchResult = URLRequest.fireGETRequest(urlFormat, requestParams);
        }
        //parsing xml
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            
            byte [] xmlDATA = searchResult.getBytes();
            ByteArrayInputStream in = new ByteArrayInputStream(xmlDATA);
            Document doc = docBuilder.parse(in);
            
            NodeList listOfURI = doc.getElementsByTagName("URI");
            System.out.println("List of URIs: " + listOfURI.getLength() + listOfURI.toString());
            for(int i = 0; i < listOfURI.getLength(); i++)
            {
                Node iNode = listOfURI.item(i);
                resourceURI = iNode.getTextContent();
                break;
            }
        }
        catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        
       // System.out.println("DBPedia Search result: "+searchResult);
        return resourceURI;
    }
    
    
     public static String lookUpVIAFURiForSearchTerm(String searchTerm)
    {
        System.out.println("Looking UP: " + searchTerm);
        
        String resourceURI = null;
       //http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString=West%20Bengal
        
        //first lookup using prefix search
        String urlFormat = "";
        String requestParams = "";
        
        urlFormat = "http://viaf.org/viaf/search";
        
        requestParams = "query=cql.any+all+%22" + searchTerm + "%22+&maximumRecords=1&startRecord=1&sortKeys=holdingscount&httpAccept=application/rss%2bxml";
        
        String searchResult = null;//URLRequest.fireGETRequest(urlFormat, requestParams);
            
        //lookup using 
        searchResult = URLRequest.fireGETRequest(urlFormat, requestParams);
        //parsing xml
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            
            byte [] xmlDATA = searchResult.getBytes();
            ByteArrayInputStream in = new ByteArrayInputStream(xmlDATA);
            Document doc = docBuilder.parse(in);
            
            NodeList listOfURI = doc.getElementsByTagName("link");
            
            System.out.println("List of VIAF URIs: " + listOfURI.getLength() + listOfURI.toString());
            for(int i = 1; i < listOfURI.getLength(); i++)
            {
                Node iNode = listOfURI.item(i);
                resourceURI = iNode.getTextContent();
                
                System.out.println("VIAF URI: " + resourceURI);
                break;
            }
        }
        catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        
       // System.out.println("DBPedia Search result: "+searchResult);
        return resourceURI;
    }
    
    
}
