/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import csv2rdf.CSVHeader;
import java.util.ArrayList;

/**
 *
 * @author apple
 */
public interface PropertyDataSource {
    
    public ArrayList<CSVHeader> getDataItems();
}
