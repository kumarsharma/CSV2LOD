/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;

/**
 *
 * @author home
 */
public class CourseLog {
    
     // URI for vocabulary elements
    protected static String uri = "http://klyuniv.ac.in/ontology/log/";
    
    // Return URI for vocabulary elements
    public static String getURI(  )
    {
        return uri;
    } 
    
    public static Property propertyForName(String name)
    {
        return new PropertyImpl(uri, name);
    }
}
