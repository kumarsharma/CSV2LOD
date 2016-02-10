/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;

import java.io.File;

import com.hp.hpl.jena.rdf.model.*;
import java.net.URL;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ObjectProperty;
import GUI.OntClassVerifierView;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModelSpec;
import LinkDataset.URiLookup;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;

public class CSV2RDF {

    /**
     * @param args the command line arguments
     */

     URL baseUrl = null;
     OntModel ontology;
     String domainName;
     String ontURI;
     File[] files;
     CSVClass[] csvClasses;
     OntClassVerifierView verifierView;
     
     public boolean ShouldLinkResource, ShouldRefineConcepts, ShouldDetectType;
     String outputDirectory;
     public ConversionListener actor;
     public long totalStatements = 0, totalProcessingTime = 0, totalNuberOfResources = 0;
     
     int totalFiles, totalFilesProcessed;
     long startTime, endTime;
    public CSV2RDF(File[] inputFiles, String domainName, String outDir, URL baseURL)
    {
        this.files = inputFiles;
        this.domainName = domainName;
        this.baseUrl = baseURL;
        this.ontURI = baseURL.toString()+"#"+domainName;
        this.outputDirectory = outDir+"/";
        this.totalFiles = 0;
        this.totalFilesProcessed = 0;
    }
    
    public void Start()
    {
        try{
            startTime = System.currentTimeMillis();
        
            this.ontURI = this.baseUrl.toString()+"/ontologies/"+this.domainName+"#";
            this.ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
            this.ontology.setNsPrefix(this.domainName, this.ontURI);
            this.CreateCanonicalComponents();
            this.GenerateDomainOntology();
            
            boolean onlyDetectTypes = false;
            
            if(onlyDetectTypes)
            {
                this.SaveDomainOntologyInFile();
                endTime = System.currentTimeMillis();
                this.totalProcessingTime = endTime - startTime;
                System.out.println("Total execution time (ms): " + this.totalProcessingTime );
                this.actor.DidFinishConversion();
            }
            else if(this.ShouldLinkResource && this.ShouldRefineConcepts)
            {
                this.LookForObjectRangeIfAny(false);
                this.GenerateModelsFromDummyOntologies();
                //this.SaveDomainOntologyInFile();
            }
            else
            {
                if(this.ShouldLinkResource)
                {
                    this.LookForObjectRangeIfAny(false);
                }
                endTime = System.currentTimeMillis();
                this.totalProcessingTime = endTime - startTime;
                System.out.println("Total execution time (ms): " + this.totalProcessingTime );
                System.out.println("Total Statements: "+ totalStatements);

                /*
                if(this.ShouldRefineConcepts)
                {
                    this.PresentConceptsGraphically();
                }*/
            }
        }catch(Exception e)
        {
            this.actor.DidFinishConversionWithError(e.toString());
        }
    }
    
    //STEP 1 FILTER CSV
    
    //STEP 2
    private void CreateCanonicalComponents()
    {
        CSVClass[] cs = new CSVClass[this.files.length];
        int i = 0;
        for(File csvFile : this.files)
        {
            CSVClass cc = new CSVClass(csvFile, this.ontology, this.ontURI);
            cc.ShouldDetectDataType = this.ShouldDetectType;
            cc.loadHeaders();
            cs[i++] = cc;
        }
        this.csvClasses = cs;
    }
    
    //STEP 3
    private void GenerateDomainOntology()
    {
        for(CSVClass cc:this.csvClasses)
        {
           cc.getConcept();
        }
    }
    
    private void GenerateModelsFromDummyOntologies()
    {
        this.totalFiles = this.csvClasses.length;
        System.out.println("Total Number of Files: " + this.totalFiles );
        for(CSVClass cc:this.csvClasses)
        {
            DataToRDF dr = new DataToRDF(this.baseUrl, cc, this.outputDirectory);
            dr.controller = this;
            new Thread(dr).start();
        }
    }
    
    public void DidFinishProcessing(DataToRDF dr, long timeElapsed)
    {
        this.totalProcessingTime += timeElapsed;
        this.totalStatements += dr.csvClass.model.listStatements().toList().size();
        this.totalNuberOfResources += dr.csvClass.model.listSubjects().toList().size();
        this.totalFilesProcessed++;
        System.out.println("Processed files: " + this.totalFilesProcessed );
        dr.csvClass.model = null;
        dr.csvClass = null;
        
        if(this.totalFilesProcessed >= this.totalFiles)
        {
            if(this.ShouldLinkResource)
            {
                this.lookUpLinkResources();
                
                this.totalStatements = 0;
                this.totalNuberOfResources = 0;
                
                this.WriteAllModelsInFile();
                //this.LinkToOutsideResources();
                endTime = System.currentTimeMillis();
                this.totalProcessingTime = endTime - startTime;
            }
            
            System.out.println("Total execution time (ms): " + this.totalProcessingTime );
            System.out.println("Total Resources (Subjects): "+ this.totalNuberOfResources);
            System.out.println("Total Statements: "+ totalStatements);

            if(this.actor!=null)
            {
                this.actor.DidFinishConversion();
            }
        }
    }
    
