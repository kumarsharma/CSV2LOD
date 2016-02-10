/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Panel;
import java.util.EventObject;
import javax.swing.JTable;

import csv2rdf.CSVHeader;
import csv2rdf.CLDataType;

/**
 *
 * @author apple
 */
public class PropertyTableModel extends AbstractTableModel  {
    
  String[] columns;
  PropertyDataSource dataSource;
  
  public PropertyTableModel()
  {
      this.columns = new String[]{"Property Name", "Range", "Add Relation", "Comment"};
  }
  
  public PropertyTableModel(ArrayList<CSVHeader> items)
  {
      this();
  }
  
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

       return new Panel();
  }
  
   public boolean shouldSelectCell(EventObject anEvent) 
   {
        return true;     
   }
   
  @Override
  public boolean isCellEditable(int i, int i1) 
  {
     return true;
  }

  @Override
  public int getRowCount() {
    
      if(null != this.dataSource)
        return this.dataSource.getDataItems().size();
      
      return 0;
  }

  @Override
  public int getColumnCount() {
  
      return this.columns.length;
  }
  
  @Override
  public String getColumnName(int i) {
     
      return this.columns[i];
   }

  @Override
  public Object getValueAt(int row, int column) {
    
      Object obj = null;
      CSVHeader item = this.dataSource.getDataItems().get(row);
      
      switch(column)
      {
          case 0:
          {
              obj = item.getGuiHeaderName(); 
          }    
          break;
              

          case 1:
          {     
              obj = item.getGuiDataType();
          }    
          break;
              
          case 2:
          {      
              obj = item.getGuiRelationshipStatus();
          }    
          break;
          
          case 3:
          {      
              obj = item.getGUIComment();
          }    
          break;
      }
        
      return obj;
  }
  
  @Override
  public void setValueAt(Object value, int row, int column) {
    
      Object obj = null;
      CSVHeader item = this.dataSource.getDataItems().get(row);
      
      switch(column)
      {
          case 0:
          {
              item.userDefinedHeader = (String)value;
          }    
          break;
              
          case 1:
          {     
              item.userDefinedDataType = (CLDataType)value;
          }    
          break;
              
          case 2:
          {      
              item.userDefinedRelationship = (boolean)value;
              
          }    
          break;
              
          case 3:
          {      
              item.userDefinedComment = (String)value;
          }    
          break;
      }
  }
  
  
  @Override
  public Class getColumnClass(int column) {
      
    return (getValueAt(0, column).getClass());
  }
  
}
