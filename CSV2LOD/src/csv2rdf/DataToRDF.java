/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ksharma
 */
public class DataToRDF implements Runnable {
 
    public URL baseUrl;
    public CSVClass csvClass;
    public Model rdfModel;
    String outputDirectory;
    public long startTime, endTime;
    public CSV2RDF controller;
    
    public DataToRDF(URL baseURL, CSVClass c, String outDir)
    {
        this.baseUrl = baseURL;
        this.csvClass = c;
        this.outputDirectory = outDir;
    }
    
    @Override
    public void run() 
    {
       this.GenerateRDFModel();
    }
    
     private void GenerateRDFModel()
    {
        startTime = System.currentTimeMillis();
        CSVClass c = this.csvClass;
        
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefixes(c.ontModel.getNsPrefixMap());
        model.setNsPrefix("DC", DCTerms.getURI());
        long rowsCount = 0;
        for(CSVHeader h : c.loadHeaders())
        {
            ArrayList<String> data = c.getDataForHeader(h);
            rowsCount = data.size();
            int row = 0;
            for(String d:data)
            {
                Resource aRes = model.getResource(this.baseUrl.toString()+"/"+c.getClassName()+"_"+row);
                if(null == aRes)
                    aRes = model.createResource(this.baseUrl.toString()+"/"+c.getClassName()+"_"+row);
                 
                if(h.property.isDatatypeProperty())
                {
                    aRes.addProperty(h.property, d);
                    
                    if(h.header.equalsIgnoreCase("Description") || h.header.equalsIgnoreCase("ProductGroupName") || h.header.equalsIgnoreCase("Name"))
                    {
                        aRes.addProperty(DCTerms.title, d);
                    }
                }
                else if(h.property.isObjectProperty())
                {
                    if(h.removedProperty != null && h.removedProperty.isDatatypeProperty())
                    {
                        aRes.addProperty(h.removedProperty, d);
                    }
                }
                
                row++;
            }
            
            endTime = System.currentTimeMillis();
        }
        
        long timeElapsed = this.endTime - this.startTime;
        long mins = TimeUnit.MILLISECONDS.toMinutes(timeElapsed);
        this.rdfModel = model;
        this.csvClass.model = model;
        this.csvClass.modelFilePath = this.writeModelToFile(model, "RDF/XML", c.getClassName()+"Data");
        long totalStatements = model.listSubjects().toList().size();
        System.out.println("Finished processing: "+ this.csvClass.className +", Total execution time (min): " + mins + ", No of Statements: "+totalStatements + " out of "+rowsCount+" rows");
       
        if(this.controller != null)
            this.controller.DidFinishProcessing(this, timeElapsed);
    }
    
     //write any model to a given file
    public String writeModelToFile(Model m, String format, String fileName)
    {
        java.io.FileOutputStream fout = null;
        String path = "";
        try{
            
            if(format.equalsIgnoreCase("RDF/XML"))
            {
                path = this.outputDirectory+fileName+".rdf";
                fout = new java.io.FileOutputStream(path);
            }
            else if(format.equalsIgnoreCase("N3"))
            {
                path = this.outputDirectory+fileName+".n3";
                fout = new java.io.FileOutputStream(path);
            }
            else if(format.equalsIgnoreCase("TURTLE") || format.equalsIgnoreCase("TTL"))
            {
                path = this.outputDirectory+fileName+".tt1";
                fout = new java.io.FileOutputStream(path);
            }
            else if(format.equalsIgnoreCase("N-TRIPLES") || format.equalsIgnoreCase("NT") || format.equalsIgnoreCase("N-TRIPLE"))
            {
                path = this.outputDirectory+fileName+".nt";
                fout = new java.io.FileOutputStream(path);
            }
            
            m.write(fout);
        }catch(Exception ie){}
        
       return path; 
    }
}