    private void WriteAllModelsInFile()
    {
        for(CSVClass cc:this.csvClasses)
        {
            Model m = cc.GetModelFrolDisk();
            this.totalStatements += m.listStatements().toList().size();
            this.totalNuberOfResources += m.listSubjects().toList().size();
            
            //totalStatements += m.listStatements().toList().size();
            this.writeModelToFile(m, "RDF/XML", cc.getClassName()+"Data");
        }
    }
    
    
    //STEP 2
    public void GenerateFinalConcept()
    {
        //this.FilterConcepts();
        
        if(this.ShouldLinkResource)
            this.LookForObjectRangeIfAny(true);
        
        this.SaveDomainOntologyInFile();
        this.GenerateModelsFromDummyOntologies();
    }
    
    public void UserCanceledOperation()
    {
        this.actor.DidFinishConversionWithError("Operation cancelled by user!");
    }
    
    private void PresentConceptsGraphically()
    {
        if(verifierView == null)
        {
            verifierView = new OntClassVerifierView(this);
            verifierView.csvClasses = this.csvClasses;
            verifierView.showGraph();
            verifierView.setSize(900, 520);
            verifierView.setVisible(true);
        }
        else
        {
            //verifierView.csvClasses = this.csvClasses;
            verifierView.showGraph();
        }
    }
    
    public void FilterConcepts()
    {
        this.ontology.removeAll();
        for(CSVClass cc : this.csvClasses)
        {
            for(CSVHeader h:cc.headers)
               h.applyUserDefinedData();
            
            cc.GenerateNewConcept();
        }
        this.PresentConceptsGraphically();
    }
    
    //STEP 3: Linking
    private void LookForObjectRangeIfAny(boolean shouldCheckRelationShipStatus)
    {
        for(CSVClass cc:this.csvClasses)
        {
             
                for(CSVHeader h:cc.loadHeaders())
                {
                    if((!shouldCheckRelationShipStatus && !h.includeRelationship) || (shouldCheckRelationShipStatus && h.includeRelationship))
                    {
                    for(CSVClass ccIn:this.csvClasses)
                    {
                        if(ccIn!=cc)
                        {
                            boolean found = false;
                            for(CSVHeader ch:ccIn.loadHeaders())
                            {
                                if(h.header.equals(ch.header))
                                {
                                    if(StringUtil.hasDuplicates(cc.getDataForHeader(h).toArray()) && !StringUtil.hasDuplicates(ccIn.getDataForHeader(ch).toArray()))
                                    {
                                        found = true;
                                        System.out.println("DROPPED:"+h.property.getLocalName()+" from "+cc.getClassName());
                                        if(h.removedProperty == null)
                                            h.removedProperty = h.property;
                                        
                                        cc.concept.dropIndividual(h.property);
                                        
                                        ObjectProperty p = this.ontology.createObjectProperty(this.ontURI+"has"+ccIn.className);
                                        p.addRange(ccIn.concept);
                                        p.addComment("Comment for "+h.header, "en");
                                        p.addDomain(cc.concept);
                                        h.rangeClass = ccIn.concept;
                                        h.rangeCSvClass = ccIn;
                                        h.property = p;
                                        h.includeRelationship = true;
                                        h.userDefinedRelationship = true;
                                        
                                        break;
                                    }
                                }
                            }
                            if(found) break;
                        }
                    }
                    }
                }
        }
    }
    
    private void lookUpLinkResources()
    {
        for(CSVClass domainCC:this.csvClasses)
        {
            System.out.println("Processing for links: "+ domainCC.className);
            for(CSVHeader h:domainCC.loadHeaders())
            {
                if(h.includeRelationship)
                {
                    OntClass rangeOfHP = h.rangeClass;
                    Model rangeModel = h.rangeCSvClass.GetModelFrolDisk();

                    /*
                    if(rangeModel == null)
                    {
                        for(CSVClass rangeCC:this.csvClasses)
                        {
                            if(rangeCC != domainCC)
                            {
                                if(rangeOfHP.equals(rangeCC.concept))
                                {
                                    rangeModel = rangeCC.model;
                                    break;
                                }
                            }
                        }
                    }*/
                    
                    if(rangeModel != null)
                    {
                        if(h.removedProperty != null)
                        {
                            Model domainModel = domainCC.GetModelFrolDisk();
                            ResIterator rit = domainModel.listSubjectsWithProperty(h.removedProperty);
                            //int size = rit.toList().size();
                            while(rit.hasNext())
                            {
                                Resource r = rit.nextResource();
                                String s1 = r.getProperty(h.removedProperty).getObject().toString();

                                ResIterator rangeRes = rangeModel.listResourcesWithProperty(h.removedProperty);
                                while (rangeRes.hasNext())
                                {
                                    Resource r2 = rangeRes.nextResource();
                                    String s2 = r2.getProperty(h.removedProperty).getObject().toString();
                                    if(s1.equalsIgnoreCase(s2))
                                    {
                                        r.addProperty(h.property, r2);
                                        System.out.println("Found ref");
                                        break;
                                    }
                                }
                            }
                            
                            domainModel = null;
                        }
                    }
                    
                    rangeModel = null;
                   
                }
            }
        }
        
    }
    
