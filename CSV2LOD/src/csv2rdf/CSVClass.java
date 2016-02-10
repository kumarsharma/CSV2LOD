/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.XSD;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import csv2rdf.DataType.TypeGuesser;


/**
 *
 * @author ksharma
 */
public class CSVClass {
    
    public OntClass concept;
    public CSVHeader[] headers;
    public ArrayList data;
    public boolean ShouldDetectDataType;
    String className;
    
    public File file;
    public Model model;
    OntModel ontModel;
    String ontURI;
    
    public String uderDefinedClassName;
    
    public String modelFilePath;

    public CSVClass(File csvFile, OntModel oModel, String ontUri)
    {
        this.file = csvFile;
        this.ontModel = oModel;
        this.ontURI = ontUri;
    }
   
    /////////////////////GETTERS
    // <editor-fold defaultstate="colapsed" desc="GETTER METHODS">
    public CSVHeader[] loadHeaders()
    {
         CSVReader reader;
         
         if(null == this.headers && this.file != null)
         {
            // <editor-fold defaultstate="collapsed" desc="Header extraction">
          try{
            reader = new CSVReader(new FileReader(this.file.getPath()), ',' , '"' , 0);
            
            String[] nextLine;
            int lines = 0;
            while ((nextLine = reader.readNext()) != null) 
            { 
               if (nextLine != null) 
               {
                   if(lines == 0)
                   {
                       break;
                   }
               }
             }
            
            
            CSVHeader[] headerVals = new CSVHeader[nextLine.length];
            for(int i = 0; i < nextLine.length; i++)
            {
               String propName = this.propertyLike(nextLine[i]);
               
               CSVHeader h = new CSVHeader(propName, i);
               headerVals[i] = h;
            }
            
            this.headers = headerVals;
            
        }catch(Exception ex){
            
            System.out.println("ERROR IN EXTRACTING HEADER FROM A CSV FILE. Class CSVClass L 63: "+ex.toString() + " For File: "+this.className);
        }
        // </editor-fold>
         }
         return this.headers;
    }
    
    public ArrayList getData()
    {
        if(null == this.data)
        {
            // <editor-fold defaultstate="collapsed" desc="Data Extraction">
            CSVReader reader;
            try{
               reader = new CSVReader(new FileReader(this.file.getPath()), ',' , '"' , 1);

               String[] nextLine;
               ArrayList list = new ArrayList<>();
               while ((nextLine = reader.readNext()) != null) 
               { 
                  if (nextLine != null) 
                  {
                      list.add(nextLine);
                  }
                }

               this.data = list;

            }catch(Exception ex){

                    System.out.println("ERROR IN EXTRACTING DATA FROM A CSV FILE. Class CSVClass L 92");
            }
            // </editor-fold>
        }
     
        return this.data;
    }
    
    public long getRowCount()
    {
        long count = 0;
        if(null == this.data)
        {
            // <editor-fold defaultstate="collapsed" desc="Data Extraction">
            CSVReader reader;
            try{
               reader = new CSVReader(new FileReader(this.file.getPath()), ',' , '"' , 1);
               count = reader.readAll().toArray().length;
               
            }catch(Exception ex){

                    System.out.println("ERROR IN PARSING COUNTING CSV DATA. Class CSVClass L 137");
            }
            // </editor-fold>
        }
     
        return count;
    }
    
    public ArrayList getDataForHeader(CSVHeader header)
    {
        ArrayList data = null;
        if(null == data)
        {
            // <editor-fold defaultstate="collapsed" desc="Data Extraction">
            CSVReader reader;
            try{
               reader = new CSVReader(new FileReader(this.file.getPath()), ',' , '"' , 1);

               String[] nextLine;
               ArrayList list = new ArrayList<>();
               while ((nextLine = reader.readNext()) != null) 
               { 
                  if (nextLine != null) 
                  {
                      list.add(nextLine[header.colIndex]);
                  }
                }

               data = list;

            }catch(Exception ex){

                    System.out.println("ERROR IN EXTRACTING DATA FROM A CSV FILE. Class CSVClass L 146");
            }
            // </editor-fold>
        }
     
        return data;
    }
    
    public String getClassName()
    {
        return this.className;
    }
    
    public OntClass getConcept()
    {
        if(null == this.concept)
        {
            OntClass c = this.GenerateNewConcept();
            this.concept = c;
        }
        
        return this.concept;
    }
    // </editor-fold>
    
