/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 *
 * @author ksharma
 */
public class CLDataType
{
    public Resource xsdDatatype;
    
    public CLDataType(Resource xsd)
    {
        this.xsdDatatype = xsd;
    }
    
    public static CLDataType newObject(Resource xsd)
    {
        return new CLDataType(xsd);
    }
    
    @Override
    public String toString()
    {
      if(this.xsdDatatype.equals(XSD.xstring))
          return "xString";
      else if(this.xsdDatatype.equals(XSD.xboolean))
          return "xBoolean";
      else if(this.xsdDatatype.equals(XSD.xbyte))
          return "xByte";
      else if(this.xsdDatatype.equals(XSD.xdouble))
          return "xDouble";
      else if(this.xsdDatatype.equals(XSD.xfloat))
          return "xFloat";
      else if(this.xsdDatatype.equals(XSD.xint))
          return "xInt";
      else if(this.xsdDatatype.equals(XSD.xlong))
          return "xLong";
      else if(this.xsdDatatype.equals(XSD.xshort))
          return "xShort";
      else if(this.xsdDatatype.equals(XSD.date))
          return "xDate";
      else if(this.xsdDatatype.equals(XSD.dateTime))
          return "xDateTime";
      else if(this.xsdDatatype.equals(XSD.decimal))
          return "xDecimal";
      
       return "None";
    }
}