    private void LinkToOutsideResources()
    {
        for(CSVClass cc:this.csvClasses)
        {
            if(cc.model!=null)
            {
                ResIterator it = cc.model.listSubjects();
                while(it.hasNext())
                {
                    Resource r = it.nextResource();
                    
                    Statement loc = r.getProperty(DCTerms.title);
                    if(loc != null)
                    {
                        String localName = loc.getObject().toString();
                        
                        if(localName.length() > 0)
                        {
                            //dbpedia
                            String otherURI = URiLookup.lookUpDBPediaURiForResourceTitle(localName, cc.getClassName());
                            if(otherURI != null)
                            {
                                r.addProperty(RDFS.seeAlso, otherURI);
                                System.out.println("Added seeAlso:"+otherURI);
                            }
                        }
                    }
                    
                }
            }
        }
        
    }
    
    private void SaveDomainOntologyInFile()
    {
        try 
          {
              File file= new File(this.outputDirectory+this.domainName+"Ontology.rdf");               
              this.ontology.write(new java.io.FileOutputStream(file));
          } 
          catch (Exception e) {}
    }
    
    private Model rdfModelForCSVClass(CSVClass c)
    {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefixes(c.ontModel.getNsPrefixMap());
        model.setNsPrefix("DC", DCTerms.getURI());
        
        for(CSVHeader h : c.loadHeaders())
        {
            ArrayList<String> data = c.getDataForHeader(h);
            
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
        }
        return model;
    }
    
    private String filterString(String s)
    {
       String result = s;
       if(s.startsWith("<a href"))
       {
           String s1[] = s.split(">");
           result = s1[1];
           result = result.replaceAll("</a", "");
           
           result = result.replaceAll(">", "");
       }
       
       return result;
    }
   
    //capitalise each word and concats without any space
    public String propertyLike(String source)
    {
        String result = "";
        String[] splitString = source.split(" ");
        for(String target : splitString){
            result += Character.toUpperCase(target.charAt(0))
                    + target.substring(1) + " ";
        }
        return result.trim().replaceAll(" ", "");
    }
    
    //write any model to a given file
    public void writeModelToFile(Model m, String format, String fileName)
    {
        try{
            java.io.FileOutputStream fout = null;
            if(format.equalsIgnoreCase("RDF/XML"))
                fout = new java.io.FileOutputStream(this.outputDirectory+fileName+".rdf");
            else if(format.equalsIgnoreCase("N3"))
                fout = new java.io.FileOutputStream(this.outputDirectory+fileName+".n3");
            else if(format.equalsIgnoreCase("TURTLE") || format.equalsIgnoreCase("TTL"))
                fout = new java.io.FileOutputStream(this.outputDirectory+fileName+".ttl");
            else if(format.equalsIgnoreCase("N-TRIPLES") || format.equalsIgnoreCase("NT") || format.equalsIgnoreCase("N-TRIPLE"))
                fout = new java.io.FileOutputStream(this.outputDirectory+fileName+".nt");
            
            m.write(fout);
        }catch(Exception ie){}
    }
    
    public void CreatePersonModel()
    {
        Model pm = ModelFactory.createDefaultModel();
        pm.setNsPrefix("RDF", RDF.getURI());
        pm.setNsPrefix("foaf", FOAF.getURI());
        pm.setNsPrefix("DC", DCTerms.getURI());
        
        ArrayList<String> persons = new ArrayList<String>();
        persons.add("Kumar");
        persons.add("Arjun");
        
        int i = 1;
        for(String p : persons)
        {
            Resource p1 = pm.createResource("http://www.testsite.com/persons/p"+i);
            p1.addProperty(FOAF.firstName, p);
            p1.addProperty(DCTerms.description, "Description of person "+i);
            p1.addProperty(FOAF.mbox, p+i+"mb@gmail.com");
            
            i++;
        }
        
        this.writeModelToFile(pm, "RDF/XML", "personData");
    }
    
}