    public OntClass GenerateNewConcept()
    {
        String conceptName;
        if(this.uderDefinedClassName!=null && this.uderDefinedClassName.length()> 0)
        {
            conceptName = this.uderDefinedClassName;
        }
        else
        {
            String str = this.file.getName();
            conceptName = str.substring(0, str.lastIndexOf("."));
        }

        OntClass c = this.ontModel.createClass(this.ontURI+conceptName);
        this.className = conceptName;
        for(CSVHeader col : this.loadHeaders())
        {
            if(!col.includeRelationship)
            {
                DatatypeProperty p = this.ontModel.createDatatypeProperty(this.ontURI+this.propertyLike(col.header));

                if(col.userDefinedDataType != null)
                    p.addRange(col.userDefinedDataType.xsdDatatype);
                else
                {
                    //without analyzing data; all data type is of string type
                    
                    if(!this.ShouldDetectDataType)
                        p.addRange(XSD.xstring);
                    else
                        p.addRange(this.rangeForHeaderColumn(col));
                }

                String comment;
                if(col.userDefinedComment!=null) comment = col.userDefinedComment;
                else comment = "Comment for "+col.header;

                p.addComment(comment, "en");
                p.addDomain(c);
                col.property = p;
                col.comment = comment;
            }
        }
        
        this.concept = c;
        
        return c;
    }
    
    // <editor-fold defaultstate="colapsed" desc="PRIVATE METHODS">
    //capitalise each word and concats without any space
    private String propertyLike(String source)
    {
        String result = "";
        String[] splitString = source.split(" ");
        for(String target : splitString){
            result += Character.toUpperCase(target.charAt(0))
                    + target.substring(1) + " ";
        }
        return result.trim().replaceAll(" ", "");
    }
    
    private Resource rangeForHeaderColumn(CSVHeader h)
    {
        ArrayList<String> values = this.getDataForHeader(h);
        
        Resource result = XSD.xstring;
        boolean found = false;
        
        //<editor-fold defaultstate="colapsed" desc="Look for Boolean">
        if(!found)
        {
            result = XSD.xboolean;
            for(String s:values)
            {
                if(!TypeGuesser.isBoolean(s))
                {
                    result = null;
                    break;
                }
            }
            
            if(result != null)
                found = true;
        }
        //</editor-fold>
        
        
        //<editor-fold defaultstate="colapsed" desc="Look for Int">
        if(!found)
        {
            result = XSD.xint;
            for(String s:values)
            {
                if(!TypeGuesser.isInteger(s))
                {
                    result = null;
                    break;
                }
            }
            
            if(result != null)
                found = true;
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="colapsed" desc="Look for Float">
        if(!found)
        {
            result = XSD.xfloat;
            for(String s:values)
            {
                if(!TypeGuesser.isFloat(s))
                {
                    result = null;
                    break;
                }
            }
            
            if(result != null)
                found = true;
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="colapsed" desc="Look for Double">
        if(!found)
        {
            result = XSD.xdouble;
            for(String s:values)
            {
                if(!TypeGuesser.isDouble(s))
                {
                    result = null;
                    break;
                }
            }
            
            if(result != null)
                found = true;
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="colapsed" desc="Look for Date">
        if(!found)
        {
            result = XSD.date;
            for(String s:values)
            {
                if(!TypeGuesser.isDate(s))
                {
                    result = null;
                    break;
                }
            }
            
            if(result != null)
                found = true;
        }
        //</editor-fold>
       
        if(result == null)
            result = XSD.xstring;
        
        h.dataType = CLDataType.newObject(result);
        return result;
    }
    
    protected String[] depreValuesForHeader(CSVHeader h)
    {
        String[] values = new String[this.data.toArray().length];
        int row = 0;
        for(Object val : this.data)
        {
            String[] vals = (String[])val;
            values[row] = vals[h.colIndex];
            row++;
        }
        return values;
    }
    // </editor-fold>
    
    @Override
    public String toString()
    {
       return this.className;
    }
    
    public String getGuiClassName()
    {
        if(this.uderDefinedClassName != null && this.uderDefinedClassName.length() > 0)
            return this.uderDefinedClassName;
        
        return this.className;
    }
    
    public Model GetModelFrolDisk()
    {
        Model m = null;
     
        if(this.modelFilePath != null)
        {
            java.io.InputStream in = null;
            
            try{
                in = new java.io.FileInputStream(this.modelFilePath);
            }
            catch(java.io.FileNotFoundException ex){}
            
            if(in != null)
            {
                m = ModelFactory.createMemModelMaker().createFreshModel();
                m.read(in, null);
                
                try{
                    in.close();
                }catch(Exception e){}
            }
        }
        
        return m;
    }
    
}
