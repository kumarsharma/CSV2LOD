/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;

import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntClass;
/**
 *
 * @author ksharma
 */
public class CSVHeader {
    
    public String header, userDefinedHeader;
    public int colIndex;
    public OntProperty property, removedProperty;
    public OntClass rangeClass;
    CLDataType dataType;
    public String comment;
    public CSVClass rangeCSvClass;
    
    public CLDataType userDefinedDataType;;
    public boolean userDefinedRelationship;
    boolean includeRelationship;
    public String userDefinedComment;
    
    public CSVHeader(String h, int r)
    {
        this.header = h;
        this.colIndex = r;
        this.includeRelationship = false;
        this.removedProperty = null;
    }
    
    public String getGuiHeaderName()
    {
        if(this.userDefinedHeader!=null && this.userDefinedHeader.length() > 0)
            return this.userDefinedHeader;
        
        return this.header;
    }
    
    public boolean getGuiRelationshipStatus()
    {
        if(this.userDefinedRelationship)
            return this.userDefinedRelationship;
        
        return this.includeRelationship;
    }
    
    public CLDataType getGuiDataType()
    {
        if(this.userDefinedDataType != null)
            return this.userDefinedDataType;
        
        return this.dataType;
    }
    
    public String getGUIComment()
    {
        if(this.userDefinedComment!= null)
            return this.userDefinedComment;
        
        return this.comment;
    }
    
    public void applyUserDefinedData()
    {
        if(this.userDefinedDataType != null)
            this.dataType = this.userDefinedDataType;
        if(this.userDefinedRelationship)
            this.includeRelationship = this.userDefinedRelationship;
        if(this.userDefinedHeader != null && this.userDefinedHeader.length() > 0)
            this.header = this.userDefinedHeader;
        if(this.userDefinedComment!=null)
            this.comment = this.userDefinedComment;
    }
}
